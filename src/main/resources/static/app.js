const showingsDiv = document.getElementById("showings");
const reservationsDiv = document.getElementById("reservations");
const reservationForm = document.getElementById("reservationForm");
const showingSelect = document.getElementById("showingId");
const message = document.getElementById("message");

async function loadShowings() {
    const response = await fetch("/api/showings");
    const showings = await response.json();

    showingsDiv.innerHTML = "";
    showingSelect.innerHTML = "";

    showings.forEach(showing => {
        const div = document.createElement("div");
        div.innerHTML = `
            <strong>${showing.movieTitle}</strong><br>
            Hall: ${showing.theaterName}<br>
            Time: ${showing.showTime}<br>
            Price: ${showing.price} DKK
            <hr>
        `;
        showingsDiv.appendChild(div);

        const option = document.createElement("option");
        option.value = showing.id;
        option.textContent = `${showing.movieTitle} - ${showing.showTime}`;
        showingSelect.appendChild(option);
    });
}

async function loadReservations() {
    const response = await fetch("/api/reservations");
    const reservations = await response.json();

    reservationsDiv.innerHTML = "";

    reservations.forEach(reservation => {
        const div = document.createElement("div");
        div.innerHTML = `
            <strong>${reservation.customerName}</strong><br>
            Tickets: ${reservation.ticketCount}<br>
            Total price: ${reservation.totalPrice} DKK<br>
            Showing ID: ${reservation.showingId}
            <hr>
        `;
        reservationsDiv.appendChild(div);
    });
}

reservationForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const reservation = {
        customerName: document.getElementById("customerName").value,
        showingId: parseInt(document.getElementById("showingId").value),
        ticketCount: parseInt(document.getElementById("ticketCount").value)
    };

    const response = await fetch("/api/reservations", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(reservation)
    });

    if (response.ok) {
        const savedReservation = await response.json();
        message.textContent = `Reservation created. Total price: ${savedReservation.totalPrice} DKK`;
        reservationForm.reset();
        loadReservations();
    } else {
        message.textContent = "Could not create reservation.";
    }
});

loadShowings();
loadReservations();