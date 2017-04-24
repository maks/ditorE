package com.lukaszgajos.ditore.gui;

import java.util.HashMap;

public class DialogResult {
    
    private HashMap<String, String> params;
    
    public DialogResult() {
        params = new HashMap<>();
    }
    
    public void set(String k, String v) {
        params.put(k, v);
    }
    
    public String get(String k) {
        return params.get(k);
    }
    
    public boolean exists(String k) {
        return params.containsKey(k);
    }
}
