package client.widgets;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HTML;

/**
 * Created by Olostan
 * Date: 30.06.2007 22:02:22
 */
public class CopyrightWidget extends Composite {

    public CopyrightWidget() {
        HTML html = new HTML("Copyright &copy; <a href='http://olostan.org.ua/' target='_blank'>Olostan</a>, 2007");
        initWidget(html);
        html.setStyleName("copy");
        setWidth("100%");
    }
}
