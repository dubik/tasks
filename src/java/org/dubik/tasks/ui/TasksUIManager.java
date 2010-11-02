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
package org.dubik.tasks.ui;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPopupMenu;
import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.LayeredIcon;
import com.intellij.util.ui.Tree;
import org.dubik.tasks.model.ITask;
import org.dubik.tasks.model.ITaskModel;
import org.dubik.tasks.model.TaskPriority;
import org.dubik.tasks.ui.tree.TaskTreeCellRenderer;
import org.dubik.tasks.ui.tree.TaskTreeModel;
import org.dubik.tasks.ui.tree.TaskTreeMouseAdapter;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergiy Dubovik
 */
public class TasksUIManager {
    private static Map<String, Icon> iconMap = new HashMap<String, Icon>();

    public static final String ICON_TASK = "/org/dubik/tasks/ui/icons/task.png";
    public static final String ICON_BIG_TASK = "/org/dubik/tasks/ui/icons/bigTask.png";
    private static final String ICON_DEFAULT_PRIORITY = "/general/todoDefault.png";
    private static final String ICON_IMPORTANT_PRIORITY = "/general/todoImportant.png";
    private static final String ICON_QUESTION_PRIORITY = "/org/dubik/tasks/ui/icons/lowPriority.png";
    public static final String ICON_CHECK = "/gutter/check.png";
    private static final String ICON_STAR = "/org/dubik/tasks/ui/icons/star.png";

    private static final String DEFAULT_ACTION_GROUP_PLACE = "TasksActionGroupPlace";

    public static Icon getIcon(String path) {
        Icon icon = iconMap.get(path);
        if (icon == null) {
            icon = IconLoader.getIcon(path);
            iconMap.put(path, icon);
        }

        return icon;
    }

    static public Icon createIcon(ITask task) {
        Icon taskIcon;
        if (task.isHighlighted()) {
            LayeredIcon layeredIcon = new LayeredIcon(2);
            layeredIcon.setIcon(getIcon(ICON_STAR), 0, 0, 0);
            layeredIcon.setIcon(findIcon(task.getPriority()), 1, 17, 0);
            taskIcon = layeredIcon;
        } else {
            taskIcon = findIcon(task.getPriority());
        }

        return taskIcon;
    }

    public static Icon findIcon(TaskPriority priority) {
        if (priority == null)
            return null;

        switch (priority) {
            case Normal:
                return getIcon(ICON_DEFAULT_PRIORITY);
            case Important:
                return getIcon(ICON_IMPORTANT_PRIORITY);
            case Questionable:
                return getIcon(ICON_QUESTION_PRIORITY);
            default:
                return null;
        }
    }

    public static JTree createTaskTree(TreeModel model, JPopupMenu popupMenu) {
        Tree tasksTree = new Tree();
        tasksTree.setShowsRootHandles(true);
        tasksTree.setLineStyleAngled();
        tasksTree.setCellRenderer(createTasksTreeCellRenderer());
        tasksTree.setRootVisible(false);
        tasksTree.setModel(model);
        tasksTree.addMouseListener(new TaskTreeMouseAdapter(popupMenu));
        return tasksTree;
    }

    @SuppressWarnings({"SameParameterValue"})
    public static JPopupMenu createTaskTreePopup(String actionGroupId) {
        ActionGroup actionGroup = (ActionGroup) ActionManager.getInstance().getAction(actionGroupId);
        actionGroup.setPopup(true);
        ActionPopupMenu popupMenu =
                ActionManager.getInstance().createActionPopupMenu(DEFAULT_ACTION_GROUP_PLACE, actionGroup);
        return popupMenu.getComponent();
    }

    public static TaskTreeModel createTaskTreeModel(ITaskModel model) {
        return new TaskTreeModel(model);
    }

    private static TreeCellRenderer createTasksTreeCellRenderer() {
        return new TaskTreeCellRenderer();
    }
}
