package com.space.flight.service;


public interface IFlightService {
	Boolean removeTouristFlight(Long touristId, Long flightId);
	Boolean addTouristToFlight(Long touristId, Long flightId);
	Boolean addFlightToTourist(Long touristId, Long flightId);
}
