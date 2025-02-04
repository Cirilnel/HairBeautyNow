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
    <title>Prenotazione</title>
</head>
<body>
<%@ include file="header.jsp" %>
<script src="static/js/prenotazioni.js"></script>

<h1>Prenotazione Professionista</h1>

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

<%@ include file="footer.jsp" %>
</body>
</html>
