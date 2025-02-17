<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.application.model.entity.Professionista" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="java.util.TreeSet" %>  <!-- Importa TreeSet per ordinare le date -->

<html>
<head>
    <title>Prenotazione</title>
    <link rel="stylesheet" href="static/style/professionisti.css">
    <script src="static/js/prenotazioni.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ionicons@5.5.0/dist/ionicons/ionicons.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <script src="static/js/UserMenu.js"></script>
    <link href="https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&family=Imperial+Script&display=swap" rel="stylesheet">
</head>

<body>

<%@ include file="header.jsp" %>

<h1>Prenotazione Professionista</h1>
<div class="prenotazione-container">
    <%
        List<Professionista> professionisti = (List<Professionista>) request.getAttribute("professionisti");
        Map<Integer, Map<LocalDate, List<String>>> fasceOrarieByProfessionista = (Map<Integer, Map<LocalDate, List<String>>>) request.getAttribute("fasceOrarieByProfessionista");
        String errorMessage = (String) request.getAttribute("errorMessage");
    %>

    <%-- Mostra messaggio di errore se presente --%>
    <% if (errorMessage != null) { %>
    <p class="error-message"><strong><%= errorMessage %></strong></p>
    <% } else if (professionisti == null || professionisti.isEmpty()) { %>
    <p class="error-message"><strong>Nessun professionista trovato in questa sede.</strong></p>
    <% } else { %>
    <p>Numero di professionisti disponibili: <%= professionisti.size() %></p>
    <ul>
        <% for (Professionista professionista : professionisti) {
            Map<LocalDate, List<String>> fasceOrarieForProfessionista = (fasceOrarieByProfessionista != null) ? fasceOrarieByProfessionista.get(professionista.getId()) : null;
            boolean hasAvailableDates = fasceOrarieForProfessionista != null && !fasceOrarieForProfessionista.isEmpty();
        %>
        <li>
            <strong><%= professionista.getNome() %></strong>

            <img src="static/images/<%= professionista.getId() %>.png" alt="Foto di <%= professionista.getNome() %>" width="150" />

            <% if (!hasAvailableDates) { %>
            <p class="error-message">Nessun orario disponibile per questo professionista.</p>
            <% } else { %>
            <form action="prenotazione" method="post">
                <input type="hidden" name="professionistaId" value="<%= professionista.getId() %>" />

                <!-- Selezione giorno -->
                <label for="data<%= professionista.getId() %>">Seleziona il giorno:</label>
                <select name="data" id="data<%= professionista.getId() %>" onchange="aggiornaOrari(<%= professionista.getId() %>)">
                    <%
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate selectedDay = (LocalDate) request.getAttribute("selectedDay"); // Giorno selezionato

                        // Ordinare le date in ordine crescente
                        TreeSet<LocalDate> sortedDates = new TreeSet<>(fasceOrarieForProfessionista.keySet());  // Usa TreeSet per ordinare

                        for (LocalDate giorno : sortedDates) {
                            String giornoFormattato = giorno.format(formatter);
                    %>
                    <option value="<%= giornoFormattato %>" <% if (giorno.equals(selectedDay)) { %>selected<% } %>><%= giornoFormattato %></option>
                    <% } %>
                </select>

                <br><br>

                <!-- Selezione orario -->
                <label for="orario<%= professionista.getId() %>">Seleziona l'orario:</label>
                <select name="orario" id="orario<%= professionista.getId() %>">
                    <%
                        if (selectedDay != null && fasceOrarieForProfessionista.containsKey(selectedDay)) {
                            List<String> availableTimes = fasceOrarieForProfessionista.get(selectedDay);
                            for (String time : availableTimes) {
                    %>
                    <option value="<%= time %>" <% if (time.equals(request.getAttribute("selectedTime"))) { %>selected<% } %>><%= time %></option>
                    <%
                            }
                        }
                    %>
                </select>

                <br><br>

                <input type="submit" value="Prenota">
            </form>
            <% } %>
        </li>
        <% } %>
    </ul>
    <% } %>

    <script>
        var fasceOrarieByProfessionista = {};

        <%
            if (fasceOrarieByProfessionista != null) {
                for (Map.Entry<Integer, Map<LocalDate, List<String>>> entry : fasceOrarieByProfessionista.entrySet()) {
                    Integer professionistaId = entry.getKey();
                    Map<LocalDate, List<String>> fasceOrarie = entry.getValue();
                    String fasceStr = fasceOrarie.entrySet().stream()
                            .map(e -> "\"" + e.getKey().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "\":["
                                    + e.getValue().stream().map(f -> "\"" + f + "\"").collect(Collectors.joining(",")) + "]")
                            .collect(Collectors.joining(","));
        %>
        fasceOrarieByProfessionista["<%= professionistaId %>"] = {<%= fasceStr %>};
        <%
                }
            }
        %>

        function aggiornaOrari(professionistaId) {
            var selezioneGiorno = document.getElementById("data" + professionistaId);
            var selezioneOrario = document.getElementById("orario" + professionistaId);

            selezioneOrario.innerHTML = "<option value=''>Caricamento...</option>";

            var selectedDate = selezioneGiorno.value;
            if (fasceOrarieByProfessionista[professionistaId] && fasceOrarieByProfessionista[professionistaId][selectedDate]) {
                selezioneOrario.innerHTML = ""; // Svuota il selettore
                var availableTimes = fasceOrarieByProfessionista[professionistaId][selectedDate];
                availableTimes.forEach(function(fascia) {
                    var option = document.createElement("option");
                    option.value = fascia;
                    option.text = fascia;
                    selezioneOrario.appendChild(option);
                });

                // Seleziona automaticamente la prima fascia oraria disponibile
                if (availableTimes.length > 0) {
                    selezioneOrario.value = availableTimes[0];
                }
            } else {
                selezioneOrario.innerHTML = "<option value=''>Nessun orario disponibile</option>";
            }
        }

        // Aggiorna l'orario automaticamente quando la pagina viene caricata
        window.onload = function() {
            <% for (Professionista professionista : professionisti) { %>
            aggiornaOrari(<%= professionista.getId() %>);
            <% } %>
        }
    </script>
</div>

<%@ include file="footer.jsp" %>
</body>
</html>
