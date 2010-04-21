/**
 * GenericCompositeCommand.java is part of Forge Project.
 *
 * Copyright 2004,2005 LainSoft Foundation, Demetrio Cruz
 *
 * You may distribute under the terms of either the GNU General Public
 * License or the Artistic License, as specified in the README file.
 *
*/
package org.lainsoft.forge.flow.nav;

import org.lainsoft.forge.flow.helper.ViewHelper;
import java.util.List;

/**
 * @author Copyright 2004, 2005 <a href="http://www.lainsoftfoundation.org">LainSoft Foundation</a>, Demetrio Cruz
 */
public class GenericCompositeCommand 
    implements Command {

    private List internalCommands;
    private String zone;
    
    public GenericCompositeCommand (List commands, String zone){
        internalCommands = commands;
        this.zone = zone;
    }
    
    public String
    execute (ViewHelper helper)
        throws CommandException{
        String portlets = "";
        for (int i=0; i<internalCommands.size(); i++){
            portlets +=((Command)internalCommands.get(i)).execute(helper)+",";
        }
        portlets = (portlets.length()>0)?portlets.substring(0,portlets.length()-1):portlets;
        return zone + "=" + portlets;
    }
}
