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
 * Task priorities.
 *
 * @author Sergiy Dubovik
 */
public enum TaskPriority {
    Important("High"), Normal("Normal"), Questionable("Low");

    private String friendlyName;

    TaskPriority(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String toString() {
        return friendlyName;
    }

    /**
     * Creates a priority from a string.
     *
     * @param enumName string which must be parsed
     * @return corresponding priority
     * @throws IllegalArgumentException if enum was not find with specified name
     */
    public static TaskPriority parse(String enumName) {
        if (enumName.equals(Normal.friendlyName))
            return Normal;

        if (enumName.equals(Important.friendlyName))
            return Important;

        if (enumName.equals(Questionable.friendlyName))
            return Questionable;

        throw new IllegalArgumentException("enum is not found - " + enumName);
    }
}