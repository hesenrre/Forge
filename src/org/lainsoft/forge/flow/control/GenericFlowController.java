/**
 * GenericFlowController.java is part of Forge Project.
 *
 * Copyright 2004,2005 LainSoft Foundation, Demetrio Cruz
 *
 * You may distribute under the terms of either the GNU General Public
 * License or the Artistic License, as specified in the README file.
 *
*/
package org.lainsoft.forge.flow.control;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lainsoft.forge.flow.helper.ViewHelper;
import org.lainsoft.forge.flow.helper.GenericViewHelper;
import org.lainsoft.forge.flow.nav.Command;
import org.lainsoft.forge.flow.nav.CommandException;
import org.lainsoft.forge.flow.nav.CommandFactory;
import org.lainsoft.forge.flow.nav.GenericCommandFactory;
import org.lainsoft.forge.flow.nav.CommandNotFoundCommand;
import org.lainsoft.forge.flow.nav.RubyControllerLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Generic implementation of a Flow Control, transforms URL paths into stimulus 
 * that a command factory can understand.
 *
 * @web.servlet name="GenericFlowController"
 *              load-on-startup="1"
 * 
 * @web.servlet-mapping url-pattern="/flow/*"
 * @web.servlet-init-param name="url-pattern"
                           value="/flow/"
 * @web.servlet-init-param name="jvms"
                           value="java,ruby"
 *
 */
public class GenericFlowController
    extends HttpServlet
    implements FlowController{

    private static Log log = LogFactory.getLog(GenericFlowController.class);

    /**
     * Receives standard HTTP requests from the public <code>service</code> method and 
     * dispatches them to the <code>do</code>XXX methods defined in this class. 
     * This method is an HTTP-specific version of the 
     * <code>Servlet.service(javax.servlet.ServletRequest, 
     * javax.servlet.ServletResponse)</code> 
     * method. There's no need to override this method.
     *
     * @param request the <code>HttpServletRequest</code> object that contains the 
     * request the client made of the servlet.
     * @param response the <code>HttpServletResponse</code> object that contains the
     * response the servlet returns to the client.
     * @throws IOException if an input or output error occurs while the servlet is
     * handling the HTTP request.
     * @throws ServletException if the HTTP request cannot be handled.
     */    

    public void
    service (HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException{
        
        GenericViewHelper helper = new GenericViewHelper(request, response, getServletContext());
        try{
            String charset;
            if (!helper.is_empty(charset = getServletContext().getInitParameter("character_encoding"))){
                request.setCharacterEncoding(charset);
            }
                
            Map flow_request = processRequest(helper);
            render(flow_request,flow(flow_request, helper),helper);
        }catch(CommandException ce){
            throw new ServletException(ce);
        }
    }


    public void
    init(){
        gfs_alias_path(new GenericViewHelper(null, null, getServletContext()));
    }
    
    
    private String
    getStimulus (ViewHelper helper){
        log.debug("In REQUEST>"+helper.getRequest().getAttribute("_STIMULUS"));
        log.debug("In PATHINFO>"+helper.getRequest().getPathInfo());
        log.debug("In SERVLETPATH>"+helper.getRequest().getServletPath());
        String stimulus = 
            ((stimulus = (String)helper.getRequest().getAttribute("_STIMULUS")) == null
            ? (stimulus = helper.getRequest().getPathInfo()) == null 
             ? (stimulus = helper.getRequest().getServletPath ()) == null
             ? "" : stimulus
             : stimulus
             : stimulus);
        
        String gfs_alias_path = gfs_alias_path(helper);
        return chompSlash((stimulus = (stimulus.startsWith(gfs_alias_path) ? 
                                  stimulus.substring(gfs_alias_path.length())
                                  : stimulus)));
        
    }

    private String
    chompSlash(String str){        
        while(str.indexOf("/") == 0){
            str = str.substring(1);
        }
        return str;
    }

    
    public Map
    processRequest (ViewHelper helper)
        throws CommandException{
        Map flow_response = new TreeMap();

        String prefix = 
            (prefix = helper.getApplicationContext().getInitParameter("command_path_prefix")) == null
            ? "" : prefix.trim();
        
        String htdocs_basepath = 
            (htdocs_basepath = (htdocs_basepath = helper.getApplicationContext().getInitParameter("htdocs_basepath")) == null
             ? "/" : htdocs_basepath.trim().startsWith("/") ? htdocs_basepath.trim(): "/"+htdocs_basepath.trim()).endsWith("/")? htdocs_basepath : htdocs_basepath + "/";
        
        String stimulus = getStimulus(helper);
        helper.getRequest().setAttribute("_stimulus",stimulus);
        log.debug("GFS stymulus>"+stimulus);

        
        //String intended_response = GenericCommandFactory.newInstance().getCommand(prefix+stimulus,helper).execute(helper);
        String intended_response = executeCommand(prefix,stimulus,helper);
        log.debug("GFC intended_response>"+intended_response);
        
        if (intended_response == null || intended_response.trim().equals("")){
            flow_response.put("template",htdocs_basepath+stimulus+".jsp");
        }else{
            String []deteminants = intended_response.split("[\\s]{0,}=>[\\s]{0,}");
            if (deteminants[0].startsWith(":ajax")){
                flow_response.put("ajax",deteminants.length == 2 ? deteminants[1]: "");
            }else if (deteminants.length < 2 || deteminants[0].startsWith(":text")){
                flow_response.put("text",deteminants.length == 2 ? deteminants[1]: deteminants[0]);
            }else if (deteminants[0].startsWith(":partial")){
                String partial = 
                    ((partial = deteminants[1]).lastIndexOf("/") >= 0 ?
                     partial.substring(0,partial.lastIndexOf("/"))+"/_"+partial.substring(partial.lastIndexOf("/")+1) 
                     : "_"+partial)+".jsp";
                
                partial = htdocs_basepath + (partial.lastIndexOf("/") >= 0 ? 
                                 partial 
                                 : stimulus.lastIndexOf("/") > 0 ? 
                                    stimulus.substring(0,stimulus.lastIndexOf("/")+1) + partial 
                                    : partial);
                flow_response.put("partial",partial);
            }else if (deteminants[0].startsWith(":template")){
                String template = 
                    htdocs_basepath + ((template = deteminants[1]).lastIndexOf("/") >= 0 ? 
                          template 
                          : stimulus.lastIndexOf("/") > 0 ? 
                            stimulus.substring(0,stimulus.lastIndexOf("/")+1) + template 
                            : template) +".jsp";
                flow_response.put("template",template);
            }else if (deteminants[0].startsWith(":action")){                
                log.debug("Action prefix>"+prefix);
                log.debug("Action stimulus>"+stimulus);
                log.debug("Action deteminant>"+deteminants[1]);
                String action = 
                    (helper.is_empty(prefix) || prefix.lastIndexOf("/")== prefix.length()? "/" : "") + ((action = deteminants[1]).indexOf("/") == 0 ? 
                          action.substring(1)
                          : stimulus.lastIndexOf("/") > 0 ? 
                            stimulus.substring(0,stimulus.lastIndexOf("/")+1) + action 
                            : action);
                
                log.debug("Genering Action>"+gfs_alias_path(helper)+"|"+action);
                flow_response.put("action",gfs_alias_path(helper)+action);
            }else{                
                throw new CommandException("Invalid response rule ("+intended_response+") in command <"+
                                           GenericCommandFactory.newInstance().getCommand(prefix+stimulus,helper).getClass().getName()+
                                           ">, please verify");
            }
        }
        return flow_response;
    }
    

    private String
    gfs_alias_path(ViewHelper helper){
        String gfs_alias_path = "";
        if (helper.is_empty(gfs_alias_path = (String)helper.getApplicationContext().getAttribute("_gfs_alias_path"))){
            gfs_alias_path = helper.is_empty(gfs_alias_path = getInitParameter("url-pattern")) ? 
                helper.is_empty(gfs_alias_path = helper.getRequest().getServletPath()) ? 
                "/"
                : gfs_alias_path : gfs_alias_path;
            helper.getApplicationContext().setAttribute("_gfs_alias_path", gfs_alias_path);            
        }
        return gfs_alias_path;
    }

    private String
    flow(Map flow_response, ViewHelper helper){
        String to_render = 
            flow_response.containsKey("missing") ? 
            "<h1>Template is missing</h1>\n<p>Missing template "+flow_response.get("missing")+"</p>"
            : flow_response.values().size() > 0 ? flow_response.values().toArray()[0].toString() : "";
        return to_render;
    }
    

    private void
    render(Map flow_response, String to_render, ViewHelper helper)
        throws ServletException, IOException{
        
        String layout = layout(helper);

        if (flow_response.containsKey("ajax") || 
            (flow_response.containsKey("text") && helper.getRequest().getHeader("x-requested-with") != null 
             && helper.getRequest().getHeader("x-requested-with").equals("XMLHttpRequest"))){
            render_ajax_text(helper.getResponse(),to_render);
        }else{
            layout = (helper.getRequest().getHeader("x-requested-with") != null
                      && helper.getRequest().getHeader("x-requested-with").equals("XMLHttpRequest")) ? null : layout;
            
            if (layout == null){
                if (flow_response.containsKey("action")){
                    straigth_forward(to_render,helper);
                }else if(flow_response.containsKey("text") || flow_response.containsKey("missing")){
                    render_text(helper.getResponse(),to_render);
                }else{
                    forward(to_render, helper);
                }
            }else{
                if(flow_response.containsKey("action")){
                    helper.getRequest().setAttribute("_STIMULUS",flow_response.get("action").toString());
                    try{
                        to_render = flow((flow_response = processRequest(helper)),helper);
                        if(flow_response.containsKey("action")){
                            render(flow_response, to_render, helper);
                            return;
                        }                        
                    }catch (CommandException ce){
                        throw new ServletException (ce);
                    }
                }
                
                if ((flow_response.containsKey("partial") || flow_response.containsKey("template")) && !exists(helper.real_path(to_render))){
                    flow_response = new TreeMap();
                    flow_response.put("missing",to_render);
                    to_render = flow(flow_response,helper);
                }

                Map content_for_layout = new TreeMap();
                content_for_layout.put(((flow_response.containsKey("text") || flow_response.containsKey("missing")) ? "text": "template"),to_render);
                helper.getRequest().setAttribute("content_for_layout", content_for_layout);
                forward(layout,helper);
            }
        
        }
    }    

    public String
    layout(ViewHelper helper){
        String layout = 
            "/layouts/"+(helper.getRequest().getAttribute("layout") == null 
                         ? ((layout = 
                             ((layout = getStimulus(helper)).lastIndexOf("/") > 0 ? 
                              layout.substring(0,layout.lastIndexOf("/")) 
                              : "")).trim().equals("") || layout.trim().equals("/") ?
                            "root.jsp"
                            :layout+".jsp") 
                         : helper.getRequest().getAttribute("layout").toString()+".jsp");
        return exists(helper.real_path(layout)) ? layout : null;
    }

    
    private void
    straigth_forward(String target, ViewHelper helper)
        throws ServletException, IOException{
        helper.getRequest().getRequestDispatcher(target).forward(helper.getRequest(),helper.getResponse());
    }

    private void
    forward (String target, ViewHelper helper)
        throws ServletException,IOException{
        if (exists(helper.real_path(target))){
            helper.getRequest().getRequestDispatcher(target).forward(helper.getRequest(),helper.getResponse());
        }else{
            Map flow_response = new TreeMap();
            flow_response.put("missing",target);
            render(flow_response,flow(flow_response, helper),helper);
        }
    }

    private void
    render_text(HttpServletResponse response, String text)
        throws IOException{
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(text);
        out.close();
    }

    private void
    render_ajax_text(HttpServletResponse response, String text)
        throws IOException{
        response.setContentType("text/xml;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(text);
        out.close();
    }

    
    private boolean
    exists(String filename){
        return new File(filename).exists();
    }
    
    private String
    executeCommand(String prefix, String stimulus, ViewHelper helper)
        throws CommandException{
        String []jvms = helper.is_empty(getInitParameter("jvms")) 
            ? new String[]{"java"} 
        : getInitParameter("jvms").split(",");
                
        String last_response = null;
        for(int i=0; i<jvms.length; i++){
            if(jvms[i].equals("java")){
                log.debug("USING JAVA MACHINE>"+prefix+stimulus);
                Command command = GenericCommandFactory.newInstance().getCommand(prefix+stimulus,helper);
                last_response = command.execute(helper);
                if (!(command instanceof CommandNotFoundCommand)){
                    break;
                }
            }else if (jvms[i].equals("ruby")){
                log.debug("USING RUBY MACHINE");                
                last_response = new RubyControllerLoader().execute(helper);
                if(last_response.startsWith(":missing_action") || last_response.startsWith(":missing_controller")){                    
                    last_response = last_response.split("[\\s]{0,}=>[\\s]{0,}")[1];
                }else{
                    break;
                }
            }
        }
        return last_response;
        
    }


}
