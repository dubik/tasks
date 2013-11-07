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
    private ITaskFilter taskFilter;
    private TreeRefresher refresher;

    public TaskTreeModel(ITaskModel taskModel) {
        this.taskModel = taskModel;

        taskModel.addChangeListener(this);

        root = new TaskGroup("All Tasks");
        root.setTaskModel(taskModel);
    }

    public void setRefresher(TreeRefresher refresher) {
        this.refresher = refresher;
    }

    public Object getRoot() {
        return root;
    }

    public Object getChild(Object parent, int index) {
        if (parent instanceof ITask) {

            ITask task = (ITask) parent;

            if (taskFilter != null) {
                int taskIndex;

                for (taskIndex = 0; taskIndex < task.size(); taskIndex++) {
                    if (taskFilter.accept(task.get(taskIndex))) {
                        if (index == 0)
                            return task.get(taskIndex);

                        index--;
                    }
                }
            } else {
                return task.get(index);
            }
        }

        return "Null";
    }

    public int getChildCount(Object parent) {
        int size = 0;
        if (parent instanceof ITask) {
            ITask task = (ITask) parent;
            size = task.size();
            if (taskFilter != null) {
                for (int i = 0; i < task.size(); i++) {
                    if (!taskFilter.accept(task.get(i)))
                        size--;
                }
            }
        }

        return size;
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

    public void setTaskFilter(ITaskFilter taskFilter) {
        this.taskFilter = taskFilter;
        updateTree();
    }

    private void updateTree() {
        if (refresher != null)
            refresher.refresh();
        else
            fireTreeStructureChanged(new TreeModelEvent(this, new Object[]{root}));

    }

    public void handleAddTaskEvent(TaskChangeEvent event) {
        updateTree();
    }

    public void handlePreDeleteTaskEvent(TaskChangeEvent event) {
    }

    public void handleDeleteTaskEvent(TaskChangeEvent event) {
        updateTree();
    }

    public void handlePreChangeTaskEvent(TaskChangeEvent event) {
    }

    public void handleChangeTaskEvent(TaskChangeEvent event) {
        updateTree();
    }

    public Object[] findPathToObject(Object root, Object task) {
        List<Object> path = new Vector<Object>();
        findPathToObject(root, task, path);
        Collections.reverse(path);
        Object[] oPath = new Object[path.size()];
        oPath = path.toArray(oPath);
        return oPath;
    }

    private boolean findPathToObject(Object parent, Object task, List<Object> path) {
        if (parent == task) {
            return true;
        }

        for (int i = 0; i < getChildCount(parent); i++) {
            if (findPathToObject(getChild(parent, i), task, path)) {
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
        updateTree();
    }
}