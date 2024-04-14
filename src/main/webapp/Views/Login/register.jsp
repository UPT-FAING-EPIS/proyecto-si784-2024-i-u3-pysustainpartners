<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro</title>
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
            <h2 class="text-2xl font-semibold mb-4 text-gray-800">Registrar Usuario</h2>
            <form action="register" method="post" class="space-y-6">
                <!-- Email -->
                <div>
                    <label for="email" class="block text-sm font-medium text-gray-700">Email</label>
                    <input type="email" name="email" id="email" required class="mt-1 p-2 w-full border rounded-md focus:ring focus:ring-blue-200 focus:border-blue-300">
                </div>

                <!-- Usuario -->
                <div>
                    <label for="user" class="block text-sm font-medium text-gray-700">Usuario</label>
                    <input type="text" name="user" id="user" required class="mt-1 p-2 w-full border rounded-md focus:ring focus:ring-blue-200 focus:border-blue-300">
                </div>

                <!-- Contraseña -->
                <div>
                    <label for="password" class="block text-sm font-medium text-gray-700">Contraseña</label>
                    <input type="password" name="password" id="password" required class="mt-1 p-2 w-full border rounded-md focus:ring focus:ring-blue-200 focus:border-blue-300">
                </div>

                <!-- Confirmar Contraseña -->
                <div>
                    <label for="password2" class="block text-sm font-medium text-gray-700">Confirmar Contraseña</label>
                    <input type="password" name="password2" id="password2" required class="mt-1 p-2 w-full border rounded-md focus:ring focus:ring-blue-200 focus:border-blue-300">
                </div>

                <% if(request.getAttribute("error") != null) { %>
                <p class="text-red-600 text-sm font-medium text-center">
                    <%= request.getAttribute("error") %>
                </p>
                <% } %>

                <div class="flex justify-center items-center mt-6">
                    <button type="submit" class="text-sm bg-green-500 text-white px-4 sm:px-6 py-2 rounded-lg hover:bg-green-600 mb-6">Registrar</button>
                </div>
            </form>
        </div>
    </div>
</body>

</html>
