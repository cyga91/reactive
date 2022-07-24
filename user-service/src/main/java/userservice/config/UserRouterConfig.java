package userservice.config;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static lombok.AccessLevel.PRIVATE;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserRouterConfig {

    RequestHandler requestHandler;

    @Bean
    public RouterFunction<ServerResponse> highLevelUserRouter() {
        return RouterFunctions.route()
                .path("user", this::serverResponseRouterFunction)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> serverResponseRouterFunction() {
        return RouterFunctions.route()
                .POST("mono/add", requestHandler::addUserHandler)
                .GET("mono/{id}", requestHandler::getUserHandler)
                .GET("order/{phoneNumber}", requestHandler::getOrdersByPhoneNumberHandler)
                .GET("{id}", requestHandler::getBestProductByUserIdHandler)
                .build();
    }
}
