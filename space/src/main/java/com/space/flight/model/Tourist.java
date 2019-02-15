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

@Entity
@Table(name = "tourist")
public class Tourist {
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
	
		//add flight to this flight
		public void addFlight(Flight addFlight) {
			flight.add(addFlight);
			addFlight.getTourists().add(this);
		}
	

		//remove flight from this flight
		public void removeFlight(Flight addFflight) {
		flight.remove(addFflight);
		addFflight.getTourists().add(this);
		}
	
	public Set<Flight> getFlight() {
		return flight;
	}
	public void setFlight(Set<Flight> flight) {
		this.flight = flight;
	}


	public Tourist() {
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public boolean isSex() {
		return sex;
	}
	public void setSex(boolean sex) {
		this.sex = sex;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
}
