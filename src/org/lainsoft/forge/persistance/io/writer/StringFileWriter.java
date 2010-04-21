package org.lainsoft.forge.persistance.io.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

public class StringFileWriter{
    
    private File file;

    private StringFileWriter(File file){
        this.file = file;
    }
    
    public static StringFileWriter
    getInstance(File file){
        return new StringFileWriter(file);
    }
    
    public static StringFileWriter
    getInstance(String filename){
        return new StringFileWriter(new File(filename));
    }
    
    public void
    write(String text){
        try{            
            PrintWriter writer = new PrintWriter(new BufferedWriter(
                                                                    new OutputStreamWriter(
                                                                                           new FileOutputStream(file), "ISO-8859-1")));            
            writer.write(text);
            writer.close();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
}
    
