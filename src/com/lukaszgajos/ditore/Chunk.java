package com.lukaszgajos.ditore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Chunk {
    
    private File f;
    private int sizeInBytes;
    private long number;
    private String content = null;
    
    public Chunk(File f, int sizeInBytes, long number) {
        this.f = f;
        this.sizeInBytes = sizeInBytes;
        this.number = number;
    }
    
    public long getIndex() {
        return number;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getContent() {
        
        if (content != null) {
            return content;
        }
        int extraBuffSize = 1000;
        long start = number * sizeInBytes;
        long preEnd = (number + 1) * sizeInBytes;
//        long end = preEnd;
        long end = preEnd + extraBuffSize; //extra buff to find some white spaces
        if (end > f.length()) {
            end = f.length();
        }
        
        try {
            RandomAccessFile raf = new RandomAccessFile(f, "r");
            raf.seek(start);
            
            byte[] buff;
            byte[] subBuff;
            int buffSize = (int) (end - start);
            
            String resultStr = "";
            
            buff = new byte[buffSize];
            raf.read(buff);
            
            int buffIndexStart = 0;
            int buffIndexEnd = (int)sizeInBytes;
            if (buffIndexEnd > buffSize) {
                buffIndexEnd = buffSize;
            }
            
            if (start > 0) {
                /*
                Looking for white space in begining when shift all data block
                in prev chunk
                */
                for (int byteIndex = 0; byteIndex < extraBuffSize; byteIndex++) {
                    if (buff[byteIndex] == '\n' || buff[byteIndex] == '\r' || buff[byteIndex] == ' ' || buff[byteIndex] == '\t' || buff[byteIndex] == ',' || buff[byteIndex] == ';') {
                        buffIndexStart = byteIndex;
                        break;
                    }
                }
            }
            /*
            Search in extra buff data for white spaces to avoid split utf8 
            character, next chunk have to do the same operation to shift
            start with the same amount of bytes. 
            */
            for (int byteIndex = (int)(preEnd - start); byteIndex < buffSize; byteIndex++) {
                    if (buff[byteIndex] == '\n' || buff[byteIndex] == '\r' || buff[byteIndex] == ' ' || buff[byteIndex] == '\t' || buff[byteIndex] == ',' || buff[byteIndex] == ';') {
                        buffIndexEnd = byteIndex;
                        break;
                    }                
            }
            subBuff = Arrays.copyOfRange(buff, buffIndexStart, buffIndexEnd);
            resultStr = new String(subBuff);
            
            raf.close();
            
            return resultStr;
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Chunk.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Chunk.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "";
    }
    
    @Override
    public String toString() {
        return getContent();
    }
    
}
