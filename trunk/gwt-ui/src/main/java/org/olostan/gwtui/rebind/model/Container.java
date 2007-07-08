package org.olostan.gwtui.rebind.model;

import org.olostan.gwtui.rebind.IdOracle;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Olostan
 * Date: 28.06.2007 16:56:25
 */
public class Container {
    public enum ContainerTypes {
        VerticalPanel, HorizontalPanel, FlowPanel, SimplePanel
    }

    private List<Container> subContainers = new LinkedList<Container>();
    private String id;
    private ContainerTypes type;

    private String halign;
    private String width;
    private String style;
    private String cellWidth;


    public Container() {
        type = ContainerTypes.FlowPanel;
        id = IdOracle.Instance().GenerateContainerId();
    }

    public Container(ContainerTypes type) {
        this.type = type;
        id = IdOracle.Instance().GenerateContainerId();
    }

    public Container(String id) {
        type = ContainerTypes.FlowPanel;
        this.id = id;
    }

    public Container(String id, ContainerTypes type) {
        this.id = id;
        this.type = type;
    }

    public void AddContainer(Container container) {
        subContainers.add(container);
    }

    public Container FindContainerById(String id) {
        if (this.id.compareToIgnoreCase(id) == 0) return this;
        for (Container container : subContainers) {
            Container result = container.FindContainerById(id);
            if (result != null) return result;
        }
        return null;
    }

    public List<Container> getSubContainers() {
        return subContainers;
    }

    public String getId() {
        return id;
    }

    private String sourceId = null;

    public String getSourceId() {
        if (sourceId == null) sourceId = id + "Container";
        return sourceId;
    }

    public ContainerTypes getType() {
        return type;
    }


    public String getHalign() {
        return halign;
    }

    public void setHalign(String halign) {
        this.halign = halign;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(String cellWidth) {
        this.cellWidth = cellWidth;
    }
}
