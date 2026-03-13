# KinoXP Reservation System

This project is a prototype cinema reservation system developed as part of the Fullstack project.

The goal of the project was to build a system that allows customers to view movie showings and create ticket reservations.

## Features

The prototype includes the following functionality:

- View available showings
- Create reservations
- View reservations
- Automatic ticket price calculation

## Technologies Used

Backend:
- Java
- Spring Boot
- Spring Web
- Spring Data JPA

Frontend:
- HTML
- CSS
- JavaScript

Database:
- H2 database

## System Architecture

The system uses a REST API where the frontend communicates with the backend.

Main endpoints:

GET /api/showings  
GET /api/reservations  
POST /api/reservations

## Data Model

Main entities:

Showing
- id
- showTime
- price
- movieTitle
- theaterName

Reservation
- id
- customerName
- ticketCount
- totalPrice
- showingId

One showing can have multiple reservations.

## Prototype Scope

The project focuses on the core reservation functionality.

Some features from the original requirements were not implemented due to time constraints, including:

- Login system
- Seat selection
- Admin security
- Advanced pricing
- Edit and delete reservations
- Docker deployment
- CI/CD pipeline

These features are included in the product backlog for future development.

## How to run the project

1. Start the Spring Boot application
2. Open a browser
3. Go to:

http://localhost:8080

## Project Structure

Backend:
- Controllers
- Repositories
- Entities

Frontend:
- index.html
- app.js
- style.css

## Documentation

The project includes:

- Product backlog
- Use case diagram
- ER diagram
- Sequence diagram
- GitHub Kanban board

## Purpose

The project demonstrates a working prototype and prioritization of requirements according to agile development principles.