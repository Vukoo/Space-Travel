package com.space.flight.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
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
@RequestMapping(value = "/tourist-management")
public class TouristController {
	@Autowired
	FlightRepository flight;
	
	@Autowired 
	TouristRepository tourist;
	
	Logger logger = LoggerFactory.getLogger(TouristController.class);
	
	@Autowired
	FlightService flightService;
	
	/*
	 * Handle empty string format for date 
	 * 
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    dateFormat.setLenient(false);

	    // true passed to CustomDateEditor constructor means convert empty String to null
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	@GetMapping(value = "/")
	public String home() {		
		return "home";
	}
	
	/*
	 * tourist-management GET (a)
	 * 
	 */
	  @GetMapping(value = "/tourist")
	    public String allTourist(Model model ) {
		  
		  List<Tourist> allTourist = tourist.findAll();
	        model.addAttribute("allTourist", allTourist);
	        model.addAttribute("tourist", new Tourist());
	        return "tourist";
	    }
	  
	  	/*
		 * add new tourist (b)
		 * 
		 */
	  @PostMapping(value = "/tourist")
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
	  
	  	/*
		 * delete tourist (c)
		 * 
		 */
	  @RequestMapping(value = "/delete/{id}")
	    public String deleteTourist(@PathVariable Long id, RedirectAttributes  redirectAttrs) { 
		  
		  	tourist.deleteById(id);
			redirectAttrs.addFlashAttribute("message", "Tourist was deleted");
			return "redirect:/tourist-management/tourist";
		  
	    }
	  	/*
		 * tourist-management  (d)
		 * 
		 */
	  @GetMapping(value = "/tourist/{id}")
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
	  
	  	/*
		 * remove tourist flight (dI)
		 * 
		 */
	  @RequestMapping(value = "/tourist/{touristId}/delete/{flightId}")
	    public String deleteTouristFlight(@PathVariable Long touristId,@PathVariable Long flightId,RedirectAttributes redirectAttrs) {
		  
		  if(flightService.removeTouristFlight(touristId, flightId))
			  redirectAttrs.addFlashAttribute("message", "Flight was deleted ");
			  else
				  redirectAttrs.addFlashAttribute("error", "Flight was not deleted! ");	
	        return "redirect:/tourist-management/tourist/{touristId}";
	    }
	  
	  	/*
		 * add flight to tourist (dII)
		 * 
		 */
	  @PostMapping(value = "/tourist/{touristId}")
	    public String addTouristFlight(HttpServletRequest request,@PathVariable Long touristId, RedirectAttributes redirectAttrs) {
		  
	        Long flightId = Long.parseLong(request.getParameter("flightId"));
	        if(flightService.addFlightToTourist(flightId,touristId))
	  		  redirectAttrs.addFlashAttribute("message", "Flight was added !");
	  		  else  redirectAttrs.addFlashAttribute("error", "To many passenger in plane!");
	        return "redirect:/tourist-management/tourist/{touristId}";
	    }
}
