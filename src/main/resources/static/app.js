const showingsDiv = document.getElementById("showings");
const reservationsDiv = document.getElementById("reservations");
const reservationForm = document.getElementById("reservationForm");
const showingSelect = document.getElementById("showingId");
const customerNameInput = document.getElementById("customerName");
const ticketCountInput = document.getElementById("ticketCount");
const message = document.getElementById("message");

let allShowings = [];

async function loadShowings() {
    const response = await fetch("/api/showings");
    const showings = await response.json();

    allShowings = showings;

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

    const selectedShowingId = parseInt(showingSelect.value);
    const ticketCount = parseInt(ticketCountInput.value);
    const selectedShowing = allShowings.find(showing => showing.id === selectedShowingId);

    if (!selectedShowing) {
        message.textContent = "Could not find selected showing.";
        return;
    }

    const reservation = {
        customerName: customerNameInput.value,
        showingId: selectedShowingId,
        ticketCount: ticketCount,
        totalPrice: selectedShowing.price * ticketCount
    };

    const response = await fetch("/api/reservations", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(reservation)
    });

    if (response.ok) {
        reservationForm.reset();
        await loadReservations();
        message.textContent = `Reservation created successfully.`;
    } else {
        const errorText = await response.text();
        message.textContent = "Could not create reservation: " + errorText;
    }
});

loadShowings();
loadReservations();