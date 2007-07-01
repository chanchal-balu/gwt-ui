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

/**
 * @author Olostan (http://olostan.org.ua/)
 * Created by Olostan
 * Date: 28.06.2007 17:01:13
 */
public class IdOracle {
    private static IdOracle instance = null;

    public static IdOracle Instance() {
        if (instance == null) {
            instance = new IdOracle();
        }
        return instance;
    }

    private int currentMaxAnonContainers = 0;

    public String GenerateContainerId() {
        StringBuilder bld = new StringBuilder(10);
        bld.append("container");
        bld.append(++currentMaxAnonContainers);
        return bld.toString();
    }

    private int currentMaxAnonWidgets = 0;

    public String GenerateWidgetId() {
        StringBuilder bld = new StringBuilder(10);
        bld.append("widget");
        bld.append(++currentMaxAnonWidgets);
        return bld.toString();
    }

    private long reloadCounter;

    public long getReloadCounter() {
        return reloadCounter;
    }

    public void Reset(long newReloadCounter) {
        reloadCounter = newReloadCounter;
        currentMaxAnonContainers = 0;
        currentMaxAnonWidgets = 0;
    }
}
