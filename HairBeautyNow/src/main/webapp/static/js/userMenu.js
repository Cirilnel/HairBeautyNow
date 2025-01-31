window.addEventListener('DOMContentLoaded', function () {
    const userIcon = document.getElementById('user-icon');
    const userMenu = document.getElementById('user-menu');

    // Funzione per controllare lo stato di login dell'utente
    function checkUserLoginStatus() {
        // Controlla se l'utente è loggato (endpoint che restituisce stato di login)
        fetch('UserServlet')
            .then(response => response.json())
            .then(data => {
                if (data.loggedIn) {
                    // Se l'utente è loggato, cambia l'icona e mostra il menu
                    userIcon.setAttribute('href', '#');  // Non porta più alla pagina di registrazione
                    userMenu.classList.remove('hidden');
                } else {
                    // Se non è loggato, rimanda alla pagina di registrazione
                    userIcon.setAttribute('href', '<%= request.getContextPath() %>/jsp/registerPage.jsp');
                    userMenu.classList.add('hidden');
                }
            });
    }

    // Esegui il controllo dello stato di login al caricamento della pagina
    checkUserLoginStatus();
});
