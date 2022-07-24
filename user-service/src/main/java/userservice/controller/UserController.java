package userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import userservice.dto.ProductDto;
import userservice.service.UserService;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @SneakyThrows
    @GetMapping("/product/{productCode}")
    public ProductDto getProductByProductCode(@PathVariable String productCode) {
        return userService.getProductByProductCode(productCode);
    }

    @GetMapping("/healthCheck")
    public String healthCheck() {
        return "hello world fromm Product Service.";
    }
}
