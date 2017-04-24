package com.lukaszgajos.ditore;

import com.lukaszgajos.ditore.task.EditorTask;

public interface EditorCallback {
    public void onTaskAdded(EditorTask t);
    public void onTaskRemoved();
}
