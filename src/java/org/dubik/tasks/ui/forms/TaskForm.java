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
import org.dubik.tasks.model.ITask;
import org.dubik.tasks.model.TaskPriority;
import org.dubik.tasks.ui.TasksUIManager;

import javax.swing.*;
import java.awt.*;

/**
 * @author Sergiy Dubovik
 */
public class TaskForm {
    private JTextField titleTextField;
    private JComboBox priorityComboBox;
    private JSpinner minutesSpinner;
    private JPanel container;
    private JComboBox parentTasksComboBox;
    private JSpinner actualMinutesSpinner;
    private JLabel actualTimeLabel;
    private JLabel actualMinutesLabel;

    private static long ONE_MINUTE = 60 * 1000L;
    private ITask selectedParentTask;

    public TaskForm(TaskSettings settings) {
        SpinnerModel minutesSpinnerModel = new SpinnerNumberModel(0, 0, 9000, 15);
        minutesSpinner.setModel(minutesSpinnerModel);

        TaskPriority[] priorities = TaskPriority.values();
        priorityComboBox.setRenderer(new PriorityComboBoxRenderer());
        for (TaskPriority priority : priorities)
            priorityComboBox.addItem(priority);

        priorityComboBox.setSelectedItem(TaskPriority.Normal);

        parentTasksComboBox.setRenderer(new TaskComboBoxRenderer());

        SpinnerModel actualMinutesSpinnerModel = new SpinnerNumberModel(0, 0, 9000, 15);
        actualMinutesSpinner.setModel(actualMinutesSpinnerModel);

        setActualsVisible(settings.isEnableActualTime());
    }

    public String getTitle() {
        return titleTextField.getText();
    }

    public long getEstimatedTime() {
        int minutes = (Integer) minutesSpinner.getValue();

        return minutes * ONE_MINUTE;
    }

    public long getActualTime() {
        int minutes = (Integer) actualMinutesSpinner.getValue();

        return minutes * ONE_MINUTE;
    }

    public TaskPriority getPriority() {
        return (TaskPriority) priorityComboBox.getSelectedItem();
    }

    public void setPriority(TaskPriority priority) {
        priorityComboBox.setSelectedItem(priority);
    }

    public void setEstimatedTime(long time) {
        minutesSpinner.setValue((int) (time / ONE_MINUTE));
    }

    public void setTitle(String title) {
        titleTextField.setText(title);
    }

    public JPanel getContainer() {
        return container;
    }

    public void setParentTasksList(ITask rootTask, ITask[] parentTaskList) {
        parentTasksComboBox.addItem(rootTask);

        for (ITask task : parentTaskList)
            parentTasksComboBox.addItem(task);

        parentTasksComboBox.setSelectedItem(selectedParentTask);
    }

    public ITask getSelectedParent() {
        Object selectedParent = parentTasksComboBox.getSelectedItem();
        if (selectedParent instanceof ITask)
            return (ITask) selectedParent;

        return null;
    }

    public void setSelectedParentTask(ITask selectedTask) {
        selectedParentTask = selectedTask;
    }

    public void setActualsVisible(boolean visible) {
        actualMinutesLabel.setVisible(visible);
        actualTimeLabel.setVisible(visible);
        actualMinutesSpinner.setVisible(visible);
    }

    public void setActualTime(long actualTime) {
        actualMinutesSpinner.setValue((int) (actualTime / ONE_MINUTE));
    }

    /**
     * Combo box priority cell renderer.
     */
    class PriorityComboBoxRenderer extends JLabel implements ListCellRenderer {
        public PriorityComboBoxRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            TaskPriority priority = (TaskPriority) value;

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            setIcon(TasksUIManager.findIcon(priority));
            setText(priority.toString());
            setFont(list.getFont());

            return this;
        }
    }

    /**
     * Combo box tasks cell renderer.
     */
    class TaskComboBoxRenderer extends JLabel implements ListCellRenderer {
        public TaskComboBoxRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            ITask task = (ITask) value;
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            setText(task.getTitle());
            setIcon(TasksUIManager.createIcon(task));

            return this;
        }
    }
}
