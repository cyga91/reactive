package userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@Service
public class HttpRequestHandler {

    public Mono<Throwable> get4xxMono(ClientResponse clientResponse) {
        if (HttpStatus.BAD_REQUEST.equals(clientResponse.statusCode())) {
            return Mono.error(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad request.."));
        } else {
            return Mono.error(new HttpClientErrorException(HttpStatus.NOT_FOUND, "Entity not found."));
        }
    }

    public Mono<Throwable> get5xxMono(ClientResponse clientResponse) {
        return Mono.error(new HttpClientErrorException(clientResponse.statusCode()));
    }
}
