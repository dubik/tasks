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
import org.dubik.tasks.TaskSettings;
import org.dubik.tasks.model.ITask;
import org.dubik.tasks.model.TaskPriority;
import org.dubik.tasks.ui.TasksUIManager;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Sergiy Dubovik
 */
public class TaskForm extends DialogWrapper {
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

    private Action addAction;
    private Action addToRootAction;
    private boolean addToRoot = false;

    public TaskForm(Project project, TaskSettings settings) {
        super(project, false);

        setTitle("New Task");

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

        init();
    }

    @Nullable
    protected JComponent createCenterPanel() {
        return container;
    }

    protected void createDefaultActions() {
        super.createDefaultActions();
        addAction = new AddAction();
        addToRootAction = new AddToRootAction();
    }

    public boolean isAddToRoot() {
        return addToRoot;
    }

    protected Action[] createActions() {
        return new Action[]{addToRootAction, addAction, getCancelAction()};
    }

    public JComponent getPreferredFocusedComponent() {
        return titleTextField;
    }

    public String getTaskTitle() {
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

    public void setTaskTitle(String title) {
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

    private class AddAction extends AbstractAction {
        public AddAction() {
            putValue(Action.NAME, "&Add");
            putValue(DEFAULT_ACTION, Boolean.TRUE);
        }

        public void actionPerformed(ActionEvent event) {
            addToRoot = false;
            doOKAction();
        }
    }

    private class AddToRootAction extends AbstractAction {
        public AddToRootAction() {
            putValue(Action.NAME, "Add to &Root");
        }

        public void actionPerformed(ActionEvent event) {
            addToRoot = true;
            doOKAction();
        }
    }
}
