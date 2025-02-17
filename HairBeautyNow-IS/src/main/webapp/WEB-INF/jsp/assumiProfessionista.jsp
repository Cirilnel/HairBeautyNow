<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <link rel="stylesheet" href="static/style/assumiProfessionista.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ionicons@5.5.0/dist/ionicons/ionicons.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&family=Imperial+Script&display=swap" rel="stylesheet">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Assumi Professionista</title>


    <!-- Add jQuery for simplicity (can use pure JavaScript too) -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

</head>
<body>
<script src="static/js/UserMenu.js"></script>
<%@ include file="headerSede.jsp" %>
<a href="javascript:history.back()" class="back-arrow">
    <i class="fa fa-arrow-left"></i>
</a>
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
<div id="error" style="display:none; color: red;">
    Errore nell'assumere il professionista. Riprovare!
</div>

<script>
    $(document).ready(function() {
        // Prevent form from submitting the normal way
        $("#assumiForm").submit(function(event) {
            event.preventDefault();

            // Collect form data
            var nomeProfessionista = $("#nome").val();

            // Regex to check for special characters (only allows letters, spaces, and basic punctuation)
            var specialCharsRegex = /^[a-zA-ZÀ-ÿ\s\.,'-]+$/;

            // Check if the nomeProfessionista matches the regex
            if (!specialCharsRegex.test(nomeProfessionista)) {
                // Show error message if invalid characters are present
                $("#error").text("Il nome del professionista contiene caratteri non validi.").show();
                $("#successMessage").hide();
                return; // Prevent form submission
            }

            var formData = { nome: nomeProfessionista };

            // Send the form data via AJAX
            $.ajax({
                type: "POST",
                url: "<%= request.getContextPath() %>/assumiProfessionista",
                data: formData,
                success: function(response) {
                    // Show the success message if the professionista was added successfully
                    $("#successMessage").show();
                    $("#error").hide();
                },
                error: function(xhr, status, error) {
                    // Show error message if something goes wrong
                    $("#successMessage").hide();
                    $("#error").show();
                    var errorMessage = JSON.parse(xhr.responseText).message; // Parse the JSON error message
                    $("#error").text(errorMessage); // Display the error message in the error div
                }
            });
        });
    });
</script>
<%@ include file="footer.jsp" %>

</body>
</html>
