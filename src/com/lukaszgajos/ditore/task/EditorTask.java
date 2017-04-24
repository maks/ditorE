package com.lukaszgajos.ditore.task;

import com.lukaszgajos.ditore.Chunk;

public interface EditorTask {
    public Chunk proceed(Chunk ch);
    public String getLabel();
}
