package application;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import controller.HomeController;
import controller.LoginController;
import controller.Controller;
import controller.DescController;
import controller.DoLoginController;

public class MyApplication {

    private TemplateEngine templateEngine;
    private Map<String, Controller> controllersByURL;
    
    public MyApplication(final ServletContext servletContext) {
        super();
        /*1、实例化模板解析器，并配置相关选项*/
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        /*1.1、其实thymeleaf模板引擎的默认模式是HTML，在这里再次设置只是为了更好理解*/ 
        templateResolver.setTemplateMode(TemplateMode.HTML);
        /*1.2、设置模板引擎的默认加载路径*/
        templateResolver.setPrefix("/WEB-INF/templates/");
        /*1.3、设置模板引擎的文件后缀*/
        templateResolver.setSuffix(".html");
        /*1.4、设置模板缓存时间为1小时，如果不设置，模板将一直在缓存中*/
        templateResolver.setCacheTTLMs(Long.valueOf(3600000L));
        /*1.5、模板引擎的默认缓存是开启的，这里设置true是为了更好理解； 其实如果这里设置false的话，当你修改模板后，你就可以在浏览器中实时查看到修改后的模板内容*/
        templateResolver.setCacheable(false);

        /*2、实例化模板引擎*/
        this.templateEngine = new TemplateEngine();
        /*3、将模板解析器装载到模板引擎中*/
        this.templateEngine.setTemplateResolver(templateResolver);
        
        /*4、针对不同的url实例化不同的业务控制器*/
        this.controllersByURL = new HashMap<String, Controller>();
        this.controllersByURL.put("/login", new LoginController());
        this.controllersByURL.put("/doLogin", new DoLoginController());
        this.controllersByURL.put("/desc", new DescController());
        this.controllersByURL.put("/", new HomeController());
    }
    
    public Controller resolveControllerForRequest(final HttpServletRequest request) {
        final String path = getRequestPath(request);
        return this.controllersByURL.get(path);
    }
    
    
    public ITemplateEngine getTemplateEngine() {
        return this.templateEngine;
    }

    
    private static String getRequestPath(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        final String contextPath = request.getContextPath();
        
        final int fragmentIndex = requestURI.indexOf(';'); 
        if (fragmentIndex != -1) {
            requestURI = requestURI.substring(0, fragmentIndex);
        }
        
        if (requestURI.startsWith(contextPath)) {
            return requestURI.substring(contextPath.length());
        }
        return requestURI;
    }
}
