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

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import org.dubik.tasks.model.ITask;
import org.dubik.tasks.model.ITaskModel;
import org.dubik.tasks.model.TaskPriority;
import org.dubik.tasks.model.impl.TaskModel;
import org.dubik.tasks.ui.TasksUIManager;
import org.dubik.tasks.ui.forms.TasksSettingsForm;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * Task application component.
 * Responsible for tasks and ui settings persistancy.
 * Contributes option dialog to IntelliJ global options.
 * It contains task model, since task model is global and
 * there is always only one instance of it.
 *
 * @author Sergiy Dubovik
 */
public class TasksApplicationComponent implements ApplicationComponent, JDOMExternalizable,
        Configurable {
    private static final String TASKS = "tasks";
    private static final String TASK = "task";
    private static final String TASK_PRIORITY = "priority";
    private static final String TASK_COMPLETED = "completed";
    private static final String TASK_ESTIMATED = "estimated";
    private static final String TASK_CREATED = "created";
    private static final String TASK_HIGHLIGHTED = "highlighted";
    private static final String TASK_TITLE = "title";
    private static final String TASK_ACTUAL = "actual";

    private ITaskModel taskModel;
    private TaskSettings taskSettings;
    private TasksSettingsForm tasksSettingsForm;

    /**
     * Creates TasksApplicationComponent.
     */
    public TasksApplicationComponent() {
        taskModel = new TaskModel();
        taskSettings = new TaskSettings();
    }

    /**
     * Initializes component.
     */
    public void initComponent() {
    }

    /**
     * Destroys component.
     */
    public void disposeComponent() {
    }

    /**
     * Returns component name.
     *
     * @return component name
     */
    @NotNull
    public String getComponentName() {
        return "TasksComponent";
    }

    /**
     * Returns task model
     *
     * @return task model
     */
    @NotNull
    public ITaskModel getTaskModel() {
        return taskModel;
    }

    /**
     * Returns plugin settings.
     *
     * @return settings
     */
    @NotNull
    public TaskSettings getSettings() {
        return taskSettings;
    }

    /**
     * Writes plugin state to the specified element.
     * It saves tasks and settings.
     *
     * @param element root of the plugin data
     * @throws WriteExternalException thrown when something unexpected happens
     */
    public void writeExternal(Element element) throws WriteExternalException {
        Element tasksRoot = new Element(TASKS);
        element.addContent(tasksRoot);
        for (int i = 0; i < taskModel.size(); i++) {
            ITask task = taskModel.getTask(i);
            writeTasksRecursively(tasksRoot, task);
        }

        taskSettings.writeExternal(element);
    }

    private void writeTasksRecursively(Element taskRoot, ITask task) {
        Element newTaskRoot = writeTask(taskRoot, task);
        for (int i = 0; i < task.size(); i++) {
            ITask subTask = task.get(i);
            writeTasksRecursively(newTaskRoot, subTask);
        }
    }

    private Element writeTask(Element taskRoot, ITask task) {
        Element xTask = new Element(TASK);
        if (task.getPriority() != null)
            xTask.setAttribute(TASK_PRIORITY, task.getPriority().name());
        xTask.setAttribute(TASK_COMPLETED, Boolean.toString(task.isCompleted()));
        xTask.setAttribute(TASK_ESTIMATED, Long.toString(task.getEstimatedTime()));
        xTask.setAttribute(TASK_CREATED, Long.toString(task.getCreationTime()));
        xTask.setAttribute(TASK_ACTUAL, Long.toString(task.getActualTime()));
        xTask.setAttribute(TASK_HIGHLIGHTED, Boolean.toString(task.isHighlighted()));
        xTask.setAttribute(TASK_TITLE, task.getTitle());
        taskRoot.addContent(xTask);

        return xTask;
    }

    /**
     * Populates model and plugin settings from specified element.
     *
     * @param element root of the plugin data
     * @throws InvalidDataException thrown if element is invalid
     */
    public void readExternal(Element element) throws InvalidDataException {
        Element tasksRoot = element.getChild(TASKS);
        if (tasksRoot == null)
            return;

        List tasks = tasksRoot.getChildren();

        for (Object taskElem : tasks) {
            Element xTask = (Element) taskElem;
            addTasksRecursively(xTask, taskModel, null);
        }

        taskSettings.readExternal(element);
    }

    private void addTasksRecursively(Element taskElem, ITaskModel model, ITask parentTask) {
        TaskPriority priority = ExternalizeSupport.getSafelyTaskPriority(taskElem, TASK_PRIORITY, TaskPriority.Normal);
        boolean completed = ExternalizeSupport.getSafelyBoolean(taskElem, TASK_COMPLETED, false);
        boolean highlighted = ExternalizeSupport.getSafelyBoolean(taskElem, TASK_HIGHLIGHTED, false);
        long estimated = ExternalizeSupport.getSafelyLong(taskElem, TASK_ESTIMATED, 0);
        long actual = ExternalizeSupport.getSafelyLong(taskElem, TASK_ACTUAL, 0);
        long created = ExternalizeSupport.getSafelyLong(taskElem, TASK_CREATED, System.currentTimeMillis());

        String oldVersionTitle = taskElem.getText();
        String newVersionTitle = taskElem.getAttributeValue(TASK_TITLE);
        String title;
        if (newVersionTitle != null && newVersionTitle.length() != 0)
            title = newVersionTitle;
        else
            title = oldVersionTitle;

        ITask task = model.addTask(parentTask, title, priority, estimated, actual,
                created, completed, highlighted);
        for (Object subTaskElem : taskElem.getChildren()) {
            Element xSubTask = (Element) subTaskElem;
            addTasksRecursively(xSubTask, model, task);
        }
    }

    /**
     * Returns plugin settings UI form.
     *
     * @return root panel of UI form
     */
    public JComponent createComponent() {
        tasksSettingsForm = new TasksSettingsForm();
        tasksSettingsForm.setData(taskSettings);
        return tasksSettingsForm.getContainer();
    }

    /**
     * Shows if UI form has be modified.
     *
     * @return <code>true</code> if form has been modified, otherwise <code>false</code>
     */
    public boolean isModified() {
        return tasksSettingsForm.isModified(taskSettings);
    }

    public void apply() throws ConfigurationException {
        tasksSettingsForm.getData(taskSettings);
    }

    /**
     * Resets all changed values in the UI settings form to their previous values.
     */
    public void reset() {
        tasksSettingsForm.setData(taskSettings);
    }

    /**
     * Desposes plugin settings form.
     */
    public void disposeUIResources() {
        tasksSettingsForm = null;
    }

    @Nls
    public String getDisplayName() {
        return "Tasks";
    }

    /**
     * Returns settings form icon.
     *
     * @return settings form icon
     */
    public Icon getIcon() {
        return TasksUIManager.getIcon(TasksUIManager.ICON_BIG_TASK);
    }

    @Nullable
    @NonNls
    public String getHelpTopic() {
        return null;
    }
}