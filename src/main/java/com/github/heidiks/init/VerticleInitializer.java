package com.github.heidiks.init;

import com.github.heidiks.controller.QuartzVerticle;
import io.vertx.core.Vertx;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class VerticleInitializer {

    private final QuartzVerticle quartzVerticle;

    public VerticleInitializer(QuartzVerticle quartzVerticle) {
        this.quartzVerticle = quartzVerticle;
    }

    @PostConstruct
    public void init() {
        Vertx.vertx().deployVerticle(quartzVerticle);
    }
}
