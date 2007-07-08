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
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.TypeOracleException;
import com.google.gwt.user.client.ui.Widget;
import org.olostan.gwtui.rebind.model.Container;
import org.olostan.gwtui.rebind.model.Content;
import org.olostan.gwtui.rebind.model.StateDefinition;
import org.olostan.gwtui.rebind.model.WidgetDefinition;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by Olostan
 * Date: 28.06.2007 17:17:45
 */
class DocumentParser {


    public class InvalidSyntaxException extends Exception {
        public InvalidSyntaxException(String message) {
            super(message);
        }
    }

    private Document document;
    private UIConfiguration ui;
    private GeneratorContext ctx;
    private TreeLogger logger;

    // calculated
    private final JClassType WidgetClass;
    private String widgetPackage = null;

    public DocumentParser(Document document, UIConfiguration ui, GeneratorContext ctx, TreeLogger logger) {
        this.document = document;
        this.ui = ui;
        this.ctx = ctx;
        this.logger = logger;

        WidgetClass = ctx.getTypeOracle().findType(Widget.class.getName());

    }

    public void Parse() throws InvalidSyntaxException {
        LoadContainers();
        LoadStates();
    }

    private void LoadStates() throws InvalidSyntaxException {
        NodeList topStates = document.getElementsByTagName("states");
        if (topStates.getLength() != 1) throw new InvalidSyntaxException("Only one set of states allowed");
        Node states = topStates.item(0);

        NamedNodeMap attributes = states.getAttributes();
        Node attribute = attributes.getNamedItem("widgetpackage");
        if (attribute != null) widgetPackage = attribute.getNodeValue();


        NodeList statesList = states.getChildNodes();
        for (int c = 0; c < statesList.getLength(); c++) {
            Node stateNode = statesList.item(c);
            if (stateNode.getNodeName().equals("state")) {
                StateDefinition state = LoadState(stateNode);
                ui.getStates().add(state);
            }
        }
    }

    private void LoadContainers() throws InvalidSyntaxException {
        NodeList layouts = document.getElementsByTagName("layout");
        if (layouts.getLength() != 1) throw new InvalidSyntaxException("Only one layout supported");
        Node layout = layouts.item(0);
        NodeList contents = layout.getChildNodes();
        for (int c = 0; c < contents.getLength(); c++) {
            Node containerNode = contents.item(c);
            if (containerNode.getNodeName().equals("container")) {
                Container container = LoadContainer(containerNode);
                ui.getContainers().add(container);
            }
        }
    }

    private Container LoadContainer(Node containerNode) {
        String type = null;
        String id = null;
        NamedNodeMap attributes = containerNode.getAttributes();
        Node attribute = attributes.getNamedItem("type");
        if (attribute != null) type = attribute.getNodeValue();
        attribute = attributes.getNamedItem("id");
        if (attribute != null) id = attribute.getNodeValue();


        Container container;
        if (type != null && id != null) container = new Container(id, Container.ContainerTypes.valueOf(type));
        else if (type != null) container = new Container(Container.ContainerTypes.valueOf(type));
        else if (id != null) container = new Container(id);
        else container = new Container();

        // visual properties
        attribute = attributes.getNamedItem("align");
        if (attribute != null) {
            String align = attribute.getNodeValue();
            /*
            if (align.equals("center")) container.setHalign(HasHorizontalAlignment.ALIGN_CENTER);
            else if (align.equals("left")) container.setHalign(HasHorizontalAlignment.ALIGN_LEFT);
            else if (align.equals("right")) container.setHalign(HasHorizontalAlignment.ALIGN_RIGHT);*/
            container.setHalign(align);
        }
        attribute = attributes.getNamedItem("width");
        if (attribute != null) container.setWidth(attribute.getNodeValue());
        attribute = attributes.getNamedItem("cellwidth");
        if (attribute != null) container.setCellWidth(attribute.getNodeValue());
        attribute = attributes.getNamedItem("style");
        if (attribute != null) container.setStyle(attribute.getNodeValue());

        NodeList list = containerNode.getChildNodes();
        if (list.getLength() != 0) {
            for (int c = 0; c < list.getLength(); c++) {
                Node node = list.item(c);
                if (node.getNodeName() != null && node.getNodeName().equals("container"))
                    container.AddContainer(LoadContainer(node));
            }
        }
        return container;
    }

    private StateDefinition LoadState(Node stateNode) throws InvalidSyntaxException {
        String id;
        NamedNodeMap attributes = stateNode.getAttributes();
        Node attribute = attributes.getNamedItem("id");
        id = attribute.getNodeValue();

        StateDefinition state = new StateDefinition(id);

        attribute = attributes.getNamedItem("default");
        if (attribute != null) ui.setDefaultState(state);

        attribute = attributes.getNamedItem("parent");
        if (attribute != null) {
            String parentStateName = attribute.getNodeValue();
            state.setParent(ui.FindStateById(parentStateName));
        }


        NodeList list = stateNode.getChildNodes();
        if (list.getLength() != 0) {
            for (int c = 0; c < list.getLength(); c++) {
                Node node = list.item(c);
                if (node.getNodeName() != null && node.getNodeName().equals("content"))
                    state.getContents().add(LoadContent(node));
            }
        } else throw new InvalidSyntaxException("State must contain contents: " + id);
        return state;
    }

    private Content LoadContent(Node contentNode) throws InvalidSyntaxException {
        String containerName;
        NamedNodeMap attributes = contentNode.getAttributes();
        Node attribute = attributes.getNamedItem("container");
        containerName = attribute.getNodeValue();
        Container container = ui.FindContainerById(containerName);
        if (container == null) throw new InvalidSyntaxException("Content with invalid container:" + containerName);
        Content content = new Content(container);
        NodeList list = contentNode.getChildNodes();
        if (list.getLength() != 0) {
            for (int c = 0; c < list.getLength(); c++) {
                Node node = list.item(c);
                if (node.getNodeName() != null && node.getNodeName().equals("widget"))
                    content.getWidgets().add(LoadWidget(node));
            }
        }
        return content;
    }

    private WidgetDefinition LoadWidget(Node widgetNode) throws InvalidSyntaxException {
        String widgetTypeName;
        NamedNodeMap attributes = widgetNode.getAttributes();
        Node attribute = attributes.getNamedItem("type");
        widgetTypeName = attribute.getNodeValue();
        if (widgetPackage != null) widgetTypeName = widgetPackage + "." + widgetTypeName;

        String widgetName = null;
        attribute = attributes.getNamedItem("name");
        if (attribute != null) widgetName = attribute.getNodeValue();


        JType type;
        try {
            type = ctx.getTypeOracle().parse(widgetTypeName);
        } catch (TypeOracleException e) {
            logger.log(TreeLogger.ERROR, "No widget type found: " + widgetTypeName, e);
            throw new InvalidSyntaxException("Invalid widget type");
        }
        JClassType widgetType = type.isClass();
        if (widgetType == null) throw new InvalidSyntaxException("Type of widget must be class: " + widgetTypeName);
        if (!widgetType.isAssignableTo(WidgetClass))
            throw new InvalidSyntaxException("Type '" + widgetTypeName + "' is not assignable to Widget");

        WidgetDefinition widget;
        if (widgetName != null) {
            widget = ui.FindWidgetById(widgetName);
            if (widget == null) {
                widget = new WidgetDefinition(widgetType, widgetName);
                ui.getWidgets().add(widget);
            } else
                // check types
                if (widget.getJType() != widgetType)
                    throw new InvalidSyntaxException("Conflicting widget types for widget " + widgetName);
        } else {
            widget = new WidgetDefinition(widgetType);
            ui.getWidgets().add(widget);
        }
        return widget;
    }


}
