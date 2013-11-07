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
package org.dubik.tasks.ui.clipboard;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

/**
 * Class which encapsulates text manipulations on the clipboard.
 *
 * @author Sergiy Dubovik
 */
public class TextTransfer implements ClipboardOwner {
    /**
     * Creates <code>TextTransfer</code> object.
     */
    public TextTransfer() {
    }

    /**
     * Puts specified string to the clipboard and makes this class the owner
     * of the clipbaord's content.
     *
     * @param content string which should be on clipboard
     */
    public void setClipboardContent(String content) {
        StringSelection stringSelection = new StringSelection(content);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, this);
    }

    /**
     * Get the String residing on the clipboard.
     *
     * @return any text found on the Clipboard; if none found, return an
     *         empty String.
     */
    public String getClipboardContents() {
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        //odd: the Object param of getContents is not currently used
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText =
                (contents != null) &&
                        contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText) {
            try {
                result = (String) contents.getTransferData(DataFlavor.stringFlavor);
            }
            catch (UnsupportedFlavorException ex) {
                //highly unlikely since we are using a standard DataFlavor
                System.out.println(ex);
                ex.printStackTrace();
            }
            catch (IOException ex) {
                System.out.println(ex);
                ex.printStackTrace();
            }
        }

        return result;
    }

    /**
     * Notifies this object that it is no longer the clipboard owner.
     * This method will be called when another application or another
     * object within this application asserts ownership of the clipboard.
     *
     * @param clipboard the clipboard that is no longer owned
     * @param contents  the contents which this owner had placed on the clipboard
     */
    public void lostOwnership(Clipboard clipboard, Transferable contents) {

    }
}
