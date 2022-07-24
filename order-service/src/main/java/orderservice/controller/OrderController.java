package orderservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class OrderController {

    @GetMapping("/healthCheck")
    public String healthCheck() {
        return "hello world from Order Service.";
    }
}
