package org.unl.pacas;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Clock;
import java.util.Arrays;

@SpringBootApplication
@PWA(name = "Vaadin Pacas", shortName = "Pacas", offlineResources = {"images/logo.png"})
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("No se pudo determinar la dirección del host, usando localhost como fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                        "Aplicación '{}' se está ejecutando! Accede a:\n\t" +
                        "Local: \t\t{}://localhost:{}{}\n\t" +
                        "Externa: \t{}://{}:{}{}\n\t" +
                        "Perfiles(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name", "vaadin-pacas"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                env.getActiveProfiles().length == 0 ? Arrays.toString(env.getDefaultProfiles()) : Arrays.toString(env.getActiveProfiles())
        );
    }
}