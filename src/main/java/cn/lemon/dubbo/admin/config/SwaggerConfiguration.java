package cn.lemon.dubbo.admin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by lonyee on 2016/06/20.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration
{
	/*
	 * http://localhost:8010/swagger-ui.html
	 */
	@Value("${spring.swagger.enable}")
	public Boolean enableConfiguredFlag;
	
	@Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.enable(enableConfiguredFlag)
                .apiInfo(apiInfo())
                //.genericModelSubstitutes(DeferredResult.class)
                //.genericModelSubstitutes(ResponseEntity.class)
                //.useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.lemon.dubbo.rest"))
                .paths(PathSelectors.any())
                .build();
    }
        
    private ApiInfo apiInfo()
    {
    	return new ApiInfoBuilder()
        		.title("Lemon-china rest api")
                .description("more info visit https://github.com/lemon-china")
                .termsOfServiceUrl("https://github.com/lemon-china")
                .contact("lonyee@live.com")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("v1.0.0")
                .build();
    }
}