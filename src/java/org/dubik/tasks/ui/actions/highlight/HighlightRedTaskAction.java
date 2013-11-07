package org.dubik.tasks.ui.actions.highlight;

import org.dubik.tasks.model.TaskHighlightingType;

/**
 *
 */
public class HighlightRedTaskAction extends BaseHighlightTaskAction {
    TaskHighlightingType getHightlightingType() {
        return TaskHighlightingType.Red;
    }
}
