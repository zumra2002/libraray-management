package com.example.library.controller;

import com.example.library.entity.Reservation;
import com.example.library.entity.User;
import com.example.library.entity.Book;
import com.example.library.service.ReservationService;
import com.example.library.service.UserService;
import com.example.library.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/reservations")
public class ReservationController {


private final ReservationService reservationService;
private final UserService userService;
private final BookService bookService;


public ReservationController(ReservationService reservationService, UserService userService, BookService bookService) {
this.reservationService = reservationService;
this.userService = userService;
this.bookService = bookService;
}


@GetMapping
public List<Reservation> all() { return reservationService.findAll(); }


@GetMapping("/{id}")
public ResponseEntity<Reservation> getById(@PathVariable Long id) {
return reservationService.findById(id)
.map(ResponseEntity::ok)
.orElse(ResponseEntity.notFound().build());
}


@PostMapping
public ResponseEntity<Reservation> create(@RequestParam Long userId, @RequestParam Long bookId) {
java.util.Optional<User> userOpt = java.util.Optional.ofNullable(userService.findById(userId));
java.util.Optional<Book> bookOpt = java.util.Optional.ofNullable(bookService.findBookById(bookId));


if (!userOpt.isPresent() || !bookOpt.isPresent()) {
return ResponseEntity.badRequest().build();
}


Reservation reservation = new Reservation(userOpt.get(), bookOpt.get());
return ResponseEntity.ok(reservationService.save(reservation));
}


@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(@PathVariable Long id) {
reservationService.deleteById(id);
return ResponseEntity.noContent().build();
}
} 