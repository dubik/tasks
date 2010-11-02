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

import org.dubik.tasks.TaskController;
import org.dubik.tasks.model.ITask;
import org.dubik.tasks.model.ITaskGroup;
import org.dubik.tasks.model.TaskPriority;
import org.dubik.tasks.ui.forms.TaskForm;

import java.util.Set;

/**
 * @author Sergiy Dubovik
 */
class TasksActionUtils {
    static void preselectPriority(TaskController controller, TaskForm form) {
        ITask[] selectedTask = controller.getSelectedTasks();
        if (selectedTask.length == 1) {
            //noinspection EmptyCatchBlock
            try {
                TaskPriority priority = TaskPriority.parse(selectedTask[0].getTitle());
                form.setPriority(priority);
            } catch (IllegalArgumentException e) {
            }
        }
    }

    static void preselectParentTask(TaskController controller, TaskForm form) {
        Set<ITask> allTasks = controller.getAllTasks();
        ITask[] selectedTasks = controller.getSelectedTasks();
        if (selectedTasks.length == 1 && !(selectedTasks[0] instanceof ITaskGroup)) {
            ITask selectedTask = selectedTasks[0];
            Set<ITask> subTasks = controller.getSubTasks(selectedTask);
            subTasks.remove(selectedTask);
            allTasks.removeAll(subTasks);
            form.setSelectedParentTask(selectedTask);
        } else {
            form.setSelectedParentTask(controller.getDummyRootTaskInstance());
        }

        ITask[] parentTasks = new ITask[allTasks.size()];
        parentTasks = allTasks.toArray(parentTasks);
        form.setParentTasksList(controller.getDummyRootTaskInstance(), parentTasks);
    }
}
