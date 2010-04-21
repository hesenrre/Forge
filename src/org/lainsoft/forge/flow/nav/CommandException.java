/**
 * CommandException.java is part of Forge Project.
 *
 * Copyright 2004,2005 LainSoft Foundation, Demetrio Cruz
 *
 * You may distribute under the terms of either the GNU General Public
 * License or the Artistic License, as specified in the README file.
 *
*/
package org.lainsoft.forge.flow.nav;

public class CommandException
    extends Exception{

    public CommandException(){
        super();
    }
    
    public CommandException(Throwable t){
        super(t);
    }
    
    public CommandException(String msg){
        super(msg);
    }
    
    public CommandException(String msg, Throwable t){
        super(msg,t);
    }
    
}
