package dev.overlax.agency.controller;

import dev.overlax.agency.dto.TourFilterRequest;
import dev.overlax.agency.dto.TourRequest;
import dev.overlax.agency.dto.TourResponse;
import dev.overlax.agency.model.type.HotelType;
import dev.overlax.agency.model.type.TourType;
import dev.overlax.agency.model.type.TransferType;
import dev.overlax.agency.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/tours")
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
    public String getAll(Model model,
                         @ModelAttribute("filterRequest") TourFilterRequest filter,
                         Pageable pageable) {
        Page<TourResponse> tours = tourService.findAll(filter, pageable);
        model.addAttribute("tours", tours);

        return "index";
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable UUID id, Model model) {
        TourResponse tour = tourService.findById(id);
        model.addAttribute("tour", tour);
        return "tours/detail";
    }

    @GetMapping("/new")
    public String newForm(@ModelAttribute TourRequest tourRequest) {
        return "tours/form";
    }

    @PostMapping
    public String create(@ModelAttribute TourRequest tourRequest) {
        TourResponse created = tourService.create(tourRequest);
        return "redirect:/tours/" + created.id();
    }

    @GetMapping("/manage")
    public String dashboard(Model model,
                            @ModelAttribute("filterRequest") TourFilterRequest filter,
                            Pageable pageable) {
        Page<TourResponse> tours = tourService.findAll(filter, pageable);
        model.addAttribute("tours", tours);
        return "manager/dashboard";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable UUID id, Model model) {
        model.addAttribute("tourRequest", tourService.getForEdit(id));
        model.addAttribute("tourId", id);
        return "tours/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable UUID id, @ModelAttribute TourRequest tourRequest) {
        tourService.update(id, tourRequest);
        return "redirect:/tours/manage";
    }

    @PostMapping("/{id}/hot")
    public String toggleHot(@PathVariable UUID id) {
        tourService.toggleHot(id);
        return "redirect:/tours/manage";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable UUID id) {
        tourService.delete(id);
        return "redirect:/tours/manage";
    }
}
