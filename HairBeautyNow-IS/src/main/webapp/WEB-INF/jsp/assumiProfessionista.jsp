<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Assumi Professionista</title>
    <link rel="stylesheet" href="static/style.css">
</head>
<body>

<h1>Assumi un Professionista</h1>

<!-- Il form punta alla servlet tramite il path corretto -->
<form action="<%= request.getContextPath() %>/assumiprofessionista" method="POST">
    <label for="nome">Nome del Professionista:</label>
    <input type="text" id="nome" name="nome" required>

    <button type="submit">Assumi Professionista</button>
</form>

</body>
</html>
