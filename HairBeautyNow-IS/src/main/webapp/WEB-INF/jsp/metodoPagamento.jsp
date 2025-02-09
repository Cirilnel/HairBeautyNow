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

  <script>
    // Funzione per mostrare/nascondere i campi in base al metodo di pagamento selezionato
    function cambiaCampiPagamento() {
      var metodoPagamento = document.getElementById("metodoPagamento").value;

      // Nascondi tutte le sezioni
      document.getElementById("paypalFields").style.display = "none";
      document.getElementById("visaFields").style.display = "none";
      document.getElementById("mastercardFields").style.display = "none";

      // Mostra il campo selezionato
      if (metodoPagamento === "paypal") {
        document.getElementById("paypalFields").style.display = "block";
      } else if (metodoPagamento === "visa" || metodoPagamento === "mastercard") {
        // Mostra i campi Visa e MasterCard
        document.getElementById("visaFields").style.display = "block";
        document.getElementById("mastercardFields").style.display = "block";  // Se hai una sezione separata
      }
    }
  </script>

</head>
<body>
<%@ include file="header.jsp" %>

<h1 class="titolo-pagamento">Metodo di Pagamento</h1>
<div class="metodo-pagamento-container">
  <form action="completaPrenotazione" method="post" onsubmit="return validateForm()">

    <!-- Selezione del metodo di pagamento -->
    <label for="metodoPagamento">Seleziona il metodo di pagamento:</label>
    <select name="metodoPagamento" id="metodoPagamento" onchange="cambiaCampiPagamento()">
      <option value="">Seleziona</option>
      <option value="paypal"
              <%= (request.getAttribute("metodoDiPagamento") != null && "paypal".equals(((MetodoDiPagamento) request.getAttribute("metodoDiPagamento")).getMetodoPagamento())) ? "selected" : "" %>>PayPal</option>
      <option value="visa"
              <%= (request.getAttribute("metodoDiPagamento") != null && "visa".equals(((MetodoDiPagamento) request.getAttribute("metodoDiPagamento")).getMetodoPagamento())) ? "selected" : "" %>>VISA</option>
      <option value="mastercard"
              <%= (request.getAttribute("metodoDiPagamento") != null && "mastercard".equals(((MetodoDiPagamento) request.getAttribute("metodoDiPagamento")).getMetodoPagamento())) ? "selected" : "" %>>Mastercard</option>
    </select>
    <br><br>

    <!-- Campi comuni per PayPal, VISA e MasterCard -->
    <label for="indirizzo">Indirizzo di fatturazione:</label>
    <input type="text" id="indirizzo" name="indirizzo"
           value="<%= (request.getAttribute("metodoDiPagamento") != null) ? ((MetodoDiPagamento) request.getAttribute("metodoDiPagamento")).getIndirizzo() : "" %>">
    <br><br>

    <!-- Campi specifici per PayPal -->
    <div id="paypalFields" style="display:none;">
      <label for="emailPaypal">Email PayPal:</label>
      <input type="email" id="emailPaypal" name="emailPaypal"
             value="<%= (request.getAttribute("metodoDiPagamento") != null && "paypal".equals(((MetodoDiPagamento) request.getAttribute("metodoDiPagamento")).getMetodoPagamento())) ? ((MetodoDiPagamento) request.getAttribute("metodoDiPagamento")).getEmail() : "" %>">
      <br><br>
    </div>

    <!-- Campi specifici per VISA e MasterCard (comuni) -->
    <div id="visaFields" style="display:none;">
      <label for="numeroCarta">Numero di carta:</label>
      <input type="text" id="numeroCarta" name="numeroCarta" maxlength="16"
             value="<%= (request.getAttribute("metodoDiPagamento") != null && ("visa".equals(((MetodoDiPagamento) request.getAttribute("metodoDiPagamento")).getMetodoPagamento()) || "mastercard".equals(((MetodoDiPagamento) request.getAttribute("metodoDiPagamento")).getMetodoPagamento()))) ? ((MetodoDiPagamento) request.getAttribute("metodoDiPagamento")).getnCarta() : "" %>">
      <br><br>

      <label for="cvv">CVV:</label>
      <input type="text" id="cvv" name="cvv" maxlength="3"
             value="<%= (request.getAttribute("metodoDiPagamento") != null && ("visa".equals(((MetodoDiPagamento) request.getAttribute("metodoDiPagamento")).getMetodoPagamento()) || "mastercard".equals(((MetodoDiPagamento) request.getAttribute("metodoDiPagamento")).getMetodoPagamento()))) ? ((MetodoDiPagamento) request.getAttribute("metodoDiPagamento")).getCvv() : "" %>">
      <br><br>

      <label for="scadenza">Data di scadenza (MM/AA):</label>
      <input type="text" id="scadenza" name="scadenza" placeholder="MM/YY"
             value="<%= (request.getAttribute("metodoDiPagamento") != null && ("visa".equals(((MetodoDiPagamento) request.getAttribute("metodoDiPagamento")).getMetodoPagamento()) || "mastercard".equals(((MetodoDiPagamento) request.getAttribute("metodoDiPagamento")).getMetodoPagamento()))) ? new java.text.SimpleDateFormat("MM/yy").format(((MetodoDiPagamento) request.getAttribute("metodoDiPagamento")).getDataScadenza()) : "" %>">
      <br><br>
    </div>

    <div id="mastercardFields" style="display:none;">
      <!-- Non è necessario aggiungere campi extra, perché sono già gestiti con visaFields -->
    </div>

    <input type="submit" value="Effettua Prenotazione">
  </form>
</div>

<%@ include file="footer.jsp" %>

</body>
</html>
