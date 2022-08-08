package userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import userservice.dto.ProductDto;
import userservice.service.UserService;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class UserController {

    UserService userService;

    @SneakyThrows
    @GetMapping("/product/{productCode}")
    public ProductDto getProductByProductCode(@PathVariable String productCode) {
        return userService.getProductByProductCode(productCode);
    }

    @SneakyThrows
    @GetMapping("/{id}")
    public Mono<ProductDto> getBestProductByUserId(@PathVariable String id,
                                                   @RequestHeader(required = false, name = "X-RequestId") String requestId) {

        return userService.getBestProductByUserId(id, requestId);
    }

    @GetMapping("/healthCheck")
    public String healthCheck() {
        return "hello world fromm Product Service.";
    }
}
