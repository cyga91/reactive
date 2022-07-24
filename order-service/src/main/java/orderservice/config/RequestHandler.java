package orderservice.config;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import orderservice.dto.OrderDto;
import orderservice.exception.InputValidationException;
import orderservice.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RequestHandler {

    OrderService orderService;

    public Mono<ServerResponse> orderServiceHandler(ServerRequest serverRequest) {
        String phoneNumber = serverRequest.pathVariable("phoneNumber");

        if (phoneNumber.length() != 11) {
            return Mono.error(new InputValidationException("wrong phone number", 400, phoneNumber));
        }

        Flux<OrderDto> responseFlux = orderService.getOrdersReactiveByPhoneNumber(phoneNumber);
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(responseFlux, OrderDto.class);
    }
}
