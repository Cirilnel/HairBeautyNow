<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Imperial+Script&display=swap" rel="stylesheet">
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>HairBeauty Now</title>
  <link rel="stylesheet" href="static/style/style.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ionicons@5.5.0/dist/ionicons/ionicons.css">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&family=Imperial+Script&display=swap" rel="stylesheet">
</head>
<body>

<%@ include file="WEB-INF/jsp/header.jsp" %>
<h1>HairBeauty Now</h1>
<section class="hero">
  <img src="static/images/hero-image.png" alt="Parrucchiera con vista panoramica">
</section>

<section class="intro">
  <h2>L'eleganza deriva dall'essere belli dentro come fuori</h2>
</section>

<section class="services">
  <div class="service-card">
    <img src="static/images/makeup.png" alt="Make Up">
    <div class="service-item">
      <!-- Aggiungi qui l'icona piccola -->
      <img src="static/images/small-makeup-icon.png" alt="Icona Make Up" class="service-icon">
      <p>Make Up</p>
    </div>
  </div>

  <div class="service-card">
    <img src="static/images/hair.png" alt="Hair Styling">
    <div class="service-item">
      <!-- Aggiungi qui l'icona piccola -->
      <img src="static/images/small-hair-icon.png" alt="Icona Hair Styling" class="service-icon">
      <p>Hair Styling</p>
    </div>
  </div>

  <div class="service-card">
    <img src="static/images/nail.png" alt="Nail Care">
    <div class="service-item">
      <!-- Aggiungi qui l'icona piccola -->
      <img src="static/images/small-nail-icon.png" alt="Icona Nail Care" class="service-icon">
      <p>Nail Care</p>
    </div>
  </div>

  <div class="service-card">
    <img src="static/images/massage.png" alt="Massaggi">
    <div class="service-item">
      <!-- Aggiungi qui l'icona piccola -->
      <img src="static/images/small-massage-icon.png" alt="Icona Massaggi" class="service-icon">
      <p>Massaggi</p>
    </div>
  </div>

  <div class="service-card">
    <img src="static/images/spa.png" alt="SPA">
    <div class="service-item">
      <!-- Aggiungi qui l'icona piccola -->
      <img src="static/images/small-spa-icon.png" alt="Icona SPA" class="service-icon">
      <p>SPA</p>
    </div>
  </div>

</section>



<%@ include file="WEB-INF/jsp/footer.jsp" %>

</body>
</html>
