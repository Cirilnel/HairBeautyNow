<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Assumi Professionista</title>
    <link rel="stylesheet" href="static/style.css">

    <!-- Add jQuery for simplicity (can use pure JavaScript too) -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

</head>
<body>
<script src="static/js/UserMenu.js"></script>
<%@ include file="headerSede.jsp" %>
<h1>Assumi un Professionista</h1>

<!-- Il form punta alla servlet tramite il path corretto -->
<form id="assumiForm" action="<%= request.getContextPath() %>/assumiprofessionista" method="POST">
    <label for="nome">Nome del Professionista:</label>
    <input type="text" id="nome" name="nome" required>

    <button type="submit">Assumi Professionista</button>
</form>

<!-- Success Message Placeholder -->
<div id="successMessage" style="display:none; color: green;">
    Professionista assunto con successo!
</div>

<!-- Error Message Placeholder -->
<div id="errorMessage" style="display:none; color: red;">
    Errore nell'assumere il professionista. Riprovare!
</div>

<script>
    $(document).ready(function() {
        // Prevent form from submitting the normal way
        $("#assumiForm").submit(function(event) {
            event.preventDefault();

            // Collect form data
            var nomeProfessionista = $("#nome").val();
            var formData = { nome: nomeProfessionista };

            // Send the form data via AJAX
            $.ajax({
                type: "POST",
                url: "<%= request.getContextPath() %>/assumiprofessionista",
                data: formData,
                success: function(response) {
                    // Show the success message if the professionista was added successfully
                    $("#successMessage").show();
                    $("#errorMessage").hide();
                },
                error: function(xhr, status, error) {
                    // Show error message if something goes wrong
                    $("#successMessage").hide();
                    $("#errorMessage").show();
                }
            });
        });
    });
</script>
<%@ include file="footer.jsp" %>

</body>
</html>
