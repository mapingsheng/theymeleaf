package controller;

import java.util.Calendar;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;

import entity.User;

public class DoLoginController implements Controller{
	 
    public DoLoginController() {
        super();
    }
    
    public void process(
            final HttpServletRequest request, final HttpServletResponse response,
            final ServletContext servletContext, final ITemplateEngine templateEngine)
            throws Exception {
        
    	String username = request.getParameter("username");
    	String password = request.getParameter("password");
    	
        WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("username", username);
        ctx.setVariable("password", password);
        
        String[] types = {"足球","篮球","羽毛球"};
        ctx.setVariable("types", types);
        
        User user = new User("zhang","san","china",25);
        ctx.setVariable("user", user);
        
        templateEngine.process("desc", ctx, response.getWriter());//模板引擎转向到对应的模板页面
    }
}
