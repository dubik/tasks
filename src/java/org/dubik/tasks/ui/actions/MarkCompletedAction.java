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
import org.dubik.tasks.TaskSettings;
import org.dubik.tasks.model.ITask;
import org.dubik.tasks.ui.forms.ActualTimeForm;

/**
 * @author Sergiy Dubovik
 */
@SuppressWarnings({"WeakerAccess"})
public class MarkCompletedAction extends BaseTaskAction {
    public void actionPerformed(AnActionEvent e) {
        TaskController controller = getController(e);
        if (controller != null) {
            ITask[] selectedTasks = controller.getSelectedTasks();
            for (ITask selectedTask : selectedTasks) {
                updateActualTime(e, selectedTask);
                controller.completeTask(selectedTask);
            }
        }
    }

    private void updateActualTime(AnActionEvent e, ITask selectedTask) {
        TaskSettings settings = getSettings();
        if (settings.isEnableActualTime() && settings.isAskActualWhenCompleteTask()) {
            ActualTimeForm actualTimeForm = new ActualTimeForm(getProject(e), selectedTask.getTitle());
            actualTimeForm.setTitle("Actual Time");
            actualTimeForm.setActualTime(selectedTask.getActualTime());
            actualTimeForm.show();
            if (actualTimeForm.isOK()) {
                TaskController controller = getController(e);
                controller.updateActualTime(selectedTask, actualTimeForm.getActualTime());
            }
        }
    }

    protected void update(TaskController controller, ITask[] selectedTasks,
                          Presentation presentation) {
        presentation.setEnabled(selectedTasks.length != 0);

        for (ITask task : selectedTasks) {
            if (!controller.canComplete(task)) {
                presentation.setEnabled(false);
                break;
            }
        }
    }
}
