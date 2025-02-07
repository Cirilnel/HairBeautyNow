<%@ page import="java.util.List, it.unisa.application.model.entity.UtenteGestoreSede" %>
<!DOCTYPE html>
<html lang="it">
<head>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ionicons@5.5.0/dist/ionicons/ionicons.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&family=Imperial+Script&display=swap" rel="stylesheet">
  <title>Rimozione Professionisti</title>
  <link rel="stylesheet" href="static/style/rimuoviGestore.css">
  <meta charset="UTF-8">
  <title>Rimuovi Gestore</title>
  <script src="static/js/UserMenu.js"></script>
</head>
<body>
<%@ include file="headerCatena.jsp" %>

<a href="javascript:history.back()" class="back-arrow">
  <i class="fa fa-arrow-left"></i>
</a>
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
    <td colspan="4" id="error-message">Nessun gestore con sede trovato.</td>
  </tr>
  <% } %>
</table>

<%@ include file="footer.jsp" %>
</body>
</html>
