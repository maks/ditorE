package com.lukaszgajos.ditore;

import com.lukaszgajos.ditore.task.EditorTask;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class State implements Serializable {
    public String fileIn;
    public String fileOut;
    public ArrayList<EditorTask> tasks;
    
    public boolean fileInExists() {
        File f = new File(fileIn);
        return f.exists();
    }
    
    public String getName() {
        File f = new File(fileIn);
        return f.getName();
    }
}
