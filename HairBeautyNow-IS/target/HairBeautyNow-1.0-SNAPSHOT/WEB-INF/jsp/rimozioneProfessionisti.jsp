<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.application.model.entity.Professionista" %>
<%@ page import="it.unisa.application.model.entity.Sede" %>
<%@ page import="it.unisa.application.model.entity.UtenteGestoreSede" %>
<html>
<head>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ionicons@5.5.0/dist/ionicons/ionicons.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&family=Imperial+Script&display=swap" rel="stylesheet">
  <title>Rimozione Professionisti</title>
  <link rel="stylesheet" href="static/style/rimozioneProfessionisti.css">
</head>
<body>
<%@ include file="header.jsp" %>

<h1>Rimozione Professionisti</h1>
<div class="rimozione-container">

  <%
    // Recupero i professionisti dalla request
    List<Professionista> professionisti = (List<Professionista>) request.getAttribute("professionisti");
    // Recupero la sede dalla request
    Sede sede = (Sede) request.getAttribute("sede");

    if (professionisti == null || professionisti.isEmpty()) {
  %>
  <p>Nessun professionista trovato.</p>
  <%
  } else {
  %>
  <h3>Professionisti nella sede: <%= sede.getIndirizzo() %>, <%= sede.getCittà() %></h3>
  <ul>
    <%
      // Ciclo attraverso i professionisti per mostrarli
      for (Professionista professionista : professionisti) {
    %>
    <li>
      <strong>ID:</strong> <%= professionista.getId() %> - <strong>Nome:</strong> <%= professionista.getNome() %>
      <br>
      <form action="rimuoviProfessionista" method="post">
        <!-- Aggiungi ID professionista da rimuovere -->
        <input type="hidden" name="professionistaId" value="<%= professionista.getId() %>" />
        <input type="submit" value="Rimuovi" />
      </form>
    </li>
    <%
      }
    %>
  </ul>
  <%
    }
  %>

</div>

<%@ include file="footer.jsp" %>
</body>
</html>
