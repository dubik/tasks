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
package org.dubik.tasks.ui.containers;

import javax.swing.*;

/**
 * @author Sergiy Dubovik
 */
public class ScopedTasksPanel extends JPanel {
    private JTree moduleTaskTree;
    private JTree projectTaskTree;
    private JTree globalTaskTree;

    public ScopedTasksPanel(JTree globalTaskTree, JTree projectTaskTree, JTree moduleTaskTree) {
        this.moduleTaskTree = moduleTaskTree;
        this.projectTaskTree = projectTaskTree;
        this.globalTaskTree = globalTaskTree;
    }

    public JTree getModuleTaskTree() {
        return moduleTaskTree;
    }

    public JTree getProjectTaskTree() {
        return projectTaskTree;
    }

    public JTree getGlobalTaskTree() {
        return globalTaskTree;
    }
}
