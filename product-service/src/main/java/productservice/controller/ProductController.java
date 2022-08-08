package productservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import productservice.dto.ProductDto;
import productservice.service.ProductService;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("product")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class ProductController {

    ProductService productService;

    @GetMapping(value = "/{productCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductDto getProductsByProductCode(@PathVariable String productCode,
                                               @RequestHeader(required = false, name = "X-RequestId") String requestId) {

        log.info("X-RequestId: " + requestId);
        return productService.getProductsByProductCode(productCode);
    }

    @GetMapping("/healthCheck")
    public String healthCheck() {
        return "hello world fromm Product Service.";
    }
}
