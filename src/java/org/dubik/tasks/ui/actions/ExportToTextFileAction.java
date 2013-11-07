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
package org.dubik.tasks.ui.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogWrapper;
import org.dubik.tasks.model.ITask;
import org.dubik.tasks.ui.clipboard.TextTransfer;
import org.dubik.tasks.ui.forms.ExportToFileForm;
import org.dubik.tasks.ui.tree.TaskTreeModel;

import javax.swing.*;
import java.io.*;

/**
 * @author Sergiy Dubovik
 */
public class ExportToTextFileAction extends BaseTaskAction {
    public ExportToTextFileAction() {
    }

    public void actionPerformed(AnActionEvent e) {
        ExportToFileForm form = new ExportToFileForm(getProject(e));
        TaskTreeModel model = getTreeController(e).getTreeModel();

        OutputStream os = new ByteArrayOutputStream();
        writeTasks(model, os);

        form.setPreview(os.toString());

        form.show();
        if (form.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
            if (!form.isExportToClipboard()) {
                File file = form.getFile();
                if (file.exists()) {
                    if (JOptionPane.showConfirmDialog(null, "File already exists, do you really want to overwrite it?",
                            "Export Tasks", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        writeTasks(file, os.toString());
                    }
                } else {
                    writeTasks(file, os.toString());
                }
            } else {
                new TextTransfer().setClipboardContent(os.toString());
            }
        }
    }

    private void writeTasks(File file, String text) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(text.getBytes());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null)
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
        }
    }

    private void writeTasks(TaskTreeModel model, OutputStream os) {
        PrintWriter out = new PrintWriter(os);

        ITask root = (ITask) model.getRoot();
        for (int i = 0; i < root.size(); i++) {
            ITask task = root.get(i);
            writeTasksInternally(task, 0, out);
        }

        out.flush();
        out.close();
    }

    private void writeTasksInternally(ITask task, int depthLevel, PrintWriter out) {
        out.print(makeIndent(depthLevel));
        out.println(task.getTitle());

        for (int i = 0; i < task.size(); i++) {
            writeTasksInternally(task.get(i), depthLevel + 1, out);
        }
    }

    private String makeIndent(int depthLevel) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < depthLevel; i++) {
            buf.append("    ");
        }

        return buf.toString();
    }
}
