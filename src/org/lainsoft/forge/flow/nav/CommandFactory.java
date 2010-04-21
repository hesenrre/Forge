/**
 * CommandFactory.java is part of Forge Project.
 *
 * Copyright 2004,2005 LainSoft Foundation, Demetrio Cruz
 *
 * You may distribute under the terms of either the GNU General Public
 * License or the Artistic License, as specified in the README file.
 *
*/
package org.lainsoft.forge.flow.nav;

import org.lainsoft.forge.flow.helper.ViewHelper;

/**
 * Represents a Factory of commands, this means the signature
 * that any specific factory must have.
 */
public interface CommandFactory{
    /**
     * Retrives the command requested.
     * @param command The name of the command to be created.
     * @param helper The ViewHelper to be used by the command.
     */
    public Command getCommand (String command, ViewHelper helper);
}
