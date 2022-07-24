package orderservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class InputValidationException extends RuntimeException {

    private final String message;
    private final int errorCode;
    private final String  input;
}
