function rimuoviProfessionista(professionistaId) {
    var xhr = new XMLHttpRequest();

    // Aggiungi il log per vedere cosa c'è dentro contextPath
    console.log("contextPath: ", contextPath);  // Debugging

    // Verifica l'URL completo
    var url = contextPath + "/removeProfessionista";
    console.log("Request URL: ", url);  // Debugging

    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    var params = "professionistaId=" + professionistaId;
    console.log("Request params: ", params);  // Debugging

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            if (xhr.status == 200) {
                var response = xhr.responseText;
                if (response === "success") {
                    // Ricaricare la pagina per riflettere la rimozione
                    location.reload();  // Ricarica la pagina
                } else {
                    alert("Errore nella rimozione del professionista: " + response);
                }
            } else {
                alert("Errore nel server: " + xhr.statusText);
            }
        }
    };

    xhr.send(params);
}
