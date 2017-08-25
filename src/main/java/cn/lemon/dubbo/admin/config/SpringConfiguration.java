package cn.lemon.dubbo.admin.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.lemon.dubbo.admin.authc.AuthenticationIntercept;
import cn.lemon.dubbo.admin.authc.PermissionIntercept;
import cn.lemon.framework.utils.JsonMapper;

@Configuration
@AutoConfigureAfter({ DubboConfiguration.class })  
public class SpringConfiguration extends WebMvcConfigurerAdapter{

	@Value("${spring.cross.mapping}")
	private String crossMapping; //设置允许跨域访问的域名
	@Value("${spring.interceptor.excludes}")
	private String[] interceptorExcludes; //设置排除拦截的地址
	
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/locked").setViewName("locked");
        registry.addViewController("/demo").setViewName("demo/demo");
    }
	
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(crossMapping)
            .allowedMethods("GET","POST","PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600);
    }
	
	@Bean
	public AuthenticationIntercept authenticationIntercept() {
	    return new AuthenticationIntercept();
	}
	
	@Bean
	public PermissionIntercept permissionIntercept() {
	    return new PermissionIntercept();
	}
	
	/**加入拦截器 */
    @Override
	public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(authenticationIntercept()).addPathPatterns("/**").excludePathPatterns(interceptorExcludes);
    	registry.addInterceptor(permissionIntercept()).addPathPatterns("/**").excludePathPatterns(interceptorExcludes);
    }
	
    /** JSON消息转换 */
	@Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		//Set HTTP Message converter using a JSON implementation.
        MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
        // Add supported media type returned by BI API.
        //List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
        //supportedMediaTypes.add(new MediaType("text", "plain"));
        //supportedMediaTypes.add(new MediaType("application", "json"));
        //supportedMediaTypes.add(new MediaType("text/html", "charset=UTF-8"));
        //jsonMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
        jsonMessageConverter.setObjectMapper(new JsonMapper());
        converters.add(jsonMessageConverter);
    }
}
