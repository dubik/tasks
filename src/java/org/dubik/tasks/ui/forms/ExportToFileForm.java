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

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;

/**
 * @author Sergiy Dubovik
 */
public class ExportToFileForm extends DialogWrapper {
    private Project project;
    private JTextArea previewTextArea;
    private JPanel container;
    private JPanel browsePanel;
    private JLabel errorLabel;
    private TextFieldWithBrowseButton textFieldWithBrowseButton;


    private Action exportToClipboardAction;
    private boolean exportToClipboard;

    public ExportToFileForm(Project project) {
        super(project, false);

        this.project = project;

        setTitle("Export Tasks");
        init();
        setSize(640, 580);
    }

    private void createUIComponents() {
        textFieldWithBrowseButton = new TextFieldWithBrowseButton();
        FileChooserDescriptor descriptor = new FileChooserDescriptor(true, true, false, false, false, false);
        textFieldWithBrowseButton.addBrowseFolderListener("Select file", "Choose a file for exported tasks", project, descriptor);
        this.browsePanel = textFieldWithBrowseButton;

        textFieldWithBrowseButton.getTextField().addInputMethodListener(new InputMethodListener() {
            public void inputMethodTextChanged(InputMethodEvent event) {
                validateForm();
            }

            public void caretPositionChanged(InputMethodEvent event) {
                validateForm();
            }
        });

        textFieldWithBrowseButton.getTextField().addKeyListener(new KeyListener() {
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
        File file = getFile();
        if (file.isDirectory()) {
            errorLabel.setText("Please select or type a valid file path");
            getOKAction().setEnabled(false);
        } else {
            errorLabel.setText("");
            getOKAction().setEnabled(true);
        }
    }

    public JPanel getBrowsePanel() {
        return browsePanel;
    }

    @SuppressWarnings({"BooleanMethodIsAlwaysInverted"})
    public boolean isExportToClipboard() {
        return exportToClipboard;
    }

    @Nullable
    protected JComponent createCenterPanel() {
        return container;
    }

    public void setPreview(@Nullable String preview) {
        previewTextArea.setText(preview);
    }

    private void exportToClipboard() {
        exportToClipboard = true;
    }

    protected void createDefaultActions() {
        super.createDefaultActions();

        exportToClipboardAction = new ExportToClipboardAction();
    }

    protected Action[] createActions() {
        return new Action[]{exportToClipboardAction, getOKAction(), getCancelAction()};
    }

    public File getFile() {
        return new File(textFieldWithBrowseButton.getText());
    }

    class ExportToClipboardAction extends AbstractAction {
        public ExportToClipboardAction() {
            super("Copy to Clipboard");
        }

        public void actionPerformed(ActionEvent event) {
            exportToClipboard();
            doOKAction();
        }
    }
}
