package com.space.flight.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.space.flight.model.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
			@Query(value = "SELECT Count(tourist_id) FROM tourist_flight where flight_id=:uid",
					nativeQuery = true)
			Long countPassenger(@Param("uid") Long flightFilter);
			@Query(value = "Select seats_number from flight where id=:uid",
					nativeQuery = true)
			Long availableSeats(@Param("uid") Long flightFilter);
			
			@Query(value="Select f.* from flight f join tourist_flight tf on f.id=tf.flight_id where tf.tourist_id=:uid",
					nativeQuery = true)
			List<Flight> findPassenger(@Param("uid") Long flightFilter);
			@Query(value = "Select * from flight where id NOT IN (Select f.id from flight f join tourist_flight tf on f.id=tf.flight_id where tf.tourist_id=:uid)",
					nativeQuery = true)
			List<Flight> findNewFlight(@Param("uid") Long flightFilter);
}
