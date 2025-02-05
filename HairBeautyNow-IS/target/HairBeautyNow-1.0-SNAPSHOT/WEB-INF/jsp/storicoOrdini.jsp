<%@ page import="java.util.List, it.unisa.application.model.entity.Prenotazione, it.unisa.application.model.dao.ProfessionistaDAO" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<html>
<head>
  <link rel="stylesheet" href="static/style/storicoOrdini.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ionicons@5.5.0/dist/ionicons/ionicons.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ionicons@5.5.0/dist/ionicons/ionicons.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  <link href="https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&family=Imperial+Script&display=swap" rel="stylesheet">
  <title>Storico Ordini</title>
  <script src="static/js/UserMenu.js"></script>
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
