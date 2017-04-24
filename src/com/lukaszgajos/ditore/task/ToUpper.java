package com.lukaszgajos.ditore.task;

import com.lukaszgajos.ditore.Chunk;
import java.io.Serializable;

public class ToUpper implements EditorTask,Serializable {

    @Override
    public Chunk proceed(Chunk ch) {
        
        String in = ch.getContent();
        in = in.toUpperCase();
        ch.setContent(in);
        
        return ch;
    }
    
    @Override
    public String getLabel() {
        return "To upper case";
    }
}
