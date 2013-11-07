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
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.openapi.vcs.changes.LocalChangeList;
import org.dubik.tasks.TaskController;
import org.dubik.tasks.model.ITask;
import org.dubik.tasks.ui.forms.NewChangelistForm;

/**
 * @author Sergiy Dubovik
 */
public class NewChangeListAction extends BaseTaskAction {
    public void actionPerformed(AnActionEvent e) {
        TaskController controller = getController(e);
        ITask[] selectedTasks = controller.getSelectedTasks();

        String changelist = generateChangelistTitle(selectedTasks);


        NewChangelistForm newChangelistDlg = new NewChangelistForm(getProject(e));
        newChangelistDlg.setName(changelist);
        newChangelistDlg.show();

        if (newChangelistDlg.getExitCode() == DialogWrapper.OK_EXIT_CODE &&
                newChangelistDlg.getName().length() > 0) {
            ChangeListManager changeListManager = ChangeListManager.getInstance(getProject(e));
            LocalChangeList list = changeListManager.addChangeList(newChangelistDlg.getName(),
                    newChangelistDlg.getDescription());

            if (newChangelistDlg.isNewChangelistActive())
                changeListManager.setDefaultChangeList(list);
        }
    }

    protected void update(TaskController controller, ITask[] selectedTasks, Presentation presentation) {
        presentation.setEnabled(selectedTasks != null && selectedTasks.length > 0);
    }

    private String generateChangelistTitle(ITask[] selectedTasks) {
        if (selectedTasks == null || selectedTasks.length == 0)
            return "";

        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < selectedTasks.length; i++) {
            buf.append(selectedTasks[i].getTitle());
            if (i + 1 < selectedTasks.length)
                buf.append(", ");
        }

        return buf.toString();
    }
}