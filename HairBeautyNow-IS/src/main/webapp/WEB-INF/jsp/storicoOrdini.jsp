<%@ page import="java.util.List, it.unisa.application.model.entity.Prenotazione, it.unisa.application.model.dao.ProfessionistaDAO" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<html>
<head>
  <title>Storico Ordini</title>
</head>
<body>
<%@ include file="header.jsp" %>
<h2>Storico Prenotazioni</h2>
<table border="1">
  <tr>
    <th>Servizio</th>
    <th>Professionista</th>
    <th>Indirizzo</th>
    <th>Prezzo</th>
    <th>Data</th>
    <th>Fascia Oraria</th>  <!-- Aggiunta la colonna per la fascia oraria -->
  </tr>
  <%
    List<Prenotazione> prenotazioni = (List<Prenotazione>) request.getAttribute("prenotazioni");
    ProfessionistaDAO professionistaDAO = new ProfessionistaDAO();

    if (prenotazioni != null) {
      for (Prenotazione p : prenotazioni) {
        // Ottieni il nome del professionista
        String nomeProfessionista = professionistaDAO.getProfessionistaById(p.getProfessionistaId());

        // Recupera il professionista per ottenere il sedeId
        int sedeId = professionistaDAO.getSedeIdByProfessionistaId(p.getProfessionistaId());

        // Ottieni l'indirizzo tramite il sedeId
        String indirizzo = professionistaDAO.getIndirizzoBySedeId(sedeId);

        // Ottieni la fascia oraria
        LocalTime fasciaOraria = p.getData().toLocalTime(); // Ottieni solo l'orario
        LocalTime fineFasciaOraria = fasciaOraria.plusMinutes(30); // Aggiungi 30 minuti per l'orario di fine

        // Formatta la fascia oraria come "HH:mm-HH:mm"
        String fasciaFormattata = fasciaOraria.format(DateTimeFormatter.ofPattern("HH:mm")) + "-" + fineFasciaOraria.format(DateTimeFormatter.ofPattern("HH:mm"));

        // Ottieni la data in formato "dd-MM-yyyy"
        String dataFormattata = p.getData().toLocalDate().toString();  // Esempio: 2025-02-04
  %>
  <tr>
    <td><%= p.getServizioName() %></td>
    <td><%= nomeProfessionista != null ? nomeProfessionista : "N/A" %></td>
    <td><%= indirizzo != null ? indirizzo : "N/A" %></td>
    <td><%= p.getPrezzo() %></td>
    <td><%= dataFormattata %></td> <!-- Mostra la data -->
    <td><%= fasciaFormattata %></td> <!-- Mostra la fascia oraria formattata -->
  </tr>
  <%
    }
  } else {
  %>
  <tr>
    <td colspan="6">Nessuna prenotazione trovata.</td>
  </tr>
  <%
    }
  %>
</table>
<%@ include file="footer.jsp" %>
</body>
</html>
