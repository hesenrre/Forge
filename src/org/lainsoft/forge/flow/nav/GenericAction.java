/**
 * GenericAction.java is part of Forge Project.
 *
 * Copyright 2004,2005,2006 LainSoft Foundation, Demetrio Cruz
 *
 * You may distribute under the terms of either the GNU General Public
 * License or the Artistic License, as specified in the README file.
 *
*/
package org.lainsoft.forge.flow.nav;

import java.util.Map;
import org.lainsoft.forge.flow.helper.ViewHelper;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * Interface that represents an action responding a requested stimulus.
 * It must be implemented to create an accion.
 * @author Copyright 2004, 2005, 2006 <a href="http://www.lainsoftfoundation.org">LainSoft Foundation</a>, Demetrio Cruz
 */
public abstract class GenericAction
    implements Command{
    
    private ViewHelper helper;

    
    private void
    helper(ViewHelper helper){
        this.helper = helper;
    }

    public void
    layout(String layout){
        helper().layout(layout);
    }

    public void
    default_layout(){
        helper().default_layout();
    }
    
    public ViewHelper
    helper(){
        return helper;
    }
    
    public String
    param(String param){
        return helper.getParameter(param);
    }
    
    public String
    param(String param, String def){
        return helper.getParameter(param, def);
    }
    
    public Map
    params(String pattern){
        return helper.search_params_for(pattern);
    }

    public HttpServletRequest
    request(){
        return helper.getRequest();
    }
    
    public void
    request(String attr, Object o){
        helper.getRequest().setAttribute(attr, o);
    }
    
    public Object
    request(String attr){
        return helper.getRequest().getAttribute(attr);
    }


    public HttpSession
    session(){
        return helper.getSession();       
    }
    
    public void
    session(String attr, Object o){
        helper.getSession().setAttribute(attr, o);
    }
    
    public Object
    session(String attr){
        return helper.getSession().getAttribute(attr);
    }
    
    public ServletContext
    application(){
        return helper.getApplicationContext();
    }

    public void
    application(String attr, Object o){
        helper.getApplicationContext().setAttribute(attr,o);
    }
    
    public Object
    application(String attr){
        return helper.getApplicationContext().getAttribute(attr);
    }

    public String execute(ViewHelper helper)
        throws CommandException{        
        helper(helper);
        before_execute();
        String response = execute();
        after_execute();
        return response;
    }
    /**
     * executes the accion requested.     
     */
    public abstract String execute() throws CommandException;
    
    public void before_execute(){}
    public void after_execute(){}
}
