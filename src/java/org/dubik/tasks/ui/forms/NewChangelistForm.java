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
package org.dubik.tasks.ui.forms;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author Sergiy Dubovik
 */
public class NewChangelistForm extends DialogWrapper {
    private JTextField nameTextField;
    private JCheckBox makeThisChangelistActiveCheckBox;
    private JPanel container;
    private JTextArea commentsTextArea;
    private Project project;

    public NewChangelistForm(Project project) {
        super(project, false);
        this.project = project;
        init();
        setTitle("New Changelist");
        makeThisChangelistActiveCheckBox.setSelected(true);
        nameTextField.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                validateForm();
            }
        });
    }

    private void validateForm() {
        if (ChangeListManager.getInstance(project).findChangeList(getName()) != null) {
            setOKActionEnabled(false);
        } else {
            setOKActionEnabled(true);
        }
    }

    @Nullable
    protected JComponent createCenterPanel() {
        return container;
    }

    @NotNull
    public String getName() {
        return nameTextField.getText();
    }

    @NotNull
    public String getDescription() {
        return commentsTextArea.getText();
    }

    public void setName(String name) {
        nameTextField.setText(name);
    }

    public void setDescription(String description) {
        commentsTextArea.setText(description);
    }

    public boolean isNewChangelistActive() {
        return makeThisChangelistActiveCheckBox.isSelected();
    }
}
