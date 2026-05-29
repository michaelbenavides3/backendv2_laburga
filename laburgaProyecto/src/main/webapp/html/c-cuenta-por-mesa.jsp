<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>panel cuenta mesa individual</title>
  <link rel="stylesheet" href="../css/cuenta-por-mesa.css">
</head>
<body>
    <!-- Header -->
    <header class="header">
      <h1>Gestión de Facturación - Panel Cajero</h1>
    </header>
  
    <!-- Layout principal -->
    <div class="layout">
      <!-- Sidebar -->
      <aside class="sidebar">
        <h2>Panel Menú Cajero</h2>
        <nav>
          <img src="../recurso/logo-burguer.png" alt="imagen-logo-lagurga">
          <a href="cuenta-por-mesa.html" class="btn">Consultar Cuenta</a> <!-- RF-11 -->
          <a href="confirmar-consumo.html" class="btn">Confirmar Consumo</a> <!-- RF-12 -->
          <a href="registrar-pago.html" class="btn">Registrar Pago</a> <!-- RF-14 -->
        </nav>
      </aside>

    <!-- Contenido principal -->
    <main class="contenido">
      <!-- Cuadrícula de mesas -->
      <section class="mesas">
        <div class="mesa ocupada">
          <h3>Mesa 1</h3>
          <p class="estado--mesa">Estado: Ocupada</p>
          <a href="cuenta-por-mesa.html?mesa=1" class="btn btn--consultar-cuenta btn-naranja">Consultar Cuenta</a>
        </div>
        <div class="mesa ocupada">
          <h3>Mesa 2</h3>
          <p class="estado--mesa">Estado: Ocupada</p>
          <a href="cuenta-por-mesa.html?mesa=2" class="btn btn--consultar-cuenta btn-naranja">Consultar Cuenta</a>
        </div>
        <div class="mesa ocupada">
            <h3>Mesa 3</h3>
            <p class="estado--mesa">Estado: Ocupada</p>
            <a href="cuenta-por-mesa.html?mesa=3" class="btn btn--consultar-cuenta btn-naranja">Consultar Cuenta</a>
        </div>
        <div class="mesa ocupada">
            <h3>Mesa 4</h3>
            <p class="estado--mesa">Estado: Ocupada</p>
            <a href="cuenta-por-mesa.html?mesa=4" class="btn btn--consultar-cuenta btn-naranja">Consultar Cuenta</a>
        </div>
        <div class="mesa ocupada">
            <h3>Mesa 5</h3>
            <p class="estado--mesa">Estado: Ocupada</p>
            <a href="cuenta-por-mesa.html?mesa=5" class="btn btn--consultar-cuenta btn-naranja">Consultar Cuenta</a>
        </div>
        <div class="mesa ocupada">
            <h3>Mesa 6</h3>
            <p class="estado--mesa">Estado: Ocupada</p>
            <a href="cuenta-por-mesa.html?mesa=6" class="btn btn--consultar-cuenta btn-naranja">Consultar Cuenta</a>
        </div>
        <div class="mesa ocupada">
            <h3>Mesa 7</h3>
            <p class="estado--mesa">Estado: Ocupada</p>
            <a href="cuenta-por-mesa.html?mesa=7" class="btn btn--consultar-cuenta btn-naranja">Consultar Cuenta</a>
        </div>
        <div class="mesa ocupada">
            <h3>Mesa 8</h3>
            <p class="estado--mesa">Estado: Ocupada</p>
            <a href="cuenta-por-mesa.html?mesa=8" class="btn btn--consultar-cuenta btn-naranja">Consultar Cuenta</a>
        </div>
      </section>
    </main>  
  
    <!-- Footer -->
    <footer class="footer">
      <p>&copy; 2025 Labur-Ga. Todos los derechos reservados.</p>
    </footer>
  </body>

</body>
</html>