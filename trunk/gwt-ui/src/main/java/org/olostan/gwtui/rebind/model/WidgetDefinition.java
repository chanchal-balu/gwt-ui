package org.olostan.gwtui.rebind.model;

import com.google.gwt.core.ext.typeinfo.JClassType;
import org.olostan.gwtui.rebind.IdOracle;

/**
 * Created by Olostan
 * Date: 30.06.2007 21:01:36
 */
public class WidgetDefinition {
    private JClassType type;
    private String name;

    public WidgetDefinition(JClassType type) {
        this.type = type;
        this.name = IdOracle.Instance().GenerateWidgetId();
    }

    public WidgetDefinition(JClassType type, String name) {
        this.type = type;
        this.name = name;
    }

    public JClassType getJType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
