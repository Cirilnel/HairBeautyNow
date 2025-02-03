document.addEventListener("DOMContentLoaded", function () {
    console.log("UserMenu.js è stato caricato e il DOM è pronto!");

    const userIcon = document.getElementById("logged-user-icon");
    const dropdownMenu = document.getElementById("dropdown-menu");

    // Verifica se gli elementi esistono
    if (userIcon && dropdownMenu) {
        console.log("Gli elementi sono stati trovati!");

        userIcon.addEventListener("click", function (event) {
            event.stopPropagation(); // Evita la chiusura immediata quando si clicca l'icona
            dropdownMenu.classList.toggle("show"); // Mostra/nasconde il menu
        });

        // Chiudi il menu se si clicca fuori
        document.addEventListener("click", function (event) {
            if (!userIcon.contains(event.target) && !dropdownMenu.contains(event.target)) {
                dropdownMenu.classList.remove("show");
            }
        });
    }
});
