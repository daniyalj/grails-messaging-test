package com.microservice

import grails.boot.GrailsApp
import groovy.transform.InheritConstructors
import org.springframework.boot.Banner
import org.springframework.core.env.Environment
import org.springframework.core.io.ResourceLoader

@InheritConstructors
class BannerGrailsApp extends GrailsApp {
    
    public BannerGrailsApp(Object... sources) {
        super(sources)
        bannerMode = Banner.Mode.LOG
    }
    
    public BannerGrailsApp(ResourceLoader resourceLoader, Object... sources) {
        super(resourceLoader, sources)
        bannerMode = Banner.Mode.LOG
    }
    
    protected void printBanner(final Environment environment) {
        // Create GrailsBanner instance.
        final GroovyBanner banner = new GroovyBanner()
        
        banner.printBanner(environment, Application, System.out)
    }
    
}




