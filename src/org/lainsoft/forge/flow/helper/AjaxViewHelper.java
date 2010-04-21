/**
 * AjaxViewHelper.java is part of Forge Project.
 *
 * Copyright 2004,2005 LainSoft Foundation, Demetrio Cruz, Israel Buitrón
 *
 * You may distribute under the terms of either the GNU General Public
 * License or the Artistic License, as specified in the README file.
 *
*/
package org.lainsoft.forge.flow.helper;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.List;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;

import org.lainsoft.commons.beanutils.ValidatedBean;


/**
 * Helper for ajax specialized responses 
 * @author Copyright 2004, 2005, 2006 <a href="http://www.lainsoftfoundation.org">LainSoft Foundation</a>, Demetrio Cruz, Israel Buitr&oacute;n.
 */
public class AjaxViewHelper extends GenericViewHelper {
 
    /**
     * Contructor, generates an instance of a
     */
    public AjaxViewHelper(HttpServletRequest request, HttpServletResponse response,ServletContext context) {
	super(request,response,context);
    }

    public AjaxViewHelper (ViewHelper helper){
	super(helper);
    }

    public String
    auto_complete_for(List list){
        return auto_complete_for(list, null);
    }

    public String
    auto_complete_for(List list, String field){
        
        String buffer = "<ul>";
        for (int i=0; i<list.size(); i++){
            try{
                buffer += is_empty(list.get(i)) ? "" : (!is_empty(field) && list.get(i) instanceof ValidatedBean) ? "<li>"+((ValidatedBean)list.get(i)).get(field)+"</li>" : "<li>"+list.get(i).toString()+"</li>";
            }catch(NoSuchMethodException nsme){
                buffer += "";
                nsme.printStackTrace();
            }catch(IllegalAccessException iae){
                buffer += "";
                iae.printStackTrace();
            }catch(InvocationTargetException ite){
                buffer += "";
                ite.printStackTrace();
            }
            
        }
        return buffer+"</ul>";
    }

    public String response_for_select(Map map) {
	if(map==null)
	    return "[]";

	String resp="[";

	Iterator iter = map.keySet().iterator();
	while(iter.hasNext()) {
	    Object key = iter.next();
	    Object value = map.get(key);

	    resp += ( (key==null ? "" : key.toString()) + "=" + 
		      (value==null ? "" : value.toString()) +
		      (iter.hasNext() ? "," : "" ) );
	}

	return resp += "]";
    }

    public String response_for_select(List list) {
	if(list==null)
	    return "[]";

	String resp = "[";

	Iterator iter = list.iterator();
	while(iter.hasNext()) {
	    Object value = iter.next();

	    resp += ( list.indexOf(value) + "=" + 
		      (value==null ? "" : value.toString()) +
		      (iter.hasNext() ? "," : "" ) );
	}

	return resp += "]";
    }
}
