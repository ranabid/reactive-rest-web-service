package com.spring.reactor.ws.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.spring.reactor.ws.rest.common.ApplicationProperties;
import com.spring.reactor.ws.rest.core.TokenManager;

import javax.annotation.PostConstruct;

@Component
public class StaticContextInitializer {

    @Autowired
    private ApplicationProperties prop;

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    public void init() {
        TokenManager.setApplicationProperties(prop);
    }
}