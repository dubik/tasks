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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Sergiy Dubovik
 */
public class ActualTimeForm extends DialogWrapper {
    private static long ONE_MINUTE = 60 * 1000L;

    private JSpinner actualTimeSpinner;
    private JPanel container;
    private JLabel taskDescriptionLabel;

    public ActualTimeForm(Project project, String taskDescription) {
        super(project, false);
        taskDescriptionLabel.setText(taskDescription);
        initForm();
        init();
    }

    private void initForm() {
        SpinnerModel minutesSpinnerModel = new SpinnerNumberModel(0, 0, 9000, 15);
        actualTimeSpinner.setModel(minutesSpinnerModel);
    }

    @Nullable
    protected JComponent createCenterPanel() {
        return container;
    }

    public long getActualTime() {
        int minutes = (Integer) actualTimeSpinner.getValue();
        return minutes * ONE_MINUTE;
    }

    public void setActualTime(long actualTime) {
        actualTimeSpinner.setValue((int) (actualTime / ONE_MINUTE));
    }
}
