package userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
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

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
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

    public Mono<ProductDto> getBestProductByUserId(String id) {
        return getUserById(id)
                .log()
                .map(UserDto::getPhoneNumber)
                .flatMapMany(orderServiceClient::getOrdersByPhoneNumber)
                .log()
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                .map(orderDto -> productServiceClient.getProductByProductCode(orderDto.getProductCode()))
                .log()
                .collectList()
                .map(this::getProductDtoWithHighestScore)
                .log()
                .subscribeOn(Schedulers.boundedElastic()); // why 
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
        return productServiceClient.getProductByProductCode(productCode);
    }

    private ProductDto getProductDtoWithHighestScore(List<ProductDto> productDtos) {
        return productDtos.stream()
                .max(Comparator.comparing(ProductDto::getScore))
                .orElse(new ProductDto());
    }

}
