package productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProductDto {

    String productId;
    String productCode;
    String productName;
    Double score;
}
