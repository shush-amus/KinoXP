package com.example.kinoxp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private int ticketCount;
    private double totalPrice;
    private Long showingId;
    private Double bookingFee;

    public Reservation() {
    }

    public Reservation(String username, int ticketCount, double totalPrice, Long showingId, double bookingFee) {
        this.username = username;
        this.ticketCount = ticketCount;
        this.totalPrice = totalPrice;
        this.showingId = showingId;
        this.bookingFee = bookingFee;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getShowingId() {
        return showingId;
    }

    public void setShowingId(Long showingId) {
        this.showingId = showingId;
    }

    public Double getBookingFee() {
        return bookingFee == null ? 0.0 : bookingFee;
    }

    public void setBookingFee(Double bookingFee) {
        this.bookingFee = bookingFee;
    }
}
