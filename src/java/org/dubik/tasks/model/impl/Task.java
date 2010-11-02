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

import org.dubik.tasks.model.ITask;
import org.dubik.tasks.model.TaskPriority;
import org.jetbrains.annotations.NotNull;

import java.util.Vector;

/**
 * @author Sergiy Dubovik
 */
public class Task implements ITask {
    private String title;
    private TaskPriority priority;
    private long estimatedTime;
    private long actualTime;
    private long creationTime = System.currentTimeMillis();
    private boolean completed;
    private boolean highlighted;
    private Vector<ITask> subTasks = new Vector<ITask>();
    private ITask parent;

    public Task() {
    }

    public Task(String title) {
        this.title = title;
    }

    public Task(String title, TaskPriority priority) {
        this.title = title;
        this.priority = priority;
    }

    public Task(String title, TaskPriority priority, long estimatedTime) {
        this.title = title;
        this.priority = priority;
        this.estimatedTime = estimatedTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public long getEstimatedTime() {
        long estimated = 0;
        if (subTasks.size() > 0) {
            for (ITask task : subTasks)
                estimated += task.getEstimatedTime();
        } else {
            estimated = estimatedTime;
        }

        return estimated;
    }

    public long getActualTime() {
        long actual = 0;
        if (subTasks.size() > 0) {
            for (ITask task : subTasks)
                actual += task.getActualTime();
        } else {
            actual = actualTime;
        }

        return actual;
    }

    public void setActualTime(long actualTime) {
        this.actualTime = actualTime;
    }

    public void setEstimatedTime(long estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public boolean isCompleted() {
        boolean compl;
        if (subTasks.size() > 0) {
            compl = true;
            for (ITask task : subTasks)
                compl = compl && task.isCompleted();
            return compl;
        } else {
            compl = completed;
        }

        return compl;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public int getCompletionRatio() {
        int totalTasks = subTasks.size();
        if (totalTasks == 0)
            return isCompleted() ? 100 : 0;

        int cumCompletionRatio = 0;
        for (ITask task : subTasks) {
            cumCompletionRatio += task.getCompletionRatio();
        }

        return cumCompletionRatio / totalTasks;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public String toString() {
        return title;
    }

    public void add(@NotNull ITask task) {
        subTasks.add(task);
    }

    public int size() {
        return subTasks.size();
    }

    public ITask get(int index) {
        return subTasks.get(index);
    }

    public void setParent(ITask parent) {
        this.parent = parent;
    }

    public ITask getParent() {
        return parent;
    }

    public void remove(ITask task) {
        subTasks.remove(task);
    }
}