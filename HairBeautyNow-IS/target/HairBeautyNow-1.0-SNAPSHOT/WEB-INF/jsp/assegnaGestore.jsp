<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List, it.unisa.application.model.entity.UtenteGestoreSede" %>
<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <title>Assegna Gestore</title>
</head>
<body>
<h2>Assegna un gestore a una sede</h2>

<table border="1">
  <tr>
    <th>Immagine</th>
    <th>Username</th>
    <th>Password</th>
    <th>Azione</th>
  </tr>
  <%
    List<UtenteGestoreSede> gestoriSenzaSede = (List<UtenteGestoreSede>) request.getAttribute("gestoriSenzaSede");
    Integer sedeIDObj = (Integer) session.getAttribute("sedeID");
    int sedeID = (sedeIDObj != null) ? sedeIDObj : 0; // Usa 0 come valore di default
    if (gestoriSenzaSede != null && !gestoriSenzaSede.isEmpty()) {
      for (UtenteGestoreSede gestore : gestoriSenzaSede) {
        String passwordMasked = "*".repeat(gestore.getPassword().length()); // Maschera la password
  %>
  <tr>
    <td><img src="static/images/<%= gestore.getUsernameUGS() %>.png" alt="Immagine Utente" width="50" height="50"></td>
    <td><%= gestore.getUsernameUGS() %></td>
    <td><%= passwordMasked %></td>
    <td>
      <form action="assegnaGestore" method="post">
        <input type="hidden" name="usernameUGS" value="<%= gestore.getUsernameUGS() %>">
        <input type="hidden" name="sedeID" value="<%= sedeID %>">
        <button type="submit">Assumi Gestore</button>
      </form>
    </td>
  </tr>
  <%
    }
  } else {
  %>
  <tr>
    <td colspan="4">Nessun gestore senza sede trovato.</td>
  </tr>
  <% } %>
</table>
</body>
</html>
