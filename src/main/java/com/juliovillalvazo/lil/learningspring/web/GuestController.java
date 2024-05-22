package com.juliovillalvazo.lil.learningspring.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.juliovillalvazo.lil.learningspring.business.ReservationService;
import com.juliovillalvazo.lil.learningspring.data.Guest;

@Controller
@RequestMapping("/guests")
public class GuestController {
    private final ReservationService reservationService;

    public GuestController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getGuestsLis(Model model) {
        List<Guest> guests = this.reservationService.getAllGuests();
        model.addAttribute("guestList", guests);
        
        return "guest";
    }
}
