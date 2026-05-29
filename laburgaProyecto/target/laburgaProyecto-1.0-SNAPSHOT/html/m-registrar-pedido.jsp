<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    // esta línea de Java atrapamos el ID que viaja en la URL
    String idMesaCapturado = request.getParameter("idMesa");

    // Si por alguna razón entran directo sin dar clic a una mesa, le ponemos "1" por defecto
    //String idMesaCapturado = (idMesaParam !=null) ? idMesaParam : "1";
%>



<!DOCTYPE html>
<html lang="es">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="../css/registrar-pedido.css">
        <link rel="stylesheet" href="../css/style.css">
        <link rel="stylesheet" href="../css/variables.css">
        <title>Registrar perdido</title>
    </head>

    <body>

        <!-- header -->
        <header class="header">
            <h1>Gestion menu - panel mesero Mesa <%= idMesaCapturado%> </h1>
        </header>
        <!-- contener el formulario y contener el logo, para lograr separarlos en dos paneles iguales -->
        <!-- Sidebar -->
        <div class="layout">
            <aside class="sidebar">
                <h2>Panel MenÃº Mesero</h2>
                <nav>
                    <img src="../recurso/logo-burguer.png" alt="imagen-logo-lagurga">
                    <a href="m-meserocopy.jsp" class="btn">Mesas</a>
                   <!-- <a href="unir-mesas.jsp" class="btn">Unir Mesas</a>
                    <a href="separar-mesas.jsp" class="btn">Separar Mesas</a>
                    <a href="enviar-cuenta.jsp" class="btn">Enviar cuenta</a> -->
                    <a href="verificarcuenta.jsp" class="btn">Verificar cuenta</a>
                    <a href="m-formulario-reserva.jsp" class="btn">Realizar Reserva</a>
                    <a href="m-formulario-clientenuevo-mesero.jsp" class="btn">Nuevo Cliente</a>
                </nav>
            </aside>
            <main class="contenido">
                <form action="../PedidoControlador" method="POST">

                    <input type="hidden" name="txtIdMesa" value="<%= idMesaCapturado%>">
                    <!-- <h2 class="contenido__titulo">Carta del Menu</h2> -->
                    <div class="productos-grid">
                        <h2 class="titulo__hamburguesa">Hamburguesas</h2>
                        <div class="producto-card">
                            <img src="../img-productos/hamburguesa-clasica.png" alt="Hamburguesa ClÃ¡sica" class="producto-img">
                            <h3 class="producto-nombre">Hamburguesa ClÃ¡sica</h3>
                            <p class="producto-descripcion">Carne 100% de res, queso amarillo, lechuga, tomate, cebolla y mayonesa en pan
                                brioche.</p>
                            <p class="producto-precio">$19.000</p>
                            <label>Cantidad:
                                <input type="number" name="prod_1" class="producto-cantidad" value="0" min="0">
                            </label>
                        </div>
                        <div class="producto-card">
                            <img src="../img-productos/burguer-espeacial.png" alt="hamburguesa especial" class="producto-img">
                            <h3 class="producto-nombre">Hamburguesa Especial</h3>
                            <p class="producto-descripcion">Carne de res, queso chedar, tocino crujiente, cebolla caramelizada y salsa BBQ
                            </p>
                            <p class="producto-precio">$25.000</p>
                            <label>Cantidad:
                                <input type="number" name="prod_2" class="producto-cantidad" value="0" min="0">
                            </label>
                        </div>
                        <div class="producto-card">
                            <img src="../img-productos/hamburguesa-pollo.png" alt="hamburguesa pollo" class="producto-img">
                            <h3 class="producto-nombre">Hamburguesa de Pollo</h3>
                            <p class="producto-descripcion">Carne de pollo, guacamole, chiles, pico de gallo, queso amarillo y mayonesa de
                                la casa</p>
                            <p class="producto-precio">$22.000</p>
                            <label>Cantidad:
                                <input type="number" name="prod_3" class="producto-cantidad" value="0" min="0">
                            </label>
                        </div>
                        <div class="producto-card">
                            <img src="../img-productos/hamburguesa-suprema.png" alt="hamburguesa suprema" class="producto-img">
                            <h3 class="producto-nombre">Hamburguesa Suprema</h3>
                            <p class="producto-descripcion">Doble carne, queso cheddar, tocino crujiente, aros de cebolla, lechuga, tomate
                                y salsa de la casa.</p>
                            <p class="producto-precio">$35.000</p>
                            <label>Cantidad:
                                <input type="number" name="prod_4" class="producto-cantidad" value="0" min="0">
                            </label>
                        </div>
                        <h2 class="titulo__hotdog">Hotdog</h2>
                        <div class="producto-card">
                            <img src="../img-productos/hotdog-clasico (2).png" alt="hotdog clasico" class="producto-img">
                            <h3 class="producto-nombre">Hotdog Clasico</h3>
                            <p class="producto-descripcion">Salchicha Franckfur en pan suave, con mostaza, ketchup, cebolla picada y el
                                toque tradicioanl.</p>
                            <p class="producto-precio">$19.000</p>
                            <label>Cantidad:
                                <input type="number" name="prod_5" class="producto-cantidad" value="0" min="0">
                            </label>
                        </div>
                        <div class="producto-card">
                            <img src="../img-productos/hotdog-casa.png" alt="hotdog de la casa" class="producto-img">
                            <h3 class="producto-nombre">Hotdog de la Casa</h3>
                            <p class="producto-descripcion">Salchicha premium sobre pan artesanal, baÃ±ada en chili con carne, queso y
                                toque de jalapeÃ±os.</p>
                            <p class="producto-precio">$19.000</p>
                            <label>Cantidad:
                                <input type="number" name="prod_6" class="producto-cantidad" value="0" min="0">
                            </label>
                        </div>
                        <div class="producto-card">
                            <img src="../img-productos/hotdog-americano.png" alt="hotdog americano" class="producto-img">
                            <h3 class="producto-nombre">Hotdog Americano</h3>
                            <p class="producto-descripcion">Salchicha Frankfurt, pan al vapor, mostaza, chucrut y cebolla dorada, el
                                autentico sabor neoyorquino.</p>
                            <p class="producto-precio">$19.000</p>
                            <label>Cantidad:
                                <input type="number" name="prod_7" class="producto-cantidad" value="0" min="0">
                            </label>
                        </div>
                        <div class="producto-card">
                            <img src="../img-productos/hotdog-xxl.png" alt="hotdog xxl 32cm" class="producto-img">
                            <h3 class="producto-nombre">Hotdog XXL 32cm</h3>
                            <p class="producto-descripcion">Salchicha gigante de 32cm en pan artesanal, doble porcion de inredientes,
                                pollo, carne, salsa de la casa.</p>
                            <p class="producto-precio">$19.000</p>
                            <label>Cantidad:
                                <input type="number" name="prod_8" class="producto-cantidad" value="0" min="0">
                            </label>
                        </div>
                        <h2 class="titulo__papaslocas">Papas Locas</h2>
                        <div class="producto-card">
                            <img src="../img-productos/papalocas.png" alt="papas locas" class="producto-img">
                            <h3 class="producto-nombre">Papas Locas</h3>
                            <p class="producto-descripcion">Papas crujientes, baÃ±adas en queso, carne, salsa y una explosion de toppings
                                para compartir.</p>
                            <p class="producto-precio">$25.000</p>
                            <label>Cantidad:
                                <input type="number" name="prod_9" class="producto-cantidad" value="0" min="0">
                            </label>
                        </div>
                        <div class="producto-card">
                            <img src="../img-productos/papasespeciales.png" alt="papas espciales" class="producto-img">
                            <h3 class="producto-nombre">Papas Especiales</h3>
                            <p class="producto-descripcion">Papas, salchichas premium, tocineta, salsa de queso, aros de cebolla,
                                mermelada de pimenton.</p>
                            <p class="producto-precio">$40.000</p>
                            <label>Cantidad:
                                <input type="number" name="prod_10" class="producto-cantidad" value="0" min="0">
                            </label>
                        </div>
                        <h2 class="titulo__otros-productos">Otros</h2>
                        <div class="producto-card">
                            <img src="../img-productos/gaseosa.png" alt="gseosas" class="producto-img">
                            <h3 class="producto-nombre">Gaseosas</h3>
                            <p class="producto-descripcion">Sabores productos postobon.</p>
                            <p class="producto-precio">$5.000</p>
                            <label>Cantidad:
                                <input type="number" name="prod_11" class="producto-cantidad" value="0" min="0">
                            </label>
                        </div>
                        <div class="producto-card">
                            <img src="../img-productos/agua.png" alt="agua" class="producto-img">
                            <h3 class="producto-nombre">Botella de Agua</h3>
                            <p class="producto-descripcion">Botella de agua con gas o sin gas.</p>
                            <p class="producto-precio">$5.000</p>
                            <label>Cantidad:
                                <input type="number" name="prod_12" class="producto-cantidad" value="0" min="0">
                            </label>
                        </div>
                        <div class="producto-card">
                            <img src="../img-productos/te-hatsu.png" alt="te hatsu" class="producto-img">
                            <h3 class="producto-nombre">Te Hatsu</h3>
                            <p class="producto-descripcion">Varios sabores.</p>
                            <p class="producto-precio">$7.000</p>
                            <label>Cantidad:
                                <input type="number" name="prod_13" class="producto-cantidad" value="0" min="0">
                            </label>
                        </div>
                        <div class="producto-card">
                            <img src="../img-productos/cerveza.png" alt="cerveza" class="producto-img">
                            <h3 class="producto-nombre">Cervezas</h3>
                            <p class="producto-descripcion">Importadas.</p>
                            <p class="producto-precio">$12.000</p>
                            <label>Cantidad:
                                <input type="number" name="prod_14" class="producto-cantidad" value="0" min="0">
                            </label>
                        </div>
                    </div>
                    <!-- espacio para que el mesero ingrese las observaciones del pedido -->
                    <div class="observaciones">
                        <label class="pedido__label">Observaciones generales del pedido:</label>
                        <textarea name="txtObservaciones" class="pedido__observacion-general" placeholder="Observaciones Generales"> </textarea>
                        <!-- BotÃ³n Guardar -->
                        <button type="submit" class="pedido__boton pedido__boton--guardar">Guardar Pedido</button>
                        <button type="button" class="pedido__boton pedido__boton--cancelar" onclick="cancelarPedido()">Cancelar
                            Pedido</button>
                    </div>
                </form>
            </main>
            <!-- </form> -->
        </div>
        <footer class="footer">
            <p>&copy; 2025 Labur-Ga. Todos los derechos reservados.</p>
        </footer>
        <!-- </div> -->
    </body>

</html>