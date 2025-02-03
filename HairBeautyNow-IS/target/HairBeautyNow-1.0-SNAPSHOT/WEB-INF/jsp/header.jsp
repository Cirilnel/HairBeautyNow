<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header class="header">
    <link rel="stylesheet" type="text/css" href="static/style/header.css">
    <div class="top-bar">
        <!-- Social Icons -->
        <div class="social-icons">
            <a href="#"><i class="fab fa-facebook-f" style="font-size: 30px; color: #007DFF;"></i></a>
            <a href="#"><i class="fab fa-instagram" style="font-size: 30px; color: #007DFF;"></i></a>
        </div>

        <!-- Navigation Bar -->
        <nav>
            <ul>
                <li><a href="#" class="interactive-link">Home</a></li>
                <li><a href="<%= request.getContextPath() %>/serviziServlet" class="interactive-link">Servizi</a></li>
                <li>
                    <img src="static/images/logo.png" alt="Icona">
                </li>
                <li><a href="#" class="interactive-link">Promozioni</a></li>
                <li><a href="#" class="interactive-link">Storico Ordini</a></li>
            </ul>
        </nav>

        <!-- User Icon -->
        <div class="user-icons">
            <a href="<%= request.getContextPath() %>/loginPage" id="user-icon">
                <i class="fas fa-user" style="font-size: 30px; color: #007DFF;"></i>
            </a>
        </div>
    </div>
</header>