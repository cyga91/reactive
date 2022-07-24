package productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import productservice.dto.ProductDto;

import java.security.SecureRandom;
import java.util.Random;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProductService {

    Random random = new SecureRandom();

    public ProductDto getProductsByProductCode(String productCode) {
        return new ProductDto(getRandomValue(), productCode, "shoes", getScore());
    }

    private String getRandomValue() {
        return String.valueOf(Math.abs(random.nextInt()));
    }

    private double getScore() {
        return random.nextInt(100) / 100.0;
    }
}
