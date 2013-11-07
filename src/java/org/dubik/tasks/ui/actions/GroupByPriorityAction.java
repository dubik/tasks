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
import org.dubik.tasks.ui.TasksUIManager;
import org.dubik.tasks.ui.tree.TreeController;

import javax.swing.*;

/**
 * @author Sergiy Dubovik
 */
public class GroupByPriorityAction extends BaseTaskAction {
    public void actionPerformed(AnActionEvent e) {
        TreeController treeController = getTreeController(getProject(e));
        if (treeController != null) {
            treeController.groupByPriority();
        }
    }


    public void update(AnActionEvent e) {
        super.update(e);
        TreeController treeController = getTreeController(e);
        if (treeController != null) {
            if (treeController.isGroupByPriority()) {
                Icon check = TasksUIManager.getIcon(TasksUIManager.ICON_CHECK);
                e.getPresentation().setIcon(check);
            } else {
                e.getPresentation().setIcon(null);
            }
        }
    }
}
