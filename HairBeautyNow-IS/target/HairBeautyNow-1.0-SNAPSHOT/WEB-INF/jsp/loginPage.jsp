<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="static/style/login.css">
  <meta charset="UTF-8">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Imperial+Script&display=swap" rel="stylesheet">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>HairBeauty Now</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ionicons@5.5.0/dist/ionicons/ionicons.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&family=Imperial+Script&display=swap" rel="stylesheet">
</head>
<body class="login">
<%@ include file="header.jsp" %>
<script src="static/js/UserMenu.js"></script>

<h1>Autenticazione</h1>

<section class="auth-container">
  <a href="javascript:history.back()" class="back-arrow">
    <i class="fa fa-arrow-left"></i>
  </a>

  <section class="auth-container">
    <form action="<%= request.getContextPath() %>/loginPage" method="post" class="auth-form">
      <label for="username">Username</label> <!-- Cambiato da 'Email' a 'Username' -->
      <input type="text" id="username" name="username" placeholder="Value" required> <!-- Cambiato da 'email' a 'username' -->

      <label for="password">Password</label>
      <input type="password" id="password" name="password" placeholder="Value" required>

      <button type="submit" class="auth-button">Autenticazione</button>

      <!-- Se c'è un messaggio di errore, mostralo -->
      <c:if test="${not empty errorMessage}">
        <div class="error-message" style="display: block;">
            ${errorMessage}
        </div>
      </c:if>

      <p class="register-prompt">Non sei ancora registrato?
        <a href="<%= request.getContextPath() %>/register" class="register-link">Registrati qui</a>
      </p>
    </form>
  </section>
</section>

<%@ include file="footer.jsp" %>

</body>
</html>
