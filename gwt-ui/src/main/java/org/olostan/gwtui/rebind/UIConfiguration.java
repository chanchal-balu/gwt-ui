/*
 * Copyright 2007 Valentyn Shybanov
 * http://olostan.org.ua/
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.olostan.gwtui.rebind;

import org.olostan.gwtui.rebind.model.Container;
import org.olostan.gwtui.rebind.model.ModuleDefinition;
import org.olostan.gwtui.rebind.model.StateDefinition;
import org.olostan.gwtui.rebind.model.WidgetDefinition;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Olostan
 * Date: 28.06.2007 17:11:18
 */
class UIConfiguration {
    private List<Container> containers = new LinkedList<Container>();
    private List<StateDefinition> states = new LinkedList<StateDefinition>();
    private List<WidgetDefinition> widgets = new LinkedList<WidgetDefinition>();
    private List<ModuleDefinition> modules = new LinkedList<ModuleDefinition>();
    private StateDefinition defaultState = null;

    public List<Container> getContainers() {
        return containers;
    }

    public List<StateDefinition> getStates() {
        return states;
    }

    public Container FindContainerById(String containerId) {
        for (Container container : containers) {
            Container result = container.FindContainerById(containerId);
            if (result != null) return result;
        }
        return null;
    }


    public List<WidgetDefinition> getWidgets() {
        return widgets;
    }

    public WidgetDefinition FindWidgetById(String id) {
        for (WidgetDefinition widget : widgets) {
            if (widget.getName().equalsIgnoreCase(id)) return widget;
        }
        return null;
    }

    public void setDefaultState(StateDefinition state) {
        defaultState = state;
    }

    public StateDefinition getDefaultState() {
        return defaultState;
    }

    public StateDefinition FindStateById(String stateName) {
        for (StateDefinition state : states) {
            if (state.getId().equalsIgnoreCase(stateName)) return state;
        }
        return null;
    }

	public List<ModuleDefinition> getModules() {
		return modules;
	}
    
}
