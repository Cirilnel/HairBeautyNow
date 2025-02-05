<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header class="header">
    <script src="static/js/UserMenu.js"></script>
    <link rel="stylesheet" type="text/css" href="static/style/header.css">
    <script defer src="static/js/UserMenu.js"></script>

    <div class="top-bar">
        <!-- Social Icons -->
        <div class="social-icons">
            <a href="#"><i class="fab fa-facebook-f" style="font-size: 30px; color: #007DFF;"></i></a>
            <a href="#"><i class="fab fa-instagram" style="font-size: 30px; color: #007DFF;"></i></a>
        </div>

        <!-- Navigation Bar -->
        <nav>
            <ul>
                <li><a href="<%= request.getContextPath() %>/home" class="interactive-link">Home</a></li>
                <li><a href="<%= request.getContextPath() %>/serviziServlet" class="interactive-link">Servizi</a></li>
                <li>
                    <img src="static/images/logo.png" alt="Icona">
                </li>
                <li><a href="<%= request.getContextPath() %>/promozioni" class="interactive-link">Promozioni</a></li>
                <li><a href="<%= request.getContextPath() %>/storicoOrdini" class="interactive-link">Storico Ordini</a></li>
            </ul>
        </nav>

        <div class="user-icons">
            <%
                // Controlla se l'utente è loggato, qualsiasi tipo di utente
                Object user = session.getAttribute("user");
                if (user != null) {  // Se l'utente è loggato
            %>
            <div class="user-menu">
                <i class="fas fa-user" id="logged-user-icon" style="font-size: 30px; color: #007DFF; cursor: pointer;"></i>
                <div class="dropdown-menu" id="dropdown-menu">
                    <a href="<%= request.getContextPath() %>/accountInfo">Informazioni sull'account</a>
                    <a href="<%= request.getContextPath() %>/logout" class="logout-link">
                        <i class="fas fa-sign-out-alt"></i> Disconnetti
                    </a>
                </div>
            </div>
            <%
            } else {  // Se non c'è un utente loggato
            %>
            <a href="<%= request.getContextPath() %>/loginPage">
                <i class="fas fa-user" id="user-icon" style="font-size: 30px; color: #007DFF;"></i>
            </a>
            <%
                }
            %>
        </div>

    </div>
</header>
