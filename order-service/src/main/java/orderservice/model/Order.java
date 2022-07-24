package orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Order {

    String productCode;
    String phoneNumber;
    String orderNumber;
}
