package userservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.ProxySelector;
import java.net.http.HttpClient;

@Configuration
public class Beans {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build();
    }
}
