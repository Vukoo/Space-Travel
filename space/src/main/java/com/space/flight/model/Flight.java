package com.space.flight.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "flight")
public @Data class Flight {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private @Getter @Setter Long id;
	@NotNull
	@Column(name= "departure_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime  departureTime;
	
	@NotNull
	@Column(name= "arrival_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private  LocalDateTime arrivalTime;
	@Column(name= "seats_number")
	@NotNull
	private  Long seatsNumber;
	@NotNull
	private Double price;
	
	@ManyToMany(cascade = CascadeType.MERGE)
	 @JoinTable(
		        name = "tourist_flight", 
		        joinColumns = { @JoinColumn(name = "flight_id") }, 
		        inverseJoinColumns = { @JoinColumn(name = "tourist_id") }
		    )
    private Set<Tourist> tourists = new HashSet<>();
    
	public Flight() {	
	}
	
	public void addTourist(Tourist tourist) {
		tourists.add(tourist);
		tourist.getFlight().add(this);
	}
	
	public void removeTourist(Tourist tourist) {
	tourists.remove(tourist);
	tourist.getFlight().add(this);
	}


}