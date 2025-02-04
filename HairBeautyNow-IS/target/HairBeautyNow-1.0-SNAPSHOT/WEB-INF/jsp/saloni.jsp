<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.application.model.entity.Sede" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<html>
<head>
  <title>Prenotazione</title>
  <script>
    // Funzione che invia il form per aggiornare la selezione della città
    function aggiornaSedi() {
      document.getElementById("selezioneCitta").submit();
    }
  </script>
</head>
<body>
<%@ include file="header.jsp" %>

<h1>Prenotazione</h1>

<%
  // Recupera i dati dalla servlet
  HttpSession sessione = request.getSession();
  String servizioPrenotato = (String) sessione.getAttribute("servizioPrenotato"); // Servizio prenotato
  String cittaUtente = (String) sessione.getAttribute("cittaUtente"); // Città selezionata
  List<String> cittaDisponibili = (List<String>) request.getAttribute("cittaDisponibili"); // Liste delle città disponibili
  List<Sede> saloni = (List<Sede>) request.getAttribute("saloni"); // Lista delle sedi

  if (cittaDisponibili == null) {
    cittaDisponibili = List.of("Roma", "Milano", "Napoli"); // Fallback in caso di null
  }
%>

<form id="selezioneCitta" action="prenota" method="post">
  <!-- Label per la selezione della città con l'attributo for che corrisponde all'id del select -->
  <label for="citta">Città selezionata: </label>
  <strong><%= cittaUtente != null ? cittaUtente : "Seleziona una città" %></strong>

  <select id="citta" name="citta" onchange="aggiornaSedi()">
    <% for (String citta : cittaDisponibili) { %>
    <option value="<%= citta %>" <%= citta.equals(cittaUtente) ? "selected" : "" %>><%= citta %></option>
    <% } %>
  </select>

  <!-- Label per la selezione del servizio con l'attributo for che corrisponde all'id del campo -->
  <label for="servizio">Servizio selezionato: </label>
  <strong><%= servizioPrenotato != null ? servizioPrenotato : "Seleziona un servizio" %></strong>

  <input type="hidden" id="servizio" name="servizio" value="<%= servizioPrenotato != null ? servizioPrenotato : "" %>"/>
</form>

<h2>Sedi disponibili</h2>
<% if (saloni != null && !saloni.isEmpty()) { %>
<ul>
  <% for (Sede salone : saloni) { %>
  <li><strong><%= salone.getNome() %></strong> - <%= salone.getIndirizzo() %> (<%= salone.getCittà() %>)</li>
  <% } %>
</ul>
<% } else { %>
<p>Nessuna sede disponibile per la città selezionata.</p>
<% } %>

<%@ include file="footer.jsp" %>
</body>
</html>
