package com.unibuc.gymapp.repositories;

import com.unibuc.gymapp.models.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SetRepository extends JpaRepository<Set, Long> {

}
