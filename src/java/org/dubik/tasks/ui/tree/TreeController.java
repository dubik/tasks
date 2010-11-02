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

import org.dubik.tasks.model.ITask;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.tree.TreePath;

/**
 * @author Sergiy Dubovik
 */
public class TreeController {
    private TaskTreeModel treeModel;
    private JTree tree;
    private boolean groupedByPriority;

    public TreeController(TaskTreeModel treeModel, JTree tree) {
        this.treeModel = treeModel;
        this.tree = tree;
        groupedByPriority = false;
    }

    public void groupByPriority() {
        groupedByPriority = !groupedByPriority;
        treeModel.groupByPriority(groupedByPriority);

        if (groupedByPriority) {
            expandWholeTreeOneLevel(treeModel.getRoot());
        }
    }

    public boolean isGroupByPriority() {
        return groupedByPriority;
    }

    private void expandWholeTreeOneLevel(Object root) {
        TreePath rootPath = new TreePath(root);
        for (int i = 0; i < treeModel.getChildCount(root); i++) {
            Object child = treeModel.getChild(root, i);
            TreePath pathToChild = rootPath.pathByAddingChild(child);
            tree.expandPath(pathToChild);
        }
    }

    public void expandToTask(ITask task) {
        Object[] path = treeModel.findPathToTask(treeModel.getRoot(), task);
        tree.expandPath(new TreePath(path).pathByAddingChild(task));
    }

    public void refreshTree() {
        refreshTreeRecursively(new TreePath(treeModel.getRoot()));
    }

    private void refreshTreeRecursively(TreePath path) {
        treeModel.fireTreeNodesChanged(new TreeModelEvent(this, path));

        Object root = path.getLastPathComponent();
        for (int i = 0; i < treeModel.getChildCount(root); i++) {
            refreshTreeRecursively(path.pathByAddingChild(treeModel.getChild(root, i)));
        }
    }
}
