package orderservice.exception.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@ToString
public class FailedValidationResponse {

    private final int errorCode;
    private final String input;
    private final String message;
}
