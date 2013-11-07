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
import org.dubik.tasks.model.ITaskModel;
import org.dubik.tasks.model.impl.TaskModel;
import org.dubik.tasks.ui.TasksUIManager;
import org.dubik.tasks.ui.forms.TasksSettingsForm;
import org.dubik.tasks.utils.SerializeSupport;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

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
     * <p/>
     * Now <code>NamedTaskStorage</code> is responsible for writting real tasks from it's own
     * xml file.
     *
     * @param element root of the plugin data
     * @throws WriteExternalException thrown when something unexpected happens
     * @see org.dubik.tasks.NamedTaskStorage
     */
    public void writeExternal(Element element) throws WriteExternalException {
        SerializeSupport.writeDummy(element);
    }

    /**
     * Populates model and plugin settings from specified element.
     * <p/>
     * Now <code>NamedTaskStorage</code> is responsible for reading but for compatibility with
     * old release.
     *
     * @param element root of the plugin data
     * @throws InvalidDataException thrown if element is invalid
     * @see org.dubik.tasks.NamedTaskStorage
     */
    public void readExternal(Element element) throws InvalidDataException {
        SerializeSupport.readExternal(taskModel, taskSettings, element);
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