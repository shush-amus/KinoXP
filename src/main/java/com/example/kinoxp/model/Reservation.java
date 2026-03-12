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

    private String customerName;
    private int ticketCount;
    private double totalPrice;
    private Long showingId;

    public Reservation(){}

    public Reservation(String customerName, int ticketCount, double totalPrice, Long showingId){
        this.customerName=customerName;
        this.ticketCount=ticketCount;
        this.totalPrice=totalPrice;
        this.showingId = showingId;
    }

    public Long getId(){return id;}

    public String getCustomerName(){return customerName;}

    public void setCustomerName(String customerName){this.customerName=customerName;}

    public int getTicketCount(){return ticketCount;}

    public void setTicketCount(int ticketCount){this.ticketCount=ticketCount;}

    public double getTotalPrice() {return totalPrice;}

    public Long getShowingId(){return showingId;}

    public void setShowingId(Long showingId){this.showingId=showingId;}


}
