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

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import org.dubik.tasks.TaskController;
import org.dubik.tasks.model.ITask;
import org.dubik.tasks.ui.TasksUIManager;

/**
 * Highlight task.
 *
 * @author Sergiy Dubovik
 */
@SuppressWarnings({"WeakerAccess"})
public class HighlightTaskAction extends BaseTaskAction {
    public void actionPerformed(AnActionEvent e) {
        TaskController controller = getController(e);
        if (controller != null) {
            ITask[] selectedTasks = controller.getSelectedTasks();
            if (canHighlightOrUnhighlight(selectedTasks, controller)) {
                if (areAllHighlighted(selectedTasks)) {
                    for (ITask task : selectedTasks) {
                        controller.unhighlightTask(task);
                    }
                } else {
                    for (ITask task : selectedTasks) {
                        controller.highlightTask(task);
                    }
                }
            }
        }
    }

    protected void update(TaskController controller, ITask[] selectedTasks, Presentation presentation) {
        if (selectedTasks.length == 0) {
            presentation.setIcon(null);
            presentation.setEnabled(false);
            return;
        }

        if (canHighlightOrUnhighlight(selectedTasks, controller)) {
            presentation.setEnabled(true);
            if (areAllHighlighted(selectedTasks)) {
                presentation.setIcon(TasksUIManager.getIcon(TasksUIManager.ICON_CHECK));
            } else {
                presentation.setIcon(null);
            }
        } else {
            presentation.setEnabled(false);
        }
    }

    private boolean areAllHighlighted(ITask[] tasks) {
        boolean highlighted = true;

        for (ITask task : tasks) {
            highlighted = highlighted && task.isHighlighted();
        }

        return highlighted;
    }

    private boolean canHighlightOrUnhighlight(ITask[] tasks, TaskController controller) {
        boolean result = true;

        for (ITask task : tasks) {
            result = result && (controller.canHighlight(task) || controller.canBeUnhighlighted(task));
        }

        return result;
    }
}
