package cn.lemon.dubbo.admin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.alibaba.dubbo.rpc.Exporter;

@Configuration
@ConditionalOnClass(Exporter.class)
public class DubboConfiguration {
	
	@Bean
	@ConfigurationProperties(prefix="dubbo.application")
	public ApplicationConfig applicationConfig() {
		return new ApplicationConfig();
	}
	
	@Bean
	@ConfigurationProperties(prefix="dubbo.registry")
	public RegistryConfig registryConfig() {
		return new RegistryConfig();
	}

	@Bean
	@ConfigurationProperties(prefix="dubbo.protocol")
	public ProtocolConfig protocolConfig() {
		return new ProtocolConfig();
	}

	@Bean
	@ConfigurationProperties(prefix="dubbo.annotation")
	public AnnotationBean annotationBean(@Value("${dubbo.annotation.package}") String packageName) {
		AnnotationBean annotationBean = new AnnotationBean();
		annotationBean.setPackage(packageName);
		return annotationBean;
	}
	
	@Bean
	@ConfigurationProperties(prefix="dubbo.provider")
	public ProviderConfig providerConfig() {
		return new ProviderConfig();
	}

	@Bean
	@ConfigurationProperties(prefix="dubbo.consumer")
	public ConsumerConfig consumerConfig() {
		return new ConsumerConfig();
	}
}
