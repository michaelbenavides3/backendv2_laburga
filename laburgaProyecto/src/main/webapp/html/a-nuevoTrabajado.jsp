<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Labur-Ga | Administrador</title>
        <link rel="stylesheet" href="../css/style.css">
        <link rel="stylesheet" href="../css/nuevotrabjador.css">
        <link rel="stylesheet" href="../css/variables.css">
    </head>
    <body class="admin">

        <header class="encabezado">
            <h1>Panel del Administrador</h1>
            <p>Gestion de trabajadores y reportes</p>
        </header>

        <main>
            <section class="nuevo-trabajador">
                <h2>Agregar nuevo trabajador</h2>
                <form action="<%= request.getContextPath() %>/UsuarioControlador" method="POST">
                    <label>Nombre completo:</label>
                    <input type="text" name="nombre" pattern="[A-Za-zÀ-ÿ\s]+" title="Solo se permiten letras y espacios"
                           placeholder="Ingrese Nombre: Ej: Falcao Rodriguez">

                    <label>Correo electronico:</label>
                    <input type="email" name="email" placeholder="Ej: correo@yahoo.com" required>

                    <label>Telefono:</label>
                    <input type="tel" name="telefono" pattern="[0-9]{0, 10)" title="Solo aceptan Numeros"
                       maxlength="10" oninput="this.value = this.value.replace(/[^0-9]/g, '')"
                       placeholder="Solo recibe Numero del 0 a 9">

                    <!-- <label>Rol:</label>
                    <select name="rol">
                      <option value="mesero">Mesero</option>
                      <option value="cajero">Cajero</option>
                      <option value="cajero">Cocinero</option>
                      <option value="cajero">Administrador</option>
                      <option value="admin">Administrador</option>
                    </select> -->
                    <label>Usuario:</label>
                    <input type="text" name="usuario">
                    <label>Contraseña:</label>
                    <input type="password" name="password">
                    <label for="idRol">Asignar Rol:</label>
                    <select name="idRol" id="idRol" required>
                        <option value="0">-------</option>
                        <option value="1">Mesero</option>
                        <option value="2">Cajero</option>
                        <option value="3">Cocinero</option>
                        <option value="4">Administrador</option>
                    </select>
                    <div class="form__botones">
                        <button type="submit" class="btn btn-naranja">Registrar</button>
                        <!-- <button type="reset" class="btn btn-rojo">Cancelar</button> -->
                        <button type="reset" class="btn btn-rojo btn--cancelar" onclick="window.location.href = 'a-panel-principal-admin.jsp'">Cancelar Registro</button>
                    </div>
                </form>
            </section>
        </main>

        <footer class="footer">
            <p>&copy; 2025 Labur-Ga. Todos los derechos reservados.</p>
        </footer>

    </body>
</html>
