package client;

import com.google.gwt.user.client.ui.Panel;
import org.olostan.gwtui.client.UIManager;

/**
 * Created by Olostan
 * Date: 30.06.2007 19:44:15
 */
public interface MyUI extends UIManager {
    public Panel getMainContainer();
    public Panel getCenterContentContainer();
}
