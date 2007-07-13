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

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Olostan
 * Date: 30.06.2007 17:27:13
 */
public class UIClassGenerator extends Generator {
    private TreeLogger logger;
    private GeneratorContext ctx;

    private UIConfiguration ui;


    private void LoadConfiguration(String typeName) throws UnableToCompleteException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        URL moduleURL;
        // Lets search for definition file
        // first - direct search
        moduleURL =  classLoader.getResource(typeName+".ui.xml");
        // then only last part
        if (moduleURL==null && typeName.indexOf('.')!=-1) {
            typeName = typeName.substring(typeName.lastIndexOf('.')+1);
            classLoader.getResource(typeName+".ui.xml");
        }
        // and last - simple
        if (moduleURL==null) moduleURL =  classLoader.getResource("ui.xml");

        // found?
        if (moduleURL != null) {
            String externalForm = moduleURL.toExternalForm();
            logger.log(TreeLogger.TRACE, "UI location: " + externalForm, null);
            try {
                if ((!(externalForm.startsWith("jar:file")))
                        && (!(externalForm.startsWith("zip:file")))
                        && (!(externalForm.startsWith("http://")))
                        && (!(externalForm.startsWith("ftp://")))) {

                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document document = builder.parse(new File(new URI(externalForm)));
                    ui = new UIConfiguration();
                    DocumentParser parser = new DocumentParser(document, ui, ctx, logger);
                    parser.Parse();
                }
            } catch (URISyntaxException e) {
                logger.log(TreeLogger.ERROR, "Error parsing URI", e);
                throw new UnableToCompleteException();
            } catch (IOException e) {
                logger.log(TreeLogger.ERROR, "IO Error", e);
                throw new UnableToCompleteException();
            } catch (ParserConfigurationException e) {
                logger.log(TreeLogger.ERROR, "Error parsing configuration", e);
                throw new UnableToCompleteException();
            } catch (SAXException e) {
                logger.log(TreeLogger.ERROR, "Error parsing XML", e);
                throw new UnableToCompleteException();
            } catch (DocumentParser.InvalidSyntaxException e) {
                logger.log(TreeLogger.ERROR, "Error parsing UI configuration " + e.toString(), e);
                throw new UnableToCompleteException();
            }
        }
        if (moduleURL == null) {
            String msg = "Unable to find '"
                    + "ui.xml' or '"+typeName
                    + ".ui.xml' on your classpath; could be a typo, or maybe you forgot to include a classpath entry for source?";
            logger.log(TreeLogger.ERROR, msg, null);
            throw new UnableToCompleteException();
        }

    }

    public String GenerateManagerClass(String requestedType) throws UnableToCompleteException {
        LoadConfiguration(requestedType);
        // TODO: Reset IdOracle only when needed (taking into account 'skins')
        /*
        if (ctx.getTypeOracle().getReloadCount() != IdOracle.Instance().getReloadCounter())
            IdOracle.Instance().Reset(ctx.getTypeOracle().getReloadCount());
            */
        IdOracle.Instance().Reset(ctx.getTypeOracle().getReloadCount());

        CodeBuilder builder = new CodeBuilder(ui, ctx, logger);

        String packageName = "client";
        JClassType sourceType;
        try {
            sourceType = ctx.getTypeOracle().getType(requestedType);
        } catch (NotFoundException e) {
            logger.log(TreeLogger.ERROR, "Unable to find type " + requestedType, e);
            throw new UnableToCompleteException();
        }
        String resultingType = sourceType.getSimpleSourceName() + "Imp";

        builder.Build(packageName, resultingType, sourceType);

        return packageName + "." + resultingType;
    }

    public String generate(TreeLogger treeLogger, GeneratorContext generatorContext, String requestedType)
            throws UnableToCompleteException {
        ctx = generatorContext;
        logger = treeLogger;
        return GenerateManagerClass(requestedType);
    }
}
