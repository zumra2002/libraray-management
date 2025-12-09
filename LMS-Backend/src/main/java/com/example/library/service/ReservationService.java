package com.example.library.service;


import com.example.library.entity.Reservation;
import com.example.library.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class ReservationService {


private final ReservationRepository repo;


public ReservationService(ReservationRepository repo) { this.repo = repo; }


public List<Reservation> findAll() { return repo.findAll(); }


public Optional<Reservation> findById(Long id) { return repo.findById(id); }


public Reservation save(Reservation reservation) { return repo.save(reservation); }


public void deleteById(Long id) { repo.deleteById(id); }
}