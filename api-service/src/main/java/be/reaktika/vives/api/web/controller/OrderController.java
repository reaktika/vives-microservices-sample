package be.reaktika.vives.api.web.controller;

import be.reaktika.vives.furniture.api.product.OrderApiController;
import be.reaktika.vives.furniture.api.product.OrderApiDelegate;
import org.springframework.stereotype.Controller;

@Controller
public class OrderController extends OrderApiController {
    public OrderController(OrderApiDelegate delegate) {
        super(delegate);
    }
}
