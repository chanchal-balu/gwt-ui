package client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;

/**
 * @author Olostan (http://olostan.org.ua/)
 *         Date: 01.07.2007 19:22:56
 */
public class GWTUISample implements EntryPoint, HistoryListener {
    private MyUI ui;
    public void onModuleLoad() {
         //UIManager myManager = (UIManager) GWT.create(UIManager.class);
        ui = (MyUI) GWT.create(MyUI.class);
        //Widget ui = myManager.LoadUIToWidget();
        RootPanel.get().add(ui.getMainContainer());
        ui.setState(History.getToken());
        History.addHistoryListener(this);
        RootPanel.get("loading").setVisible(false);
        Instance = this;
    }

    public void onHistoryChanged(String historyToken) {
        ui.setState(historyToken);
    }
    private static GWTUISample Instance;
    
	public static GWTUISample getInstance() {
		return Instance;
	}

	public MyUI getUi() {
		return ui;
	}
	
    
}
