package dev.overlax.agency.controller;

import dev.overlax.agency.dto.TourDto;
import dev.overlax.agency.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;

    @GetMapping
    public String getAll(Model model, Pageable pageable) {
        List<TourDto> tours = tourService.findAll(pageable);
        model.addAttribute("tours", tours);

        return "index";
    }
}
