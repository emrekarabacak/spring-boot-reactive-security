package com.ekarabacak.reactivesecurity.routes;

import com.ekarabacak.reactivesecurity.handler.AuthHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class AuthRouter {

    @Bean
    RouterFunction<ServerResponse> authRoutes(AuthHandler authenticationHeader){
        return RouterFunctions.route(RequestPredicates.POST("/oauth/token"),authenticationHeader::authenticate);
    }
}
