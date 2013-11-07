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
package org.dubik.tasks.ui.tree.dnd;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

/**
 * @author Sergiy Dubovik
 */
public class MoveTaskTransferHandler extends TransferHandler {
    public MoveTaskTransferHandler() {
        super();
    }


    protected Transferable createTransferable(JComponent source) {
        System.out.println("Creating transferable from ");
        return new GenericTransferable(((JTree) source).getSelectionPaths());
    }

    public int getSourceActions(JComponent jComponent) {
        return TransferHandler.MOVE;
    }

    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
        System.out.println("Transfer Flavors: " + transferFlavors.length + " : " + transferFlavors[0].toString());
        return true;
    }

    public boolean importData(JComponent comp, Transferable t) {
        return true;
    }

    /*
    protected void exportDone(JComponent source, Transferable data, int action) {
        super.exportDone(source, data, action);
    }



    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
        //System.out.println("MoveTaskTransferHandler.canImport");
        // transferFlavors.length != 0 && transferFlavors[0].equals(GenericTransferable.flavors[0]);
        return true;
    }

    public boolean importData(JComponent comp, Transferable t) {
        //System.out.println("MoveTaskTransferHandler.importData");
        return false;
    }
    */
}
