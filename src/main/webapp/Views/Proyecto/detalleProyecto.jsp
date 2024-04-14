<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.loscuchurrumines.Model.Proyecto" %>
<%@ page import="com.loscuchurrumines.DAO.ProyectoDAO" %>
<%@ page import="com.loscuchurrumines.Model.Usuario" %>
<%
    ProyectoDAO proyectoDAO = new ProyectoDAO();
    Usuario usuarioActual = (Usuario) session.getAttribute("user");
    Proyecto proyecto = (Proyecto) session.getAttribute("proyectoActual");
    int donadores = (Integer) session.getAttribute("donadores");
    List<Integer> categoriasProyecto = (List<Integer>) session.getAttribute("categorias");
    int voluntarios = (Integer) session.getAttribute("voluntarios");
    Dictionary<Integer, String> regiones = new Hashtable<Integer, String>();
    regiones.put(1,"Amazonas");
    regiones.put(2,"Ancash");
    regiones.put(3,"Apurimac");
    regiones.put(4,"Arequipa");
    regiones.put(5,"Ayacucho");
    regiones.put(6,"Cajamarca");
    regiones.put(7,"Callao");
    regiones.put(8,"Cuzco");
    regiones.put(9,"Huancavelica");
    regiones.put(10,"Huanuco");
    regiones.put(11,"Ica");
    regiones.put(12,"Junin");
    regiones.put(13,"La Libertad");
    regiones.put(14,"Lambayeque");
    regiones.put(15,"Lima Metropolitana");
    regiones.put(16,"Lima Provincias");
    regiones.put(17,"Loreto");
    regiones.put(18,"Madre de Dios");
    regiones.put(19,"Moquegua");
    regiones.put(20,"Pasco");
    regiones.put(21,"Piura");
    regiones.put(22,"Puno");
    regiones.put(23,"San Martin");
    regiones.put(24,"Tacna");
    regiones.put(25,"Tumbes");
    regiones.put(26,"Ucayali");
    Dictionary<Integer, String> categorias = new Hashtable<Integer, String>();
    categorias.put(1,"Fin de la pobreza");
    categorias.put(2,"Hambre cero");
    categorias.put(3,"Salud y bienestar");
    categorias.put(4,"Educación de calidad");
    categorias.put(5,"Igualdad de género");
    categorias.put(6,"Agua limpia y saneamiento");
    categorias.put(7,"Energía asequible y no contaminante");
    categorias.put(8,"Trabajo decente y crecimiento económico");
    categorias.put(9,"Industria, innovación e infraestructura");
    categorias.put(10,"Reducción de las desigualdades");
    categorias.put(11,"Ciudades y comunidades sostenibles");
    categorias.put(12,"Producción y consumo responsables");
    categorias.put(13,"Acción por el clima");
    categorias.put(14,"Vida submarina");
    categorias.put(15,"Vida de ecosistemas terrestres");
    categorias.put(16,"Paz, justicia e instituciones sólidas");
    categorias.put(17,"Alianzas para lograr los objetivos");

    boolean isAdmin = usuarioActual.getFkCargo() == 2;
    boolean isProjectActive = proyecto.getEstado();
    int montoRecaudado = (Integer) session.getAttribute("montoRecaudado");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title><%= proyecto.getNombre() %></title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Newsreader:wght@400;500;600;700&display=swap" rel="stylesheet">
<style>

.bg-gray-200 {
    background-color: #e2e8f0;
}

.h-2.5 {
    height: 0.625rem;
}

.bg-blue-600 {
    background-color: #2563eb;
}

.rounded-full {
    border-radius: 9999px;
}

.flex {
    display: flex;
}

.justify-between {
    justify-content: space-between;
}

</style>

</head>
<body class="bg-gray-100 font-['Newsreader']">
    <main class="container mx-auto px-10 py-6"><br>
    <div class="pt-4 flex flex-col sm:flex-row justify-between item-center mb-4">
        <h1 class="text-3xl text-gray-700 font-semibold mb-4">Detalle Proyecto</h1>
        <button class="text-sm bg-green-500 text-white px-4 sm:px-6 py-2 rounded-lg hover:bg-green-600 mb-6" onclick="location.href='dashboard'">Volver al Inicio</button>
    </div>
        <section class="bg-white p-6 rounded-lg shadow-lg mb-6">
            <h1 class="text-2xl font-semibold mb-4"><%= proyecto.getNombre() %></h1>
            <% if(proyecto.getFoto() != null) { %>
            <div>
                <img src="<%= proyecto.getFoto() %>" alt="Imagen del proyecto" class="rounded-lg w-70 h-70 object-auto mb-3">
            <% } %>
            <p class="mb-4"><%= proyecto.getDescripcion() %></p>
            
            <div class="grid grid-cols-2 gap-4 mb-4">
                <div>
                    <h2 class="font-semibold text-lime-800">Recaudación</h2>
                    <div class="w-full bg-gray-200 rounded-full h-2.5 dark:bg-gray-700">
                        <% int fondoTotal = proyectoDAO.getFondo(proyecto.getIdProyecto()); %>
                        <% if(fondoTotal < montoRecaudado){%>
                            <div class="bg-blue-600 h-2.5 rounded-full" style="width:<%= (float) montoRecaudado / montoRecaudado *100 %>%"></div>
                            <% } else { %>
                            <div class="bg-red-600 h-2.5 rounded-full" style="width:<%= (float) montoRecaudado / fondoTotal *100 %>%"></div><% } %>
                        
                    </div>
                    <div class="flex justify-between">
                        <span>Recaudado: <%= montoRecaudado %></span>
                        <span>Meta: <%= fondoTotal %></span>
                    </div>
                </div>
<br>
                <div>
                    <h2 class="font-semibold text-lime-800">Región</h2>
                    <p><%= regiones.get(proyecto.getFkRegion()) %></p>
                </div>
            </div>

            <div class="grid grid-cols-2 gap-4 mb-4">
                <div>
                    <h2 class="font-semibold text-lime-800">Participantes</h2>
                    <p>Donadores: <%= donadores %></p>
                    <p>Voluntarios: <%= voluntarios %></p>
                </div>
            </div>

            <% if(isProjectActive) { %>
                <button class="text-sm bg-green-500 text-white px-4 sm:px-6 py-2 rounded-lg hover:bg-green-600 mb-6" onclick="location.href='participar?id=<%=proyecto.getIdProyecto()%>'">Participar</button>
            <% } else { %><br>
                <button class="text-sm bg-gray-500 text-white px-4  cursor-not-allowed sm:px-6 py-2 rounded-lg " disabled>Participar</button><br>
            <% } %>
            <% if(isAdmin) { %>
                <form action="proyecto?action=changeStatus" method="post"> 
                    <input type="hidden" name="idProyecto" value="<%= proyecto.getIdProyecto() %>">
                    <input type="hidden" name="newStatus" value="<%= !isProjectActive %>"><br>
                    <button type="submit" class="text-sm bg-red-500 text-white px-6 py-2 rounded-lg hover:bg-red-600 mb-6">Cambiar Estado del Proyecto</button>
                </form>
            <% } %>

        </section>

        <section class="bg-white p-6 rounded-lg shadow-lg">
            <h2 class="text-xl font-semibold mb-4">Detalles del Proyecto</h2>
            <div class="space-y-3">
                <div>
                    <h3 class="font-semibold">Descripción:</h3>
                    <p><%= proyecto.getDescripcion() %></p>
                </div>

                <div>
                    <h3 class="font-semibold">Objetivo:</h3>
                    <p><%= proyecto.getObjetivo() %></p>
                </div>

                <div>
                    <h3 class="font-semibold">Categoría:</h3>
                    <% for(Integer categoriaId : categoriasProyecto) { %>
                        <p><%= categorias.get(categoriaId) %></p>
                    <% } %>
                </div>
            </div>

        </section>
    </main>
</body>
</html>