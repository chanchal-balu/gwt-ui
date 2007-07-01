package client.widgets;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import org.olostan.gwtui.client.UIStateListener;


/**
 * Created by Olostan
 * Date: 30.06.2007 21:59:52
 */
public class LeftMenu extends Composite implements UIStateListener {
    Hyperlink general;
    Hyperlink state1;
    public LeftMenu() {
        VerticalPanel panel = new VerticalPanel();
        panel.setWidth("100%");


        general = new Hyperlink("Initial state", "general");
        state1 = new Hyperlink("State 1", "state1");

        Label header = new Label("Menu");
        header.setStyleName("header");
        panel.add(header);
        panel.add(general);
        panel.add(state1);
        initWidget(panel);
    }

    public void OnStateChanged(String newState) {
        general.setStyleName("normallink");
        state1.setStyleName("normallink");
        if (newState.equals("general")) general.setStyleName("activelink");
        else if (newState.equals("state1")) state1.setStyleName("activelink");
    }
}
