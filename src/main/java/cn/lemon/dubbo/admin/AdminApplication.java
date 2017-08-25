package cn.lemon.dubbo.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * 消费端服务 <br>
 * 使用spring原生rest接口开发
 * @date 2017年8月4日 上午10:32:23 <br>
 * @author lonyee
 */
@EnableAutoConfiguration
@SpringBootApplication
public class AdminApplication extends SpringBootServletInitializer {
	private static Logger logger = LoggerFactory.getLogger(AdminApplication.class);
	
	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(AdminApplication.class);
		springApplication.run(args);
		logger.info("SpringBoot lemon-admin Start Success");
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AdminApplication.class);
	}
}
