        <%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.application.model.entity.Professionista" %>
<%@ page import="it.unisa.application.model.entity.FasciaOraria" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

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
    <p>Numero di professionisti: <%=professionisti.size()%></p>
    <% } %>

    <% Map<Integer, Map<LocalDate, List<String>>> fasceOrarieByProfessionista = null;
        if (professionisti == null || professionisti.isEmpty()) { %>
    <p>Nessun professionista disponibile.</p>
    <% } else { %>
    <ul>
        <%
            // Recupera le fasce orarie specifiche per ogni professionista
            fasceOrarieByProfessionista = (Map<Integer, Map<LocalDate, List<String>>>) request.getAttribute("fasceOrarieByProfessionista");
        %>
        <% for (Professionista professionista : professionisti) { %>
        <li>
            <strong><%=professionista.getNome()%></strong>

            <!-- Stampa l'ID del professionista per debug -->
            <p>ID Professionista: <%=professionista.getId()%></p>

            <!-- Immagine del professionista -->
            <img src="static/images/<%=professionista.getId()%>.png" alt="Foto di <%=professionista.getNome()%>" width="150" />

            <!-- Form per la selezione -->
            <form action="prenotazione" method="post">
                <input type="hidden" name="professionistaId" value="<%=professionista.getId()%>" />

                <!-- Selezione giorno -->
                <label for="data<%=professionista.getId()%>">Seleziona il giorno:</label>

                <select name="data" id="data<%=professionista.getId()%>" onchange="aggiornaOrari(
                    <%=professionista.getId()%>)">
                    <%
                        Map<LocalDate, List<String>> fasceOrarieForProfessionista = fasceOrarieByProfessionista.get(professionista.getId());
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        for (LocalDate giorno : fasceOrarieForProfessionista.keySet()) {
                            String giornoFormattato = giorno.format(formatter);
                    %>
                    <option value="<%=giornoFormattato%>"><%=giornoFormattato%></option>
                    <% } %>
                </select>

                <br><br>

                <!-- Selezione orario -->
                <label for="orario<%=professionista.getId()%>">Seleziona l'orario:</label>
                <select name="orario" id="orario<%=professionista.getId()%>">
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
        var fasceOrarieByProfessionista = {};

        <%
                // Passa i dati delle fasce orarie per ogni professionista
                for (Map.Entry<Integer, Map<LocalDate, List<String>>> entry : fasceOrarieByProfessionista.entrySet()) {
                    Integer professionistaId = entry.getKey();
                    Map<LocalDate, List<String>> fasceOrarie = entry.getValue();
                    String fasceStr = fasceOrarie.entrySet().stream()
                            .map(e -> "\"" + e.getKey().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "\":[" + e.getValue().stream().map(f -> "\"" + f + "\"").collect(Collectors.joining(",")) + "]")
                            .collect(Collectors.joining(","));
                    %>
        fasceOrarieByProfessionista["<%=professionistaId%>"] = {<%=fasceStr%>};
        <% } %>

        // Funzione per aggiornare gli orari per ogni professionista
        function aggiornaOrari(professionistaId) {
            var selezioneGiorno = document.getElementById("data" + professionistaId);
            var selezioneOrario = document.getElementById("orario" + professionistaId);

            // Pulisce le opzioni esistenti
            selezioneOrario.innerHTML = "<option value=''>Caricamento...</option>";

            var selectedDate = selezioneGiorno.value;
            if (fasceOrarieByProfessionista[professionistaId] && fasceOrarieByProfessionista[professionistaId][selectedDate]) {
                // Aggiungi le fasce orarie disponibili per il giorno selezionato
                fasceOrarieByProfessionista[professionistaId][selectedDate].forEach(function(fascia) {
                    var option = document.createElement("option");
                    option.value = fascia;
                    option.text = fascia;
                    selezioneOrario.appendChild(option);
                });
            } else {
                var option = document.createElement("option");
                option.text = "Nessun orario disponibile";
                selezioneOrario.appendChild(option);
            }
        }
    </script>

</div>

<%@ include file="footer.jsp" %>
</body>
</html>
