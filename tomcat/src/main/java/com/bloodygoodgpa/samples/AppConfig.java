package com.bloodygoodgpa.samples;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;


//from auth0
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.bloodygoodgpa.samples"})
public class AppConfig implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext container) {
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(AppConfig.class);

		container.addListener(new ContextLoaderListener(rootContext));
		AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();

		ServletRegistration.Dynamic dispatcher = container
				.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
	}

}
