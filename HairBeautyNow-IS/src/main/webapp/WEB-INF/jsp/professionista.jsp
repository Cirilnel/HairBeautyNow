<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.application.model.entity.Professionista" %>
<%@ page import="it.unisa.application.model.entity.FasciaOraria" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="java.time.format.DateTimeFormatter" %> <!-- Importa il formatter -->

<html>
<head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ionicons@5.5.0/dist/ionicons/ionicons.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&family=Imperial+Script&display=swap" rel="stylesheet">
    <title>Prenotazione</title>
    <link rel="stylesheet" href="static/style/professionisti.css">
</head>
<body>
<%@ include file="header.jsp" %>
<script src="static/js/prenotazioni.js"></script>

<h1>Prenotazione Professionista</h1>
<div class="prenotazione-container">
<%
    List<Professionista> professionisti = (List<Professionista>) request.getAttribute("professionisti");
    if (professionisti == null || professionisti.isEmpty()) {
%>
<p>Nessun professionista trovato.</p>
<%
} else {
%>
<p>Numero di professionisti: <%= professionisti.size() %></p>
<% } %>

<% if (professionisti == null || professionisti.isEmpty()) { %>
<p>Nessun professionista disponibile.</p>
<% } else { %>
<ul>
    <% for (Professionista professionista : professionisti) { %>
    <li>
        <strong><%= professionista.getNome() %></strong>

        <!-- Immagine del professionista -->
        <img src="static/images/<%= professionista.getId() %>.png" alt="Foto di <%= professionista.getNome() %>" width="150" />

        <!-- Form per la selezione -->
        <form action="prenotazione" method="post">
            <input type="hidden" name="professionistaId" value="<%= professionista.getId() %>" />

            <!-- Selezione giorno -->
            <label for="data">Seleziona il giorno:</label>

            <select name="data" id="data" onchange="aggiornaOrari()">
                <%
                    Map<LocalDate, List<String>> fasceOrarieByDay = (Map<LocalDate, List<String>>) request.getAttribute("fasceOrarieByDay");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Crea il formatter
                    for (LocalDate giorno : fasceOrarieByDay.keySet()) {
                        String giornoFormattato = giorno.format(formatter); // Formatta la data
                %>
                <option value="<%= giornoFormattato %>"><%= giornoFormattato %></option>
                <% } %>
            </select>

            <br><br>

            <!-- Selezione orario -->
            <label for="orario">Seleziona l'orario:</label>
            <select name="orario" id="orario">
                <!-- Gli orari saranno aggiornati dinamicamente in base al giorno -->
            </select>

            <br><br>

            <input type="submit" value="Prenota">
        </form>
    </li>
    <% } %>
</ul>
<% } %>

<!-- Creiamo la variabile JavaScript fasceOrarieByDay con i dati dal server -->
<script>
    var fasceOrarieByDay = {};

    <%
        Map<LocalDate, List<String>> fasceOrarieByDay = (Map<LocalDate, List<String>>) request.getAttribute("fasceOrarieByDay");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Ciclo sui giorni per costruire la struttura JavaScript
        for (Map.Entry<LocalDate, List<String>> entry : fasceOrarieByDay.entrySet()) {
            String giorno = entry.getKey().format(formatter);  // Formattiamo il giorno
            List<String> fasce = entry.getValue();
            String fasceStr = fasce.stream().map(f -> "\"" + f + "\"").collect(Collectors.joining(","));
    %>
    fasceOrarieByDay["<%= giorno %>"] = [<%= fasceStr %>];
    <% } %>
</script>

</div>

<%@ include file="footer.jsp" %>
</body>
</html>
