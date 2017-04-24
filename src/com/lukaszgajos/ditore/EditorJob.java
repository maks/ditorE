package com.lukaszgajos.ditore;

import com.lukaszgajos.ditore.gui.DialogResult;
import com.lukaszgajos.ditore.job.BackgroundJob;

public class EditorJob {
    
    private BackgroundJob job;
    private boolean isBusy = false;
    private Thread background;
    private DialogResult result;
    
    public boolean isJobPossible() {
        return !isBusy;
    }
    
    public void newJob(BackgroundJob job) {
        this.job = job;
    }
    
    public DialogResult getResult() {
        return result;
    }
    
    public String getTitle() {
        return job.getTitle();
    }
    
    public void start() {
        isBusy = true;
        
        background = new Thread(new Runnable() {
            @Override
            public void run() {
                job.start();
                result = job.getResult();
            }
        });
        background.setName("Background job");
        background.start();
    }
    
    public boolean isRunning() {
        return job.isRunning();
    }
    
    public void cancel() {
        if (job.isRunning()) {
            job.cancel();
            background.interrupt();
        }
        isBusy = false;
    }
    
    public int getProgress() {
        return job.getProgress();
    }
}
