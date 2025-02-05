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
  <link rel="stylesheet" href="static/style/rimuoviProfessionista.css">
  <script src="static/js/rimuoviProfessionista.js"></script>
</head>
<body>
<%@ include file="headerSede.jsp" %>
<script src="static/js/UserMenu.js"></script>

<h1>Rimozione Professionisti</h1>
<div class="rimozione-container">

  <%
    List<Professionista> professionisti = (List<Professionista>) request.getAttribute("professionisti");
    Sede sede = (Sede) request.getAttribute("sede");

    if (professionisti == null || professionisti.isEmpty()) {
  %>
  <p class="error">Nessun professionista trovato.</p>
  <%
  } else {
  %>
  <h3>Professionisti nella sede: <%= sede.getIndirizzo() %>, <%= sede.getCittà() %></h3>
  <ul id="professionisti-list">
    <%
      for (Professionista professionista : professionisti) {
    %>
    <li id="professionista-<%= professionista.getId() %>">
      <img src="static/images/<%= professionista.getId() %>.png" alt="Foto di <%= professionista.getNome() %>" width="100" height="100" />
      <br>
      <strong>Nome:</strong> <strong><%= professionista.getNome() %></strong>
      <br>
      <button onclick="rimuoviProfessionista(<%= professionista.getId() %>)">Rimuovi</button>
    </li>


    <%
      }
    %>

  </ul>
  <%
    }
  %>

</div>

<script>
  var contextPath = "<%= request.getContextPath() %>";
</script>

<%@ include file="footer.jsp" %>
</body>
</html>
