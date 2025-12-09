package com.example.library.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
public class Reservation {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


@ManyToOne
@JoinColumn(name = "user_id")
private User user;


@ManyToOne
@JoinColumn(name = "book_id")
private Book book;


private LocalDateTime reservedAt;


public Reservation() {}


public Reservation(User user, Book book) {
this.user = user;
this.book = book;
this.reservedAt = LocalDateTime.now();
}


public Long getId() { return id; }
public void setId(Long id) { this.id = id; }


public User getUser() { return user; }
public void setUser(User user) { this.user = user; }


public Book getBook() { return book; }
public void setBook(Book book) { this.book = book; }


public LocalDateTime getReservedAt() { return reservedAt; }
public void setReservedAt(LocalDateTime reservedAt) { this.reservedAt = reservedAt; }
}