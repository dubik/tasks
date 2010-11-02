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

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Sergiy Dubovik
 */
public class TaskTreeMouseAdapter extends MouseAdapter {
    private JPopupMenu popupMenu;

    public TaskTreeMouseAdapter(JPopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }

    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            maybeShowPopup(e);
        } else if (e.getClickCount() == 2) {
            DataContext context = DataManager.getInstance().getDataContext();
            AnAction anAction = ActionManager.getInstance().getAction("TaskPropertyAction");
            AnActionEvent event = new AnActionEvent(null, context, "", anAction.getTemplatePresentation(),
                    ActionManager.getInstance(), 0);
            anAction.actionPerformed(event);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            maybeShowPopup(e);
        }
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            if (e.getComponent() instanceof JTree) {
                JTree tree = (JTree) e.getComponent();
                popupMenu.show(tree, e.getX(), e.getY());
            }
        }
    }
}
