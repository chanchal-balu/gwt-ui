package client.widgets;

import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;

import org.olostan.gwtui.client.NotificationReciever;


/**
 * Created by Olostan
 * Date: 30.06.2007 22:09:43
 */
public class State1Widget extends Composite implements NotificationReciever {
    private Label label;
    public State1Widget() {
        
        label = new Label("State1 widget");
        initWidget(label);        
    }

	public void OnUINotification(Object data) {
		label.setText("State1 widget got message:"+data.toString());
		
	}
}
