package com.example.moviesApi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.moviesApi.entities.Movie;

public interface MovieRepository extends JpaRepository<Movie, Integer>{

}
