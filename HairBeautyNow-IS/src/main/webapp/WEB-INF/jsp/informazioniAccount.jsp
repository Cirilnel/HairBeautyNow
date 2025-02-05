<%@ page import="it.unisa.application.model.entity.UtenteGestoreCatena" %>
<%@ page import="it.unisa.application.model.entity.UtenteGestoreSede" %>
<%@ page import="it.unisa.application.model.entity.UtenteAcquirente" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Imperial+Script&display=swap" rel="stylesheet">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HairBeauty Now</title>
    <link rel="stylesheet" href="static/style/info.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ionicons@5.5.0/dist/ionicons/ionicons.css">
    <link href="https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&family=Imperial+Script&display=swap" rel="stylesheet">
</head>
<body>
<%@ include file="header.jsp" %>
<script src="static/js/UserMenu.js"></script>

<div class="auth-container">
    <a href="javascript:history.back()" class="back-arrow">
        <i class="fa fa-arrow-left"></i>
    </a>
    <h1 class="account-title">Informazioni sull'account</h1>
</div>

<%
    // Recupera l'oggetto Utente dalla sessione
    Object loggedUser = request.getAttribute("user");

    // Se l'utente è loggato, mostriamo le informazioni in base al tipo
    if (loggedUser != null) {
        if (loggedUser instanceof UtenteAcquirente) {
            UtenteAcquirente acquirente = (UtenteAcquirente) loggedUser;
            String password = acquirente.getPassword();
            String maskedPassword = "*".repeat(password.length()); // Crea la stringa di asterischi per la password
%>
<div class="account-info">
    <div class="profile-section">
        <div class="profile-icon">
            <i class="fas fa-user-circle" style="font-size: 100px; color: #007DFF;"></i>
        </div>
        <div class="user-details">
            <p><strong>Nome e Cognome:</strong> <%= acquirente.getNome() + " " + acquirente.getCognome() %></p>
            <p><strong>Username:</strong> <%= acquirente.getUsername() %></p>
            <p><strong>Città:</strong> <%= acquirente.getCitta() %></p>
            <p><strong>Email:</strong> <%= acquirente.getEmail() %></p>
            <p><strong>Password:</strong> <span><%= maskedPassword %></span></p>
        </div>
    </div>
</div>
<%
} else if (loggedUser instanceof UtenteGestoreCatena) {
    UtenteGestoreCatena gestoreCatena = (UtenteGestoreCatena) loggedUser;
%>
<div class="account-info">
    <div class="profile-section">
        <div class="profile-icon">
            <i class="fas fa-user-circle" style="font-size: 100px; color: #007DFF;"></i>
        </div>
        <div class="user-details">
            <p><strong>Username:</strong> <%= gestoreCatena.getUsername() %></p>
            <p><strong>Sedi Gestite:</strong> <%= gestoreCatena.getN_SediGestite() %></p>
            <p><strong>ID Sede:</strong> <%= gestoreCatena.getSedeID() %></p>
            <p><strong>Gestore:</strong> <%= gestoreCatena.getUsernameUGS() %></p>
        </div>
    </div>
</div>
<%
} else if (loggedUser instanceof UtenteGestoreSede) {
    UtenteGestoreSede gestoreSede = (UtenteGestoreSede) loggedUser;
%>
<div class="account-info">
    <div class="profile-section">
        <div class="profile-icon">
            <i class="fas fa-user-circle" style="font-size: 100px; color: #007DFF;"></i>
        </div>
        <div class="user-details">
            <p><strong>Username:</strong> <%= gestoreSede.getUsernameUGS() %></p>
            <p><strong>ID Sede:</strong> <%= gestoreSede.getSedeID() %></p>
        </div>
    </div>
</div>
<%
} else {
%>
<p>Tipo di utente non riconosciuto.</p>
<%
        }

    }
%>

</body>
</html>
