package client.widgets;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Window;

/**
 * Created by Olostan
 * Date: 30.06.2007 22:01:41
 */
public class HelloWidget extends Composite {

    public HelloWidget() {
        VerticalPanel panel = new VerticalPanel();
        Label hello = new Label("This demo shows main features of GWT-UI project.");
        panel.add(hello);
        String xmltext = "<gwtui>\n" +
                "<layout>\n" +
                "   <container type='VerticalPanel' id='main' width='100%'>     \n" +
                "     <container id='top' type='HorizontalPanel' align='right' width='100%' style='bar' />\n" +
                "     <container id='middle' type='HorizontalPanel' width=\"100%\" >\n" +
                "        <container id='leftContent' type='VerticalPanel' style='bar' cellwidth='250px' width='100%' />\n" +
                "        <container id='centerContent' type='VerticalPanel' style='bar' width='100%' />\n" +
                "        <container id='rightContent' type='VerticalPanel' style='bar' cellwidth='150px' />\n" +
                "     </container>\n" +
                "     <container id='bottom' type='HorizontalPanel' align='center' width='100%' style='bar' />\n" +
                "   </container>   \n" +
                "</layout>\n" +
                "<states widgetpackage='client.widgets'>\n" +
                "  <state id='base' >\n" +
                "     <content container='top'><widget type='LogoWidget' /></content>\n" +
                "     <content container='leftContent'><widget type='LeftMenu' /></content>\n" +
                "     <content container='rightContent'><widget type='HelpWidget' /> </content>\n" +
                "     <content container='bottom'><widget type='CopyrightWidget' /></content>\n" +
                "  </state>  \n" +
                "\n" +
                "  <state id='general' default='true' parent='base'>\n" +
                "     <content container='centerContent'><widget type='HelloWidget' name='helloWidget' /></content>\n" +
                "  </state>  \n" +
                "  <state id='state1' parent='base'>\n" +
                "     <content container='centerContent'><widget type='State1Widget' /></content>     \n" +
                "  </state>\n" +
                "</states>\n" +
                "</gwtui>";

        xmltext = xmltext.replaceAll("<(/?\\w+)(.*?)(/?)>","<font color='blue'>&lt;$1</font>$2<font color='blue'>$3&gt;</font>");
        xmltext = xmltext.replaceAll("(type|id|width|align|cellwidth|style|container|widgetpackage|default|parent)=([\"'].+?[\"'])","<font color='magenta'>$1</font>=<font color='green'>$2</font>");
        xmltext = xmltext.replaceAll("\n","<br />");
        //xmltext = "<pre>"+xmltext+"</pre>";
        HTML xml = new HTML(xmltext);        
        xml.setStyleName("sourcebar");
        panel.add(xml);
        panel.setWidth("100%");
        initWidget(panel);

    }
}
