package dev.overlax.agency.controller;

import dev.overlax.agency.security.AuthUser;
import dev.overlax.agency.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public String cart(@AuthenticationPrincipal AuthUser user, Model model) {
        model.addAttribute("cart", cartService.getCart(user.getId()));
        return "cart/cart";
    }

    @PostMapping("/add")
    public String add(@AuthenticationPrincipal AuthUser user,
                      @RequestParam UUID tourId,
                      @RequestParam(defaultValue = "1") int seats) {
        cartService.addToCart(user.getId(), tourId, seats);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String update(@AuthenticationPrincipal AuthUser user,
                         @RequestParam UUID tourId,
                         @RequestParam int seats) {
        cartService.updateSeats(user.getId(), tourId, seats);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String remove(@AuthenticationPrincipal AuthUser user,
                         @RequestParam UUID tourId) {
        cartService.removeFromCart(user.getId(), tourId);
        return "redirect:/cart";
    }
}
