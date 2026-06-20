package dev.overlax.agency.controller;

import dev.overlax.agency.dto.TourRequest;
import dev.overlax.agency.dto.TourResponse;
import dev.overlax.agency.model.type.HotelType;
import dev.overlax.agency.model.type.TourType;
import dev.overlax.agency.model.type.TransferType;
import dev.overlax.agency.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;

    @ModelAttribute("tourTypes")
    public TourType[] tourTypes() {
        return TourType.values();
    }

    @ModelAttribute("hotelTypes")
    public HotelType[] hotelTypes() {
        return HotelType.values();
    }

    @ModelAttribute("transferTypes")
    public TransferType[] transferTypes() {
        return TransferType.values();
    }

    @GetMapping
    public String getAll(Model model, Pageable pageable) {
        List<TourResponse> tours = tourService.findAll(pageable);
        model.addAttribute("tours", tours);

        return "index";
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable UUID id, Model model) {
        TourResponse tour = tourService.findById(id);
        model.addAttribute("tour", tour);
        return "tours/detail";
    }

    @GetMapping("/tours/new")
    public String newForm(Model model) {
        model.addAttribute("tourRequest", new TourRequest(null, null, null, null, null, null, null, null, null, null));
        return "tours/form";
    }

    @PostMapping("/tours")
    public String create(@ModelAttribute TourRequest tourRequest) {
        TourResponse created = tourService.create(tourRequest);
        return "redirect:/" + created.id();
    }
}
