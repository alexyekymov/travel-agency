package dev.overlax.agency.controller;

import dev.overlax.agency.security.AuthUser;
import dev.overlax.agency.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public String orders(@AuthenticationPrincipal AuthUser user, Model model) {
        model.addAttribute("orders", orderService.getOrders(user.getId()));
        return "order/orders";
    }

    @PostMapping("/checkout")
    public String checkout(@AuthenticationPrincipal AuthUser user, RedirectAttributes redirectAttributes) {
        try {
            orderService.checkout(user.getId());
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart";
        }
        return "redirect:/orders";
    }
}
