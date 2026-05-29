<!DOCTYPE html>
<html lang="es">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Registrar pago</title>
  <link rel="stylesheet" href="../css/registrar-pago.css">
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
        <img src="../recurso/logo-burguer.png" alt="Logo Labur-Ga">
        <a href="cuenta-por-mesa.html" class="btn">Consultar Cuenta</a>
        <a href="confirmar-consumo.html" class="btn">Confirmar Consumo</a>
        <a href="registrar-pago.html" class="btn">Registrar Pago</a>
      </nav>
    </aside>

  <main class="contenido">
    <h2>Registrar Pago - Mesa (# )</h2>

    <form id="form-pago">
      <p>Total a pagar: <strong>$48.000</strong></p>

      <label for="metodo">Método de Pago:</label>
      <select id="metodo" name="metodo" required>
        <option value="">Seleccione...</option>
        <option value="efectivo">Efectivo</option>
        <option value="transferencia">Transferencia</option>
        <option value="pagotarjeta">Pago con Tarjeta</option>
      </select>

      <button type="submit" class="btn btn--pago">Confirmar Pago</button>
    </form>

      <!-- Footer -->
      <footer class="footer">
        <p>&copy; 2025 Labur-Ga. Todos los derechos reservados.</p>
      </footer>
  </main>

</body>

</html>