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
package org.dubik.tasks.ui.widgets;

import javax.swing.*;
import javax.swing.plaf.metal.MetalToolTipUI;
import java.awt.*;

/**
 * @author Sergiy Dubovik
 */
public class ProgressTooltipUI extends MetalToolTipUI {
    private JToolTip toolTip = new JToolTip();

    public ProgressTooltipUI() {
    }

    public void paint(Graphics g, JComponent c) {
        toolTip.getUI().paint(g, c);

        if (c instanceof ProgressTooltip) {
            ProgressTooltip tooltip = (ProgressTooltip) c;
            tooltip.getPercentage();

            Dimension d = getPreferredSize(c);


            int middlePoint = (int) ((d.getWidth() - 2) * tooltip.getPercentage());

            g.setColor(new Color(0x00B000));
            g.fillRect(4, (int) d.getHeight() - 6, middlePoint, 3);

            g.setColor(new Color(0xB00000));
            if (middlePoint < 4)
                middlePoint = 4;
            g.fillRect(middlePoint, (int) d.getHeight() - 6, (int) d.getWidth() - middlePoint - 4, 3);
        }
    }

    public Dimension getPreferredSize(JComponent c) {
        Dimension preferredSize = super.getPreferredSize(c);
        if (c instanceof ProgressTooltip) {
            preferredSize.setSize(preferredSize.getWidth(), preferredSize.getHeight() + 6);
        }

        return preferredSize;
    }
}
