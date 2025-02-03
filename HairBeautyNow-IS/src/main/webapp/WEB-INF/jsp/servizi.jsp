<%@ page import="it.unisa.application.model.entity.Servizio" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Imperial+Script&display=swap" rel="stylesheet">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HairBeauty Now</title>
    <link rel="stylesheet" href="static/style/servizi.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ionicons@5.5.0/dist/ionicons/ionicons.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&family=Imperial+Script&display=swap" rel="stylesheet">
</head>
<body>

<%@ include file="header.jsp" %>
<h1>Servizi</h1>
<section class="hero">
    <img src="static/images/her-image2.png" alt="Parrucchiera con vista panoramica">
</section>

<section class="intro">
    <h2>Prezzi e Servizi</h2>
</section>

<!-- Verifica se ci sono servizi per tipo -->
<%
    Map<String, List<Servizio>> serviziPerTipo = (Map<String, List<Servizio>>) request.getAttribute("serviziPerTipo");
    int index = 0; // Aggiungiamo un contatore per alternare la posizione dell'immagine
    if (serviziPerTipo != null && !serviziPerTipo.isEmpty()) {
        // Itera sui servizi raggruppati per tipo
        for (Map.Entry<String, List<Servizio>> entry : serviziPerTipo.entrySet()) {
            String tipo = entry.getKey();
            List<Servizio> servizi = entry.getValue();

            // Alterna la posizione dell'immagine
            String immagineTipo = "static/images/" + tipo.toLowerCase() + "-image.png"; // Definisci un'immagine per ogni tipo
            String layoutClass = (index % 2 == 0) ? "left" : "right"; // Alterna tra "left" e "right" in base all'indice
            index++;
%>
<section class="service-type <%= layoutClass %>">
    <div class="service-image">
        <img src="<%= immagineTipo %>" alt="<%= tipo %>">
    </div>
    <div class="services-list">
        <h2><%= tipo %></h2> <!-- Tipo di servizio, es. "Make Up", "Hair Styling", ecc. -->
        <!-- Itera sui singoli servizi di quel tipo -->
        <%
            for (Servizio servizio : servizi) {
                String nome = servizio.getNome();
                double prezzo = servizio.getPrezzo();
        %>
        <div class="service-item">
            <div class="service-text">
                <h3><%= nome %></h3> <!-- Nome del servizio -->
                <p>Prezzo: &euro;<%= prezzo %></p>
                <!-- Prezzo del servizio -->
            </div>
        </div>
        <%
            }
        %>
    </div>
</section>
<%
    }
} else {
%>
<p>Non ci sono servizi disponibili al momento.</p>
<%
    }
%>

<%@ include file="footer.jsp" %>

</body>
</html>
