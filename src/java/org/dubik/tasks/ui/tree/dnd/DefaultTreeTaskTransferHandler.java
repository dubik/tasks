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
package org.dubik.tasks.ui.tree.dnd;

import org.dubik.tasks.TaskController;
import org.dubik.tasks.model.ITask;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.TreePath;
import java.awt.*;

/**
 * @author Sergiy Dubovik
 */
public class DefaultTreeTaskTransferHandler extends AbstractTreeTransferHandler {
    private TaskController taskController;

    protected DefaultTreeTaskTransferHandler(DNDTree tree, int action) {
        super(tree, action, false);
    }

    public boolean canPerformAction(DNDTree target, ITask draggedTask, int action, Point location) {
        System.out.println("DefaultTreeTaskTransferHandler.canPerformAction");

        TreePath treePath = getTree().getPathForLocation(location.x, location.y);

        ITask newParentTask = (ITask) getTree().getModel().getRoot();
        if (treePath != null)
            newParentTask = (ITask) treePath.getLastPathComponent();

        ITask parent = draggedTask.getParent();

        return parent != newParentTask && taskController.canEdit(draggedTask);
    }

    public boolean executeDrop(DNDTree tree, ITask draggedTask, ITask newParentTask, int action) {
        System.out.println("DefaultTreeTaskTransferHandler.executeDrop");
        System.out.println("Dragged task: " + draggedTask.getTitle());
        System.out.println("New Parent task: " + newParentTask.getTitle());

        if (taskController == null)
            return false;

        ITask parent = draggedTask.getParent();
        if (parent == newParentTask)
            return false;

        if (!taskController.canEdit(draggedTask))
            return false;

        taskController.updateTask(draggedTask, newParentTask, draggedTask.getTitle(), draggedTask.getPriority(),
                draggedTask.getEstimatedTime());

        return true;
    }

    public void setTaskController(@NotNull TaskController taskController) {
        this.taskController = taskController;
    }
}
