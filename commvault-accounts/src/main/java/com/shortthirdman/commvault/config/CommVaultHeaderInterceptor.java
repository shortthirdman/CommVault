// Copyright (c) ShortThirdMan 2025. All rights reserved.
package com.shortthirdman.commvault.config;

import com.shortthirdman.commvault.exception.CommVaultException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

@Slf4j
@Component
public class CommVaultHeaderInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        log.info("In the preHandle() method: Before sending request to controller :: {}", request);
        var apiSecret = request.getHeader("X-CommVault-Api-Token");
        if (Objects.isNull(apiSecret) || apiSecret.isEmpty()) {
            log.error("CommVault API Token is missing");
            try {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write( "Forbidden: Invalid or missing CommVault API Token");
            } catch (Exception e) {
                log.error("Failed to write error response: {}", ExceptionUtils.getFullStackTrace(e));
                throw new CommVaultException("Failed to write error response", e);
            }
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) throws Exception {
        log.info("In the postHandle() method: Before sending request to client :: {}", request);
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) throws Exception {
        log.info("In the afterCompletion() method: After sending response to client :: {}", request);
        if (ex != null) {
            throw new CommVaultException("Unknown error occurred while processing request", ex);
        }
    }
}
