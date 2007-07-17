package client.widgets;

import client.GWTUISample;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

import org.olostan.gwtui.client.UIStateListener;

/**
 * Created by Olostan
 * Date: 01.07.2007 15:20:48
 */
public class HelpWidget extends Composite implements UIStateListener {
    Label content = new Label();
    public HelpWidget() {
        VerticalPanel panel = new VerticalPanel();
        panel.setWidth("250px");
        Label header = new Label("Help");
        header.setStyleName("header");
        panel.add(header);
        panel.setCellHorizontalAlignment(header, HasHorizontalAlignment.ALIGN_RIGHT);
        content.setStyleName("justified-text");
        panel.add(content);
        Button button = new Button("Send message to widgets");
        button.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				GWTUISample.getInstance().getUi().SendNotification("Message", true);
			}} );
        panel.add(button);
        initWidget(panel);
    }

    public void OnStateChanged(String newState) {
        if (newState.equals("general")) {
            content.setText("This is default state of UI. If no state defined this state is opened.");
        } else if (newState.equals("state1")) {
            content.setText("This is 'state1' state. You should see here 'State1Widget'. All other content is derived from generic state;");
        }
    }
}
