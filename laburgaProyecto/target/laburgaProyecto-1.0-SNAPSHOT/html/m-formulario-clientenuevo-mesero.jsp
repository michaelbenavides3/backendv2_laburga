
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>registrar cliente</title>
        <link rel="stylesheet" href="../css/formulario-clientenuevo-mesero.css">
        <link rel="stylesheet" href="../css/variables.css">
    </head>
    <body>
        <header>
            <h1>Panel Mesero - Nuevo Cliente</h1>
        </header>

        <main>
            <form action="${pageContext.request.contextPath}/registrarCliente" method="post">
                <!-- Nombre del cliente -->
                <label for="nombre">Nombre:</label>
                <input type="text" id="nombre" name="nombre" required>

                <label for="documento">Documento de Identidad (CC):</label>
                <input type="text" id="documento" name="documento" required pattern="[0-9]{6, 10)" title="Solo aceptan Numeros"
                       maxlength="10" oninput="this.value = this.value.replace(/[^0-9]/g, '')">

                <!-- Teléfono -->
                <label for="telefono">Telefono:</label>
                <!-- por medio del pattern se le dice al navgador que solo acepte numero del 0 al 9 y que su maximo sea 10 -->
                 <!--  -->
                 <!-- en el oinput esta liena nos determina que solamente se debe ingresar numero no deja ingresr letras ni caracteres -->
                <input type="tel" id="telefono" name="telefono" required pattern="[0-9]{10}" title="El numero debe tener exactamente 10 numeros" 
                       maxlength="10" oninput="this.value = this.value.replace(/[^0-9]/g, '').slice(0, 10)">

                <!-- Correo -->
                <label for="correo">Correo:</label>
                <input type="email" id="correo" name="correo">

                <label for="fechaNacimiento">Fecha de Nacimiento:</label>
                <input type="date" id="fechaNacimiento" name="fechaNacimiento">

                <!-- Botón de enviar y cancelar -->
                <button type="submit" class="btn--envio">Registrar Cliente</button>
                <button type="button" class="btn--cancelar" onclick="window.location.href = 'm-meserocopy.jsp'">Cancelar Registro</button>
            </form>
        </main>

        <footer class="footer"> 
            <p>&copy; 2025 Labur-Ga. Todos los derechos reservados.</p>
        </footer>

        
    </body>



</html>