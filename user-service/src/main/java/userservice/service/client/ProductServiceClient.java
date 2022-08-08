package userservice.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import userservice.dto.ProductDto;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;
import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProductServiceClient {

    @NonFinal
    @Value("${product.service.url}")
    String url;
    HttpClient httpClient;
    ObjectMapper objectMapper;

    @SneakyThrows
    public ProductDto getProductByProductCode(String productCode, String requestId) {
        HttpRequest httpRequest = buildHttpRequest(productCode, requestId);
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), ProductDto.class);
    }

    private HttpRequest buildHttpRequest(String productCode, String requestId) {
        String uri = url + productCode;
        return HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("X-RequestId", requestId)
                .timeout(Duration.of(10, SECONDS))
                .GET()
                .build();
    }
}
