/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.trippledave.textstuff.helper;

import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class DragDropFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private final FileListReceiver receiver;

    public DragDropFrame(FileListReceiver receiver) {
        super("Drag and drop");

        this.receiver = receiver;

        this.setSize(550, 550);
        new DropTarget(this, new MyDragDropListener());
        this.getContentPane().add(BorderLayout.CENTER, new JLabel("Drag something here!", SwingConstants.CENTER));
        this.setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Closed");
                e.getWindow().dispose();
            }
        });
    }

    class MyDragDropListener implements DropTargetListener {

        @Override
        public void drop(DropTargetDropEvent event) {

            event.acceptDrop(DnDConstants.ACTION_COPY);

            Transferable transferable = event.getTransferable();
            DataFlavor[] flavors = transferable.getTransferDataFlavors();

            // Loop through the flavors
            for (DataFlavor flavor : flavors) {

                try {

                    // If the drop items are files
                    if (flavor.isFlavorJavaFileListType()) {

                        // Get all of the dropped files
                        final List<File> filesOrDirectory = (List) transferable.getTransferData(flavor);

                        receiver.handleFiles(getFiles(filesOrDirectory));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            event.dropComplete(true);
        }

        @Override
        public void dragEnter(DropTargetDragEvent event) {
        }

        @Override
        public void dragExit(DropTargetEvent event) {
        }

        @Override
        public void dragOver(DropTargetDragEvent event) {
        }

        @Override
        public void dropActionChanged(DropTargetDragEvent event) {
        }

        private List<File> getFiles(List<File> filesOrDirectory) {
            final List<File> onlyFiles = new LinkedList<>();
            
            for (File file : filesOrDirectory) {
                if (file.isFile()) {
                    onlyFiles.add(file);
                }else if (file.isDirectory()) {
                    try {
                        Files.walk(Paths.get(file.getAbsolutePath()))
                                .filter(Files::isRegularFile)
                                .filter(Files::isReadable)
                                .map(p -> p.toFile())
                                .forEach(onlyFiles::add);
                    } catch (IOException ex) {
                        Logger.getLogger(DragDropFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
            for (File file : onlyFiles) {
                // Print out the file path
                System.out.println("Files are '" + file.getPath() + "'.");
            }
            return onlyFiles;
        }
    }
}
