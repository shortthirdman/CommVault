package com.shortthirdman.commvault.bdd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
class RuntimeEnvironment {
    @Autowired
    private Environment environment;

    private String serverUrl;


    public String getServerUrl() {
        if (serverUrl == null) {
            serverUrl = "http://localhost:" + environment.getProperty("local.server.port") + "/";
        }

        return serverUrl;
    }
}
