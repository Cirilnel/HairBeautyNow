<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ionicons@5.5.0/dist/ionicons/ionicons.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&family=Imperial+Script&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="static/style/creaGestore.css">

  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Crea Gestore</title>
  <script src="static/js/UserMenu.js"></script>

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
