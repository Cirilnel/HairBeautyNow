<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HairBeauty Now</title>
    <link rel="stylesheet" href="static/style/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ionicons@5.5.0/dist/ionicons/ionicons.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&family=Imperial+Script&display=swap" rel="stylesheet">
</head>
<body>
<script src="static/js/UserMenu.js"></script>
<%@ include file="headerCatena.jsp" %>
<h1 class="general">HairBeauty Now</h1>
<section class="hero general">
    <img src="static/images/hero-image.png" alt="Parrucchiera con vista panoramica">
</section>

<section class="intro general">
    <h1>Gestione Sede</h1>
</section>
<section class="services general">
    <div class="service-card general">
        <a href="<%= request.getContextPath() %>/aggiungiSede">
            <img src="static/images/new.png" alt="Make Up">
            <div class="service-item">
                <p>Aggiungi Sede</p>
            </div>
        </a>
    </div>

    <div class="service-card general">
        <a href="<%= request.getContextPath() %>/creaGestore">
            <img src="static/images/add.png" alt="Hair Styling">
            <div class="service-item">
                <p>Crea Gestore</p>
            </div>
        </a>
    </div>

    <div class="service-card general">
        <a href="<%= request.getContextPath() %>/rimuoviGestore">
            <img src="static/images/remove.png" alt="Nail Care">
            <div class="service-item">
                <p>Rimuovi Gestore </p>
            </div>
        </a>
    </div>
</section>

<%@ include file="footer.jsp" %>

<script>
    window.onload = function() {
        const gestoreCreato = "<%= session.getAttribute("gestoreCreato") %>";
        const gestoreRimosso = "<%= session.getAttribute("gestoreRimosso") %>";

        if (gestoreCreato === "ok") {
            alert("Gestore creato con successo!");

            // Rimuovi l'attributo dalla sessione sul server tramite una richiesta GET
            const xhr = new XMLHttpRequest();
            xhr.open("GET", "<%= request.getContextPath() %>/creaGestore?removeGestoreCreato=true", true);
            xhr.send();
        }

        if (gestoreRimosso === "ok") {
            alert("Gestore rimosso con successo!");

            // Rimuoviamo l'attributo dalla sessione sul server tramite una richiesta GET
            const xhr = new XMLHttpRequest();
            xhr.open("GET", "<%= request.getContextPath() %>/rimuoviGestore?removeGestoreRimosso=true", true);
            xhr.send();
        }

        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.has('successo')) {
            alert("Sede creata con successo e gestore assegnato!");
        }
    };

</script>



</body>
</html>
