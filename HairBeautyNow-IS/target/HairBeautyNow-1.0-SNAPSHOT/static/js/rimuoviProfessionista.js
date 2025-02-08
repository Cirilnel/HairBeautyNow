function rimuoviProfessionista(professionistaId) {
    var xhr = new XMLHttpRequest();

    console.log("contextPath: ", contextPath);  // Debugging

    var url = contextPath + "/rimuoviProfessionista";
    console.log("Request URL: ", url);  // Debugging

    if (!professionistaId || isNaN(professionistaId)) {
        console.log("ID professionista non valido");
        alert("ID professionista non valido");
        return;
    }

    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    var params = "professionistaId=" + professionistaId;
    console.log("Request params: ", params);  // Debugging

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            if (xhr.status == 200) {
                var response = xhr.responseText;
                console.log("Response from server: ", response);  // Log della risposta del server
                if (response.includes("successo")) {
                    location.reload();  // Ricarica la pagina se la rimozione è andata a buon fine
                } else {
                    alert("Errore nella rimozione del professionista: " + response);  // Mostra l'errore specifico
                }
            } else if (xhr.status == 400) {  // Gestisci errore 400 (Bad Request)
                var response = xhr.responseText;
                alert("Errore nella rimozione del professionista: " + response);  // Mostra il messaggio di errore
            } else {
                alert("Errore nel server: " + xhr.statusText);
            }
        }
    };

    console.log("Sending data: ", params);  // Log dei parametri che stai inviando

    xhr.send(params);
}
