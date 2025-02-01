
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Imperial+Script&display=swap" rel="stylesheet">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HairBeauty Now</title>
    <link rel="stylesheet" href="static/style/login.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ionicons@5.5.0/dist/ionicons/ionicons.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&family=Imperial+Script&display=swap" rel="stylesheet">
</head>
<body>
<%@ include file="header.jsp" %>

<h1>Registrazione</h1>

<section class="auth-container">
    <a href="javascript:history.back()" class="back-arrow">
        <i class="fa fa-arrow-left"></i>
    </a>

    <form class="auth-form">
        <!-- Nome e Cognome -->
        <label for="nome">Nome</label>
        <input type="text" id="nome" name="nome" placeholder="Inserisci il tuo nome" required>

        <label for="cognome">Cognome</label>
        <input type="text" id="cognome" name="cognome" placeholder="Inserisci il tuo cognome" required>

        <!-- Username -->
        <label for="username">Username</label>
        <input type="text" id="username" name="username" placeholder="Inserisci il tuo username" required>

        <!-- Città -->
        <label for="citta">Città</label>
        <input type="text" id="citta" name="citta" placeholder="Inserisci la tua città" required>

        <!-- Email -->
        <label for="email">Email</label>
        <input type="email" id="email" name="email" placeholder="Inserisci la tua email" required>

        <!-- Password -->
        <label for="password">Password</label>
        <input type="password" id="password" name="password" placeholder="Inserisci la tua password" required>

        <!-- Pulsante di invio -->
        <button type="submit" class="auth-button">Registrati</button>

        <!-- Link per il login -->
        <p class="register-prompt">Hai già un account? <a href="<%= request.getContextPath() %>/loginPage" class="register-link">Accedi qui</a></p>
    </form>
</section>

<%@ include file="footer.jsp" %>
</body>
</html>