package userservice.service.client;

import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import userservice.dto.OrderDto;
import userservice.exception.HttpRequestHandler;

import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class OrderServiceClient {

    @NonFinal
    @Value("${order.service.url}")
    String url;
    HttpRequestHandler httpRequestHandler;

    public OrderServiceClient(HttpRequestHandler requestHandler) {
        this.httpRequestHandler = requestHandler;
    }

    public Flux<OrderDto> getOrdersByPhoneNumber(String phoneNumber) {
        return buildWebClient()
                .get()
                .uri("flux/{phoneNumber}", phoneNumber)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, httpRequestHandler::get4xxMono)
                .onStatus(HttpStatus::is5xxServerError, httpRequestHandler::get5xxMono)
                .bodyToFlux(OrderDto.class)
                .onErrorStop();
    }

    private WebClient buildWebClient() {
        return WebClient.builder()
                .baseUrl(url)
                .build();
    }
}
