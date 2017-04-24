package com.lukaszgajos.ditore.job;

import com.lukaszgajos.ditore.Chunk;
import com.lukaszgajos.ditore.Editor;
import com.lukaszgajos.ditore.FileAccessException;
import com.lukaszgajos.ditore.gui.DialogResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Save implements BackgroundJob {

    private Editor editor;
    private File file;
    private long progress;
    
    private boolean isRunning = false;
    private boolean stopJob = false;
    
    public String getTitle() {
        return "Save file from " + editor.getSourceFile().getName() + " to " + file.getName();
    }
    
    public Save(Editor e, File f) throws FileAccessException {
        
        if (f.exists() && !f.canWrite()) {
            throw new FileAccessException("Could not write to " + f.getAbsolutePath());
        }
        if (f.isFile() && !f.getParentFile().canWrite()) {
            throw new FileAccessException("Could not write to " + f.getAbsolutePath());
        }
        if (f.isDirectory()) {
            throw new FileAccessException("Could not write to directory " + f.getAbsolutePath());
        }
        
        editor = e;
        file = f;
        progress = 0;
    }
    
    @Override
    public int getProgress() {
        int x = (int)(((double)progress/(double)editor.getMaxChunkIndex()) * 100);
        return x;
    }

    @Override
    public void start() {
        
        System.out.println("Saving to " + file.getAbsolutePath());
        
        isRunning = true;
                
        long start = System.currentTimeMillis();
        
        FileWriter writer;
        File temp = null;
        try {
            temp = File.createTempFile("temp-file-name", ".tmp");
            // @TODO Check if there is enough space for temp file
            
            writer = new FileWriter(temp);
            for (int i = 0; i <= editor.getMaxChunkIndex(); i++) {
                Chunk ch = editor.getChunk(i);
                writer.write(ch.getContent());
                
                progress = i;
                
                if (stopJob) {
                    Files.delete(Paths.get(temp.getAbsolutePath()));
                    break;
                }
                
            }
            writer.flush();
            writer.close();
            
            Files.move(Paths.get(temp.getAbsolutePath()), Paths.get(file.getAbsolutePath()), REPLACE_EXISTING);
            
        } catch (IOException ex) {
            Logger.getLogger(Save.class.getName()).log(Level.SEVERE, null, ex);
            try {
                Files.delete(Paths.get(temp.getAbsolutePath()));
            } catch (IOException ex1) {
                Logger.getLogger(Save.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        
        long end = System.currentTimeMillis();
        System.out.println("Saved in " + (end - start) / 1000f + " seconds");        
        
        isRunning = false;
    }

    @Override
    public void cancel() {
        stopJob = true;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public DialogResult getResult() {
        return null;
    }
    
}
