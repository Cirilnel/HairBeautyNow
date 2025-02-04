<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Metodo di Pagamento</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ionicons@5.5.0/dist/ionicons/ionicons.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&family=Imperial+Script&display=swap" rel="stylesheet">
  <title>Prenotazione</title>
  <link rel="stylesheet" href="static/style/metodoDiPagamento.css">
    <script src="static/js/metodoPagamento.js"></script>
</head>
<body>
<%@ include file="header.jsp" %>

<h1>Metodo di Pagamento</h1>
<div class="metodo-pagamento-container-container">
<form action="completaPrenotazione" method="post" onsubmit="return validateForm()">
  <label for="metodoPagamento">Seleziona il metodo di pagamento:</label>
  <select name="metodoPagamento" id="metodoPagamento">
    <option value="">Seleziona</option>
    <option value="paypal">PayPal</option>
    <option value="visa">VISA</option>
    <option value="mastercard">Mastercard</option>
  </select>
  <br><br>

  <label for="numeroCarta">Numero di carta:</label>
  <input type="text" id="numeroCarta" name="numeroCarta" maxlength="16">
  <br><br>

  <label for="cvv">CVV:</label>
  <input type="text" id="cvv" name="cvv" maxlength="3">
  <br><br>

  <label for="scadenza">Data di scadenza (MM/AA):</label>
  <input type="text" id="scadenza" name="scadenza" placeholder="MM/YY">
  <br><br>

  <label for="indirizzo">Indirizzo di fatturazione:</label>
  <input type="text" id="indirizzo" name="indirizzo">
  <br><br>

  <input type="submit" value="Effettua Prenotazione">
</form>
</div>
<%@ include file="footer.jsp" %>

</body>
</html>
