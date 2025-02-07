<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Aggiungi Sede</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ionicons@5.5.0/dist/ionicons/ionicons.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&family=Imperial+Script&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="static/style/aggiungiSede.css">
  <script src="static/js/UserMenu.js"></script>
</head>
<body>

<%@ include file="headerCatena.jsp" %>
<a href="javascript:history.back()" class="back-arrow">
  <i class="fa fa-arrow-left"></i>
</a>
<h1 class="general">Aggiungi una nuova sede</h1>


<section class="form-container">
  <form action="<%= request.getContextPath() %>/aggiungiSede" method="post">
    <label for="indirizzo">Indirizzo:</label><!--Via Senato 156 Via Giovanni 56-->
    <input type="text" id="indirizzo" name="indirizzo" required>

    <label for="citta">Citt&agrave;:</label>
    <input type="text" id="citta" name="citta" required>
    <button type="submit">Aggiungi Sede</button>
  </form>
</section>

<%@ include file="footer.jsp" %>

</body>
</html>
