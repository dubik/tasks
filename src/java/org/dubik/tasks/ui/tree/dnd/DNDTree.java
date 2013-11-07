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
import org.dubik.tasks.ui.widgets.ProgressTooltip;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;

/**
 * @author Sergiy Dubovik
 */
public class DNDTree extends JTree {
    private Insets autoscrollInsets = new Insets(20, 20, 20, 20); // insets
    // private DefaultTreeTaskTransferHandler transferHandler;

    public DNDTree() {
        setAutoscrolls(true);
        // transferHandler = new DefaultTreeTaskTransferHandler(this, DnDConstants.ACTION_COPY_OR_MOVE);
    }

    public void autoscroll(Point cursorLocation) {
        Insets insets = getAutoscrollInsets();
        Rectangle outer = getVisibleRect();
        Rectangle inner = new Rectangle(outer.x + insets.left, outer.y + insets.top,
                outer.width - (insets.left + insets.right), outer.height - (insets.top + insets.bottom));
        if (!inner.contains(cursorLocation)) {
            Rectangle scrollRect = new Rectangle(cursorLocation.x - insets.left, cursorLocation.y - insets.top,
                    insets.left + insets.right, insets.top + insets.bottom);
            scrollRectToVisible(scrollRect);
        }
    }

    public Insets getAutoscrollInsets() {
        return (autoscrollInsets);
    }

    public void setTaskController(@NotNull TaskController taskController) {
        // transferHandler.setTaskController(taskController);
    }

    public JToolTip createToolTip() {
        Point pos = getMousePosition();
        JToolTip tooltip = super.createToolTip();

        if (pos != null) {
            TreePath treePath = getPathForLocation(pos.x, pos.y);
            if (treePath != null) {
                Object lastComponent = treePath.getLastPathComponent();
                if (lastComponent != null) {
                    ITask task = (ITask) lastComponent;
                    if (task.size() != 0) {
                        tooltip = new ProgressTooltip((float) completed(task) / (float) task.size());
                    }
                }
            }
        }

        tooltip.setComponent(this);
        return tooltip;
    }

    private int completed(@NotNull ITask task) {
        int completed = 0;
        for (int i = 0; i < task.size(); i++) {
            if (task.get(i).isCompleted())
                completed++;
        }

        return completed;
    }
}
