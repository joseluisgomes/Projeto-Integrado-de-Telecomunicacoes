package com.example.demo.metereology;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeteorologyRepo extends JpaRepository<Long, Meteorology> {

}
