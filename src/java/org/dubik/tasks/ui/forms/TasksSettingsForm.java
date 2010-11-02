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
package org.dubik.tasks.ui.forms;

import org.dubik.tasks.TaskSettings;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Sergiy Dubovik
 */
public class TasksSettingsForm {
    private JCheckBox enableActualTimeFeatureCheckBox;
    private JCheckBox showEnterActualTimeCheckBox;
    private JPanel container;

    public TasksSettingsForm() {
        enableActualTimeFeatureCheckBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                updateControls();
            }
        });

        updateControls();
    }

    public JComponent getContainer() {
        return container;
    }

    public void setData(TaskSettings taskSettings) {
        enableActualTimeFeatureCheckBox.setSelected(taskSettings.isEnableActualTime());
        showEnterActualTimeCheckBox.setSelected(taskSettings.isAskActualWhenCompleteTask());
        updateControls();
    }

    public void getData(TaskSettings data) {
        data.setEnableActualTime(enableActualTimeFeatureCheckBox.isSelected());
        data.setAskActualWhenCompleteTask(showEnterActualTimeCheckBox.isSelected());
    }

    public boolean isModified(TaskSettings data) {
        return data.isAskActualWhenCompleteTask() != showEnterActualTimeCheckBox.isSelected()
                || data.isEnableActualTime() != enableActualTimeFeatureCheckBox.isSelected();
    }

    private void updateControls() {
        showEnterActualTimeCheckBox.setEnabled(enableActualTimeFeatureCheckBox.isSelected());
    }
}
