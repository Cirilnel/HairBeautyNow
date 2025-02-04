function validateForm() {
    var metodoPagamento = document.getElementById("metodoPagamento").value;
    var numeroCarta = document.getElementById("numeroCarta").value;
    var cvv = document.getElementById("cvv").value;
    var scadenza = document.getElementById("scadenza").value;
    var indirizzo = document.getElementById("indirizzo").value;

    // Controllo base se i campi sono vuoti
    if (!metodoPagamento || !numeroCarta || !cvv || !scadenza || !indirizzo) {
        alert("Tutti i campi sono obbligatori.");
        return false;
    }

    // Controllo della validità del numero di carta (lunghezza e formato)
    var regexCarta = /^[0-9]{16}$/;
    if (!regexCarta.test(numeroCarta)) {
        alert("Il numero di carta deve essere di 16 cifre.");
        return false;
    }

    // Controllo della validità del CVV (lunghezza 3 cifre)
    var regexCVV = /^[0-9]{3}$/;
    if (!regexCVV.test(cvv)) {
        alert("Il CVV deve essere di 3 cifre.");
        return false;
    }

    // Controllo della data di scadenza (MM/YY)
    var regexScadenza = /^(0[1-9]|1[0-2])\/\d{2}$/;
    if (!regexScadenza.test(scadenza)) {
        alert("La data di scadenza deve essere nel formato MM/YY.");
        return false;
    }

    // Aggiungi il controllo se la data di scadenza è passata
    var currentDate = new Date();
    var currentMonth = currentDate.getMonth() + 1; // I mesi vanno da 0 a 11, quindi aggiungiamo 1
    var currentYear = currentDate.getFullYear() % 100; // Otteniamo solo gli ultimi due numeri dell'anno (YY)

    // Estrai mese e anno dalla data di scadenza
    var scadenzaParts = scadenza.split('/');
    var scadenzaMonth = parseInt(scadenzaParts[0], 10);
    var scadenzaYear = parseInt(scadenzaParts[1], 10);

    // Controlla se l'anno di scadenza è passato o se è lo stesso mese ma con data inferiore
    if (scadenzaYear < currentYear || (scadenzaYear === currentYear && scadenzaMonth < currentMonth)) {
        alert("La data di scadenza della carta è già passata.");
        return false;
    }

    // Se la validazione è passata, invia il form
    return true;
}
