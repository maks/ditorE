package com.lukaszgajos.ditore.task;

import com.lukaszgajos.ditore.Chunk;
import java.io.Serializable;

public class SearchReplace implements EditorTask, Serializable {
    
    private String from;
    private String to;
    
    public SearchReplace(String from, String to) {
        
        from = from.replace("\\n", "\n").replace("\\r", "\r").replace("\\t", "\t");
        to = to.replace("\\n", "\n").replace("\\r", "\r").replace("\\t", "\t");
        
        this.from = from;
        this.to = to;
    }

    @Override
    public Chunk proceed(Chunk ch) {
        
        String in = ch.getContent();
        in = in.replace(from, to);
        ch.setContent(in);
        return ch;
        
    }
    
    @Override
    public String getLabel() {
        return "Search for: " + from + " and replace to " + to;
    }
    
}
