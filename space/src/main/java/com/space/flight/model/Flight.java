package com.space.flight.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "flight")
public class Flight {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	@Column(name= "departure_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime  departureTime;
	
	@NotNull
	@Column(name= "arrival_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime arrivalTime;
	@Column(name= "seats_number")
	@NotNull
	private Long seatsNumber;
	@NotNull
	private Double price;
	
	@ManyToMany(cascade = CascadeType.MERGE)
	 @JoinTable(
		        name = "tourist_flight", 
		        joinColumns = { @JoinColumn(name = "flight_id") }, 
		        inverseJoinColumns = { @JoinColumn(name = "tourist_id") }
		    )
    private Set<Tourist> tourists = new HashSet<>();
	
	
	//add tourist to this flight
	public void addTourist(Tourist tourist) {
		tourists.add(tourist);
		tourist.getFlight().add(this);
	}
	//remove toursit from this flight
	public void removeTourist(Tourist tourist) {
	tourists.remove(tourist);
	tourist.getFlight().add(this);
	}
    
	public Set<Tourist> getTourists() {
		return tourists;
	}
	public void setTourists(Set<Tourist> tourists) {
		this.tourists = tourists;
	}
	public Flight() {
		
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDateTime  getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(LocalDateTime  departureTime) {
		this.departureTime = departureTime;
	}
	public LocalDateTime getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(LocalDateTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public Long getSeatsNumber() {
		return seatsNumber;
	}
	public void setSeatsNumber(Long seatsNumber) {
		this.seatsNumber = seatsNumber;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}

}