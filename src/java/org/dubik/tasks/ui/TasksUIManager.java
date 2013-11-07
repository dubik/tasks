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
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.LayeredIcon;
import com.intellij.util.ui.UIUtil;
import org.dubik.tasks.TaskController;
import org.dubik.tasks.TaskSettings;
import org.dubik.tasks.TasksApplicationComponent;
import org.dubik.tasks.model.ITask;
import org.dubik.tasks.model.ITaskModel;
import org.dubik.tasks.model.TaskHighlightingType;
import org.dubik.tasks.model.TaskPriority;
import org.dubik.tasks.ui.tree.TaskTreeCellRenderer;
import org.dubik.tasks.ui.tree.TaskTreeModel;
import org.dubik.tasks.ui.tree.TaskTreeMouseAdapter;
import org.dubik.tasks.ui.tree.dnd.DNDTree;
import org.dubik.tasks.ui.widgets.ProgressTooltipUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.plaf.ToolTipUI;
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
    private static final String ICON_STAR_RED = "/org/dubik/tasks/ui/icons/star_red.png";
    private static final String ICON_STAR_YELLOW = "/org/dubik/tasks/ui/icons/star_yellow.png";
    private static final String ICON_STAR_GREEN = "/org/dubik/tasks/ui/icons/star_green.png";

    private static final String DEFAULT_ACTION_GROUP_PLACE = "TasksActionGroupPlace";

    private static final ToolTipUI progressTooltipUI = new ProgressTooltipUI();

    public static Icon getIcon(@NotNull String path) {
        Icon icon = iconMap.get(path);
        if (icon == null) {
            icon = IconLoader.getIcon(path);
            iconMap.put(path, icon);
        }

        return icon;
    }

    @NotNull
    public static Icon createIcon(@NotNull ITask task) {
        Icon taskIcon;
        if (task.isHighlighted()) {
            LayeredIcon layeredIcon = new LayeredIcon(2);
            layeredIcon.setIcon(findIcon(task.getHighlightingType()), 0, 0, 0);
            layeredIcon.setIcon(findIcon(task), 1, 17, 0);
            taskIcon = layeredIcon;
        } else {
            taskIcon = findIcon(task);
        }

        return taskIcon;
    }

    @NotNull
    public static Icon findIcon(@NotNull ITask task) {
        TasksApplicationComponent appCmp =
                ApplicationManager.getApplication().getComponent(TasksApplicationComponent.class);
        TaskSettings settings = appCmp.getSettings();

        TaskPriority priority;

        if (settings.isPropagatePriority()) {
            priority = findHighestPriorityRecursively(task, settings.isPriorityPropagatedOneLevelOnly());
        } else {
            priority = task.getPriority();
        }

        return findIcon(priority);
    }

    public static Icon findIcon(@NotNull TaskPriority priority) {
        switch (priority) {
            case Normal:
                return getIcon(ICON_DEFAULT_PRIORITY);
            case Important:
                return getIcon(ICON_IMPORTANT_PRIORITY);
            case Questionable:
                return getIcon(ICON_QUESTION_PRIORITY);
            default:
                return getIcon(ICON_DEFAULT_PRIORITY);
        }
    }

    @NotNull
    public static Icon findIcon(@NotNull TaskHighlightingType highlightingType) {
        switch (highlightingType) {
            case Red:
                return getIcon(ICON_STAR_RED);
            case Yellow:
                return getIcon(ICON_STAR_YELLOW);
            case Green:
                return getIcon(ICON_STAR_GREEN);
            default:
                throw new IllegalArgumentException("unknown highlighting type: " + highlightingType.toString());
        }
    }

    private static TaskPriority findHighestPriorityRecursively(ITask task, boolean onlyFirstLevel) {
        TaskPriority taskPriority = task.getPriority();

        for (int i = 0; i < task.size(); i++) {
            ITask subTask = task.get(i);

            if (!onlyFirstLevel)
                taskPriority = taskPriority.max(findHighestPriorityRecursively(subTask, false));
            else
                taskPriority = taskPriority.max(subTask.getPriority());
        }

        return taskPriority;
    }

    @NotNull
    public static JTree createTaskTree(@NotNull TreeModel model, @NotNull TaskController taskController,
                                       @NotNull JPopupMenu popupMenu) {
        DNDTree tasksTree = new DNDTree();
        tasksTree.setShowsRootHandles(true);
        UIUtil.setLineStyleAngled(tasksTree);
        tasksTree.setCellRenderer(createTasksTreeCellRenderer());
        tasksTree.setRootVisible(false);
        tasksTree.setModel(model);
        tasksTree.setTaskController(taskController);
        tasksTree.addMouseListener(new TaskTreeMouseAdapter(popupMenu));

        ToolTipManager.sharedInstance().registerComponent(tasksTree);

        return tasksTree;
    }

    @SuppressWarnings({"SameParameterValue"})
    @NotNull
    public static JPopupMenu createTaskTreePopup(String actionGroupId) {
        ActionGroup actionGroup = (ActionGroup) ActionManager.getInstance().getAction(actionGroupId);
        actionGroup.setPopup(true);
        ActionPopupMenu popupMenu =
                ActionManager.getInstance().createActionPopupMenu(DEFAULT_ACTION_GROUP_PLACE, actionGroup);
        return popupMenu.getComponent();
    }

    @NotNull
    public static TaskTreeModel createTaskTreeModel(@NotNull ITaskModel model) {
        return new TaskTreeModel(model);
    }

    @NotNull
    public static TaskTreeModel createModuleTaskTreeModel(@NotNull ITaskModel model) {
        return new TaskTreeModel(model);
    }

    @NotNull
    public static TaskTreeModel createProjectTaskTreeModel(@NotNull ITaskModel model) {
        return new TaskTreeModel(model);
    }

    @NotNull
    public static TaskTreeModel createGlobalTaskTreeModel(@NotNull ITaskModel model) {
        return new TaskTreeModel(model);
    }

    @NotNull
    private static TreeCellRenderer createTasksTreeCellRenderer() {
        return new TaskTreeCellRenderer();
    }

    @NotNull
    public static ToolTipUI getProgressTooltipUI() {
        return progressTooltipUI;
    }
}
