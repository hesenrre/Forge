/**
 * GenericViewHelper.java is part of Forge Project.
 *
 * Copyright 2004,2005 LainSoft Foundation, Demetrio Cruz
 *
 * You may distribute under the terms of either the GNU General Public
 * License or the Artistic License, as specified in the README file.
 *
*/
package org.lainsoft.forge.flow.helper;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.fileupload.FileItem;
import org.lainsoft.commons.beanutils.ValidatedBean;

/**
 * A Generic implementation of <code>ViewHelper</code>. You can extend or wrap this 
 * class to create an specific ViewHelper that suits your needs.
 */
public class GenericViewHelper
    implements ViewHelper{

    
    private HttpServletResponse response;
    private HttpServletRequest request;
    private ServletContext context;
    private Map params;

    
    /**
     * Creates a new instance of <code>GenericViewHelper</code>.
     * @param request an <code>HttpServletRequest</code> object that contains the request the client has made of the servlet.
     * @param response an <code>HttpServletResponse</code> object that contains the response to the client.
     * @param context a reference to the <code>ServletContext</code> in which this FlowControl is running.
     */
    public GenericViewHelper (HttpServletRequest request, HttpServletResponse response,ServletContext context){
        this.request = request;
        this.context = context;
        this.response = response;
    }

    /**
     * Creates a new instance of <code>GenericViewHelper</code>.
     * @param helper an <code>ViewHelper</code> object.
     */
    public GenericViewHelper (ViewHelper helper){
        this.request = helper.getRequest();
        this.context = helper.getApplicationContext();
        this.response = helper.getResponse();
    }

    
    public HttpServletRequest
    getRequest(){
        return request;
    }

    public HttpServletResponse 
    getResponse (){
        return response;
    }

    public HttpSession
    getSession (){
        return request.getSession ();
    }
    
    public ServletContext
    getApplicationContext(){
        return context;
    }
    
    public String
    real_path(String seudo_path){
        return getApplicationContext().getRealPath(seudo_path);
    }

    public boolean 
    is_empty (Object o){
        return o == null || (o instanceof String && ((String)o).trim().equals("")) || (o instanceof List && ((List)o).isEmpty());
    }


    public  void 
    setError (String param, String errormsg){
        Map params_errors = null;
        if (is_empty(params_errors = (Map) request.getAttribute("errors"))){
            request.setAttribute("errors", params_errors = new TreeMap());
        }
        params_errors.put(param, errormsg);
    }

    private void
    setErrors (String bean_name, Map errors){
        Map params_errors = null;
        if (is_empty(params_errors = (Map) request.getAttribute("errors"))){
            request.setAttribute(bean_name+"_errors", params_errors = new TreeMap());
        }
        params_errors.putAll(errors);
    }

    
    public boolean
    save_from_request (ValidatedBean bean)
        throws ServletException{
        String obj_name = 
            (obj_name = bean.getClass().getName()).lastIndexOf(".") < 0 ? 
            obj_name 
            : obj_name.substring(obj_name.lastIndexOf(".")+1);
        Map params = search_params_for(obj_name);
        try{
            if (!bean.save(params)){
                setErrors(bean.getClass().getName(), bean.errors());
                set_bean_to_request(bean);
                return false;
            }
        }catch(InvocationTargetException ite){
            System.out.println("InvocationTargetException in save_from_request>"+ite);
            throw new ServletException(ite);
        }catch(NoSuchMethodException nsme){
            System.out.println("NoSuchMethodException in save_from_request>"+nsme);
            throw new ServletException(nsme);
        }
        set_bean_to_request(bean);
        return true;
    }
        

    public void
    set_bean_to_request(ValidatedBean bean){
        if (bean == null) return;
        String bean_name = 
            (is_empty(bean_name = bean.getClass().getName()) ? 
              "" 
             : bean_name).lastIndexOf(".") > 0 ? 
                  bean_name.substring(bean_name.lastIndexOf(".")+1) 
                  : bean_name;
        getRequest().setAttribute(bean_name, bean);
    }
    
    public Map
    track_parameters(){
        if (params == null || params.isEmpty()){
            params = new TreeMap();
            params.put("request_params",request.getParameterMap());
            params.put("mime_params", new TreeMap());
            ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
            if(upload.isMultipartContent(new ServletRequestContext(request))){
                System.out.println("FILEUPLOAD>Parsing params");

                List items = new ArrayList();
                try{
                    items = upload.parseRequest(request);
                }catch(FileUploadException fue){                
                    System.out.println("Couldn't parse request>"+fue);
                    fue.printStackTrace();
                }
                Map tracked_req_params = new TreeMap();
                Map tracked_mime_params = new TreeMap();
                for(int i=0; i < items.size(); i++){                
                    FileItem item = (FileItem) items.get(i);
                    if(item.isFormField()){
                        tracked_req_params.put(item.getFieldName(),item.getString());
                    }else{
                        tracked_mime_params.put(item.getFieldName(),item);
                    }
                }
                params.put("request_params", tracked_req_params);
                params.put("mime_params",tracked_mime_params);
            }
        }
        return params;
    }
    
    public Map
    search_params_for (String obj){
        Map tracked = (Map)track_parameters().get("request_params");
        Map params = new TreeMap();
        for (Iterator param_names = tracked.keySet().iterator(); param_names.hasNext();){            
            String param_name = (String)param_names.next();
            if (param_name.toLowerCase().startsWith(obj.toLowerCase()+"[")){
                params.put(param_name.substring(param_name.indexOf("[")+1,param_name.lastIndexOf("]")), real_parameter_value(tracked.get(param_name)));
            }
        }
        System.out.println("Parameters Searched for "+obj+">"+params);        
        return params;
    }
    
    public FileItem
    getFileParameter(String param){
        return (FileItem)((Map)track_parameters().get("mime_params")).get(param);
    }

    public String
    getParameter(String param){
        return getParameter(param, null);
    }
    
    public String
    getParameter(String param, String def){
        Map request_params  = (Map)track_parameters().get("request_params");
        String prm = is_empty(prm = real_parameter_value(request_params.get(param))) ? def : prm;
        return (String)prm;
    }

    
    private String
    real_parameter_value(Object param){
        return param instanceof Object[] ? ((String[])param)[0] : (String) param;        
    }
    
    public void layout(String layout){
        request.setAttribute("layout", layout);
    }
    
    public void default_layout(){
        layout("root");
    }
}
