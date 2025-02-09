function validateForm() {
    var metodoPagamento = document.getElementById("metodoPagamento").value;
    var numeroCarta = document.getElementById("numeroCarta").value;
    var cvv = document.getElementById("cvv").value;
    var scadenza = document.getElementById("scadenza").value;
    var indirizzo = document.getElementById("indirizzo").value;
    var emailPaypal = document.getElementById("emailPaypal") ? document.getElementById("emailPaypal").value : '';

    // Reset error messages
    resetErrorMessages();

    // Controllo base se i campi sono vuoti (ignora numeroCarta, CVV e scadenza se il metodo di pagamento è PayPal)
    if (!metodoPagamento || (!indirizzo && metodoPagamento !== "paypal")) {
        showError("Tutti i campi sono obbligatori.");
        return false;
    }

    // Se il metodo di pagamento è PayPal, solo l'email di PayPal deve essere presente
    if (metodoPagamento === "paypal") {
        if (!emailPaypal) {
            showError("L'email PayPal è obbligatoria.");
            return false;
        }
    }

    // Se il metodo di pagamento non è PayPal, esegui il controllo per carta, cvv, e scadenza
    if (metodoPagamento === "visa" || metodoPagamento === "mastercard") {

        // Controllo della validità del numero di carta (lunghezza e formato)
        if (numeroCarta.length !== 16) {
            showError("Il numero di carta deve essere di 16 cifre.");
            return false;
        }
        var regexCarta = /^[0-9]{16}$/;
        if (!regexCarta.test(numeroCarta)) {
            showError("Il numero di carta deve essere di 16 cifre.");
            return false;
        }

        // Controllo della validità del CVV (lunghezza 3 cifre)
        var regexCVV = /^[0-9]{3}$/;
        if (!regexCVV.test(cvv)) {
            showError("Il CVV deve essere di 3 cifre.");
            return false;
        }

        // Controllo della data di scadenza (MM/YY)
        var regexScadenza = /^(0[1-9]|1[0-2])\/\d{2}$/;
        if (!regexScadenza.test(scadenza)) {
            showError("La data di scadenza deve essere nel formato MM/YY.");
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
            showError("La data di scadenza della carta è già passata.");
            return false;
        }
    }

    // Se la validazione è passata, invia il form
    return true;
}

function showError(message) {
    // Mostra l'errore in un messaggio globale o vicino al campo
    var errorMessageDiv = document.getElementById("errorMessages");
    if (!errorMessageDiv) {
        errorMessageDiv = document.createElement("div");
        errorMessageDiv.id = "errorMessages";
        errorMessageDiv.style.color = "red";
        errorMessageDiv.style.fontWeight = "bold";
        document.body.insertBefore(errorMessageDiv, document.body.firstChild);
    }
    errorMessageDiv.innerText = message;
}

function resetErrorMessages() {
    // Reset dei messaggi di errore visivi
    var errorMessageDiv = document.getElementById("errorMessages");
    if (errorMessageDiv) {
        errorMessageDiv.innerText = ''; // Pulisce il messaggio di errore esistente
    }
}
