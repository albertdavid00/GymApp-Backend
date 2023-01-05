package com.unibuc.gymapp.repositories;

import com.unibuc.gymapp.models.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {
    Optional<Gym> findByName(String name);
    Optional<Gym> findByNameAndLocation(String name, String location);
}
