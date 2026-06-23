package dev.overlax.agency.controller;

import dev.overlax.agency.model.type.Role;
import dev.overlax.agency.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @ModelAttribute("allRoles")
    public Role[] allRoles() {
        return Role.values();
    }

    @GetMapping("/users")
    public String users(@PageableDefault(sort = {"lastName", "firstName"}) Pageable pageable,
                        Model model) {
        model.addAttribute("users", userService.getAllUsers(pageable));
        return "admin/users";
    }

    @PostMapping("/users/{id}/block")
    public String block(@PathVariable UUID id) {
        userService.blockUser(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/unblock")
    public String unblock(@PathVariable UUID id) {
        userService.unblockUser(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/roles")
    public String changeRoles(@PathVariable UUID id, @RequestParam(required = false) Set<Role> roles) {
        userService.changeRoles(id, roles == null ? Set.of() : roles);
        return "redirect:/admin/users";
    }
}
