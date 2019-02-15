package com.space.flight.controller;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.space.flight.model.Flight;
import com.space.flight.model.Tourist;
import com.space.flight.repository.FlightRepository;
import com.space.flight.repository.TouristRepository;
import com.space.flight.service.FlightService;

@Controller
@RequestMapping(value = "/flight-management")
public class FlightController {

	@Autowired
	FlightRepository flight;
	
	@Autowired 
	TouristRepository tourist;
	Logger logger = LoggerFactory.getLogger(FlightController.class);
	
	@Autowired
	FlightService flightService;
	
	/*
	 * flight-management GET (a)
	 */
	  @GetMapping(value = "/flight")
	    public String allFlights(Model model,@Valid Flight flightValid,BindingResult bindingResult) {
		  
		  List<Flight> allFlight = flight.findAll();
	        model.addAttribute("allFlight", allFlight);
	        model.addAttribute("flight", new Flight());
		  return "flight";
	    }
	  
	  /*
	   * add new flight (b)
	   */
	  @PostMapping(value = "/flight")
	    public String addFlight(@Valid Flight flightValid,BindingResult bindingResult,RedirectAttributes redirectAttrs,Model model) {
		  
		  if(bindingResult.hasErrors()) {
			  logger.info( " Cos poszło nie tak!");
			  List<Flight> allFlight = flight.findAll();
		      model.addAttribute("allFlight", allFlight);
			  return "flight";
		  }
		  else {
		  redirectAttrs.addFlashAttribute("message", "Added new flight!");
		  flight.save(flightValid);
	        return "redirect:/flight-management/flight";
		  }
	    }
	  /*
	   * delete flight (c)
	   */
	  @RequestMapping(value = "/delete/{id}")
	    public String deleteFlight(@PathVariable Long id, RedirectAttributes redirectAttrs) {
		  
		  	flight.deleteById(id);
			redirectAttrs.addFlashAttribute("message", "Flight was deleted");
			return "redirect:/flight-management/flight";
	    }
	  
	  /*
	   * flight list (c)
	   */
	  @GetMapping(value = "/flight/{id}")
	    public String editFlight(Model model,@PathVariable Long id) {
		  
		  List<Tourist> allFlightTourist = tourist.findFlight(id);
		  List<Tourist> allNewTourist = tourist.findNewPassenger(id);
		  Optional<Flight> currentFlight = flight.findById(id);
		  Flight currFlight = currentFlight.get();
		  model.addAttribute("flight",currFlight);
		  model.addAttribute("tourist",allFlightTourist );
		  model.addAttribute("test",allNewTourist );	
	        return "flightManagement";
	    }
	  /*
	   * remove tourist flight (dI)
	   */
	  @RequestMapping(value = "/flight/{flightId}/delete/{touristId}")
	    public String deleteTouristFromFlight(@PathVariable Long touristId, @PathVariable Long flightId,RedirectAttributes redirectAttrs) {

		  if(flightService.removeTouristFlight(touristId, flightId))
			  redirectAttrs.addFlashAttribute("message", "Tourist was deleted from flight");
			  else
				  redirectAttrs.addFlashAttribute("error", "Tourist was not deleted from flight");		  
	        return "redirect:/flight-management/flight/{flightId}";
	    }
	  
	  /*
	   * add tourist to flight (dII)
	   */
	  @PostMapping(value = "/flight/{flightId}")
	    public String addTouristToFlight(HttpServletRequest request,@PathVariable Long flightId, RedirectAttributes redirectAttrs) {
		  
		  Long touristId = Long.parseLong(request.getParameter("touristId"));
		  if(flightService.addTouristToFlight(flightId,touristId))
		  redirectAttrs.addFlashAttribute("message", "Tourist was added to flight!");
		  else  redirectAttrs.addFlashAttribute("error", "To many passenger in plane!");
	        return "redirect:/flight-management/flight/{flightId}";
	    }
}
