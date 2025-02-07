<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Crea Gestore</title>
  <link rel="stylesheet" href="static/style/style.css">
</head>
<body>
<%@ include file="headerCatena.jsp" %>
<h1 class="general">Crea Gestore</h1>

<% if (request.getParameter("errore") != null) { %>
<p class="error-message"><%= request.getParameter("errore") %></p>
<% } %>

<form action="<%= request.getContextPath() %>/creaGestore" method="post" class="form-container">
  <label for="usernameUGS">Username:</label>
  <input type="text" id="usernameUGS" name="usernameUGS" required>

  <label for="password">Password:</label>
  <input type="password" id="password" name="password" required>

  <button type="submit">Crea Gestore</button>
</form>


<%@ include file="footer.jsp" %>
</body>
</html>
