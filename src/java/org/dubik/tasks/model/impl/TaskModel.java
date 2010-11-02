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
package org.dubik.tasks.model.impl;

import org.dubik.tasks.model.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Vector;

/**
 * @author Sergiy Dubovik
 */
public class TaskModel implements ITaskModel {
    private List<ITask> tasks;

    private List<ITaskModelChangeListener> changeListeners = new Vector<ITaskModelChangeListener>();

    public TaskModel() {
        tasks = new Vector<ITask>();
    }

    public ITask addTask(String title, TaskPriority priority, long estimatedTime) {
        assert title != null;

        return addTask(title, priority, estimatedTime, System.currentTimeMillis());
    }

    public ITask addTask(String title, TaskPriority priority, long estimatedTime, long creationTime) {
        return addTask(null, title, priority, estimatedTime, creationTime, false, false);
    }

    public ITask addTask(ITask parent, String title, TaskPriority priority, long estimatedTime,
                         long creationTime, boolean completed, boolean highlighed) {
        Task task = new Task(title, priority, estimatedTime);
        task.setCreationTime(creationTime);
        task.setCompleted(completed);
        task.setHighlighted(highlighed);
        task.setParent(parent);

        if (parent == null) {
            tasks.add(task);
        } else {
            parent.add(task);
        }

        fireAddTaskEvent(task);

        return task;
    }

    public ITask addTask(ITask parent, String title, TaskPriority priority, long estimatedTime, long actualTime,
                         long creationTime, boolean completed, boolean highlighted) {
        Task task = (Task) addTask(parent, title, priority, estimatedTime, creationTime, completed, highlighted);
        task.setActualTime(actualTime);

        return task;
    }

    public void addTask(ITask parentTask, String title, TaskPriority priority, long estimatedTime) {
        Task task = new Task(title, priority, estimatedTime);
        parentTask.add(task);
        task.setParent(parentTask);
        fireAddTaskEvent(task);
    }

    protected void addTask(ITask task) {
        assert task != null;
        tasks.add(task);
        fireAddTaskEvent(task);
    }

    public void updateTask(ITask task, ITask parent, String title, TaskPriority priority, long estimatedTime) {
        assert task != null;

        firePreChangeTaskEvent(task);

        Task mutableTask = (Task) task;
        mutableTask.setTitle(title);
        mutableTask.setPriority(priority);
        mutableTask.setEstimatedTime(estimatedTime);

        // Update parents only if it was changed
        if (parent != task.getParent()) {
            Task mutableOldParent = (Task) task.getParent();
            if (mutableOldParent != null) {
                mutableOldParent.remove(task);
                if (parent == null) {
                    tasks.add(task);
                }
            } else {
                tasks.remove(task);
            }

            if (parent != null) {
                Task mutableParent = (Task) parent;
                mutableParent.add(task);
            }

            mutableTask.setParent(parent);
        }

        fireChangeTaskEvent(task);
    }

    public void updateActualTime(ITask task, long actualTime) {
        assert task != null;
        assert actualTime >= 0;

        firePreChangeTaskEvent(task);

        Task mutableTask = (Task) task;
        mutableTask.setActualTime(actualTime);

        fireChangeTaskEvent(task);
    }

    public void deleteTask(ITask task) {
        assert task != null;

        firePreDeleteTaskEvent(task);
        ITask parent = task.getParent();
        if (parent == null)
            tasks.remove(task);
        else {
            Task mutableParent = (Task) parent;
            mutableParent.remove(task);
        }
        fireDeleteTaskEvent(task);
    }

    public void completeTask(ITask task) {
        assert task != null;

        firePreChangeTaskEvent(task);

        Task mutableTask = (Task) task;
        mutableTask.setCompleted(true);

        fireChangeTaskEvent(task);
    }

    public void uncompleteTask(ITask task) {
        assert task != null;

        firePreChangeTaskEvent(task);

        Task mutableTask = (Task) task;
        mutableTask.setCompleted(false);

        fireChangeTaskEvent(task);
    }

    public void highlightTask(ITask task) {
        assert task != null;

        firePreChangeTaskEvent(task);

        Task mutableTask = (Task) task;
        mutableTask.setHighlighted(true);

        fireChangeTaskEvent(task);
    }

    public void unhighlightTask(ITask task) {
        assert task != null;

        firePreChangeTaskEvent(task);

        Task mutableTask = (Task) task;
        mutableTask.setHighlighted(false);

        fireChangeTaskEvent(task);
    }

    public int size() {
        return tasks.size();
    }

    @NotNull
    public ITask getTask(int index) {
        return tasks.get(index);
    }

    public void setTask(int index, @NotNull ITask task) {
        tasks.add(index, task);
        fireChangeTaskEvent(task);
    }

    public void addChangeListener(ITaskModelChangeListener listener) {
        assert listener != null;

        changeListeners.add(listener);
    }

    public void removeChangeListener(ITaskModelChangeListener listener) {
        assert listener != null;

        changeListeners.remove(listener);
    }

    private void fireAddTaskEvent(ITask task) {
        TaskChangeEvent event = new TaskChangeEvent(task);
        for (ITaskModelChangeListener listener : changeListeners)
            listener.handleAddTaskEvent(event);
    }

    private void firePreDeleteTaskEvent(ITask task) {
        TaskChangeEvent event = new TaskChangeEvent(task);
        for (ITaskModelChangeListener listener : changeListeners)
            listener.handlePreDeleteTaskEvent(event);
    }

    private void fireDeleteTaskEvent(ITask task) {
        TaskChangeEvent event = new TaskChangeEvent(task);
        for (ITaskModelChangeListener listener : changeListeners)
            listener.handleDeleteTaskEvent(event);
    }

    private void firePreChangeTaskEvent(ITask task) {
        TaskChangeEvent event = new TaskChangeEvent(task);
        for (ITaskModelChangeListener listener : changeListeners)
            listener.handlePreChangeTaskEvent(event);
    }

    private void fireChangeTaskEvent(ITask task) {
        TaskChangeEvent event = new TaskChangeEvent(task);
        for (ITaskModelChangeListener listener : changeListeners)
            listener.handleChangeTaskEvent(event);
    }
}