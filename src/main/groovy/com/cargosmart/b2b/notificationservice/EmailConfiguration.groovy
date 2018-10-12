package com.cargosmart.b2b.notificationservice

import groovy.util.logging.Slf4j
import org.simplejavamail.mailer.Mailer
import org.simplejavamail.mailer.MailerBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Slf4j
@Component
@ConditionalOnBean(value = EmailProperty.class)
class EmailConfiguration {

    @Autowired
    EmailProperty emailProperty;

    @Bean
    Mailer mailer() {
        log.info("Mailer config host:${emailProperty.host} port:${emailProperty.port} username:${emailProperty.username} password:${emailProperty.password}");
        return MailerBuilder
                .withSMTPServerHost(emailProperty.host)
                .withSMTPServerPort(emailProperty.port)
                .withSMTPServerUsername(emailProperty.username?.isEmpty() ? null : emailProperty.username)
                .withSMTPServerPassword(emailProperty.password?.isEmpty() ? null : emailProperty.password)
                .buildMailer();
    }
}

@Configuration
@ConfigurationProperties(prefix = "email")
@ConditionalOnProperty(prefix = 'email', name = 'host')
class EmailProperty {
    String host;
    int port;
    String username = '';
    String password = '';
    Notification notification;

}

class Notification{
    boolean enable;
    String from;
    List<String> toList = [];
    List<String> ccList = [];
}
