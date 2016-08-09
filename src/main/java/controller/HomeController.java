package controller;

import java.util.Calendar;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;

public class HomeController implements Controller{
	 
    public HomeController() {
        super();
    }
    
    public void process(
            final HttpServletRequest request, final HttpServletResponse response,
            final ServletContext servletContext, final ITemplateEngine templateEngine)
            throws Exception {
        
        WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("today", Calendar.getInstance());//thymeleaf的上下文中设置变量数据
        
        templateEngine.process("home", ctx, response.getWriter());//模板引擎转向到对应的模板页面
    }
}
