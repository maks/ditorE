package com.lukaszgajos.ditore.task;

import com.lukaszgajos.ditore.Chunk;
import java.io.Serializable;

public class SetContent implements EditorTask, Serializable {

    private int index;
    private String content;
    
    public SetContent(int index, String content) {
        this.index = index;
        this.content = content;
    }
    
    @Override
    public Chunk proceed(Chunk ch) {
        
        if (ch.getIndex() == index) {
            ch.setContent(content);
        }
        
        return ch;
    }
    
    @Override
    public String getLabel() {
        return "New content for chunk " + index;
    }
    
    public int getChunkIndex() {
        return index;
    }
    
}
