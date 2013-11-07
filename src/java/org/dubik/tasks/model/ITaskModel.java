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

/**
 * Task model contains all tasks (however sub tasks can not be accessed by this interface)
 *
 * @author Sergiy Dubovik
 */
public interface ITaskModel {
    /**
     * Returns size of the model.
     *
     * @return amount of tasks
     */
    public int size();

    /**
     * Returns task at specified index.
     *
     * @param index index of a task
     * @return task
     */
    public
    @NotNull
    ITask getTask(int index);

    /**
     * Updates task at specified index. Existing task will be replaced.
     *
     * @param index index of task to update
     * @param task  task which contains update
     */
    public void setTask(int index, @NotNull ITask task);

    /**
     * Adds change listener.
     *
     * @param listener change listener
     */
    public void addChangeListener(ITaskModelChangeListener listener);

    /**
     * Removes change listener.
     *
     * @param listener already registered change listener
     */
    public void removeChangeListener(ITaskModelChangeListener listener);

    /**
     * Deletes specified task from model. It also can delete sub tasks.
     *
     * @param task task to delete
     */
    public void deleteTask(ITask task);

    /**
     * Completes specified task. Note, it will be up to a task if it will be completed
     * or not. If it has sub tasks this call will have no effect.
     *
     * @param task task to complete
     */
    public void completeTask(ITask task);

    /**
     * Creates and adds task.
     *
     * @param title         task title
     * @param priority      task priority
     * @param estimatedTime estimated time to complete a task
     * @return created task
     */
    public ITask addTask(String title, TaskPriority priority, long estimatedTime);

    /**
     * Creates and adds task.
     *
     * @param title         task title
     * @param priority      task priority
     * @param estimatedTime estimated time to complete a task
     * @param creationTime  time when note has been created
     * @return created task
     */
    public ITask addTask(String title, TaskPriority priority, long estimatedTime, long creationTime);

    /**
     * Creates and adds task.
     *
     * @param parent        parent task
     * @param title         task title
     * @param priority      task priority
     * @param estimatedTime estimated time to complete a task
     * @param creationTime  time when note has been created
     * @param completed     indicates if task is completed or not
     * @param highlighted   indicates if task is highlighted or not
     * @return created task
     */
    public ITask addTask(ITask parent, String title, TaskPriority priority, long estimatedTime,
                         long creationTime, boolean completed, boolean highlighted);

    /**
     * Creates and adds task.
     *
     * @param parent        parent task
     * @param title         task title
     * @param priority      task priority
     * @param estimatedTime estimated time to complete a task
     * @param actualTime    elapsed time
     * @param creationTime  time when note has been created
     * @param completed     indicates if task is completed or not
     * @param highlighted   indicates if task is highlighted or not
     * @return created task
     */
    public ITask addTask(ITask parent, String title, TaskPriority priority, long estimatedTime,
                         long actualTime, long creationTime, boolean completed, boolean highlighted);

    /**
     * Update specified task.
     *
     * @param task          task to update
     * @param parent        new parent
     * @param title         new title
     * @param priority      new priority
     * @param estimatedTime new estimated time
     */
    void updateTask(ITask task, ITask parent, String title, TaskPriority priority, long estimatedTime);

    /**
     * Create and add task.
     *
     * @param parentTask    parent task
     * @param title         task title
     * @param priority      task priority
     * @param estimatedTime estimated time to complete a task
     */
    void addTask(ITask parentTask, String title, TaskPriority priority, long estimatedTime);

    /**
     * Sets task to uncomplete state.
     *
     * @param task task which must be set to uncomplete
     */
    void uncompleteTask(ITask task);

    /**
     * Highlight specified task.
     *
     * @param task task to highlight
     */
    void highlightTask(ITask task);

    /**
     * Unhighlight specified task.
     *
     * @param task task to unhighlight
     */
    void unhighlightTask(ITask task);

    /**
     * Updates task actual time.
     *
     * @param task       task which must be updated
     * @param actualTime new real time spent on task
     */
    void updateActualTime(ITask task, long actualTime);

    boolean canMoveUp(@NotNull ITask task);

    boolean canMoveDown(@NotNull ITask task);

    void moveUp(@NotNull ITask task);

    void moveDown(@NotNull ITask task);

    void setTaskHighlightingType(ITask task, TaskHighlightingType hightlightingType);
}
