/*
 * Copyright 2006 Sergiy Dubovik
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dubik.tasks;

import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;

import java.beans.PropertyChangeSupport;

/**
 * @author Sergiy Dubovik
 */
public class TaskSettings extends PropertyChangeSupport implements JDOMExternalizable {
    private boolean enableActualTime;
    private boolean askActualWhenCompleteTask;
    private static final String TASKS_SETTINGS_OPTIONS = "options";
    private static final String TASKS_SETTINGS_ENABLE_ACTUAL_TIME = "enableActualTime";
    private static final String TASKS_SETTINGS_ASK_ACTUAL = "askActualWhenCompleteTask";

    public TaskSettings() {
        super(TaskSettings.class);
    }

    public boolean isEnableActualTime() {
        return enableActualTime;
    }

    public void setEnableActualTime(boolean enableActualTime) {
        boolean oldValue = this.enableActualTime;
        this.enableActualTime = enableActualTime;
        firePropertyChange(TASKS_SETTINGS_ENABLE_ACTUAL_TIME, oldValue, enableActualTime);
    }

    public boolean isAskActualWhenCompleteTask() {
        return askActualWhenCompleteTask;
    }

    public void setAskActualWhenCompleteTask(boolean askActualWhenCompleteTask) {
        boolean oldValue = this.askActualWhenCompleteTask;
        this.askActualWhenCompleteTask = askActualWhenCompleteTask;
        firePropertyChange(TASKS_SETTINGS_ASK_ACTUAL, oldValue, askActualWhenCompleteTask);
    }

    public void writeExternal(Element element) throws WriteExternalException {
        Element xTasksSettings = new Element(TASKS_SETTINGS_OPTIONS);
        element.addContent(xTasksSettings);

        xTasksSettings.setAttribute(TASKS_SETTINGS_ENABLE_ACTUAL_TIME, Boolean.toString(enableActualTime));
        xTasksSettings.setAttribute(TASKS_SETTINGS_ASK_ACTUAL, Boolean.toString(askActualWhenCompleteTask));
    }

    public void readExternal(Element element) throws InvalidDataException {
        Element xTasksSettings = element.getChild(TASKS_SETTINGS_OPTIONS);
        if (xTasksSettings == null)
            return;

        enableActualTime =
                ExternalizeSupport.getSafelyBoolean(xTasksSettings, TASKS_SETTINGS_ENABLE_ACTUAL_TIME, false);
        askActualWhenCompleteTask =
                ExternalizeSupport.getSafelyBoolean(xTasksSettings, TASKS_SETTINGS_ASK_ACTUAL, false);
    }
}