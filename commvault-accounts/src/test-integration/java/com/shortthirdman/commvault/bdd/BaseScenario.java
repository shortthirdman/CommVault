package com.shortthirdman.commvault.bdd;

import org.springframework.beans.factory.annotation.Autowired;

public class BaseScenario {

    @Autowired
    protected CucumberContextHolder cucumberContextHolder;

    @Autowired
    protected RuntimeEnvironment runtimeEnv;

}
