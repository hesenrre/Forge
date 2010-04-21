package org.lainsoft.forge.persistance.io.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;



public class StringFileReader{
    
    private StringFileReader (){}
    
    public static StringFileReader
    getInstance(){
        return new StringFileReader();
    }

    public String
    read(String source)
        throws FileNotFoundException{
        //return read(new FileReader(source));
        try{
            return read(new InputStreamReader(new FileInputStream(source),"ISO-8859-1"));
        }catch (UnsupportedEncodingException uee){
            uee.printStackTrace();
            return "";
        }
    }

    public String
    read(File source)
        throws FileNotFoundException{
        //return read(new FileReader(source));
        try{
            return read(new InputStreamReader(new FileInputStream(source),"ISO-8859-1"));
        }catch (UnsupportedEncodingException uee){
            uee.printStackTrace();
            return "";
        }
    }
    
    public String
    read(Reader reader){
        BufferedReader internalReader = new BufferedReader(reader);
        String buffer = "";
        try {
            for (String carrier=""; (carrier=internalReader.readLine())!=null;){
                buffer += carrier + "\n";
            }
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        return buffer;
    }

}
