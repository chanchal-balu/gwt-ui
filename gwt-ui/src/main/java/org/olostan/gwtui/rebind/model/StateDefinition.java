package org.olostan.gwtui.rebind.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Olostan
 * Date: 30.06.2007 21:19:40
 */
public class StateDefinition {
    private String Id;
    private StateDefinition parent;
    private List<Content> contents = new LinkedList<Content>();


    public StateDefinition(String id) {
        Id = id;
    }

    public String getId() {
        return Id;
    }

    public List<Content> getContents() {
        return contents;
    }

    public StateDefinition getParent() {
        return parent;
    }

    public void setParent(StateDefinition parent) {
        this.parent = parent;
    }
}
