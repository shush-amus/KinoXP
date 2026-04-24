const showingsDiv = document.getElementById("showings");
const reservationsDiv = document.getElementById("reservations");
const reservationForm = document.getElementById("reservationForm");
const loginForm = document.getElementById("loginForm");
const logoutButton = document.getElementById("logoutButton");
const showingSelect = document.getElementById("showingId");
const ticketCountInput = document.getElementById("ticketCount");
const message = document.getElementById("message");
const authStatus = document.getElementById("authStatus");
const authMessage = document.getElementById("authMessage");
const protectedSections = document.querySelectorAll(".protected");

let allShowings = [];
let currentUser = null;
let csrfToken = "";

async function readErrorMessage(response, fallbackMessage) {
    const contentType = response.headers.get("content-type") || "";

    if (contentType.includes("application/json")) {
        try {
            const data = await response.json();
            return data.message || data.error || fallbackMessage;
        } catch (error) {
            return fallbackMessage;
        }
    }

    const text = await response.text();
    return text || fallbackMessage;
}

function getCookieValue(name) {
    const cookie = document.cookie
        .split("; ")
        .find((part) => part.startsWith(`${name}=`));

    return cookie ? decodeURIComponent(cookie.split("=")[1]) : "";
}

async function fetchCsrfToken() {
    const response = await fetch("/api/auth/csrf", { credentials: "same-origin" });
    if (response.ok) {
        const data = await response.json();
        csrfToken = data.token;
    }
}

async function apiFetch(url, options = {}) {
    const headers = new Headers(options.headers || {});
    const method = (options.method || "GET").toUpperCase();

    if (["POST", "PUT", "PATCH", "DELETE"].includes(method)) {
        headers.set("X-XSRF-TOKEN", csrfToken || getCookieValue("XSRF-TOKEN"));
    }

    return fetch(url, {
        credentials: "same-origin",
        ...options,
        headers
    });
}

function renderProtectedState() {
    const authenticated = Boolean(currentUser?.authenticated);

    protectedSections.forEach((section) => {
        section.classList.toggle("is-hidden", !authenticated);
    });

    authStatus.textContent = authenticated
        ? `Logged in as ${currentUser.username}`
        : "You are not logged in.";
}

function formatPrice(value) {
    return new Intl.NumberFormat("da-DK", {
        style: "currency",
        currency: "DKK"
    }).format(value);
}

function formatDateTime(value) {
    return new Intl.DateTimeFormat("da-DK", {
        dateStyle: "medium",
        timeStyle: "short"
    }).format(new Date(value));
}

async function loadShowings() {
    const response = await fetch("/api/showings");
    const showings = await response.json();

    allShowings = showings;
    showingsDiv.innerHTML = "";
    showingSelect.innerHTML = "";

    showings.forEach((showing) => {
        const tile = document.createElement("article");
        tile.className = "tile";
        tile.innerHTML = `
            <h3>${showing.movieTitle}</h3>
            <p><strong>Hall:</strong> ${showing.theaterName}</p>
            <p><strong>Time:</strong> ${formatDateTime(showing.showTime)}</p>
            <p><strong>Base price:</strong> ${formatPrice(showing.price)}</p>
        `;
        showingsDiv.appendChild(tile);

        const option = document.createElement("option");
        option.value = showing.id;
        option.textContent = `${showing.movieTitle} - ${formatDateTime(showing.showTime)}`;
        showingSelect.appendChild(option);
    });
}

async function loadCurrentUser() {
    const response = await fetch("/api/auth/me", { credentials: "same-origin" });
    currentUser = await response.json();
    renderProtectedState();

    if (currentUser.authenticated) {
        await loadReservations();
    } else {
        reservationsDiv.innerHTML = "";
    }
}

async function loadReservations() {
    const response = await apiFetch("/api/reservations");
    if (!response.ok) {
        reservationsDiv.innerHTML = "";
        return;
    }

    const reservations = await response.json();
    reservationsDiv.innerHTML = "";

    if (reservations.length === 0) {
        reservationsDiv.innerHTML = `<article class="tile"><p>No reservations yet for this user.</p></article>`;
        return;
    }

    reservations.forEach((reservation) => {
        const showing = allShowings.find((item) => item.id === reservation.showingId);
        const tile = document.createElement("article");
        tile.className = "tile";
        tile.innerHTML = `
            <h3>${showing ? showing.movieTitle : `Showing #${reservation.showingId}`}</h3>
            <p><strong>User:</strong> ${reservation.username}</p>
            <p><strong>Tickets:</strong> ${reservation.ticketCount}</p>
            <p><strong>Booking fee:</strong> ${formatPrice(reservation.bookingFee)}</p>
            <p><strong>Total price:</strong> ${formatPrice(reservation.totalPrice)}</p>
            <button data-reservation-id="${reservation.id}" class="secondary">Delete reservation</button>
        `;
        reservationsDiv.appendChild(tile);
    });
}

loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    authMessage.textContent = "";

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const response = await apiFetch("/api/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ username, password })
    });

    if (response.ok) {
        currentUser = await response.json();
        renderProtectedState();
        authMessage.textContent = "Login successful.";
        await loadReservations();
    } else {
        authMessage.textContent = await readErrorMessage(response, "Login failed.");
    }
});

logoutButton.addEventListener("click", async () => {
    await apiFetch("/api/auth/logout", { method: "POST" });
    currentUser = null;
    message.textContent = "";
    authMessage.textContent = "Logged out.";
    renderProtectedState();
    reservationsDiv.innerHTML = "";
});

reservationForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    message.textContent = "";

    const selectedShowingId = Number.parseInt(showingSelect.value, 10);
    const ticketCount = Number.parseInt(ticketCountInput.value, 10);

    const response = await apiFetch("/api/reservations", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            showingId: selectedShowingId,
            ticketCount
        })
    });

    if (response.ok) {
        reservationForm.reset();
        message.textContent = "Reservation created successfully.";
        await loadReservations();
    } else {
        message.textContent = await readErrorMessage(
            response,
            "Could not create reservation."
        );
    }
});

reservationsDiv.addEventListener("click", async (event) => {
    const reservationId = event.target.dataset.reservationId;
    if (!reservationId) {
        return;
    }

    const response = await apiFetch(`/api/reservations/${reservationId}`, {
        method: "DELETE"
    });

    if (response.ok) {
        await loadReservations();
    }
});

async function init() {
    await fetchCsrfToken();
    await loadShowings();
    await loadCurrentUser();
}

init();
