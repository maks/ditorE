package com.lukaszgajos.ditore.job;

import com.lukaszgajos.ditore.gui.DialogResult;

public interface BackgroundJob {
    public int getProgress();
    public void start();
    public void cancel();
    public boolean isRunning();
    public String getTitle();
    public DialogResult getResult();
}
