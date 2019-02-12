package com.space.flight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.space.flight.model.Tourist;

@Repository
public interface TouristRepository extends JpaRepository<Tourist, Long> {
	@Query(value="Select t.id,t.first_name, t.last_name, t.sex, t.country, t.note, t.birthdate"
			+ " from tourist t join tourist_flight tf on t.id=tf.tourist_id "
			+ "where tf.flight_id=:uid",
			nativeQuery = true)
	List<Tourist> findFlight(@Param("uid") Long flightFilter);
	@Query(value = "Select * from tourist where " + 
			"id NOT IN (Select t.id " + 
			"from tourist t join tourist_flight tf on t.id=tf.tourist_id where tf.flight_id=:uid);",
			nativeQuery = true)
	List<Tourist> findNewPassenger(@Param("uid") Long flightFilter);
}
