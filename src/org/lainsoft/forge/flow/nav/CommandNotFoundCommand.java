/**
 * CommandNotFoundCommand.java is part of Forge Project.
 *
 * Copyright 2004,2005 LainSoft Foundation, Demetrio Cruz
 *
 * You may distribute under the terms of either the GNU General Public
 * License or the Artistic License, as specified in the README file.
 *
*/
package org.lainsoft.forge.flow.nav;

import org.lainsoft.forge.flow.helper.ViewHelper;
import javax.servlet.ServletContext;

/**
 * Specific implementation of command enabled to attempt when the acction 
 * specified does not exist.
 */
public class CommandNotFoundCommand
    implements Command{

    public String
    execute (ViewHelper helper){                        
        return "Can not find action "+helper.getRequest().getAttribute("_stimulus");
    }
    
}
