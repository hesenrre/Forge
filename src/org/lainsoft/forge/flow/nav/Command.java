/**
 * Command.java is part of Forge Project.
 *
 * Copyright 2004,2005,2006,2007 LainSoft Foundation, Demetrio Cruz
 *
 * You may distribute under the terms of either the GNU General Public
 * License or the Artistic License, as specified in the README file.
 *
*/
package org.lainsoft.forge.flow.nav;

import org.lainsoft.forge.flow.helper.ViewHelper;

/**
 * Interface that represents an action responding a requested stimulus.
 * It must be implemented to create an accion.
 * @author Copyright 2004, 2005 <a href="http://www.lainsoftfoundation.org">LainSoft Foundation</a>, Demetrio Cruz
 */
public interface Command{
    
    /**
     * executes the accion requested.
     * @param helper ViewHelper that wraps the servlet environment
     *               to be used for the action.
     */
    public String execute (ViewHelper helper) throws CommandException;
}
