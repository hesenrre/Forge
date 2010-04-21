/**
 * Route.java is part of Forge Project.
 *
 * Copyright 2004,2005 LainSoft Foundation, Demetrio Cruz
 *
 * You may distribute under the terms of either the GNU General Public
 * License or the Artistic License, as specified in the README file.
 *
*/
package org.lainsoft.forge.flow.nav.config.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean that represents the configuration of a route.
 */
public class Route{

    private List regexps;
    private Composite composite;
    
    public Route(){
        regexps = new ArrayList();
        composite = new Composite ();
    }

    public Route (List regexps, Composite composite){
        this.regexps=regexps;
        this.composite=composite;
    }

    public List
    getRegexps(){
        return regexps;
    }
    
    public Composite
    getComposite(){
        return composite;
    }
    
    public void
    addRegexp (String regexp){
        regexps.add (regexp);
    }
    
    public void
    setComposite (Composite composite){
        this.composite = composite;
    }

    public boolean
    matchesRoute (String path){
        for (int i=0; i<regexps.size();i++){
            if (path.matches(regexps.get(i).toString()))
                return true;
        }
        return false;
    }

    public boolean
    contains(String classname){
        return composite.getClassName().equals(classname);
    }

    public boolean
    isEmpty(){
        return regexps.isEmpty() && composite.isEmpty();
    }

    public String
    toString (){
        return "Route [regexps="+regexps+",  composite="+composite+"]";
    }

}
