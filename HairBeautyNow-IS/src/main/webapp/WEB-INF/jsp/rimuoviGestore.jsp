<%@ page import="java.util.List, it.unisa.application.model.entity.UtenteGestoreSede" %>
<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <title>Rimuovi Gestore</title>
  <link rel="stylesheet" href="static/style/style.css">
</head>
<body>
<%@ include file="headerCatena.jsp" %>
<h1>Rimuovi Gestore</h1>

<% if (request.getParameter("errore") != null) { %>
<p class="error-message"><%= request.getParameter("errore") %></p>
<% } %>

<table>
  <tr>
    <th>Immagine</th>
    <th>Username</th>
    <th>Password</th>
    <th>Azione</th>
  </tr>
  <%
    List<UtenteGestoreSede> gestoriConSede = (List<UtenteGestoreSede>) request.getAttribute("gestoriConSede");
    if (gestoriConSede != null && !gestoriConSede.isEmpty()) {
      for (UtenteGestoreSede gestore : gestoriConSede) {
        String passwordMasked = "*".repeat(gestore.getPassword().length()); // Maschera la password
  %>
  <tr>
    <td><img src="static/images/<%= gestore.getUsernameUGS() %>.png" alt="Immagine Utente" width="50" height="50"></td>
    <td><%= gestore.getUsernameUGS() %></td>
    <td><%= passwordMasked %></td>
    <td>
      <form action="<%= request.getContextPath() %>/rimuoviGestore" method="post">
        <input type="hidden" name="usernameUGS" value="<%= gestore.getUsernameUGS() %>">
        <button type="submit">Rimuovi</button>
      </form>
    </td>
  </tr>
  <%
    }
  } else {
  %>
  <tr>
    <td colspan="4">Nessun gestore con sede trovato.</td>
  </tr>
  <% } %>
</table>

<%@ include file="footer.jsp" %>
</body>
</html>
