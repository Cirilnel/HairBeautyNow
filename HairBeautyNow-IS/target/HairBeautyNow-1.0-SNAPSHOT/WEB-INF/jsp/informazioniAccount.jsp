<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Imperial+Script&display=swap" rel="stylesheet">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HairBeauty Now</title>
    <link rel="stylesheet" href="static/style/login.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ionicons@5.5.0/dist/ionicons/ionicons.css">
    <link href="https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&family=Imperial+Script&display=swap" rel="stylesheet">
</head>
<body>
<%@ include file="header.jsp" %>

<%
    // Recupera l'oggetto UtenteAcquirente dalla sessione
    user = (UtenteAcquirente) session.getAttribute("user");

    // Verifica se l'utente è loggato, altrimenti ridirigi alla pagina di login
    if (user == null) {
        response.sendRedirect("loginPage.jsp");
        return;
    }

    // Ottieni la password e crea una stringa di asterischi
    String password = user.getPassword(); // Assumendo che il metodo getPassword() restituisca la password
    String maskedPassword = "*".repeat(password.length()); // Crea una stringa di asterischi lunga quanto la password
%>

<div class="account-info">
    <h1>Informazioni sull'account</h1>

    <div class="profile-section">
        <!-- Icona profilo -->
        <div class="profile-icon">
            <i class="fas fa-user-circle" style="font-size: 100px; color: #007DFF;"></i>
        </div>

        <!-- Dettagli utente -->
        <div class="user-details">
            <p><strong>Nome e Cognome:</strong> <%= user.getNome() + " " + user.getCognome() %></p>
            <p><strong>Username:</strong> <%= user.getUsername() %></p>
            <p><strong>Città:</strong> <%= user.getCitta() %></p>
            <p><strong>Email:</strong> <%= user.getEmail() %></p>
            <p><strong>Password:</strong> <span><%= maskedPassword %></span></p> <!-- La password è nascosta, ma mantiene la lunghezza -->
        </div>
    </div>

</div>

<%@ include file="footer.jsp" %>
</body>
</html>
