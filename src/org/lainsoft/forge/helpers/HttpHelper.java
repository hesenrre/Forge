package org.lainsoft.forge.helpers;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HttpHelper{
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private ServletContext context;

    public void request(String name, Object o){
        request.setAttribute(name, o);
    }

    public <T> T request(String name){
        return (T)request.getAttribute(name);
    }

    public HttpServletRequest request(){
        return request;
    }

    public void session(String name, Object o){
        session.setAttribute(name, o);
    }

    public <T> T session(String name){
        return (T)session.getAttribute(name);
    }

    public HttpSession session(){
        return session;
    }

    public void context(String name, Object o){
        context.setAttribute(name, o);
    }

    public <T> T context(String name){
        return (T)context.getAttribute(name);
    }

    public ServletContext context(){
        return context;
    }

    public HttpServletResponse response(){
        return response;
    }

    public String realPath(String seudoPath){
        return context().getRealPath(seudoPath);
    }

}
