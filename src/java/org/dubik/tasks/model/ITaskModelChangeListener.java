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
 * Model change listener. Extend this interface and register it in the model.
 *
 * @author Sergiy Dubovik
 */
public interface ITaskModelChangeListener {
    /**
     * Fired when new task added to a model.
     *
     * @param event task add event
     */
    public void handleAddTaskEvent(TaskChangeEvent event);

    /**
     * Fired just before deletion will occure. You can do some
     * preprocessing here.
     *
     * @param event event task pre delete event
     */
    public void handlePreDeleteTaskEvent(TaskChangeEvent event);

    /**
     * Fired after task deletion occurded.
     *
     * @param event event task delete event
     */
    public void handleDeleteTaskEvent(TaskChangeEvent event);

    /**
     * Fired just before task change will occure. You can do some
     * preprocessing here.
     *
     * @param event event task pre delete event
     */
    public void handlePreChangeTaskEvent(TaskChangeEvent event);

    /**
     * Fired after task change event occured.
     *
     * @param event event task change event
     */
    public void handleChangeTaskEvent(TaskChangeEvent event);
}
