package com.space.flight.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.space.flight.model.Flight;
import com.space.flight.model.Tourist;
import com.space.flight.repository.FlightRepository;
import com.space.flight.repository.TouristRepository;

@Service
public class FlightService implements IFlightService {

	@Autowired
	FlightRepository flight;
	@Autowired 
	TouristRepository tourist;
	
	@Override
	public Boolean removeTouristFlight(Long touristId, Long flightId) {
		Tourist currTourist = findTourist(touristId);
		Flight currFlight = findFlight(flightId);
		currFlight.removeTourist(currTourist);
		flight.save(currFlight);
		return true;
	}
	
	@Override
	public Boolean addTouristToFlight(Long flightId , Long touristId) {
		Tourist currTourist = findTourist(touristId);
		Flight currFlight = findFlight(flightId);
		
		if(hasSeats(currTourist, currFlight)) {
			currFlight.addTourist(currTourist);
			 flight.save(currFlight);
		  return true;
		}
		return false;
	}
	
	@Override
	public Boolean addFlightToTourist(Long flightId , Long touristId) {
		Tourist currTourist = findTourist(touristId);
		Flight currFlight = findFlight(flightId);
		
		if(hasSeats(currTourist, currFlight)) {
			currTourist.addFlight(currFlight);
			 tourist.save(currTourist);
		  return true;
		}
		return false;
	}
	
	public Tourist findTourist(Long touristId) {
		  Optional<Tourist> addTourist = tourist.findById(touristId);
		  Tourist currentTourist = addTourist.get();		  
		return currentTourist;
	}
	public Flight findFlight(Long flightId) {
		 Optional<Flight> currentFlight = flight.findById(flightId);
		  Flight flightCurrent = currentFlight.get();
		return flightCurrent;
	}
	
	
	public boolean hasSeats(Tourist touristId, Flight flightId) {
		if(flight.countPassenger(flightId.getId() ) >= flight.availableSeats(flightId.getId()) )
		return false;
		else return true;
	}
	
}
