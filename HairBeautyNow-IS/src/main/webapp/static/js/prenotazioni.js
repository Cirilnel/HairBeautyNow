function aggiornaOrari() {
    console.log("La funzione aggiornaOrari è stata chiamata.");

    let giornoSelezionato = document.getElementById("data").value;
    console.log("Giorno selezionato:", giornoSelezionato);

    let orari = document.getElementById("orario");

    // Svuota il select degli orari
    orari.innerHTML = "<option value=''>Caricamento...</option>";

    // Verifica se ci sono orari per il giorno selezionato
    if (fasceOrarieByDay[giornoSelezionato]) {
        console.log("Orari disponibili per il giorno selezionato:", fasceOrarieByDay[giornoSelezionato]);

        // Aggiungi gli orari al select
        fasceOrarieByDay[giornoSelezionato].forEach(fascia => {
            let option = document.createElement("option");
            option.value = fascia;
            option.textContent = fascia; // Mostra l'orario
            orari.appendChild(option);
        });
    } else {
        console.log("Nessun orario disponibile per il giorno selezionato.");
        let option = document.createElement("option");
        option.textContent = "Nessun orario disponibile"; // Messaggio di errore
        orari.appendChild(option);
    }
}
