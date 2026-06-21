<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>formulario reserva mesa</title>
        <link rel="stylesheet" href="../css/formulario-reserva.css">
        <link rel="stylesheet" href="../css/variables.css">
    </head>
    <body>
        <main>
            <header>
                <h1>Panel Mesero - Reserva Mesas</h1>
            </header>

            <section class="form-reserva">
                <h2>Realizar Reserva</h2>
                <form action="../registrarReserva" method="post">
                    <label for="nombre">Nombre del cliente:</label>
                    <input type="text" id="nombre" name="nombre" required>

                    <label for="telefono">Telefono:</label>
                    <input type="text" id="telefono" name="telefono"  pattern="[0-9]{0, 10)" title="Solo aceptan Numeros"
                       maxlength="10" oninput="this.value = this.value.replace(/[^0-9]/g, '')" required >

                    <label for="nombre">Ocasion especial:</label>
                    <input type="text" id="ocasion" name="ocasion" required>

                    <label for="fecha">Fecha:</label>
                    <input type="date" id="fecha" name="fecha" required>

                    <label for="hora">Hora:</label>
                    <input type="time" id="hora" name="hora" required>

                    <label for="personas">Numero de personas:</label>
                    <input type="number" id="personas" name="personas" min="1" max="8" required>

                    <label for="mesa">Mesa:</label>
                    <select id="mesa" name="mesa" required>
                        <option value="">Seleccione una mesa</option>
                        <option value="1">Mesa 1</option>
                        <option value="2">Mesa 2</option>
                        <option value="3">Mesa 3</option>
                        <option value="4">Mesa 4</option>
                        <option value="5">Mesa 5</option>
                        <option value="6">Mesa 6</option>
                        <option value="7">Mesa 7</option>
                        <option value="8">Mesa 8</option>
                    </select>

                    <button type="submit" class="btn--envio">Confirmar Reserva</button>
                    <button type="button" class="btn--cancelar" onclick="window.location.href = 'm-meserocopy.jsp'">Cancelar Reserva</button>
                    <!-- <button type="reset" class="btn--cancelar onclick=" window.location.href = 'm-meserocopy.jsp'">Cancelar Reserva</button> -->
                </form>
            </section>
        </main>
        
        
         <%
            String finalizado = request.getParameter("finalizado");
        %>

        <% if ("true".equals(finalizado)) { %>

        <script>
            alert("Reserva creada correctamente");
        </script>

        <% }%>

        
        
        
        
                
       
                
                
        <footer class="footer">
            <p>&copy; 2025 Labur-Ga. Todos los derechos reservados.</p>
        </footer>

    </body>
</html>