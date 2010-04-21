/**
 * ViewHelper.java is part of Forge Project.
 *
 * Copyright 2004,2005 LainSoft Foundation, Demetrio Cruz
 *
 * You may distribute under the terms of either the GNU General Public
 * License or the Artistic License, as specified in the README file.
 *
*/
package org.lainsoft.forge.flow.helper;

import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import org.lainsoft.commons.beanutils.ValidatedBean;
import org.apache.commons.fileupload.FileItem;

/**
 * Interface of a helper, defines a wraper for the <code>HttpServletRequest, HttpSession</code>
 * and <code>ServletContext</code>. It must be an specific implementation for the view interaction.
 */
public interface ViewHelper{
    /**
     * Obtains the <code>HttpServletRequest</code>.
     * @return The <code>HttpServletRequest</code> which is wrapped.
     */
    public HttpServletRequest getRequest ();
    
    /**
     * Obtains the <code>HttpServletResponse</code>.
     * @return The <code>HttpServletResponse</code> which is wrapped.
     */
    public HttpServletResponse getResponse ();

    /**
     * Obtains the <code>HttpSession</code>.
     * @return The <code>HttpSession</code> which is wrapped.
     */
    public HttpSession getSession ();
    
    /**
     * Obtains the <code>ServletContext</code>.
     * @return The <code>ServletContext</code> which is wrapped.
     */
    public ServletContext getApplicationContext();
    
    /**
     * Saves mapped parameters in the request to a <code>ValidatedBean</code>.
     * @param bean <code>ValidatedBean</code> wich will be filled.
     * @return <code>true</code> if saving was successful or <code>false</code> if saving have errors.
     */
    public boolean save_from_request(ValidatedBean bean) throws ServletException;
    

    /**
     * Set a <code>ValidatedBean</code> to the request.
     * @param bean <code>ValidatedBean</code> wich will be published.
     */
    public void set_bean_to_request(ValidatedBean bean);
    
    /**
     * Search param with the prefix pattern described. For example:<br>
     * - if you specify "Line" the method will look for Line[whatever] pattern.
     * @param obj pattern to search for.
     * @return <code>Map</code> with the parameters found.
     */
    public Map search_params_for(String obj);
    
    /**
     * Returns the web application file system path for the seudo path given. For example:<br>
     * - <code>helper.real_path("/WEB-INF/web.xml");</code> will return /my_file_system/path/.../app_server/my_webapp/WEB-INF/web.xml
     * @param seudo_path the seudo path to evaluate.
     * @return The real file system path for the seudo path.
     */
    public String real_path(String seudo_path);
    
    /**
     * Evaluates if an object is empty
     * @param obj to evaluate.
     * @return <code>true</code> if the object is empty (this could mean null or empty), <code>false</code> if the object is filled.
     */
    public boolean is_empty(Object obj);

    /**
     * Publish error message related to a parameter
     * @param param the parameter related.
     * @param msg the error message.
     */
    public void setError(String param, String msg);

    /**
     * Obtains a request parameter
     * @param param Parameter name to search for.
     * @return the value assigned to this parameter.
     */
    public String getParameter(String param);
    

    /**
     * Obtains a request parameter, if null returns given the default value.
     * @param param Parameter name to search for.
     * @param def default value if parameter value is empty or not present.
     * @return the value assigned to this parameter.
     */   
    public String getParameter(String param, String def);
    
    /**
     * Obtains a file request parameter.
     * @param param Parameter name mapped to the file request object.
     * @return <code>FileItem</code> related to this request parameter.
     */
    public FileItem getFileParameter(String param);
    
    public void layout(String layout);
    
    public void default_layout();
}
