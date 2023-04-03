package com.example.demo.config;

import ch.qos.logback.core.net.ssl.KeyStoreFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyStoreConfig {

    @Bean
    public KeyStoreFactoryBean keyStoreFactory() {
        KeyStoreFactoryBean factoryBean = new KeyStoreFactoryBean();
        factoryBean.setType("JKS");
        factoryBean.setLocation("./certificates/keystore.jks");
        factoryBean.setPassword("keystore-password");
        return factoryBean;
    }

}
