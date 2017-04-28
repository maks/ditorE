package com.lukaszgajos.ditore.gui;

import com.lukaszgajos.ditore.Chunk;
import com.lukaszgajos.ditore.Editor;
import com.lukaszgajos.ditore.EditorCallback;
import com.lukaszgajos.ditore.FileAccessException;
import com.lukaszgajos.ditore.State;
import com.lukaszgajos.ditore.task.EditorTask;
import com.lukaszgajos.ditore.task.SetContent;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.undo.UndoManager;

public class EditorFrame extends javax.swing.JPanel {

    private Editor e;
    private int currentChunkIndex = 0;
    private boolean useTemporatyFile;
    private UndoManager undoManager = new UndoManager();
    private String currentHighlight = "";
    private String currentSearchQuery = "";
    private String currentReplaceQuery = "";
    
    private void initialize(File f) {
        
        try {
            e = new Editor(f);
            if (e.getMaxChunkIndex() == 0) {
                chunkSelector.setVisible(false);
                chunkSelector.setEnabled(false);
            } else {
                chunkSelector.setVisible(true);
                chunkSelector.setEnabled(true);
                
                chunkSelector.setMaximum((int) e.getMaxChunkIndex());
            }
            textArea.setText(e.getChunk(0).toString());
            textArea.requestFocus();
            tasksList.setVisible(false);
            
            e.registerCallback(new EditorCallback() {
                @Override
                public void onTaskAdded(EditorTask t) {
                    updateTaskList();
                    if (e.getMaxChunkIndex() == 0) {
                        chunkSelector.setEnabled(false);
                        chunkSelector.setVisible(false);
                    } else {
                        chunkSelector.setEnabled(true);
                        chunkSelector.setEnabled(true);
                    }
                }

                @Override
                public void onTaskRemoved() {
                    updateTaskList();
                    if (e.getMaxChunkIndex() == 0) {
                        chunkSelector.setEnabled(false);
                        chunkSelector.setVisible(false);
                    } else {
                        chunkSelector.setEnabled(true);
                        chunkSelector.setEnabled(true);
                    }                    
                }
            });
            
        } catch (FileAccessException ex) {
            // show error dialog
            Logger.getLogger(EditorFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
            }
        });
    }
    
    public boolean saveNeed() {
        
        if (e.getTasks().size() > 0) {
            return true;
        }
        
        return false;
    }
    
    public void editorUndo() {
        if (undoManager.canUndo()) {
            undoManager.undo();
        }
    }
    
    public void editorRedo() {
        if (undoManager.canRedo()) {
            undoManager.redo();;
        }
    }
    
    public void highlightText(String str) {
        
        currentHighlight = str;
        updateHightlight();
        
    }
    
    private void updateHightlight() {
        
        if (currentHighlight.length() == 0) {
            return;
        }
        
        Highlighter highlighter = textArea.getHighlighter();
        highlighter.removeAllHighlights();
        
        HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.ORANGE);

        int caretPosition = 0;
        int p0 = 0;
        int p1 = 0;
        
        do {
            
            p0 = textArea.getText().toLowerCase().indexOf(currentHighlight.toLowerCase(), p1 + 1);
            p1 = p0 + currentHighlight.length();
            if (p0 < 0) {
                break;
            }
            
            try {
                highlighter.addHighlight(p0, p1, painter);
            } catch (BadLocationException ex) {
                Logger.getLogger(EditorFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (caretPosition == 0) {
                caretPosition = p0;
            }
            
        } while (p0 > 0);
        
    }
    
    public EditorFrame(State s) {
        initComponents();
        useTemporatyFile = false;
        
        File fin = new File(s.fileIn);
        File fout = new File(s.fileOut);
        
        initialize(fin);
        e.setDestFile(fout);
        
        for (EditorTask task : s.tasks) {
            e.addTask(task);
        }
        
    }
    
    public EditorFrame(File f) {
        
        initComponents();
        
        useTemporatyFile = false;
        initialize(f);
        
    }

    public EditorFrame() {
        initComponents();
        
        useTemporatyFile = true;
        File temp;
        try {
            temp = File.createTempFile("temp-file-name", ".tmp");
            initialize(temp);
        } catch (IOException ex) {
            Logger.getLogger(EditorFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadChunk(int index) {
        
        String txt = "";
        
//        if (index > 0) {
//            Chunk prevCh = e.getChunk(index - 1);
//            txt += prevCh.getContent();
//        }
        Chunk ch = e.getChunk(index);
        txt += ch.getContent();
//        if (index < e.getMaxChunkIndex()) {
//            Chunk nextCh = e.getChunk(index + 1);
//            txt += nextCh.getContent();
//        }
        
        
        textArea.setText(txt);
        textArea.setCaretPosition(0);
        
        chunkSelector.setValue((int)index);
        currentChunkIndex = index;
        
        updateHightlight();
    }
    
    public void reloadChunk() {
        loadChunk(currentChunkIndex);
    }
    
    public void prevChunk() {
        if (currentChunkIndex - 1 < 0) {
            return;
        }
        
        loadChunk(currentChunkIndex - 1);
        
        textArea.setCaretPosition(textArea.getText().length() - 1);
    }
    
    public void nextChunk() {
        if (currentChunkIndex + 1 > e.getMaxChunkIndex()) {
            return;
        }
        
        loadChunk(currentChunkIndex + 1);
        textArea.setCaretPosition(0);
    }
    
    private void updateTaskList() {
        tasksList.removeAll();
        
        DefaultListModel model = new DefaultListModel();
        
        for (EditorTask task : e.getTasks()) {
            model.addElement(task.getLabel());
        }
        tasksList.setModel(model);
        if (model.size() > 0) {
            tasksList.setVisible(true);
            tasksList.ensureIndexIsVisible(model.size() - 1);
        }
    }
    
    public boolean useTemporaryFile() {
        return useTemporatyFile;
    }
            
    public void setTextAreaFocus() {
        textArea.requestFocus();
    }
    
    public Editor getEditor() {
        return e;
    }
    
    public File getCurrentFile() {
        return e.getDestFile();
    }
    
    public void replaceDestFile(File f) {
        e.setDestFile(f);
    }
    
    public JTextArea getTextArea() {
        return textArea;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tasksListContext = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        chunkSelector = new javax.swing.JSlider();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        tasksList = new javax.swing.JList<>();

        tasksListContext.setLabel("hgf");

        jMenuItem1.setText("Remove task");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        tasksListContext.add(jMenuItem1);

        chunkSelector.setOrientation(javax.swing.JSlider.VERTICAL);
        chunkSelector.setValue(0);
        chunkSelector.setEnabled(false);
        chunkSelector.setFocusable(false);
        chunkSelector.setInverted(true);
        chunkSelector.setName(""); // NOI18N
        chunkSelector.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                chunkSelectorStateChanged(evt);
            }
        });

        textArea.setColumns(20);
        textArea.setFont(new java.awt.Font("Monospaced", 0, 15)); // NOI18N
        textArea.setLineWrap(true);
        textArea.setRows(5);
        textArea.setAlignmentX(4.5F);
        textArea.setAlignmentY(4.5F);
        textArea.setOpaque(false);
        textArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textAreaKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(textArea);

        tasksList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tasksList.setComponentPopupMenu(tasksListContext);
        tasksList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tasksList.setDragEnabled(true);
        tasksList.setMaximumSize(new java.awt.Dimension(52, 235));
        tasksList.setVisibleRowCount(4);
        tasksList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tasksListMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(tasksList);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 844, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chunkSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chunkSelector, javax.swing.GroupLayout.DEFAULT_SIZE, 786, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void chunkSelectorStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chunkSelectorStateChanged
        int currentValue = chunkSelector.getValue();
        loadChunk(currentValue);
    }//GEN-LAST:event_chunkSelectorStateChanged

    private void textAreaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textAreaKeyReleased
        if (e.getChunk(currentChunkIndex).toString().equals(textArea.getText()) == false) {
            e.addTask(new SetContent(currentChunkIndex, textArea.getText()));
        }
    }//GEN-LAST:event_textAreaKeyReleased

    private void tasksListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tasksListMousePressed
//        if (SwingUtilities.isRightMouseButton(evt)) {
//            tasksListContext.show(tasksList, evt.getPoint().x, evt.getPoint().y);
//        }
    }//GEN-LAST:event_tasksListMousePressed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if (tasksList.getSelectedIndex() >= 0) {
            e.delTask(tasksList.getSelectedIndex());
            reloadChunk();
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSlider chunkSelector;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> tasksList;
    private javax.swing.JPopupMenu tasksListContext;
    private javax.swing.JTextArea textArea;
    // End of variables declaration//GEN-END:variables
}
