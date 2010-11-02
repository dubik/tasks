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

/**
 * Represents a logical group of tasks. You can use it group tasks by some criteria.
 * To control which tasks should belong to the group, set task filter.
 *
 * @author Sergiy Dubovik
 */
public interface ITaskGroup extends ITask {
    /**
     * Adds task group.
     *
     * @param taskGroup task group
     */
    public void add(ITaskGroup taskGroup);

    /**
     * Size of the task group.
     *
     * @return amount of items in task group
     */
    public int size();

    /**
     * Returns tasks at specified index.
     *
     * @param index index of task
     * @return task
     */
    public ITask get(int index);

    /**
     * Sets task filter.
     *
     * @param filter task filter
     */
    public void setTaskFilter(ITaskFilter filter);

    /**
     * Returns title of task group.
     *
     * @return title
     */
    public String getTitle();

    /**
     * Sets task model. Tasks will be taken from there.
     *
     * @param model task model
     */
    public void setTaskModel(ITaskModel model);
}
