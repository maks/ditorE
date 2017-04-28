package com.lukaszgajos.ditore.gui;

import com.lukaszgajos.ditore.EditorCallback;
import com.lukaszgajos.ditore.EditorJob;
import com.lukaszgajos.ditore.FileAccessException;
import com.lukaszgajos.ditore.State;
import com.lukaszgajos.ditore.job.Save;
import com.lukaszgajos.ditore.job.Search;
import com.lukaszgajos.ditore.task.EditorTask;
import com.lukaszgajos.ditore.task.FilterLines;
import com.lukaszgajos.ditore.task.SearchReplace;
import com.lukaszgajos.ditore.task.ToLower;
import com.lukaszgajos.ditore.task.ToUpper;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

public class MainWindow extends javax.swing.JFrame {

    ArrayList<EditorFrame> tabsList;
    EditorJob backgroundJob;

    public MainWindow() {

        tabsList = new ArrayList<>();
        backgroundJob = new EditorJob();

        initComponents();
        
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png")));
        
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);

        if (!loadState()) {
            newEmpty();
        }
    }

    public void newEmpty() {
        createTabWithEditor("<new>", new EditorFrame());
    }
    
    private void openFile(File f) {
        createTabWithEditor(f.getName(), new EditorFrame(f));
    }
    
    private void createTabWithEditor(String tabName, EditorFrame frame) {
        int currentTab = jTabbedPane1.getTabCount();
        tabsList.add(currentTab, frame);
        
        jTabbedPane1.add(tabName, tabsList.get(currentTab));
        jTabbedPane1.setSelectedIndex(jTabbedPane1.getTabCount() - 1);
        
        getCurrentEditor().setTextAreaFocus();
        getCurrentEditor().getTextArea().setCaretPosition(0);
    }
    
    public String getSessionFilename() {
        String path = System.getProperty("user.home") + File.separator + ".ditore" + File.separator + "editor.session";
        
        try {
            Files.createDirectories(new File(path).getParentFile().toPath());
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return path;
    }

    private boolean loadState() {
        
        String sessionFilename = getSessionFilename();
        File f = new File(sessionFilename);
        if (!f.exists()) {
            return false;
        }
        
        ArrayList<State> toRestore = new ArrayList<>();
        
        try {  
            ObjectInputStream in = new ObjectInputStream(new FileInputStream( sessionFilename ));
            toRestore = (ArrayList<State>) in.readObject();
            in.close();
            
            if (toRestore.size() > 0) {
                for (State s : toRestore) {
                    EditorFrame e = new EditorFrame(s);
                    createTabWithEditor(s.getName(), e);
                    e.reloadChunk();
                }
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    private void saveState() {
        ArrayList<State> toSave = new ArrayList<>();
        
        for (EditorFrame f : tabsList) {
            toSave.add(f.getEditor().getState());
        }
        
        try {
            
            FileOutputStream fout = new FileOutputStream( getSessionFilename() );
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(toSave);
            out.flush();
            out.close();
            fout.close();
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void saveCurrentTab(File f) {
        
        try {
            Save saveJob = new Save(getCurrentEditor().getEditor(), f);

            backgroundJob.newJob(saveJob);
            backgroundJob.start();
        
            ProgressDialog progress = new ProgressDialog(backgroundJob);
            progress.showDialog();
            
            jTabbedPane1.setTitleAt(jTabbedPane1.getSelectedIndex(), f.getName());
            
        } catch (FileAccessException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public EditorFrame getCurrentEditor() {
        int currIndex = jTabbedPane1.getSelectedIndex();
        return tabsList.get(currIndex);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuItemFileNew = new javax.swing.JMenuItem();
        menuItemFileOpen = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        menuItemFileSave = new javax.swing.JMenuItem();
        menuItemFIleSaveAs = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        menuItemFileCloseFile = new javax.swing.JMenuItem();
        menuItemFileQuit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        menuItemEditUndo = new javax.swing.JMenuItem();
        menuItemEditRedo = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        menuItemEditCopy = new javax.swing.JMenuItem();
        menuItemEditCut = new javax.swing.JMenuItem();
        menuItemEditPaste = new javax.swing.JMenuItem();
        menuItemEditSelectAll = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        menuItemEditClearHighlight = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        menuItemToolsSearchReplace = new javax.swing.JMenuItem();
        menuItemToolsFilterLines = new javax.swing.JMenuItem();
        menuItemToolsUppercase = new javax.swing.JMenuItem();
        menuItemToolsLowercase = new javax.swing.JMenuItem();
        menuItemToolsWordWrap = new javax.swing.JCheckBoxMenuItem();
        jMenu5 = new javax.swing.JMenu();
        menuItemEditPrevChunk = new javax.swing.JMenuItem();
        menuItemEditNextChunk = new javax.swing.JMenuItem();
        menuItemDocumentPrevTab = new javax.swing.JMenuItem();
        menuItemDocumentNextTab = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jMenu1.setText("File");

        menuItemFileNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        menuItemFileNew.setText("New");
        menuItemFileNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemFileNewActionPerformed(evt);
            }
        });
        jMenu1.add(menuItemFileNew);

        menuItemFileOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        menuItemFileOpen.setText("Open");
        menuItemFileOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemFileOpenActionPerformed(evt);
            }
        });
        jMenu1.add(menuItemFileOpen);
        jMenu1.add(jSeparator2);

        menuItemFileSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        menuItemFileSave.setText("Save");
        menuItemFileSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemFileSaveActionPerformed(evt);
            }
        });
        jMenu1.add(menuItemFileSave);

        menuItemFIleSaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menuItemFIleSaveAs.setText("Save as");
        menuItemFIleSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemFIleSaveAsActionPerformed(evt);
            }
        });
        jMenu1.add(menuItemFIleSaveAs);
        jMenu1.add(jSeparator3);

        menuItemFileCloseFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        menuItemFileCloseFile.setText("Close file");
        menuItemFileCloseFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemFileCloseFileActionPerformed(evt);
            }
        });
        jMenu1.add(menuItemFileCloseFile);

        menuItemFileQuit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        menuItemFileQuit.setText("Quit");
        menuItemFileQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemFileQuitActionPerformed(evt);
            }
        });
        jMenu1.add(menuItemFileQuit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        menuItemEditUndo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        menuItemEditUndo.setText("Undo");
        menuItemEditUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemEditUndoActionPerformed(evt);
            }
        });
        jMenu2.add(menuItemEditUndo);

        menuItemEditRedo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menuItemEditRedo.setText("Redo");
        menuItemEditRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemEditRedoActionPerformed(evt);
            }
        });
        jMenu2.add(menuItemEditRedo);
        jMenu2.add(jSeparator4);

        menuItemEditCopy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        menuItemEditCopy.setText("Copy");
        menuItemEditCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemEditCopyActionPerformed(evt);
            }
        });
        jMenu2.add(menuItemEditCopy);

        menuItemEditCut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        menuItemEditCut.setText("Cut");
        menuItemEditCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemEditCutActionPerformed(evt);
            }
        });
        jMenu2.add(menuItemEditCut);

        menuItemEditPaste.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        menuItemEditPaste.setText("Paste");
        menuItemEditPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemEditPasteActionPerformed(evt);
            }
        });
        jMenu2.add(menuItemEditPaste);

        menuItemEditSelectAll.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        menuItemEditSelectAll.setText("Select All");
        menuItemEditSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemEditSelectAllActionPerformed(evt);
            }
        });
        jMenu2.add(menuItemEditSelectAll);
        jMenu2.add(jSeparator1);
        jMenu2.add(jSeparator5);

        menuItemEditClearHighlight.setText("Clear highlight");
        menuItemEditClearHighlight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemEditClearHighlightActionPerformed(evt);
            }
        });
        jMenu2.add(menuItemEditClearHighlight);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Tools");

        menuItemToolsSearchReplace.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        menuItemToolsSearchReplace.setText("Search / Replace");
        menuItemToolsSearchReplace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemToolsSearchReplaceActionPerformed(evt);
            }
        });
        jMenu3.add(menuItemToolsSearchReplace);

        menuItemToolsFilterLines.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        menuItemToolsFilterLines.setText("Filter lines");
        menuItemToolsFilterLines.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemToolsFilterLinesActionPerformed(evt);
            }
        });
        jMenu3.add(menuItemToolsFilterLines);

        menuItemToolsUppercase.setText("Uppercase");
        menuItemToolsUppercase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemToolsUppercaseActionPerformed(evt);
            }
        });
        jMenu3.add(menuItemToolsUppercase);

        menuItemToolsLowercase.setText("Lowercase");
        menuItemToolsLowercase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemToolsLowercaseActionPerformed(evt);
            }
        });
        jMenu3.add(menuItemToolsLowercase);

        menuItemToolsWordWrap.setSelected(true);
        menuItemToolsWordWrap.setText("Word wrap");
        menuItemToolsWordWrap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemToolsWordWrapActionPerformed(evt);
            }
        });
        jMenu3.add(menuItemToolsWordWrap);

        jMenuBar1.add(jMenu3);

        jMenu5.setText("Document");

        menuItemEditPrevChunk.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LEFT, java.awt.event.InputEvent.ALT_MASK));
        menuItemEditPrevChunk.setText("Prev chunk");
        menuItemEditPrevChunk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemEditPrevChunkActionPerformed(evt);
            }
        });
        jMenu5.add(menuItemEditPrevChunk);

        menuItemEditNextChunk.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_RIGHT, java.awt.event.InputEvent.ALT_MASK));
        menuItemEditNextChunk.setText("Next chunk");
        menuItemEditNextChunk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemEditNextChunkActionPerformed(evt);
            }
        });
        jMenu5.add(menuItemEditNextChunk);

        menuItemDocumentPrevTab.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PAGE_UP, java.awt.event.InputEvent.CTRL_MASK));
        menuItemDocumentPrevTab.setText("Prev tab");
        menuItemDocumentPrevTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemDocumentPrevTabActionPerformed(evt);
            }
        });
        jMenu5.add(menuItemDocumentPrevTab);

        menuItemDocumentNextTab.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PAGE_DOWN, java.awt.event.InputEvent.CTRL_MASK));
        menuItemDocumentNextTab.setText("Next tab");
        menuItemDocumentNextTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemDocumentNextTabActionPerformed(evt);
            }
        });
        jMenu5.add(menuItemDocumentNextTab);

        jMenuBar1.add(jMenu5);

        jMenu4.setText("Help");

        jMenuItem1.setText("About");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem1);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 950, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
        );

        jTabbedPane1.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuItemFileNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemFileNewActionPerformed
        newEmpty();
    }//GEN-LAST:event_menuItemFileNewActionPerformed

    private void menuItemFileOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemFileOpenActionPerformed
        
        final FileDialog fileDialog = new FileDialog(this, "Select file", FileDialog.LOAD);
        fileDialog.setVisible(true);
        
        String selectedDir = fileDialog.getDirectory();
        String selectedFilename = fileDialog.getFile();
        if (selectedDir != null && selectedFilename != null) {
            File f = new File(selectedDir + selectedFilename);
            openFile(f);
        }
        
    }//GEN-LAST:event_menuItemFileOpenActionPerformed

    private void menuItemFileCloseFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemFileCloseFileActionPerformed
        
        int currentIndex = jTabbedPane1.getSelectedIndex();
        
        if (getCurrentEditor().saveNeed()) {
            int result = JOptionPane.showConfirmDialog((Component) null, "Do you want to save file?", "ditorE", JOptionPane.YES_NO_CANCEL_OPTION);
            if (result == JOptionPane.CANCEL_OPTION) {
                return;
            }
            if (result == JOptionPane.YES_OPTION) {
                menuItemFIleSaveAsActionPerformed(evt);
            }
        }
        
        jTabbedPane1.remove(currentIndex);
        tabsList.remove(currentIndex);
        
        if (jTabbedPane1.getTabCount() == 0) {
            newEmpty();
        }
        
    }//GEN-LAST:event_menuItemFileCloseFileActionPerformed

    private void menuItemEditPrevChunkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemEditPrevChunkActionPerformed
        getCurrentEditor().prevChunk();
    }//GEN-LAST:event_menuItemEditPrevChunkActionPerformed

    private void menuItemEditNextChunkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemEditNextChunkActionPerformed
        getCurrentEditor().nextChunk();
    }//GEN-LAST:event_menuItemEditNextChunkActionPerformed

    private void menuItemToolsFilterLinesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemToolsFilterLinesActionPerformed
        
        String selectedText = getCurrentEditor().getTextArea().getSelectedText();
        
        FilterDialog dialog = new FilterDialog(getCurrentEditor().getEditor());
        dialog.setFilterInput(selectedText);
        DialogResult result = dialog.showDialog();
        
        if (!result.exists("filter")) {
            return;
        }
        
        getCurrentEditor().getEditor().addTask(new FilterLines(result.get("filter")));
        getCurrentEditor().reloadChunk();
        
    }//GEN-LAST:event_menuItemToolsFilterLinesActionPerformed

    private void menuItemToolsSearchReplaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemToolsSearchReplaceActionPerformed
        
        String selectedText = getCurrentEditor().getTextArea().getSelectedText();
        
        SearchDialog dialog = new SearchDialog(getCurrentEditor().getEditor());
        dialog.setSearchInput(selectedText);
        DialogResult result = dialog.showDialog();
        
        if (!result.exists("q")) {
            return;
        }
        
        String toFind = result.get("q");
        
        if (result.exists("replace")) {
            
            String replaceWith = result.get("replace");
            
            getCurrentEditor().getEditor().addTask(new SearchReplace(toFind, replaceWith));
            getCurrentEditor().reloadChunk();
            return;
        }
        
        
        Search searchJob = new Search(getCurrentEditor().getEditor(), toFind);
        
        backgroundJob.newJob(searchJob);
        backgroundJob.start();
        
        ProgressDialog progress = new ProgressDialog(backgroundJob);
        progress.showDialog();
        
        DialogResult jobResult = backgroundJob.getResult();
        
        if (!jobResult.exists("chunk_index")) {
            return;
        }
        
        int chunkIndex = Integer.parseInt(jobResult.get("chunk_index"));
        int textPosition = Integer.parseInt(jobResult.get("text_position"));
        getCurrentEditor().loadChunk(chunkIndex);
        getCurrentEditor().highlightText(toFind);
        
    }//GEN-LAST:event_menuItemToolsSearchReplaceActionPerformed

    private void menuItemFileSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemFileSaveActionPerformed
        
        if (getCurrentEditor().useTemporaryFile()) {
            menuItemFIleSaveAsActionPerformed(evt);
            return;
        }
        
        saveCurrentTab(getCurrentEditor().getCurrentFile());
    }//GEN-LAST:event_menuItemFileSaveActionPerformed

    private void menuItemToolsUppercaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemToolsUppercaseActionPerformed
        
        getCurrentEditor().getEditor().addTask(new ToUpper());
        getCurrentEditor().reloadChunk();
        
    }//GEN-LAST:event_menuItemToolsUppercaseActionPerformed

    private void menuItemToolsLowercaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemToolsLowercaseActionPerformed
        getCurrentEditor().getEditor().addTask(new ToLower());
        getCurrentEditor().reloadChunk();
    }//GEN-LAST:event_menuItemToolsLowercaseActionPerformed

    private void menuItemFileQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemFileQuitActionPerformed
        saveState();
        dispose();
    }//GEN-LAST:event_menuItemFileQuitActionPerformed

    private void menuItemEditUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemEditUndoActionPerformed
        getCurrentEditor().editorUndo();
    }//GEN-LAST:event_menuItemEditUndoActionPerformed

    private void menuItemEditRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemEditRedoActionPerformed
        getCurrentEditor().editorRedo();
    }//GEN-LAST:event_menuItemEditRedoActionPerformed

    private void menuItemToolsWordWrapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemToolsWordWrapActionPerformed
        getCurrentEditor().getTextArea().setWrapStyleWord(menuItemToolsWordWrap.isSelected());
        getCurrentEditor().getTextArea().setLineWrap(menuItemToolsWordWrap.isSelected());
    }//GEN-LAST:event_menuItemToolsWordWrapActionPerformed

    private void menuItemEditCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemEditCopyActionPerformed
        getCurrentEditor().getTextArea().copy();
    }//GEN-LAST:event_menuItemEditCopyActionPerformed

    private void menuItemEditCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemEditCutActionPerformed
        getCurrentEditor().getTextArea().cut();
    }//GEN-LAST:event_menuItemEditCutActionPerformed

    private void menuItemEditPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemEditPasteActionPerformed
        getCurrentEditor().getTextArea().paste();
    }//GEN-LAST:event_menuItemEditPasteActionPerformed

    private void menuItemEditSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemEditSelectAllActionPerformed
        getCurrentEditor().getTextArea().selectAll();
    }//GEN-LAST:event_menuItemEditSelectAllActionPerformed

    private void menuItemFIleSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemFIleSaveAsActionPerformed
        
        final FileDialog fileDialog = new FileDialog(this, "Save file", FileDialog.SAVE);
        fileDialog.setVisible(true);

        String selectedDir = fileDialog.getDirectory();
        String selectedFilename = fileDialog.getFile();
        if (selectedDir != null && selectedFilename != null) {
            File f = new File(selectedDir + selectedFilename);

            saveCurrentTab(f);

            getCurrentEditor().replaceDestFile(f);
        }
    }//GEN-LAST:event_menuItemFIleSaveAsActionPerformed

    private void menuItemEditClearHighlightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemEditClearHighlightActionPerformed
        getCurrentEditor().highlightText("");
        getCurrentEditor().reloadChunk();
    }//GEN-LAST:event_menuItemEditClearHighlightActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        AboutDialog dlg = new AboutDialog();
        dlg.setLocationRelativeTo( null );
        dlg.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void menuItemDocumentPrevTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemDocumentPrevTabActionPerformed
        
        if (jTabbedPane1.getTabCount() == 1) {
            return;
        }
        
        int currentActive = jTabbedPane1.getSelectedIndex();
        int switchTo = currentActive;
        if (currentActive == 0) {
            switchTo = jTabbedPane1.getTabCount() - 1;
        } else {
            switchTo = currentActive - 1;
        }
        
        jTabbedPane1.setSelectedIndex(switchTo);
    }//GEN-LAST:event_menuItemDocumentPrevTabActionPerformed

    private void menuItemDocumentNextTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemDocumentNextTabActionPerformed
        if (jTabbedPane1.getTabCount() == 1) {
            return;
        }
        
        int currentActive = jTabbedPane1.getSelectedIndex();
        int switchTo = currentActive;
        if (currentActive == jTabbedPane1.getTabCount() - 1) {
            switchTo = 0;
        } else {
            switchTo = currentActive + 1;
        }
        
        jTabbedPane1.setSelectedIndex(switchTo);
    }//GEN-LAST:event_menuItemDocumentNextTabActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JMenuItem menuItemDocumentNextTab;
    private javax.swing.JMenuItem menuItemDocumentPrevTab;
    private javax.swing.JMenuItem menuItemEditClearHighlight;
    private javax.swing.JMenuItem menuItemEditCopy;
    private javax.swing.JMenuItem menuItemEditCut;
    private javax.swing.JMenuItem menuItemEditNextChunk;
    private javax.swing.JMenuItem menuItemEditPaste;
    private javax.swing.JMenuItem menuItemEditPrevChunk;
    private javax.swing.JMenuItem menuItemEditRedo;
    private javax.swing.JMenuItem menuItemEditSelectAll;
    private javax.swing.JMenuItem menuItemEditUndo;
    private javax.swing.JMenuItem menuItemFIleSaveAs;
    private javax.swing.JMenuItem menuItemFileCloseFile;
    private javax.swing.JMenuItem menuItemFileNew;
    private javax.swing.JMenuItem menuItemFileOpen;
    private javax.swing.JMenuItem menuItemFileQuit;
    private javax.swing.JMenuItem menuItemFileSave;
    private javax.swing.JMenuItem menuItemToolsFilterLines;
    private javax.swing.JMenuItem menuItemToolsLowercase;
    private javax.swing.JMenuItem menuItemToolsSearchReplace;
    private javax.swing.JMenuItem menuItemToolsUppercase;
    private javax.swing.JCheckBoxMenuItem menuItemToolsWordWrap;
    // End of variables declaration//GEN-END:variables
}
