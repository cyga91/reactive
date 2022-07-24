package orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import orderservice.dto.OrderDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.security.SecureRandom;
import java.util.Random;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class OrderService {

    Random random = new SecureRandom();

    public Flux<OrderDto> getOrdersReactiveByPhoneNumber(String phoneNumber) {
        return Flux.range(1, 10)
                .doOnNext(i -> sleepSeconds(1))
                .doOnNext(i -> System.out.println("reactive service processing"))
                .map(i -> new OrderDto(getRandomValue(), phoneNumber, getRandomValue()));
    }

    private String getRandomValue() {
        return String.valueOf(Math.abs(random.nextInt()));
    }

    public static void sleepSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
