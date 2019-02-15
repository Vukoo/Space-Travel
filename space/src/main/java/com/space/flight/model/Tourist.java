package com.space.flight.model;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Table(name = "tourist")
public @Data class Tourist {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(name= "first_name")
	private String firstName;
	@NotBlank
	@Column(name = "last_name")
	private String lastName;
	private boolean sex;
	@NotBlank
	private String country;
	private String note;
	@NotNull
	private Date birthdate;
	
	@ManyToMany(mappedBy = "tourists")
	private Set<Flight> flight = new HashSet<>();

	public Tourist() {
	}
	
	public void addFlight(Flight addFlight) {
		flight.add(addFlight);
		addFlight.getTourists().add(this);
	}

	public void removeFlight(Flight addFflight) {
	flight.remove(addFflight);
	addFflight.getTourists().add(this);
	}
}
