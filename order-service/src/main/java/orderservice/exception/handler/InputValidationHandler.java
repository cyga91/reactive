package orderservice.exception.handler;

import orderservice.exception.InputValidationException;
import orderservice.exception.model.FailedValidationResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@Service
public class InputValidationHandler {

    public BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> exceptionHandler() {
        return (err, req) -> {
            InputValidationException ex = (InputValidationException) err;
            return ServerResponse.badRequest().bodyValue(buildInputValidationResponse(ex));
        };
    }

    private FailedValidationResponse buildInputValidationResponse(InputValidationException ex) {
        return FailedValidationResponse.builder()
                .input(ex.getInput())
                .message(ex.getMessage())
                .errorCode(ex.getErrorCode())
                .build();
    }
}
