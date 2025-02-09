<%@ page import="it.unisa.application.model.entity.MetodoDiPagamento" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Metodo di Pagamento</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ionicons@5.5.0/dist/ionicons/ionicons.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&family=Imperial+Script&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="static/style/metodoDiPagamento.css">
  <script src="static/js/metodoPagamento.js"></script>
  <script src="static/js/UserMenu.js"></script>
</head>
<body>
<%@ include file="header.jsp" %>

<h1 class="titolo-pagamento">Metodo di Pagamento</h1>
<div class="metodo-pagamento-container">
  <form action="completaPrenotazione" method="post" onsubmit="return validateForm()">

    <!-- Selezione del metodo di pagamento -->
    <label for="metodoPagamento">Seleziona il metodo di pagamento:</label>
    <select name="metodoPagamento" id="metodoPagamento">
      <option value="">Seleziona</option>
      <option value="paypal"
              <%= (request.getAttribute("metodoDiPagamento") != null && "paypal".equals(((MetodoDiPagamento) request.getAttribute("metodoDiPagamento")).getMetodoPagamento())) ? "selected" : "" %>>PayPal</option>
      <option value="visa"
              <%= (request.getAttribute("metodoDiPagamento") != null && "visa".equals(((MetodoDiPagamento) request.getAttribute("metodoDiPagamento")).getMetodoPagamento())) ? "selected" : "" %>>VISA</option>
      <option value="mastercard"
              <%= (request.getAttribute("metodoDiPagamento") != null && "mastercard".equals(((MetodoDiPagamento) request.getAttribute("metodoDiPagamento")).getMetodoPagamento())) ? "selected" : "" %>>Mastercard</option>
    </select>
    <br><br>

    <!-- Numero di carta precompilato -->
    <label for="numeroCarta">Numero di carta:</label>
    <input type="text" id="numeroCarta" name="numeroCarta" maxlength="16"
           value="<%= (request.getAttribute("metodoDiPagamento") != null) ? ((MetodoDiPagamento) request.getAttribute("metodoDiPagamento")).getnCarta() : "" %>">
    <br><br>

    <!-- CVV precompilato -->
    <label for="cvv">CVV:</label>
    <input type="text" id="cvv" name="cvv" maxlength="3"
           value="<%= (request.getAttribute("metodoDiPagamento") != null) ? ((MetodoDiPagamento) request.getAttribute("metodoDiPagamento")).getCvv() : "" %>">
    <br><br>

    <!-- Data di scadenza precompilata -->
    <label for="scadenza">Data di scadenza (MM/AA):</label>
    <input type="text" id="scadenza" name="scadenza" placeholder="MM/YY"
           value="<%= (request.getAttribute("metodoDiPagamento") != null) ? new java.text.SimpleDateFormat("MM/yy").format(((MetodoDiPagamento) request.getAttribute("metodoDiPagamento")).getDataScadenza()) : "" %>">
    <br><br>

    <!-- Indirizzo di fatturazione precompilato -->
    <label for="indirizzo">Indirizzo di fatturazione:</label>
    <input type="text" id="indirizzo" name="indirizzo"
           value="<%= (request.getAttribute("metodoDiPagamento") != null) ? ((MetodoDiPagamento) request.getAttribute("metodoDiPagamento")).getIndirizzo() : "" %>">
    <br><br>

    <input type="submit" value="Effettua Prenotazione">
  </form>
</div>

<%@ include file="footer.jsp" %>

</body>
</html>
