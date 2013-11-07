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
package org.dubik.tasks.utils;

import org.dubik.tasks.model.TaskHighlightingType;
import org.dubik.tasks.model.TaskPriority;
import org.jdom.Element;

/**
 * @author Sergiy Dubovik
 */
public class ExternalizeSupport {
    @SuppressWarnings({"SameParameterValue"})
    static public boolean getSafelyBoolean(Element el, String attrName, boolean defaultValue) {
        String attrStr = el.getAttributeValue(attrName);
        try {
            return Boolean.parseBoolean(attrStr);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    static public long getSafelyLong(Element el, String attrName, long defaultValue) {
        String attrStr = el.getAttributeValue(attrName);
        try {
            return Long.parseLong(attrStr);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @SuppressWarnings({"SameParameterValue"})
    static public TaskPriority getSafelyTaskPriority(Element el, String attrName, TaskPriority defaultValue) {
        String attrStr = el.getAttributeValue(attrName);
        try {
            return TaskPriority.valueOf(attrStr);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    static public TaskHighlightingType getSafelyHighlightingType(Element el, String attrName,
                                                                 TaskHighlightingType defaultValue) {
        String attrStr = el.getAttributeValue(attrName);
        try {
            return TaskHighlightingType.valueOf(attrStr);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
