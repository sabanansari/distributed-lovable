package com.ansari.distributed_lovable.common_lib.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerExceptionResolver;

@AutoConfiguration
@Slf4j
public class SharedSecurityAutoConfiguration {

    @Bean
    public AuthUtil authUtil() {
        return new AuthUtil();
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter(AuthUtil authUtil,@Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver){
        return new JwtAuthFilter(authUtil, handlerExceptionResolver);
    }

    @Bean
    public RequestInterceptor requestInterceptor() {

        log.info("Configuring Feign Request Interceptor");

        return requestTemplate -> {

            Authentication authentication =
                    SecurityContextHolder.getContext()
                            .getAuthentication();

            log.info(
                    "Feign auth propagation auth={}",
                    authentication);

            if (authentication != null &&
                    authentication.getCredentials()
                            instanceof String token) {

                log.info(
                        "Propagating JWT token via Feign");

                requestTemplate.header(
                        "Authorization",
                        "Bearer " + token);
            } else {
                log.warn(
                        "No JWT found in SecurityContext");
            }
        };
    }
}
