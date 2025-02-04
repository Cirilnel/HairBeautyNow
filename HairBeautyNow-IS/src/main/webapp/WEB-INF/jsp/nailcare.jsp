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
    <link rel="stylesheet" href="static/style/servizioSpecifico.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ionicons@5.5.0/dist/ionicons/ionicons.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&family=Imperial+Script&display=swap" rel="stylesheet">
</head>
<body>
<script src="static/js/UserMenu.js"></script>

<%@ include file="header.jsp" %>

<div class="main-content">
    <h1>Servizi</h1>
    <section class="hero">
        <img src="static/images/her-image2.png" alt="Parrucchiera con vista panoramica">
    </section>

    <section class="intro">
        <h2>Prezzi e Servizi</h2>
    </section>

    <div class="service-container">
        <%
            Map<String, List<Servizio>> serviziPerTipo = (Map<String, List<Servizio>>) request.getAttribute("serviziPerTipo");
            int index = 0; // Alternanza layout
            if (serviziPerTipo != null && !serviziPerTipo.isEmpty()) {
                for (Map.Entry<String, List<Servizio>> entry : serviziPerTipo.entrySet()) {
                    String tipo = entry.getKey();
                    List<Servizio> servizi = entry.getValue();
        %>
        <section class="service-type">
            <div class="services-list">
                <h2><%= tipo %></h2>
                <%
                    for (Servizio servizio : servizi) {
                        String nome = servizio.getNome();
                        double prezzo = servizio.getPrezzo();
                        String descrizione = servizio.getDescrizione(); // Assuming there is a description field
                        String immagineTipo = "static/images/" + nome.toLowerCase().replaceAll("\\s+", "") + ".png"; // Dynamically generate image path
                        String layoutClass = (index % 2 == 0) ? "left" : "right";
                        index++;
                %>
                <div class="service-item <%= layoutClass %>">
                    <div class="service-details">
                        <h3><%= nome %></h3>
                        <p><%= descrizione %></p>
                        <p><strong>Prezzo:</strong> &euro;<%= prezzo %></p>
                        <button class="book-button">Prenota ora</button>
                    </div>
                    <div class="service-image">
                        <img src="<%= immagineTipo %>" alt="<%= nome %>">
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
        <p>Non ci sono servizi di nail care disponibili al momento.</p>
        <%
            }
        %>
    </div>
</div>

<%@ include file="footer.jsp" %>

</body>
</html>
