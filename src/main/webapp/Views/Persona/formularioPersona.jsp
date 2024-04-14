<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.loscuchurrumines.Model.Usuario" %>
<% Usuario user = (Usuario) session.getAttribute("user"); %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear Persona</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">

</head>

<body class="bg-black h-screen font-sans relative overflow-hidden">

    <h1 class="text-white text-4xl font-bold absolute top-0 left-1/2 transform -translate-x-1/2 mt-20 z-20">Sustain Partners</h1>
    
    <video id="video-background" autoplay muted loop class="absolute top-0 left-0 w-full h-full object-cover z-0 filter blur-sm">
        <source src="Assets/register.mp4" type="video/mp4">
    </video>

    <div class="absolute inset-0 bg-black opacity-50 z-5"></div>

    <div class="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 z-10">
        <div class="bg-gray-200 p-8 rounded-lg shadow-md w-96">
            <h2 class="text-2xl font-semibold mb-4 text-gray-800">Ingrese sus Datos</h2>


        <form action="persona?action=create" method="post" class="space-y-6">
            <!-- Nombre -->
            <div class="space-y-2">
                <label for="nombre" class="block text-sm font-medium text-gray-600">Nombre</label>
                <input type="text" name="nombre" id="nombre" required class="mt-1 p-2 w-full border rounded-md focus:ring focus:ring-blue-200">
            </div>

            <!-- Apellido -->
            <div class="space-y-2">
                <label for="apellido" class="block text-sm font-medium text-gray-600">Apellido</label>
                <input type="text" name="apellido" id="apellido" required class="mt-1 p-2 w-full border rounded-md focus:ring focus:ring-blue-200">
            </div>

            <!-- Celular -->
            <div class="space-y-2">
                <label for="celular" class="block text-sm font-medium text-gray-600">Telefono</label>
                <input type="text" name="celular" id="celular" required class="mt-1 p-2 w-full border rounded-md focus:ring focus:ring-blue-200">
            </div>

            <!-- Fecha de Nacimiento -->
            <div class="space-y-2">
                <label for="fechaNacimiento" class="block text-sm font-medium text-gray-600">Fecha de Nacimiento</label>
                <input type="date" name="fechaNacimiento" id="fechaNacimiento" required class="mt-1 p-2 w-full border rounded-md focus:ring focus:ring-blue-200">
            </div>

            <!-- Sexo -->
            <div class="space-y-2">
                <label for="sexo" class="block text-sm font-medium text-gray-600">Genero</label>
                <select name="sexo" id="sexo" required class="mt-1 p-2 w-full border rounded-md focus:ring focus:ring-blue-200">
                    <option value="M">Masculino</option>
                    <option value="F">Femenino</option>
                </select>
            </div>
 <% if(request.getAttribute("error") != null) { %>
                <p class="text-red-600 text-sm font-medium text-center">
                    <%= request.getAttribute("error") %>
                </p>
                <% } %>
                <input type="hidden" name="fkUser" value="<%= user.getIdUser()%>">

                <div class="flex justify-center items-center mt-6">
                    <button type="submit" class="text-sm bg-green-500 text-white px-4 sm:px-6 py-2 rounded-lg hover:bg-green-600 mb-6">Crear</button>
                </div>
            </form>
        </div>
    </div>
    <script>
    window.onload = function() {
        var fechaMaxima = new Date();
        fechaMaxima.setFullYear(fechaMaxima.getFullYear() - 18);
        document.getElementById("fechaNacimiento").setAttribute("max", fechaMaxima.toISOString().split('T')[0]);
    };
    </script>
</body>
</html>
