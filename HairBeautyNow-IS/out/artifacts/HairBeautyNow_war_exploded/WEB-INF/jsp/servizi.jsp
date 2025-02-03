<%@ page import="it.unisa.application.model.entity.Servizio" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HairBeauty Now - Servizi</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Imperial+Script&family=Abhaya+Libre:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="static/style/servizi.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

<%@ include file="header.jsp" %>

<h1>Servizi</h1>

<section class="hero">
    <img src="static/images/her-image2.png" alt="Salone di bellezza">
</section>

<section class="intro">
    <h2>Prezzi e Servizi</h2>
</section>

<%
    Map<String, List<Servizio>> serviziPerTipo = (Map<String, List<Servizio>>) request.getAttribute("serviziPerTipo");
    int index = 0;

    if (serviziPerTipo != null && !serviziPerTipo.isEmpty()) {
        for (Map.Entry<String, List<Servizio>> entry : serviziPerTipo.entrySet()) {
            String tipo = entry.getKey();
            List<Servizio> servizi = entry.getValue();
            String immagineTipo = "static/images/" + tipo.toLowerCase().replace(" ", "-") + "-image.png";
            String layoutClass = (index % 2 == 0) ? "left" : "right";
            index++;
%>
<section class="service-type <%= layoutClass %>">
    <div class="service-image">
        <img src="<%= immagineTipo %>" alt="<%= tipo %>">
    </div>
    <div class="services-list">
        <h2><%= tipo %></h2>
        <%
            for (Servizio servizio : servizi) {
        %>
        <div class="service-item">
            <h3><%= servizio.getNome() %></h3>
            <p>Prezzo: &euro;<%= servizio.getPrezzo() %></p>
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
<p class="no-services">Non ci sono servizi disponibili al momento.</p>
<%
    }
%>

<%@ include file="footer.jsp" %>

</body>
</html>
