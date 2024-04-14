<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.loscuchurrumines.Model.Persona"%>
<%@ page import="com.loscuchurrumines.Model.Proyecto"%>
<%@ page import="com.loscuchurrumines.DAO.PersonaDAO"%>
<%@ page import="com.loscuchurrumines.Model.Usuario"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sustain Partners</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Newsreader:wght@400;500;600;700&display=swap" rel="stylesheet">
<style>
    .newsreader-font {
        font-family: 'Newsreader', serif;
    }
</style>
</head>
<body class="bg-gray-100">

    <%
        Persona personaActual = (Persona) session.getAttribute("persona");
        List<Proyecto> proyectos = (List<Proyecto>) request.getAttribute("proyectos");
        PersonaDAO personaDAO = new PersonaDAO();

        if (personaActual == null) {
            response.sendRedirect("login");
            return;
        }
        Usuario usuarioActual = (Usuario) session.getAttribute("user");
        
        boolean isAdmin = usuarioActual.getFkCargo()==2;
    %>
    

    <div class="container mx-auto px-4 sm:px-10 py-6">
        <!-- Header -->
        <div class="flex justify-between items-center mb-10">
            <div class="flex items-center space-x-4 cursor-pointer" onclick="location.href='perfil'">
                <img src="<%= personaActual.getFotoPersona()%>" class="rounded-full w-10 h-10">
                <div class="hidden sm:block text-black text-sm font-semibold font-['Inter'] uppercase leading-snug tracking-wider">Ver perfil<br/></div>
</div>
            
            <button onclick="location.href='logout'" class="w-[129px] h-[27px] text-black text-sm font-semibold font-['Inter'] uppercase leading-snug tracking-wider">Cerrar Sesion</button>
            
        </div>
        

        <!-- Title -->
        <div class="text-center mb-10">
<div class="text-lime-800 font-medium newsreader-font leading-none text-3xl sm:text-5xl">Sustain Partners</div>
<div class="h-1 bg-gray-900 w-3/4 sm:w-1/2 mx-auto my-2"></div>
    <div class="mx-auto text-black text-lg sm:text-xl font-normal newsreader-font leading-tight">
Haz realidad tu proyecto para ayudar a la sociedad
</div><br><br>
    <% if(isAdmin) { %>
            <button onclick="location.href='usuario'" class="text-sm bg-blue-500 text-white px-4 sm:px-6 py-2 rounded-lg hover:bg-blue-600 mb-6">Listar Usuarios</button>
            <button onclick="location.href='estadisticas'" class="text-sm bg-blue-500 text-white px-4 sm:px-6 py-2 rounded-lg hover:bg-blue-600 mb-6">Ver estadisticas</button>
    <% } %>
    <button onclick="location.href='pago'" class="text-sm bg-green-500 text-white px-4 sm:px-6 py-2 rounded-lg hover:bg-green-600 mb-6">Pagar</button>
    <button class="text-sm bg-green-500 text-white px-4 sm:px-6 py-2 rounded-lg hover:bg-green-600 mb-6" onclick="location.href ='proyecto?vista=formularioProyecto'">Empieza tu proyecto</button><br>
    <form action="<%= request.getContextPath() %>/searchBar" method="get">
    <div class="relative">
        <input type="text" name="query" class="border rounded-md px-3 py-2 focus:ring focus:ring-green-200 transition w-3/4" placeholder="Buscar proyectos">
        <button type="submit" class="absolute right-2 top-3 text-gray-500">
            
        </button>
    </div>
</form>
</div>

<!-- Projects Section -->
<h2 class="text-xl sm:text-2xl font-semibold mb-6 text-gray-800">PROYECTOS</h2>


<div class="grid grid-cols-1 sm:grid-cols-3 gap-4 sm:gap-10 mb-10 relative"> 

<% for(Proyecto proyecto : proyectos) { %>

    <a href="proyecto?vista=detalleProyecto&id=<%= proyecto.getIdProyecto() %>" class="block">
        <div class="bg-white rounded-lg shadow-lg p-4 sm:p-6">
            <p class="text-black-1000 font-bold" ><%= proyecto.getNombre() %></p><br>
            <img src="<%= proyecto.getFoto() %>" alt="<%= proyecto.getNombre() %>" class="rounded-lg w-full h-44 sm:h-56 object-cover mb-3">
            <p class="text-sm text-gray-600 mb-1"><%= proyecto.getDescripcion() %></p>
            <% Persona persona = personaDAO.obtenerPersona(proyecto.getFkUser()); %>
            <span class="text-gray-900 font-semibold"><%=persona.getNombre() +" "+ persona.getApellido() %></span>
        </div>
    </a>
<% } %>

</div>

</div>

</body>
</html>