function rimuoviPrenotazione(prenotazioneId) {
    // Conferma la rimozione
    if (confirm('Sei sicuro di voler rimuovere questa prenotazione?')) {
        // Crea la richiesta AJAX
        var xhr = new XMLHttpRequest();

        // Configura la richiesta POST
        xhr.open("POST", "rimuoviPrenotazione", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

        // Definisci il comportamento al termine della richiesta
        xhr.onload = function () {
            if (xhr.status === 200) {
                // Se la rimozione è riuscita, rimuovi la riga dalla tabella
                var row = document.getElementById("prenotazione-" + prenotazioneId);
                if (row) {
                    row.remove(); // Rimuovi la riga dalla tabella
                }
            } else {
                alert("Errore nella rimozione della prenotazione.");
            }
        };

        // Invia la richiesta con l'ID della prenotazione
        xhr.send("prenotazioneId=" + prenotazioneId);
    }
}
