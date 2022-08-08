package userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;
import reactor.util.context.Context;
import reactor.util.retry.Retry;
import userservice.dto.OrderDto;
import userservice.dto.ProductDto;
import userservice.dto.UserDto;
import userservice.repo.UserInfoRepository;
import userservice.service.client.OrderServiceClient;
import userservice.service.client.ProductServiceClient;
import userservice.util.MapperUtil;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

    UserInfoRepository userInfoRepository;
    OrderServiceClient orderServiceClient;
    ProductServiceClient productServiceClient;

    public Mono<UserDto> addUser(Mono<UserDto> userDtoMono) {
        return userDtoMono
                .map(MapperUtil::mapToUser)
                .flatMap(userInfoRepository::insert)
                .map(MapperUtil::mapToUserDto);
    }

    public Mono<ProductDto> getBestProductByUserId(String id, String requestId) {
        return getUserById(id)
                .doOnEach(logOnNext(userDto -> log.info(userDto.toString())))
                .doOnEach(logOnError(errorProductDto -> log.info(errorProductDto.toString())))
                .map(UserDto::getPhoneNumber)
                .flatMapMany(orderServiceClient::getOrdersByPhoneNumber)
                .doOnEach(logOnNext(orderDto -> log.info(orderDto.toString())))
                .doOnEach(logOnError(errorProductDto -> log.info(errorProductDto.toString())))
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                .map(orderDto -> productServiceClient.getProductByProductCode(orderDto.getProductCode(), requestId))
                .doOnEach(logOnNext(productDto -> log.info(productDto.toString())))
                .doOnEach(logOnError(errorProductDto -> log.info(errorProductDto.toString())))
                .collectList()
                .map(this::getProductDtoWithHighestScore)
                .doOnEach(logOnNext(nextProductDto -> log.info(nextProductDto.toString())))
                .doOnEach(logOnError(errorProductDto -> log.info(errorProductDto.toString())))
                .contextWrite(Context.of("CONTEXT_KEY", requestId));
    }

    public Mono<UserDto> getUserById(String id) {
        return userInfoRepository.getUserById(id)
                .map(MapperUtil::mapToUserDto);
    }

    public Flux<OrderDto> getOrdersByPhoneNumber(String phoneNumber) {
        return orderServiceClient.getOrdersByPhoneNumber(phoneNumber);
    }

    @SneakyThrows
    public ProductDto getProductByProductCode(String productCode) {
        String id = "123";
        return productServiceClient.getProductByProductCode(productCode, id);
    }

    private ProductDto getProductDtoWithHighestScore(List<ProductDto> productDtos) {
        return productDtos.stream()
                .max(Comparator.comparing(ProductDto::getScore))
                .orElse(new ProductDto());
    }

    public static <T> Consumer<Signal<T>> logOnNext(Consumer<T> logStatement) {
        return signal -> {
            if (!signal.isOnNext()) return;
            Optional<String> toPutInMdc = signal.getContext().getOrEmpty("CONTEXT_KEY");

            toPutInMdc.ifPresentOrElse(tpim -> {
                        log.info("requestId: " + tpim);
                        try (MDC.MDCCloseable cMdc = MDC.putCloseable("MDC_KEY", tpim)) {
                            logStatement.accept(signal.get());
                        }
                    },
                    () -> logStatement.accept(signal.get()));
        };
    }

    public static Consumer<Signal<?>> logOnError(Consumer<Throwable> errorLogStatement) {
        return signal -> {
            if (!signal.isOnError()) return;
            Optional<String> toPutInMdc = signal.getContext().getOrEmpty("CONTEXT_KEY");

            toPutInMdc.ifPresentOrElse(tpim -> {
                        log.info("requestId: " + tpim);
                        try (MDC.MDCCloseable cMdc = MDC.putCloseable("MDC_KEY", tpim)) {
                            errorLogStatement.accept(signal.getThrowable());
                        }
                    },
                    () -> errorLogStatement.accept(signal.getThrowable()));
        };
    }

}