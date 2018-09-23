package com.ekarabacak.reactivesecurity.routes;

import com.ekarabacak.reactivesecurity.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class UserRouter {

    @Bean
    public RouterFunction<ServerResponse> userRoutes(UserHandler userHandler){
        return RouterFunctions.route(GET("/"), userHandler::findAllUsers)
                .andRoute(GET("/principal"),userHandler::getPrincipal)
                .andRoute(GET("/{id}"),userHandler::findUser)
                .andRoute(POST("/"),userHandler::createUser)
                .andRoute(PUT("/{id}"),userHandler::updateUser)
                .andRoute(DELETE("/{id}"),userHandler::deleteUser);
    }
}
