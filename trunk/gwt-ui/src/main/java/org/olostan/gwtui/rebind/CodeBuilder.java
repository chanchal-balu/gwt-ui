/*
 * Copyright 2007 Valentyn Shybanov
 * http://olostan.org.ua/
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.olostan.gwtui.rebind;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import org.olostan.gwtui.client.NotificationReciever;
import org.olostan.gwtui.client.NotificationSender;
import org.olostan.gwtui.client.UIStateListener;
import org.olostan.gwtui.rebind.model.Container;
import org.olostan.gwtui.rebind.model.Content;
import org.olostan.gwtui.rebind.model.StateDefinition;
import org.olostan.gwtui.rebind.model.WidgetDefinition;

import java.io.PrintWriter;

/**
 * Created by Olostan
 * Date: 30.06.2007 0:59:14
 */
class CodeBuilder {
    private UIConfiguration ui;
    private GeneratorContext ctx;
    TreeLogger logger;

    // building-time
    private final JClassType UIStateListenerType;
    private boolean notificationSupport = false;

    public CodeBuilder(UIConfiguration ui, GeneratorContext ctx, TreeLogger logger) {
        this.ui = ui;
        this.ctx = ctx;
        this.logger = logger;
        UIStateListenerType = ctx.getTypeOracle().findType(UIStateListener.class.getName());
    }

    public void Build(String packageName, String resultingType, JClassType sourceClass)
            throws UnableToCompleteException {
        PrintWriter pw = ctx.tryCreate(logger, packageName, resultingType);
        if (pw != null) {
            ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(
                    packageName, resultingType);            
            // Check whether we have to build notification support
            JClassType notificationSender = this.ctx.getTypeOracle().findType(NotificationSender.class.getName());
            
            JClassType[] interfaces = sourceClass.getImplementedInterfaces();
            for (JClassType type : interfaces) {
            	if (type.equals(notificationSender))  notificationSupport = true;
            }
            
            // Add implemented interfaces
            composerFactory.addImplementedInterface(sourceClass.getQualifiedSourceName());
            if (notificationSupport) composerFactory.addImplementedInterface(notificationSender.getQualifiedSourceName());
            
            // TODO: import only used types
            composerFactory.addImport("com.google.gwt.user.client.ui.*");
            composerFactory.addImport("com.google.gwt.user.client.ui.HasHorizontalAlignment");
            composerFactory.addImport("java.util.ArrayList");

            SourceWriter writer = composerFactory.createSourceWriter(ctx, pw);

            writer.println("/* containers */");
            for (Container container : ui.getContainers())
                WriteContainerDeclaration(writer, container);

            WriteWidgetsDefinition(writer);

            // Constructor
            writer.println();
            writer.println("private " + resultingType + "() {");
            writer.indent();
            for (Container container : ui.getContainers())
                WriteContainerCreation(writer, container);
            writer.outdent();
            writer.println("}");

            WriteChangeStateMember(writer);

            // now implement members
            JMethod[] methods = sourceClass.getMethods();
            for (JMethod method : methods) {
                String name = method.getName();
                if (name.toUpperCase().startsWith("GET") && name.endsWith("Container")) {
                    String containerName = name.substring(3, name.length() - 9);
                    Container container = ui.FindContainerById(containerName);
                    if (container == null) {
                        logger.log(TreeLogger.ERROR, "Container with ID='" + containerName + "' was not found", null);
                        throw new UnableToCompleteException();
                    }
                    writer.print("public ");
                    writer.print(method.getReturnType().getQualifiedSourceName());
                    writer.print(" ");
                    writer.print(method.getName());
                    writer.print("() { return ");
                    writer.print(container.getSourceId());
                    writer.println("; }");
                }
                if (name.toUpperCase().startsWith("GET") && name.endsWith("Widget")) {
                    String widgetName = name.substring(3, name.length() - 6);
                    WidgetDefinition widget = ui.FindWidgetById(widgetName);
                    if (widget==null) {
                        logger.log(TreeLogger.ERROR, "Widget with ID '" + widgetName + "' was not found", null);
                        throw new UnableToCompleteException();
                    }
                    writer.print("public ");
                    writer.print(method.getReturnType().getQualifiedSourceName());
                    writer.print(" ");
                    writer.print(method.getName());
                    writer.print("() { return ");
                    writer.print(widget.getName());
                    writer.println("; }");                    
                }

            }
            if (notificationSupport) WriteNotificationCode(writer);

            writer.commit(logger);
        }
        // headers

    }

    private void WriteNotificationCode(SourceWriter writer) {
    	writer.println("public void SendNotification(Object data, boolean onlyVisible) {");
    	writer.indent();
    	JClassType notificationRecieverClass = ctx.getTypeOracle().findType(NotificationReciever.class.getName());
    	for (WidgetDefinition widget : ui.getWidgets()) {
    		// check if we support NotificationReciever    		
    		if (notificationRecieverClass.isAssignableFrom(widget.getJType())) 
    		{
    			writer.print("if (");
    			writer.print(widget.getName());
    			writer.print(" !=null && ( !onlyVisible || ");
    			writer.print(widget.getName());
    			writer.print(".getParent()!=null )) ");
    			writer.print(widget.getName());
    			writer.println(".OnUINotification(data);");
    		}
    	}
    	writer.outdent();
    	writer.println("}");
    	
		
	}

	private void WriteChangeStateMember(SourceWriter file) {
        // create state implementation functions:
        file.println("private ArrayList widgetsToRemove = null;");
        file.println();
        for (StateDefinition stateDefinition : ui.getStates()) {
            file.print("private void Set");
            file.print(stateDefinition.getId());
            file.println("State() {");
            file.indent();
            if (stateDefinition.getParent() != null) {
                file.print("Set");
                file.print(stateDefinition.getParent().getId());
                file.println("State();");
            }
            WriteStateCode(file, stateDefinition);
            file.outdent();
            file.println("}");
        }


        file.println("private String currentState = null;");


        file.println("public void setState(String state) {");
        file.indent();
        file.println("if (currentState!=null && currentState.equals(state)) return;");
        file.println("currentState = state;");
        file.print("widgetsToRemove = new ArrayList(");
        file.print(String.valueOf(ui.getWidgets().size()));
        file.println(");");

        // add all widgets to remove list
        for (WidgetDefinition widgetDefinition : ui.getWidgets()) {
            file.print("if (");
            file.print(widgetDefinition.getName());
            file.print("!=null) widgetsToRemove.add(");
            file.print(widgetDefinition.getName());
            file.println(");");
        }

        for (Container container : ui.getContainers())
            WriteContainerStateChange(file, container);
        boolean first = true;
        file.println();
        file.println("// Change state");
        for (StateDefinition stateDefinition : ui.getStates()) {
            if (!first) file.print("else ");
            first = false;
            file.print("if (state.equals(\"");
            file.print(stateDefinition.getId());
            file.print("\")) Set");
            file.print(stateDefinition.getId());
            file.println("State();");
            //file.println("}");
        }
        if (ui.getDefaultState() != null) {
            file.println("else {");
            file.indent();
            file.print("currentState = \"");
            file.print(ui.getDefaultState().getId());
            file.println("\";");
            file.print("Set");
            file.print(ui.getDefaultState().getId());
            file.println("State();");
            file.outdent();
            file.println("}");
        }

        // now remove all widgets that in remove list
        file.println();
        file.println("for (int c = 0; c<widgetsToRemove.size();c++) ((Widget)widgetsToRemove.get(c)).removeFromParent();");

        file.outdent();
        file.println("}");
    }

    private void WriteContainerStateChange(SourceWriter file, Container container) {
        // TODO: Implement UIStateListener caller
    }

    private void WriteStateCode(SourceWriter file, StateDefinition stateDefinition) {
        for (Content content : stateDefinition.getContents()) {
            String container = content.getContainer().getSourceId();
            for (WidgetDefinition widgetDefinition : content.getWidgets()) {
                // lazy creation:
                file.print("if (");
                file.print(widgetDefinition.getName());
                file.print("==null) ");
                file.print(widgetDefinition.getName());
                file.print(" = new ");
                file.print(widgetDefinition.getJType().getQualifiedSourceName());
                file.println("();");

                // check UIStateListener and call
                if (widgetDefinition.getJType().isAssignableTo(UIStateListenerType)) {
                    //file.print("if (inform) ");
                    file.print(widgetDefinition.getName());
                    file.println(".OnStateChanged(currentState);");
                }
                // remove for delete list
                file.print("widgetsToRemove.remove(");
                file.print(widgetDefinition.getName());
                file.println(");");

                // adopt is nessesary
                file.print("if (");
                file.print(widgetDefinition.getName());
                file.print(".getParent() != ");
                file.print(container);
                file.print(") ");
                file.print(container);
                file.print(".add(");
                file.print(widgetDefinition.getName());
                file.println(");");
            }
        }
    }


    private void WriteContainerCreation(SourceWriter file, Container container) {
        if (container.getHalign() != null) {
            file.print(container.getSourceId());
            file.print(".setHorizontalAlignment(");
            //file.print(ctx.getTypeOracle().findType(container.getHalign().getClass().getCanonicalName()).getQualifiedSourceName());            
            file.print("HasHorizontalAlignment.ALIGN_");
            file.print(container.getHalign().toUpperCase());
            file.println(");");
        }

        if (container.getWidth() != null) {
            file.print(container.getSourceId());
            file.print(".setWidth(\"");
            file.print(container.getWidth());
            file.println("\");");
        }
        if (container.getStyle() != null) {
            file.print(container.getSourceId());
            file.print(".setStyleName(\"");
            file.print(container.getStyle());
            file.println("\");");
        }
        if (container.getSubContainers().size() > 0) {
            for (Container subContainer : container.getSubContainers()) {
                WriteContainerCreation(file, subContainer);
                file.print(container.getSourceId());
                file.print(".add(");
                file.print(subContainer.getSourceId());
                file.println(");");
                if (subContainer.getCellWidth() != null) {
                    file.print(container.getSourceId());
                    file.print(".setCellWidth(");
                    file.print(subContainer.getSourceId());
                    file.print(",\"");
                    file.print(subContainer.getCellWidth());
                    file.println("\");");
                }
            }

        }
    }

    private void WriteContainerDeclaration(SourceWriter file, Container container) {
        file.print(container.getType().toString());
        file.print(" ");
        file.print(container.getSourceId());
        file.print(" = new ");
        file.print(container.getType().toString());
        file.println("();");
        if (container.getSubContainers().size() > 0) {
            for (Container subContainer : container.getSubContainers())
                WriteContainerDeclaration(file, subContainer);
        }
    }

    private void WriteWidgetsDefinition(SourceWriter file) {
        file.println("// Widgets");
        for (WidgetDefinition widgetDefinition : ui.getWidgets()) {
            //file.print(widgetDefinition.getJType().getSimpleSourceName());
            file.print(widgetDefinition.getJType().getQualifiedSourceName());
            file.print(" ");
            file.print(widgetDefinition.getName());
            file.println(";");
        }
        file.println();
    }

}
