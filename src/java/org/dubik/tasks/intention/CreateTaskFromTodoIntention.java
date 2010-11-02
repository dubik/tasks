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
package org.dubik.tasks.intention;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.PsiSearchHelper;
import com.intellij.psi.search.TodoItem;
import com.intellij.util.IncorrectOperationException;
import org.dubik.tasks.ui.actions.AddNewTaskAction;
import org.jetbrains.annotations.NotNull;

/**
 * Create Task from TO DO item intention. It checks if to do item is under cursor and if
 * user chooses to create a task, extracts to do from a comment.
 *
 * @author Sergiy Dubovik
 */
public class CreateTaskFromTodoIntention implements IntentionAction {
    /**
     * Intention name.
     *
     * @return intention name
     */
    @NotNull
    public String getText() {
        return "Create task";
    }

    /**
     * Intention family name.
     *
     * @return intention family name
     */
    @NotNull
    public String getFamilyName() {
        return "Create Task";
    }

    /**
     * Checks if intentetion is available for specified project, editor and file.
     *
     * @param project current project reference
     * @param editor  current editor reference
     * @param file    current file
     * @return <code>true</code> if intention is available, otherwise <code>false</code>
     */
    public boolean isAvailable(Project project, Editor editor, PsiFile file) {
        int offset = editor.getCaretModel().getOffset();
        final PsiElement element = file.findElementAt(offset);

        if (element == null || !element.isWritable())
            return false;

        if (element instanceof PsiComment) {
            PsiComment psiComment = (PsiComment) element;
            return getTodoItem(file, psiComment, offset) != null;
        }

        return false;
    }

    /**
     * Performs intention.
     *
     * @param project current project
     * @param editor  current editor
     * @param file    current file
     * @throws IncorrectOperationException can be thrown if operation is incorrect :)
     */
    public void invoke(Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        int offset = editor.getCaretModel().getOffset();
        final PsiElement element = file.findElementAt(offset);
        if (element instanceof PsiComment) {
            PsiComment psiComment = (PsiComment) element;
            TodoItem todoItem = getTodoItem(file, psiComment, offset);
            String todoText = todoItem.getTextRange().substring(editor.getDocument().getText());

            AddNewTaskAction anAction = (AddNewTaskAction) ActionManager.getInstance().getAction("AddNewTask");
            anAction.actionPerformed(project, todoText);
        }
    }

    /**
     * Indicate whether this action should be invoked inside write action.
     *
     * @return true if the intention requires a write action, false otherwise.
     */
    public boolean startInWriteAction() {
        return true;
    }

    /**
     * Gets to do item from a comment if cursor points to it.
     *
     * @param file         file where search has to be performed
     * @param comment      comment psi element
     * @param editorOffset editor's caret offset
     * @return instance of to do item if found, otherwise <code>null</code>
     */
    private TodoItem getTodoItem(PsiFile file, PsiComment comment, int editorOffset) {
        final PsiManager psiManager = comment.getManager();
        final PsiSearchHelper searchHelper = psiManager.getSearchHelper();
        TextRange commentTextRange = comment.getTextRange();
        final TodoItem[] todoItems = searchHelper.findTodoItems(file,
                commentTextRange.getStartOffset(), commentTextRange.getEndOffset());
        for (final TodoItem todoItem : todoItems) {
            final TextRange todoTextRange = todoItem.getTextRange();
            if (todoTextRange.contains(editorOffset))
                return todoItem;
        }

        return null;
    }
}
