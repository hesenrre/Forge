/**
 * Composite.java is part of Forge Project.
 *
 * Copyright 2004,2005 LainSoft Foundation, Demetrio Cruz
 *
 * You may distribute under the terms of either the GNU General Public
 * License or the Artistic License, as specified in the README file.
 *
*/
package org.lainsoft.forge.flow.nav.config.data;

import java.util.List;
import java.util.ArrayList;

/**
 * Bean that represents the configuration for a CompositeCommand.
 */
public class Composite{

    private String className;
    private String zone;
    private List members;
    

    public Composite (String className, String zone, List members){
        this.className = className;
        this.zone=zone;
        this.members=members;
    }

    public Composite (){
        className = "";
        zone = "";
        members = new ArrayList();
    }

    public String
    getClassName(){
        return className;
    }
    public String
    getZone(){
        return zone;
    }

    public List
    getMembers(){
        return members;
    }

    public void
    setClassName(String className){
        this.className=className;
    }

    public void
    setZone(String zone){
        this.zone=zone;
    }

    public void
    addMember (String command){
        members.add (command);
    }
    
    public boolean
    isEmpty(){
        return className.trim().equals("") && zone.trim().equals("") && members.isEmpty();
    }

    public String
    toString (){
        return "Composite[className="+className+", zone="+zone+", members="+members+"]";
    }
}
