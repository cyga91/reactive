package orderservice.config;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import orderservice.exception.InputValidationException;
import orderservice.exception.handler.InputValidationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static lombok.AccessLevel.PRIVATE;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class OrderRouterConfig {

    RequestHandler requestHandler;
    InputValidationHandler inputValidationHandler;

    @Bean
    public RouterFunction<ServerResponse> highLevelOrderRouter() {
        return RouterFunctions.route()
                .path("order", this::serverResponseRouterFunction)
                .build();
    }

    private RouterFunction<ServerResponse> serverResponseRouterFunction() {
        return RouterFunctions.route()
                .GET("flux/{phoneNumber}", requestHandler::orderServiceHandler)
                .onError(InputValidationException.class, inputValidationHandler.exceptionHandler())
                .build();
    }
}
