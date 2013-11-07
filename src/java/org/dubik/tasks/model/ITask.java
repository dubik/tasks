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
package org.dubik.tasks.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for a task.
 *
 * @author Sergiy Dubovik
 */
public interface ITask {
    /**
     * Returns task's title.
     *
     * @return task's title
     */
    @Nullable
    public String getTitle();

    /**
     * Return's task's priority.
     *
     * @return task's priority
     */
    @NotNull
    public TaskPriority getPriority();

    /**
     * Returns estimated time for a task.
     *
     * @return estimated time
     */
    public long getEstimatedTime();

    /**
     * Returns actual time spend on task. If task has sub tasks,
     * it returns accumulated actual time.
     *
     * @return actual time
     */
    public long getActualTime();

    /**
     * Return's task's creation time.
     *
     * @return creation time
     */
    public long getCreationTime();

    /**
     * Checks whethe the task is complete.
     *
     * @return <code>true</code> if complete, <code>false</code> otherwise
     */
    public boolean isCompleted();

    /**
     * Checks whethe the task is highlighted.
     *
     * @return <code>true</code> if complete, <code>false</code> otherwise
     */
    public boolean isHighlighted();

    /**
     * Returns task's highlighting type.
     *
     * @return task's highlighting type
     */
    @NotNull
    public TaskHighlightingType getHighlightingType();

    /**
     * Returns completion ratio - 0% - 100%.
     *
     * @return 0% - no completed tasks, 100% all sub tasks completed
     */
    public int getCompletionRatio();

    /**
     * Adds sub task.
     *
     * @param task reference to a sub task, can't be null.
     */
    public void add(@NotNull ITask task);

    /**
     * Returns amount of sub tasks.
     *
     * @return amount of sub tasks
     */
    public int size();

    /**
     * Returns sub task with a given index.
     *
     * @param index index of sub task
     * @return sub task reference
     */
    public ITask get(int index);

    public ITask getParent();

    public int indexOf(ITask subTask);

    void moveUp(ITask task);

    void moveDown(ITask task);
}
