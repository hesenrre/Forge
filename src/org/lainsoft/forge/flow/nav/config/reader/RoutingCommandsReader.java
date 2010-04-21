/**
 * RoutingCommandsReader.java is part of Forge Project.
 *
 * Copyright 2004,2005 LainSoft Foundation, Demetrio Cruz
 *
 * You may distribute under the terms of either the GNU General Public
 * License or the Artistic License, as specified in the README file.
 *
*/
package org.lainsoft.forge.flow.nav.config.reader;

import org.lainsoft.forge.flow.nav.config.data.RoutingCommands;
import org.lainsoft.forge.persistance.io.reader.XMLBeanReader;
import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import org.xml.sax.SAXException;

/**
 * Configuration reader for routes and composite commands.
 */
public class RoutingCommandsReader{

    private XMLBeanReader internalReader;
    private static long filedate = 0;
    private static String fileName = "";
    private static RoutingCommands cachedRotingCommands;
    private static RoutingCommandsReader reader;

    private RoutingCommandsReader (){
        internalReader = XMLBeanReader.getInstance();
    }
    
    /**
     * Retrives an instance of the <code>RoutingCommandsReader</code>.
     */
    public static RoutingCommandsReader 
    getInstance(){
        if (reader == null)
            reader = new RoutingCommandsReader();
        return reader;
    }
    
    /**
     * Read the configuration of the specified xml.
     * @param filename the name of the configuration file.
     * @return a bean that represents the configuration readed.
     */
    public RoutingCommands
    read(String filename){
        if (filename==null || !filename.toLowerCase().matches(".+\\.xml"))
            return new RoutingCommands();
        
        long lastModified = (new File (filename)).lastModified();
        if (!fileName.equals(filename) || (lastModified != filedate)){
            fileName = fileName.equals(filename)?fileName:filename;
            filedate = lastModified;
            cachedRotingCommands = internalRead(filename);
        }
        return cachedRotingCommands;
    }

    private RoutingCommands
    internalRead (String configFile){
        try {
            return (RoutingCommands)internalReader.read (configFile,RoutingCommands.class);
        }catch (IntrospectionException ie){
            System.out.println ("IntrospectionException while reading "+configFile);
            ie.printStackTrace();
        }catch (IOException ioe){
            System.out.println ("IOException while reading "+configFile);
            ioe.printStackTrace ();
        }catch (SAXException saxe){
            System.out.println ("SAXException while reading "+configFile);
            saxe.printStackTrace();
        }
        return new RoutingCommands();
    }
    
    public static void
    main (String []args){
        RoutingCommandsReader reader = new RoutingCommandsReader();
        System.out.println (reader.read(args[0]));
    }

}
