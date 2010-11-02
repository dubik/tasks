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
package org.dubik.tasks.ui.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import org.dubik.tasks.TaskController;
import org.dubik.tasks.TaskSettings;
import org.dubik.tasks.TasksApplicationComponent;
import org.dubik.tasks.TasksProjectComponent;
import org.dubik.tasks.model.ITask;
import org.dubik.tasks.ui.tree.TreeController;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class, which provides common objects to other task actions.
 *
 * @author Sergiy Dubovik
 */
abstract public class BaseTaskAction extends AnAction {
    /**
     * Returns project associated with an action.
     *
     * @param e action event
     * @return project
     */
    Project getProject(AnActionEvent e) {
        return (Project) e.getDataContext().getData(DataConstants.PROJECT);
    }

    /**
     * Returns plugin settings.
     *
     * @return plugin settings
     */
    @NotNull
    TaskSettings getSettings() {
        TasksApplicationComponent appComp =
                ApplicationManager.getApplication().getComponent(TasksApplicationComponent.class);

        return appComp.getSettings();
    }

    /**
     * Returns task controller.
     *
     * @param e action event
     * @return task controller
     */
    TaskController getController(AnActionEvent e) {
        return getController(getProject(e));
    }

    /**
     * Returns task controller.
     *
     * @param project project associated with an action
     * @return task controller
     */
    TaskController getController(Project project) {
        TaskController controller = null;
        if (project != null) {
            TasksProjectComponent tasksProject = project.getComponent(TasksProjectComponent.class);
            if (tasksProject != null)
                controller = tasksProject.getTaskController();
        }

        return controller;
    }

    /**
     * Returns tree controller.
     *
     * @param project project associated with an action
     * @return tree controller
     */
    TreeController getTreeController(Project project) {
        TreeController controller = null;
        if (project != null) {
            TasksProjectComponent tasksProject = project.getComponent(TasksProjectComponent.class);
            if (tasksProject != null)
                controller = tasksProject.getTreeController();
        }

        return controller;
    }

    public void update(AnActionEvent e) {
        super.update(e);
        TaskController controller = getController(getProject(e));
        if (controller != null) {
            ITask[] selectedTasks = controller.getSelectedTasks();

            Presentation presentation = e.getPresentation();
            update(controller, selectedTasks, presentation);
        }
    }

    /**
     * You may ovveride this version of update() method if you need additional
     * parameters.
     *
     * @param controller    task controller
     * @param selectedTasks selected tasks
     * @param presentation  presentation
     */
    void update(TaskController controller, ITask[] selectedTasks,
                Presentation presentation) {
    }
}
