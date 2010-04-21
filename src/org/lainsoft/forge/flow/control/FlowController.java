/**
 * FlowController.java is part of Forge Project.
 *
 * Copyright 2004,2005 LainSoft Foundation, Demetrio Cruz
 *
 * You may distribute under the terms of either the GNU General Public
 * License or the Artistic License, as specified in the README file.
 *
*/
package org.lainsoft.forge.flow.control;

import java.util.Map;
import org.lainsoft.forge.flow.helper.ViewHelper;
import org.lainsoft.forge.flow.nav.CommandException;

/**
 * Interface that represents the behavour of a Flow Control
 */
public interface FlowController{
    /**
     * Process a request to convert it into an action.
     * @param helper <code>ViewHelper</code> that encapsulates the request, 
     *               session and context for the application.
     * @return portlet or list of portlets for the composite view.
     */
    public Map processRequest (ViewHelper helper)throws CommandException;
}
