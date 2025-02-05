<%--
  Created by IntelliJ IDEA.
  User: utente
  Date: 05/02/2025
  Time: 15:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style type="text/css">
        body {
            position: relative;
            margin: 0;
            padding: 0;
        }

        /* Pseudo-elemento per lo sfondo sfocato */
        body::before {
            content: "";
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: url('static/images/hero-image.png') no-repeat center center fixed;
            background-size: cover;
            filter: blur(5px);
            z-index: -1; /* Mette lo sfondo dietro il contenuto */
        }

        .content {
            position: relative;
            z-index: 1;
            text-align: center;
            padding: 20px;
        }

        h1 {
            font-family: 'Imperial Script', cursive;
            color: black;
            font-size: 90px;
            margin-top: 10px;
            text-align: center;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 20vh;
        }
    </style>
    <script src="static/js/UserMenu.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ionicons@5.5.0/dist/ionicons/ionicons.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&family=Imperial+Script&display=swap" rel="stylesheet">
    <title>Title</title>
</head>
<body>
<%@ include file="header.jsp" %>

<div class="content">
    <h1>Stay Tuned...</h1>
</div>

<%@ include file="footer.jsp" %>
</body>
</html>
