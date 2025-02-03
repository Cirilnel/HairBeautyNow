<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="static/style/login.css">
  <title>HairBeauty Now</title>
</head>
<body class="login">
<%@ include file="header.jsp" %>

<h1>Autenticazione</h1>

<section class="auth-container">
  <form action="<%= request.getContextPath() %>/login" method="post" class="auth-form">
    <label for="email">Email</label>
    <input type="email" id="email" name="email" placeholder="Value" required>

    <label for="password">Password</label>
    <input type="password" id="password" name="password" placeholder="Value" required>

    <button type="submit" class="auth-button">Autenticazione</button>

    <!-- Se c'è un messaggio di errore, mostralo -->
    <c:if test="${not empty errorMessage}">
      <div class="error-message">${errorMessage}</div>
    </c:if>

    <p class="register-prompt">Non sei ancora registrato?
      <a href="<%= request.getContextPath() %>/registerPage" class="register-link">Registrati qui</a>
    </p>
  </form>
</section>
<%@ include file="footer.jsp" %>

</body>
</html>
