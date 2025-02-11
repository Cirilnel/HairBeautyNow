<%@ page import="it.unisa.application.model.entity.UtenteGestoreSede" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="it">
<head>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ionicons@5.5.0/dist/ionicons/ionicons.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&family=Imperial+Script&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="static/style/assegnaGestore.css">
  <script src="static/js/UserMenu.js"></script>
  <meta charset="UTF-8">
  <title>Assegna Gestore</title>
</head>
<body>
<%@ include file="headerCatena.jsp" %>
<h1>Assegna un gestore a una sede</h1>

<%-- Mostra messaggio di errore, se esiste --%>
<% if (request.getParameter("errore") != null) { %>
<div class="error-message">
  <%= request.getParameter("errore") %>
</div>
<% } %>

<table>
  <tr>
    <th>Immagine</th>
    <th>Username</th>
    <th>Azione</th>
  </tr>
  <%
    List<UtenteGestoreSede> gestoriSenzaSede = (List<UtenteGestoreSede>) request.getAttribute("gestoriSenzaSede");
    if (gestoriSenzaSede != null && !gestoriSenzaSede.isEmpty()) {
      for (UtenteGestoreSede gestore : gestoriSenzaSede) {
  %>
  <tr>
    <td><img src="static/images/<%= gestore.getUsernameUGS() %>.png" alt="Immagine Utente" width="50" height="50"></td>
    <td><%= gestore.getUsernameUGS() %></td>
    <td>
      <form action="<%= request.getContextPath() %>/assegnaGestore" method="post">
        <input type="hidden" name="usernameUGS" value="<%= gestore.getUsernameUGS() %>">
        <button type="submit">Assumi</button>
      </form>
    </td>
  </tr>
  <%
    }
  } else {
  %>
  <tr>
    <td colspan="3" id="error-message">Nessun gestore senza sede trovato.</td>
  </tr>
  <% } %>
</table>

<%@ include file="footer.jsp" %>
</body>
</html>
