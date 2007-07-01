package client.widgets;

import com.google.gwt.user.client.ui.*;


import java.util.ArrayList;

import org.olostan.gwtui.client.UIStateListener;

/**
 * Created by Olostan
 * Date: 30.06.2007 21:56:56
 */
public class LogoWidget extends Composite implements UIStateListener {
    Label stateLabel = new Label("state:");
    public LogoWidget()
    {
        FlowPanel panel = new FlowPanel();
        HTML html = new HTML("<h2>This is demo of GWT-UI project</h2>");
        panel.add(html);
        panel.add(stateLabel);
        initWidget(panel);

        
    }

    public void OnStateChanged(String newState) {
        stateLabel.setText("You currently viewing state '"+newState+"'");
    }
}
