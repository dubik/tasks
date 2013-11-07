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

import javax.swing.*;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.HashSet;
import java.util.Set;

public class TreeRefresher {
    private JTree tree;
    private TreeController treeController;
    private Set<Object> expandedNodes = new HashSet<Object>();

    public TreeRefresher(JTree tree, TreeController treeController) {
        this.tree = tree;
        this.treeController = treeController;
    }

    private void storeExpansions() {
        expandedNodes.clear();
        TreeModel treeModel = tree.getModel();
        Object root = treeModel.getRoot();
        for (int i = 0; i < treeModel.getChildCount(root); i++) {
            Object child = treeModel.getChild(root, i);
            storeExpansions(new TreePath(root).pathByAddingChild(child));
        }
    }

    private void storeExpansions(TreePath path) {
        if (tree.isExpanded(path)) {
            TreeModel treeModel = tree.getModel();
            Object obj = path.getLastPathComponent();
            expandedNodes.add(obj);
            for (int i = 0; i < treeModel.getChildCount(obj); i++) {
                Object child = treeModel.getChild(obj, i);
                storeExpansions(path.pathByAddingChild(child));
            }
        }
    }

    private void restoreExpansions() {
        for (Object object : expandedNodes) {
            treeController.expandToObject(object);
        }
    }

    public void refresh() {
        refresh(tree.getModel().getRoot());
    }

    public void refresh(Object object) {
        TreePath path = treeController.pathToObject(object);
        expandedNodes.clear();
        storeExpansions(path);
        treeController.refreshTree(path);
        restoreExpansions();
    }
}