package com.space.flight.controller;


import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
public class RestController {
	
	@Autowired
	FlightRepository flight;
	@Autowired 
	TouristRepository tourist;
	Logger logger = LoggerFactory.getLogger(RestController.class);
	
	@Autowired
	FlightService flightService;
	 
	@GetMapping(value = "/home")
	public String home() {		
		return "home";
	}
	
	//tourist-management GET (a)
	  @GetMapping(value = "/tourist-management/tourist")
	    public String allTourist(Model model ) {
		  List<Tourist> allTourist = tourist.findAll();
	        model.addAttribute("allTourist", allTourist);
	        model.addAttribute("tourist", new Tourist());
	        return "tourist";
	    }
	  
	  //ADD NEW TOURIST (b)
	  @PostMapping(value = "/tourist-management/tourist")
	    public String addTourist(@Valid Tourist touristValid,BindingResult bindingResult,RedirectAttributes redirectAttrs, Model model) {
		  if(bindingResult.hasErrors()) {
			  logger.info( " Cos poszło nie tak!");
			  List<Tourist> allTourist = tourist.findAll();
		        model.addAttribute("allTourist", allTourist);
			  return "tourist";
		  }
		  else {
		  redirectAttrs.addFlashAttribute("message", "Added new tourist!");
		  tourist.save(touristValid);
	        return "redirect:/tourist-management/tourist";
		  }
	    }
	  
	  
	  //delete tourist (c)
	  @RequestMapping(value = "/tourist-management/delete/{id}")
	    public String deleteTourist(@PathVariable Long id, RedirectAttributes  redirectAttrs) { 
		  	tourist.deleteById(id);
			redirectAttrs.addFlashAttribute("message", "Tourist was deleted");
			return "redirect:/tourist-management/tourist";
		  
	    }
	  //zarządzanie turystami (d)
	  @GetMapping(value = "/tourist-management/tourist/{id}")
	    public String editTourist(Model model,@PathVariable Long id) {
		  
		  List<Flight> allTouristFlight = flight.findPassenger(id);
		  List<Flight> allNewFlight = flight.findNewFlight(id);
		  Optional<Tourist> currentTourist = tourist.findById(id);
		  Tourist currtourist = currentTourist.get();
		  model.addAttribute("tourist",currtourist);
		  model.addAttribute("flight",allTouristFlight );
		  model.addAttribute("test",allNewFlight );	
	        return "touristManagement";
	    }
	  //remove tourist flight (dI)
	  @RequestMapping(value = "/tourist-management/tourist/{touristId}/delete/{flightId}")
	    public String deleteTouristFlight(@PathVariable Long touristId,@PathVariable Long flightId,RedirectAttributes redirectAttrs) {
		  if(flightService.removeTouristFlight(touristId, flightId))
			  redirectAttrs.addFlashAttribute("message", "Flight was deleted ");
			  else
				  redirectAttrs.addFlashAttribute("error", "Flight was not deleted from ");	
	        return "redirect:/tourist-management/tourist/{touristId}";
	    }
	  
	  //add flight to tourist (dII)
	  @PostMapping(value = "/tourist-management/tourist/{touristId}")
	    public String addTouristFlight(HttpServletRequest request,@PathVariable Long touristId, RedirectAttributes redirectAttrs) {
	        Long flightId = Long.parseLong(request.getParameter("flightId"));
	        if(flightService.addFlightToTourist(flightId,touristId))
	  		  redirectAttrs.addFlashAttribute("message", "Flight was added !");
	  		  else  redirectAttrs.addFlashAttribute("error", "To many passenger in plane!");
	        return "redirect:/tourist-management/tourist/{touristId}";
	    }
	  
	  
	//flight-management GET (a)
	  @GetMapping(value = "/flight-management/flight")
	    public String allFlights(Model model,@Valid Flight flightValid,BindingResult bindingResult) {
		  List<Flight> allFlight = flight.findAll();
	        model.addAttribute("allFlight", allFlight);
	        model.addAttribute("flight", new Flight());
		  return "flight";
	    }
	  //ADD NEW Flight (b)
	  @PostMapping(value = "/flight-management/flight")
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
	  
	  //delete flight (c)
	  @RequestMapping(value = "/flight-management/delete/{id}")
	    public String deleteFlight(@PathVariable Long id, RedirectAttributes redirectAttrs) {
		  	flight.deleteById(id);
			redirectAttrs.addFlashAttribute("message", "Flight was deleted");
			return "redirect:/flight-management/flight";
	    }
	  
	  //zarządzanie lotami (d)
	  @GetMapping(value = "/flight-management/flight/{id}")
	    public String editFlight(Model model,@PathVariable Long id) {
		  //show all tourist in THIS flight	  
		  List<Tourist> allFlightTourist = tourist.findFlight(id);
		  List<Tourist> allNewTourist = tourist.findNewPassenger(id);
		  Optional<Flight> currentFlight = flight.findById(id);
		  Flight currFlight = currentFlight.get();
		  model.addAttribute("flight",currFlight);
		  model.addAttribute("tourist",allFlightTourist );
		  model.addAttribute("test",allNewTourist );	
	        return "flightManagement";
	    }
	  //remove tourist flight (dI)
	  @RequestMapping(value = "/flight-management/flight/{flightId}/delete/{touristId}")
	    public String deleteTouristFromFlight(@PathVariable Long touristId, @PathVariable Long flightId,RedirectAttributes redirectAttrs) {

		  if(flightService.removeTouristFlight(touristId, flightId))
			  redirectAttrs.addFlashAttribute("message", "Tourist was deleted from flight");
			  else
				  redirectAttrs.addFlashAttribute("error", "Tourist was not deleted from flight");		  
	        return "redirect:/flight-management/flight/{flightId}";
	    }
	  
	  //add tourist to flight (dII)
	  @PostMapping(value = "/flight-management/flight/{flightId}")
	    public String addTouristToFlight(HttpServletRequest request,@PathVariable Long flightId, RedirectAttributes redirectAttrs) {
		  Long touristId = Long.parseLong(request.getParameter("touristId"));

		  if(flightService.addTouristToFlight(flightId,touristId))
		  redirectAttrs.addFlashAttribute("message", "Tourist was added to flight!");
		  else  redirectAttrs.addFlashAttribute("error", "To many passenger in plane!");
	        return "redirect:/flight-management/flight/{flightId}";
	    }
	
}
