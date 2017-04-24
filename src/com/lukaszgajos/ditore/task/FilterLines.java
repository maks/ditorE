package com.lukaszgajos.ditore.task;

import com.lukaszgajos.ditore.Chunk;
import java.io.Serializable;
import java.util.ArrayList;

public class FilterLines implements EditorTask, Serializable {

    private String q;
    
    public FilterLines(String q) {
        this.q = q;
    }
    
    @Override
    public Chunk proceed(Chunk ch) {
        
        String in = ch.getContent();
        
        String lines[] = in.split("\\n");
        ArrayList<String> result = new ArrayList<>();
        
        for (String l: lines) {
            if (l.contains(q)) {
                result.add(l);
            }
        }
        
        String resultStr = String.join("\n", result);
        ch.setContent(resultStr);
        return ch;
    }
    
    @Override
    public String getLabel() {
        return "Filter lines: " + q;
    }
    
}
