// Copyright (c) ShortThirdMan 2025. All rights reserved.
package com.shortthirdman.commvault.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CommVaultInterceptorConfig implements WebMvcConfigurer {

    private final CommVaultHeaderInterceptor headerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.debug("Configuring interceptors for CommVault");
        registry.addInterceptor(headerInterceptor)
                .addPathPatterns("/commvault/api/accounts/test-account/**");
    }
}
