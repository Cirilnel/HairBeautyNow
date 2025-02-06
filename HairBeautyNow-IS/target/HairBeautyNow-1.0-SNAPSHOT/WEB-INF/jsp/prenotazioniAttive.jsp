<%@ page import="java.util.List" %>
<%@ page import="it.unisa.application.model.entity.Prenotazione" %>
<%@ page import="it.unisa.application.model.dao.ProfessionistaDAO" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Prenotazioni Attive</title>
  <link rel="stylesheet" href="static/style.css">
</head>
<body>

<h1>Prenotazioni Attive</h1>

<%
  List<Prenotazione> prenotazioniAttive = (List<Prenotazione>) request.getAttribute("prenotazioniAttive");
  ProfessionistaDAO professionistaDAO = new ProfessionistaDAO();

  if (prenotazioniAttive == null || prenotazioniAttive.isEmpty()) {
%>
<p>Non ci sono prenotazioni attive al momento.</p>
<% } else { %>
<table>
  <thead>
  <tr>
    <th>Servizio</th>
    <th>Professionista</th>
    <th>Indirizzo</th>
    <th>Prezzo</th>
    <th>Data</th>
    <th>Fascia Oraria</th>
  </tr>
  </thead>
  <tbody>
  <%
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    for (Prenotazione prenotazione : prenotazioniAttive) {
      // Ottieni il nome del professionista
      String nomeProfessionista = professionistaDAO.getProfessionistaById(prenotazione.getProfessionistaId());

      // Ottieni l'indirizzo del professionista
      int sedeId = professionistaDAO.getSedeIdByProfessionistaId(prenotazione.getProfessionistaId());
      String indirizzo = professionistaDAO.getIndirizzoBySedeId(sedeId);

      // Ottieni la fascia oraria
      LocalTime fasciaOraria = prenotazione.getData().toLocalTime();
      LocalTime fineFasciaOraria = fasciaOraria.plusMinutes(30);
      String fasciaFormattata = fasciaOraria.format(timeFormatter) + "-" + fineFasciaOraria.format(timeFormatter);

      // Ottieni la data in formato leggibile
      String dataFormattata = prenotazione.getData().toLocalDate().format(dateFormatter);
  %>
  <tr>
    <td><%= prenotazione.getServizioName() %></td>
    <td><%= nomeProfessionista != null ? nomeProfessionista : "N/A" %></td>
    <td><%= indirizzo != null ? indirizzo : "N/A" %></td>
    <td><%= prenotazione.getPrezzo() %>€</td>
    <td><%= dataFormattata %></td>
    <td><%= fasciaFormattata %></td>
  </tr>
  <% } %>
  </tbody>
</table>
<% } %>

</body>
</html>
