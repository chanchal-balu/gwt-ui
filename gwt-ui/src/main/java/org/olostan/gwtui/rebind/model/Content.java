package org.olostan.gwtui.rebind.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Olostan
 * Date: 30.06.2007 21:04:57
 */
public class Content {
    private Container container;
    private List<WidgetDefinition> widgets = new LinkedList<WidgetDefinition>();

    public Content(Container container) {
        this.container = container;
    }

    public Container getContainer() {
        return container;
    }

    public List<WidgetDefinition> getWidgets() {
        return widgets;
    }
}
