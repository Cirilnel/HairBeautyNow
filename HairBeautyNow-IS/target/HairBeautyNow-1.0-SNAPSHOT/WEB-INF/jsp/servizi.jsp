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

<div class="main-content">
    <h1>Servizi Make-Up</h1>
    <section class="hero">
        <img src="static/images/her-image2.png" alt="Parrucchiera con vista panoramica">
    </section>

    <section class="intro">
        <h2>Prezzi e Servizi di Make-Up</h2>
    </section>

    <div class="service-container">
        <%
            Map<String, List<Servizio>> serviziPerTipo = (Map<String, List<Servizio>>) request.getAttribute("serviziPerTipo");
            int index = 0; // Alternanza layout
            if (serviziPerTipo != null && !serviziPerTipo.isEmpty()) {
                for (Map.Entry<String, List<Servizio>> entry : serviziPerTipo.entrySet()) {
                    String tipo = entry.getKey();
                    // Procediamo solo con i servizi di make-up
                    if ("Make Up".equalsIgnoreCase(tipo)) {
                        List<Servizio> servizi = entry.getValue();
                        for (Servizio servizio : servizi) {
                            String nome = servizio.getNome();
                            String descrizione = servizio.getDescrizione(); // Assumiamo che il servizio abbia una descrizione
                            double prezzo = servizio.getPrezzo();
                            String immagineServizio = "static/images/" + nome.toLowerCase().replace(" ", "_") + "-image.png"; // Crea un nome immagine
        %>
        <section class="service-item">
            <div class="service-image">
                <img src="<%= immagineServizio %>" alt="<%= nome %>">
            </div>
            <div class="service-details">
                <h2><%= nome %></h2>
                <p><%= descrizione %></p>
                <div class="service-price">
                    <p>Prezzo: &euro;<%= prezzo %></p>
                    <button class="book-button">Prenota Servizio</button>
                </div>
            </div>
        </section>
        <%
                    }
                }
            }
        } else {
        %>
        <p>Non ci sono servizi di make-up disponibili al momento.</p>
        <%
            }
        %>
    </div>
</div>

<%@ include file="footer.jsp" %>

</body>
</html>
