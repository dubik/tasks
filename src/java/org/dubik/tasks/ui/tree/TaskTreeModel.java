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
package org.dubik.tasks.ui.tree;

import org.dubik.tasks.model.*;
import org.dubik.tasks.model.impl.TaskGroup;
import org.dubik.tasks.ui.filters.PriorityFilter;

import javax.swing.event.TreeModelEvent;
import javax.swing.tree.TreePath;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Task tree model. Feeds tree with the data.
 *
 * @author Sergiy Dubovik
 */
public class TaskTreeModel extends AbstractTreeModel implements ITaskModelChangeListener {
    private ITaskGroup root;

    private ITaskModel taskModel;

    public TaskTreeModel(ITaskModel taskModel) {
        this.taskModel = taskModel;

        taskModel.addChangeListener(this);

        root = new TaskGroup("All Tasks");
        root.setTaskModel(taskModel);
    }

    public Object getRoot() {
        return root;
    }

    public Object getChild(Object parent, int index) {
        if (parent instanceof ITaskGroup)
            return ((ITaskGroup) parent).get(index);

        if (parent instanceof ITask)
            return ((ITask) parent).get(index);

        return null;
    }

    public int getChildCount(Object parent) {
        if (parent instanceof ITaskGroup)
            return ((ITaskGroup) parent).size();

        if (parent instanceof ITask)
            return ((ITask) parent).size();

        return 0;
    }

    public boolean isLeaf(Object node) {
        return getChildCount(node) == 0;
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    public int getIndexOfChild(Object parent, Object child) {
        for (int i = 0; i < getChildCount(parent); i++) {
            if (getChild(parent, i) == child)
                return i;
        }

        return -1;
    }

    public void handleAddTaskEvent(TaskChangeEvent event) {
        ITask task = event.getTask();
        Object[] path = findPathToTask(root, task);

        if (root.size() == 1) {
            fireTreeStructureChanged(new TreeModelEvent(this, new Object[]{root}));
        } else {
            Object parent = path[path.length - 1];
            int[] childIndices = new int[]{getIndexOfChild(parent, task)};
            fireTreeNodesInserted(new TreeModelEvent(this, path,
                    childIndices, new Object[]{task}));
        }
    }

    private int deletedIndex;
    private Object[] deletedPath = null;

    public void handlePreDeleteTaskEvent(TaskChangeEvent event) {
        ITask task = event.getTask();
        Object[] path = findPathToTask(root, task);

        if (path.length == 0) {
            deletedPath = null;
            deletedIndex = -1;
        } else {
            deletedPath = path;

            Object parent = path[path.length - 1];
            deletedIndex = getIndexOfChild(parent, task);
        }
    }

    public void handleDeleteTaskEvent(TaskChangeEvent event) {
        if (deletedIndex != -1 && deletedPath != null) {
            int[] childIndices = new int[]{deletedIndex};
            fireTreeNodesRemoved(new TreeModelEvent(this, deletedPath,
                    childIndices, new Object[]{event.getTask()}));
        }
    }

    private int changedIndex;
    private Object[] changedPath = null;

    public void handlePreChangeTaskEvent(TaskChangeEvent event) {
        ITask task = event.getTask();
        Object[] path = findPathToTask(root, task);

        changedPath = path;

        Object parent = path[path.length - 1];
        changedIndex = getIndexOfChild(parent, task);

    }

    public void handleChangeTaskEvent(TaskChangeEvent event) {
        Object[] newChangedPath = findPathToTask(root, event.getTask());
        if (isSame(changedPath, newChangedPath)) {
            TreePath treePath = new TreePath(changedPath).pathByAddingChild(event.getTask());
            while (treePath.getPathCount() > 1) {
                fireTreeNodesChanged(new TreeModelEvent(this, treePath));
                treePath = treePath.getParentPath();
            }
        } else {
            int[] childIndices = new int[]{changedIndex};
            fireTreeNodesRemoved(new TreeModelEvent(this, changedPath,
                    childIndices, new Object[]{event.getTask()}));

            Object parent = newChangedPath[newChangedPath.length - 1];
            int newIndex = getIndexOfChild(parent, event.getTask());

            int[] newChildIndeces = new int[]{newIndex};
            fireTreeNodesInserted(new TreeModelEvent(this, newChangedPath,
                    newChildIndeces, new Object[]{event.getTask()}));
        }
    }

    private boolean isSame(Object[] list1, Object[] list2) {
        if (list1.length != list2.length)
            return false;

        for (int i = 0; i < list1.length; i++) {
            if (list1[i] != list2[i])
                return false;
        }

        return true;
    }

    public Object[] findPathToTask(Object root, ITask task) {
        List<Object> path = new Vector<Object>();
        findPathToTask(root, task, path);
        Collections.reverse(path);
        Object[] oPath = new Object[path.size()];
        oPath = path.toArray(oPath);
        return oPath;
    }

    private boolean findPathToTask(Object parent, ITask task, List<Object> path) {
        if (parent == task) {
            return true;
        }

        for (int i = 0; i < getChildCount(parent); i++) {
            if (findPathToTask(getChild(parent, i), task, path)) {
                path.add(parent);
                return true;
            }
        }

        return false;
    }

    public void groupByPriority(boolean group) {
        ITaskGroup newRoot = new TaskGroup("All Tasks");

        if (group) {
            for (TaskPriority priority : TaskPriority.values()) {
                ITaskGroup taskGroup = new TaskGroup(priority.toString());
                taskGroup.setTaskModel(taskModel);
                taskGroup.setTaskFilter(new PriorityFilter(priority));

                newRoot.add(taskGroup);
            }
        } else {
            newRoot.setTaskModel(taskModel);
        }

        root = newRoot;
        fireTreeStructureChanged(new TreeModelEvent(this, new Object[]{root}));
    }
}