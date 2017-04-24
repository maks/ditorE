package com.lukaszgajos.ditore;

import com.lukaszgajos.ditore.task.SetContent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import com.lukaszgajos.ditore.task.EditorTask;
import com.lukaszgajos.ditore.job.BackgroundJob;

public class Editor {
    
    private File sourceFile;
    private File destFile;
    final private int chunkSizeInBytes = 50000;
    private ArrayList<EditorTask> tasks;
    private BackgroundJob job;
    private EditorCallback callback;
    private boolean callbackRegistered = false;

    public void registerCallback(EditorCallback clbk) {
        callback = clbk;
        callbackRegistered = true;
    }
    
    public Editor(File f) throws FileAccessException {
        
        if (f == null || !f.exists()) {
            throw new FileAccessException("File not exists");
        }
        if (!f.canRead()) {
            throw new FileAccessException("File is not readable");
        }
        if (!f.isFile()) {
            throw new FileAccessException("Invalid file");
        }
        
        tasks = new ArrayList<>();
        sourceFile = f;
        destFile = new File(f.getAbsolutePath());
    }
    
    public State getState() {
        State s = new State();
        s.fileIn = sourceFile.getAbsolutePath();
        s.fileOut = destFile.getAbsolutePath();
        s.tasks = tasks;
        
        return s;
    }
    
    public void setDestFile(File f) {
        destFile = f;
    }
    
    public File getDestFile() {
        return destFile;
    }
    
    public void addTask(EditorTask t) {
        
        boolean taskReplaced = false;
        // check if last tasks was setcontent for the same
        // chunk, if yes then replace tasks instead of add another one
        if (tasks.size() > 0 && t instanceof SetContent) {
            
            int lastIndex = tasks.size() - 1;
            EditorTask lastTask = tasks.get(lastIndex);
            if (lastTask instanceof SetContent) {
                if (((SetContent) lastTask).getChunkIndex() == ((SetContent) t).getChunkIndex()) {
                    tasks.set(lastIndex, t);
                    taskReplaced = true;
                }
            }
        }
        if (!taskReplaced) {
            tasks.add(t);
        }
        
        if (callbackRegistered) {
            callback.onTaskAdded(t);
        }
    }
    
    public void delTask(int taskIndex) {
        tasks.remove(taskIndex);
        
        if (callbackRegistered) {
            callback.onTaskRemoved();
        }
    }
    
    public ArrayList<EditorTask> getTasks() {
        return tasks;
    }
    
    public int getMaxChunkIndex() {
        int ret = 0;
        ret = (int) (sourceFile.length() / chunkSizeInBytes);
        return ret;
    }
    
    public Chunk getChunk(int index) {
        Chunk ch = new Chunk(sourceFile, chunkSizeInBytes, index);
        
        for (EditorTask t: tasks) {
            ch = t.proceed(ch);
        }
        
        return ch;
    }
    
    public File getSourceFile() {
        return sourceFile;
    }
    
}
