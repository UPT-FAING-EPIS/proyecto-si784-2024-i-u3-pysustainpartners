<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.loscuchurrumines.Model.Persona"%>
<%@ page import="com.loscuchurrumines.Model.Proyecto"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perfil de Usuario</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Newsreader:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body class="bg-gray-100 font-['Newsreader']">

    <%
        Persona persona = (Persona) session.getAttribute("persona");
        List<Proyecto> proyectos = (List<Proyecto>) session.getAttribute("proyectosUsuario");
        Integer cantidadParticipacionProyectos = (Integer) session.getAttribute("cantidadParticipacionProyectos");
        if(persona == null) {
            response.sendRedirect("login");
            return;
        }
        if(cantidadParticipacionProyectos == null){
            cantidadParticipacionProyectos = 0;
        }
    %>

    <div class="container mx-auto px-4 sm:px-10 py-6">
        <!-- Header -->
        <div class="flex justify-between items-center mb-10">
            <div class="flex items-center space-x-4">
                <img src="<%= persona.getFotoPersona() %>" class="rounded-full w-10 h-10">
                <h1 class="text-xl sm:text-2xl text-gray-800 font-semibold">Perfil de <%= persona.getNombre() %></h1>
            </div>
            <a href="/logout" class="text-xs sm:text-sm font-semibold uppercase leading-snug tracking-wider text-black hover:text-gray-600">Cerrar Sesión</a>
        </div>

        <!-- User Info -->
        <div class="bg-white shadow rounded-lg p-4 sm:p-6 mb-10 mx-auto">
            <div class="flex flex-col sm:flex-row justify-between item-center mb-4">
                <h2 class="text-lg text-gray-800 font-semibold">Información del Usuario</h2>

                <button class="ml-auto text-xs sm:text-sm bg-green-500 text-white px-2 py-1 rounded-lg hover:bg-green-600 focus:outline-none focus:ring focus:ring-green-200 transition" onclick="location.href='/persona?action=editarPersona&idPersona=<%=persona.getIdPersona()%>'">Editar Perfil</button>
                
            </div>
                <p class="text-sm"><%= persona.getNombre() %> <%= persona.getApellido() %></p>
                <p class="text-sm">Teléfono: <%= persona.getCelular() %></p>
                <p class="text-sm">Género: <%= persona.getSexo() %></p>
                
            
        </div>

        <div class="bg-white shadow rounded-lg p-4 sm:p-6 mb-10">
            <div>
                <h2 class="text-lg text-gray-800 font-semibold">Proyectos</h2>
                <p class="text-sm">Ha creado <%= proyectos.size() %> proyectos y participa en un total de <%= cantidadParticipacionProyectos + proyectos.size() %> proyectos.</p>
                <ul class="list-disc pl-5">
                    <% for(Proyecto proyecto : proyectos){ %>
                        <li class="text-sm"><%= proyecto.getNombre() %></li>
                    <% } %>
                </ul>
                
            </div>
        </div>
        <div class="flex flex-col sm:flex-row gap-4 justify-center">
            <button class="text-xs sm:text-sm bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600 focus:outline-none focus:ring focus:ring-green-200 transition"
            onclick="location.href='/boleta'">
                Enviar Historial de Pagos
            </button>
            <button class="text-xs sm:text-sm bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600 focus:outline-none focus:ring focus:ring-green-200 transition" onclick = "location.href='dashboard'">Volver al Inicio</button>
        </div>

    </div>
    <script async src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js?client=ca-pub-6391756482511314"
     crossorigin="anonymous"></script>

</body>
</html>