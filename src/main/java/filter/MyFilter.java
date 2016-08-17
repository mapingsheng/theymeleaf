package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.ITemplateEngine;

import application.MyApplication;
import controller.Controller;
import entity.User;

public class MyFilter implements Filter{
	private ServletContext servletContext;
    private MyApplication application;
    
    public MyFilter() {
        super();
    }
  

    public void init(final FilterConfig filterConfig) throws ServletException {
        this.servletContext = filterConfig.getServletContext();
        this.application = new MyApplication(this.servletContext);
    }

    public void doFilter(final ServletRequest request, final ServletResponse response,
            final FilterChain chain) throws IOException, ServletException {
        
        if (!process((HttpServletRequest)request, (HttpServletResponse)response)) {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {}

    private boolean process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            //针对资源请求url进行排除
            if (request.getRequestURI().startsWith("/css") ||
                    request.getRequestURI().startsWith("/images") ||
                    request.getRequestURI().startsWith("/favicon")) {
                return false;
            }
            
            /*
             * 根据URL映射，获得处理该请求的控制器。
             */
            Controller controller = this.application.resolveControllerForRequest(request);
            if (controller == null) {
                return false;
            }

            /*
             * 获取TemplateEngine(模板引擎)实例
             */
            ITemplateEngine templateEngine = this.application.getTemplateEngine();

            /*
             * 写入响应头部信息
             */
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            /*
             * 执行控制器，并转向到对应的模板视图
             */
            controller.process(request, response, this.servletContext, templateEngine);
            return true;
        } catch (Exception e) {
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (final IOException ignored) {
            }
            throw new ServletException(e);
        }
        
    }
	
}
