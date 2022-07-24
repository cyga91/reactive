package userservice.config;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import userservice.dto.OrderDto;
import userservice.dto.ProductDto;
import userservice.dto.UserDto;
import userservice.service.UserService;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RequestHandler {

    UserService userService;

    public Mono<ServerResponse> addUserHandler(ServerRequest serverRequest) {
        Mono<UserDto> userDtoMono = serverRequest.bodyToMono(UserDto.class);
        Mono<UserDto> responseMono = userService.addUser(userDtoMono);
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(responseMono, UserDto.class);
    }

    public Mono<ServerResponse> getUserHandler(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<UserDto> responseMono = userService.getUserById(id);
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(responseMono, UserDto.class);
    }

    public Mono<ServerResponse> getOrdersByPhoneNumberHandler(ServerRequest serverRequest) {
        String phoneNumber = serverRequest.pathVariable("phoneNumber");
        Flux<OrderDto> responseFlux = userService.getOrdersByPhoneNumber(phoneNumber);
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(responseFlux, OrderDto.class);
    }

    public Mono<ServerResponse> getBestProductByUserIdHandler(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<ProductDto> responseFlux = userService.getBestProductByUserId(id);
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(responseFlux, ProductDto.class);
    }
}
