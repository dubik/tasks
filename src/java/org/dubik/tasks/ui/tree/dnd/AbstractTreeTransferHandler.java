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

import org.dubik.tasks.model.ITask;
import org.dubik.tasks.ui.tree.TaskTreeModel;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.image.BufferedImage;

public abstract class AbstractTreeTransferHandler
        implements DragGestureListener, DragSourceListener, DropTargetListener {

    private DNDTree tree;
    private DragSource dragSource;
    private DropTarget dropTarget;
    private static ITask draggedNode;
    private static BufferedImage image = null; //buff image
    private Rectangle rect2D = new Rectangle();
    private boolean drawImage;

    protected AbstractTreeTransferHandler(DNDTree tree, int action, boolean drawIcon) {
        this.tree = tree;
        drawImage = drawIcon;
        dragSource = new DragSource();
        dropTarget = new DropTarget(tree, this);
        dragSource.createDefaultDragGestureRecognizer(tree, action, this);
    }

    /* Methods for DragSourceListener */
    public void dragDropEnd(DragSourceDropEvent dsde) {
        if (dsde.getDropSuccess() && dsde.getDropAction() == DnDConstants.ACTION_MOVE) {
        }
    }

    public final void dragEnter(DragSourceDragEvent dsde) {
        System.out.println("AbstractTreeTransferHandler.dragEnter");
        int action = dsde.getDropAction();
        if (action == DnDConstants.ACTION_COPY) {
            dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
        } else {
            if (action == DnDConstants.ACTION_MOVE) {
                dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
            } else {
                dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
            }
        }
    }

    public final void dragOver(DragSourceDragEvent dsde) {
        System.out.println("AbstractTreeTransferHandler.dragOver");
        int action = dsde.getDropAction();
        if (action == DnDConstants.ACTION_COPY) {
            dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
        } else {
            if (action == DnDConstants.ACTION_MOVE) {
                dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
            } else {
                dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
            }
        }
    }

    public final void dropActionChanged(DragSourceDragEvent dsde) {
        System.out.println("AbstractTreeTransferHandler.dropActionChanged");
        int action = dsde.getDropAction();
        if (action == DnDConstants.ACTION_COPY) {
            dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
        } else {
            if (action == DnDConstants.ACTION_MOVE) {
                dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
            } else {
                dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
            }
        }
    }

    public final void dragExit(DragSourceEvent dse) {
        System.out.println("AbstractTreeTransferHandler.dragExit");
        dse.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
    }

    /* Methods for DragGestureListener */
    public final void dragGestureRecognized(DragGestureEvent dge) {
        TreePath path = tree.getSelectionPath();
        if (path != null) {
            draggedNode = (ITask) path.getLastPathComponent();
            if (drawImage) {
                Rectangle pathBounds = tree.getPathBounds(path); //getpathbounds of selectionpath
                JComponent lbl = (JComponent) tree.getCellRenderer().getTreeCellRendererComponent(tree, draggedNode,
                        false, tree.isExpanded(path),
                        ((TaskTreeModel) tree.getModel()).isLeaf(path.getLastPathComponent()), 0,
                        false);//returning the label
                lbl.setBounds(pathBounds);//setting bounds to lbl
                image = new BufferedImage(lbl.getWidth(), lbl.getHeight(),
                        java.awt.image.BufferedImage.TYPE_INT_ARGB_PRE);//buffered image reference passing the label's ht and width
                Graphics2D graphics = image.createGraphics();//creating the graphics for buffered image
                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                        0.5f));    //Sets the Composite for the Graphics2D context
                lbl.setOpaque(false);
                lbl.paint(graphics); //painting the graphics to label
                graphics.dispose();
            }

            dragSource.startDrag(dge, DragSource.DefaultMoveNoDrop, image, new Point(0, 0),
                    new TransferableTask(draggedNode), this);

        }
    }

    /* Methods for DropTargetListener */

    public final void dragEnter(DropTargetDragEvent dtde) {
        Point pt = dtde.getLocation();
        int action = dtde.getDropAction();
        if (drawImage) {
            paintImage(pt);
        }
        if (canPerformAction(tree, draggedNode, action, pt)) {
            dtde.acceptDrag(action);
        } else {
            dtde.rejectDrag();
        }
    }

    public final void dragExit(DropTargetEvent dte) {
        if (drawImage) {
            clearImage();
        }
    }

    public final void dragOver(DropTargetDragEvent dtde) {
        System.out.println("AbstractTreeTransferHandler.dragOver");
        Point pt = dtde.getLocation();
        int action = dtde.getDropAction();
        tree.autoscroll(pt);
        if (drawImage) {
            paintImage(pt);
        }
        if (canPerformAction(tree, draggedNode, action, pt)) {
            dtde.acceptDrag(action);
        } else {
            dtde.rejectDrag();
        }
    }

    public final void dropActionChanged(DropTargetDragEvent dtde) {
        Point pt = dtde.getLocation();
        int action = dtde.getDropAction();
        if (drawImage) {
            paintImage(pt);
        }
        if (canPerformAction(tree, draggedNode, action, pt)) {
            dtde.acceptDrag(action);
        } else {
            dtde.rejectDrag();
        }
    }

    public final void drop(DropTargetDropEvent dtde) {
        System.out.println("AbstractTreeTransferHandler.drop");

        try {
            if (drawImage) {
                clearImage();
            }
            int action = dtde.getDropAction();
            Transferable transferable = dtde.getTransferable();
            Point pt = dtde.getLocation();
            if (transferable.isDataFlavorSupported(TransferableTask.TASK_FLAVOR) &&
                    canPerformAction(tree, draggedNode, action, pt)) {
                TreePath pathTarget = tree.getPathForLocation(pt.x, pt.y);
                ITask node =
                        (ITask) transferable.getTransferData(TransferableTask.TASK_FLAVOR);
                ITask newParentNode = (ITask) pathTarget.getLastPathComponent();
                if (executeDrop(tree, node, newParentNode, action)) {
                    dtde.acceptDrop(action);
                    dtde.dropComplete(true);
                    return;
                }
            }
            dtde.rejectDrop();
            dtde.dropComplete(false);
        }
        catch (Exception e) {
            System.out.println(e);
            dtde.rejectDrop();
            dtde.dropComplete(false);
        }
    }

    private void paintImage(Point pt) {
        tree.paintImmediately(rect2D.getBounds());
        rect2D.setRect((int) pt.getX(), (int) pt.getY(), image.getWidth(), image.getHeight());
        tree.getGraphics().drawImage(image, (int) pt.getX(), (int) pt.getY(), tree);
    }

    private void clearImage() {
        tree.paintImmediately(rect2D.getBounds());
    }

    protected DNDTree getTree() {
        return tree;
    }

    public abstract boolean canPerformAction(DNDTree target, ITask draggedTask, int action,
                                             Point location);

    public abstract boolean executeDrop(DNDTree tree, ITask draggedTask,
                                        ITask newParentTask, int action);
}