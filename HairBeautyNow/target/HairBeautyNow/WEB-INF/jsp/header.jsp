<header>
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
            <!-- Link all'icona utente per il login o la registrazione -->
            <a href="<%= request.getContextPath() %>/registerPage" id="user-icon">
                <i class="fas fa-user"></i>
            </a>



            <!-- Menu a tendina, nascosto di default -->
            <div id="user-menu" class="hidden">
                <a href="account.jsp">Informazioni sull'account</a>
                <a href="LogoutServlet" class="logout-btn">
                    Logout <i class="fas fa-sign-out-alt"></i>
                </a>
            </div>
        </div>

        <!-- Link per caricare correttamente lo script JS -->
        <script src="<%= request.getContextPath() %>/static/js/userMenu.js"></script>
    </div>
</header>
