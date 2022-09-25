package com.emiyacc.drugreminder.configuration;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringBoot同时使用http与https: https://blog.csdn.net/qq506930427/article/details/118694568
 */

@Configuration
public class TomcatConfiguration {

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(80);
        tomcatServletWebServerFactory.addAdditionalTomcatConnectors(connector);
        return tomcatServletWebServerFactory;
    }
}
