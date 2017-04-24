package com.lukaszgajos.ditore.job;

import com.lukaszgajos.ditore.Chunk;
import com.lukaszgajos.ditore.Editor;
import com.lukaszgajos.ditore.FileAccessException;
import com.lukaszgajos.ditore.gui.DialogResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Search implements BackgroundJob {

    private Editor editor;
    private long progress;
    private String query;
    private DialogResult result;
    private boolean stopSearch = false;
    
    private boolean isRunning = false;
    
    public String getTitle() {
        return "Searching for " + query;
    }
    
    public Search(Editor e, String q)  {
        
        editor = e;
        progress = 0;
        query = q;
        
        result = new DialogResult();
    }
    
    @Override
    public int getProgress() {
        int x = (int)(((double)progress/(double)editor.getMaxChunkIndex()) * 100);
        return x;
    }

    @Override
    public void start() {
        
        isRunning = true;

        for (int i = 0; i <= editor.getMaxChunkIndex(); i++) {
            progress = i;
            Chunk ch = editor.getChunk(i);
            
            if (stopSearch) {
                break;
            }
            
            if (ch.getContent().toLowerCase().contains(query.toLowerCase())) {
                result.set("chunk_index", Long.toString(ch.getIndex()));
                result.set("text_position", Integer.toString(ch.getContent().toLowerCase().indexOf(query.toLowerCase())) );
                break;
            }
        }
        
        isRunning = false;
    }

    @Override
    public void cancel() {
        stopSearch = true;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public DialogResult getResult() {
        return result;
    }
    
}
