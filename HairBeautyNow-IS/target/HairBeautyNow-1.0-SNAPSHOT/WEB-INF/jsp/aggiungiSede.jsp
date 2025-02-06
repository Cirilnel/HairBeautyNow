<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Aggiungi Sede</title>
  <link rel="stylesheet" href="static/style/style.css">
</head>
<body>

<%@ include file="headerCatena.jsp" %>

<h1 class="general">Aggiungi una nuova sede</h1>

<section class="form-container">
  <form action="<%= request.getContextPath() %>/aggiungiSede" method="post">
    <label for="indirizzo">Indirizzo:</label>
    <input type="text" id="indirizzo" name="indirizzo" required>

    <label for="citta">Città:</label>
    <input type="text" id="citta" name="citta" required>

    <p>Nome sede: <strong>HairBeauty Now</strong></p> <!-- Nome fisso -->

    <button type="submit">Aggiungi Sede</button>
  </form>
</section>

<%@ include file="footer.jsp" %>

</body>
</html>
