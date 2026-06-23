package dev.overlax.agency.controller;

import dev.overlax.agency.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final TourService tourService;

    @GetMapping
    public String getAll() {
        return "forward:/tours";
    }
}
