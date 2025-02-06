<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.application.model.entity.Sede" %>
<%@ page import="it.unisa.application.model.entity.UtenteAcquirente" %> <!-- Importa la classe UtenteAcquirente -->
<%@ page import="jakarta.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ionicons@5.5.0/dist/ionicons/ionicons.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&family=Imperial+Script&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="static/style/saloni.css">
  <title>Prenotazione</title>
  <script>
    // Funzione che invia il form per aggiornare la selezione della città
    function aggiornaSedi() {
      document.getElementById("form-citta").submit();
    }
  </script>
</head>
<body>
<%@ include file="header.jsp" %>

<h1 class="prenotazione-titolo">Prenotazione</h1>

<%
  HttpSession sessione = request.getSession();
  String servizioPrenotato = (String) sessione.getAttribute("servizioPrenotato");

  // Creiamo una variabile UtenteAcquirente per ottenere la città
  UtenteAcquirente utenteAcquirente = (UtenteAcquirente) sessione.getAttribute("user");
  String cittaUtente = (utenteAcquirente != null) ? utenteAcquirente.getCitta() : "Seleziona una città";

  List<String> cittaDisponibili = (List<String>) request.getAttribute("cittaDisponibili");
  List<Sede> saloni = (List<Sede>) request.getAttribute("saloni");
  String messaggio = (String) request.getAttribute("messaggio");
  String cittaSelezionata = (String) request.getAttribute("cittaSelezionata");

  if (cittaDisponibili == null) {
    cittaDisponibili = List.of("Roma", "Milano", "Napoli");
  }
%>

<form id="form-citta" action="prenota" method="post" class="form-container">
  <label for="select-citta">Città selezionata: </label>
  <strong><%= cittaSelezionata != null ? cittaSelezionata : cittaUtente %></strong>

  <select id="select-citta" name="citta" onchange="aggiornaSedi()" class="select-citta">
    <% for (String citta : cittaDisponibili) { %>
    <option value="<%= citta %>" <%= citta.equals(cittaSelezionata) ? "selected" : "" %>><%= citta %></option>
    <% } %>
  </select>

  <label for="input-servizio">Servizio selezionato: </label>
  <strong><%= servizioPrenotato != null ? servizioPrenotato : "Seleziona un servizio" %></strong>

  <input type="hidden" id="input-servizio" name="servizio" value="<%= servizioPrenotato != null ? servizioPrenotato : "" %>"/>
</form>

<h2 class="sezioni-titolo">Sedi disponibili</h2>

<% if (messaggio != null) { %>
<p class="error"><%= messaggio %></p>
<% } %>

<% if (saloni != null && !saloni.isEmpty()) { %>
<div class="salon-list">
  <% for (Sede salone : saloni) { %>
  <div class="salon-card">
    <img src="static/images/<%= salone.getIndirizzo().toLowerCase().replaceAll("\\s+", "") %>.png"
         alt="Foto salone di <%= salone.getIndirizzo() %>" />
    <div class="salon-card-content">
      <h3><%= salone.getNome() %></h3>
      <p><strong>Indirizzo:</strong> <%= salone.getIndirizzo() %> (<%= salone.getCitta() %>)</p>
      <div class="salon-details">
        <ul>
          <li><strong>Orario di apertura:</strong> 8:00</li>
          <li><strong>Orario di chiusura:</strong> 18:00</li>
          <li><strong>Pausa:</strong> 12:00 - 14:00</li>
        </ul>
      </div>
      <form action="saloneSelezionato" method="post">
        <input type="hidden" name="saloneId" value="<%= salone.getId() %>"/>
        <button type="submit">Seleziona Salone</button>
      </form>
    </div>
  </div>
  <% } %>
</div>
<% } else { %>
<p class="error" style="font-size: 16px; color: #FF0000 !important; background-color: #FFEDED !important; padding: 10px; border: 1px solid #FF0000; border-radius: 5px; max-width: 600px; margin: 20px auto;">
  Nessuna sede disponibile per la città selezionata.
</p>

<% } %>

<%@ include file="footer.jsp" %>
</body>
</html>
