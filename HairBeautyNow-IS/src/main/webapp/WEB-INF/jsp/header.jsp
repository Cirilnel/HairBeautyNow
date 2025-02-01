<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header>
    <link rel="stylesheet" type="text/css" href="static/style/header.css">
    <div class="top-bar">
        <div class="social-icons">
            <a href="#"><i class="fab fa-facebook-f" style="font-size: 30px; color: #007DFF;"></i></a>
            <a href="#"><i class="fab fa-instagram" style="font-size: 30px; color: #007DFF;"></i></a>
        </div>
        <nav>
            <ul style="display: flex; align-items: center; justify-content: flex-end; padding: 0; margin: 0; width: 100%;">
                <li><a href="#" class="interactive-link" style="font-weight: bold; font-size: 18px;">Home</a></li>
                <li><a href="#" class="interactive-link" style="font-weight: bold; font-size: 18px;">Servizi</a></li>
                <li style="flex-grow: 1; text-align: center;">
                    <img src="static/images/logo.png" alt="Icona" style="max-width: 100%; height: auto;">
                </li>
                <li><a href="#" class="interactive-link" style="font-weight: bold; font-size: 18px;">Promozioni</a></li>
                <li><a href="#" class="interactive-link" style="font-weight: bold; font-size: 18px;">Storico Ordini</a></li>
            </ul>
        </nav>
        <div class="user-icons">
            <a href="<%= request.getContextPath() %>/loginPage" id="user-icon">
                <i class="fas fa-user" style="font-size: 30px; color: #007DFF;"></i>
            </a>
        </div>
    </div>
</header>