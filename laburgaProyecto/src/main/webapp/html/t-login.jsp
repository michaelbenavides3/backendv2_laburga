<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Labur-Ga | Login</title>
  <link rel="stylesheet" href="../css/login.css">
  <link rel="stylesheet" href="../css/variables.css">
</head>
<body class="login">

  <!-- Encabezado -->
  <header class="encabezado">
    <h1>Acceso al sistema</h1>
    <p>Ingresa tus credenciales</p>
  </header>

  <!-- Formulario de login -->
  <section class="form-login">
    <form action="../LoginControlador" method="POST">
      <div class="campo">
        <label for="usuario">Usuario</label>
        <input type="text" id="usuario" name="txtUsuario" required>
      </div>
      <div class="campo">
        <label for="contraseña">Contraseña</label>
        <input type="password" id="contraseña" name="txtClave" required>
      </div>
      <!-- seccion de recuperar contraseña -->
      <div class="accion-form">
        <!-- boton creado para el ingreso del login, ingresar al modulo correspondiente -->
        <button type="submit" class="btn btn-naranja">Ingresar</button>
        <!-- boton creado para cuando el usuario se le olvide la contraseña y la pueda recuperaar -->
        <button type="button" id=" btn-ayuda-admi" class="btn btn-link">
          Olvide mi contraseña
        </button>
      </div>
    </form>
  </section>

  <!-- Pie de página -->
  <footer class="footer">
    <p>&copy; 2025 Labur-Ga. Todos los derechos reservados.</p>
  </footer>

</body>
</html>
