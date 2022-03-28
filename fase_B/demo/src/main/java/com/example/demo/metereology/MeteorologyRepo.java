package com.example.demo.metereology;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeteorologyRepo extends JpaRepository<Meteorology, Long> {

    @Query(value = "SELECT s FROM Meteorology m WHERE m.timeStamp = ?1",
            nativeQuery = true)
    Optional<List<Meteorology>> findSamplesByDate(LocalDate timeStamp); // TODO: Need to solve the Query's exception
}
