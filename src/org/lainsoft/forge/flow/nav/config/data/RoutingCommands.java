/**
 * RoutingCommands.java is part of Forge Project.
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
import java.util.Map;
import java.util.TreeMap;

public class RoutingCommands{

    private List routes;

    public RoutingCommands(){
        routes = new ArrayList();
    }

    public RoutingCommands(List routes){
        this.routes = routes;
    }
    
    public List
    getRoutes(){
        return routes;
    }

    public void
    addRoute(Route route){
        routes.add(route);
    }
    
    public String
    toString (){
        return "RoutingCommands[routes="+routes+"]";
    }
}
