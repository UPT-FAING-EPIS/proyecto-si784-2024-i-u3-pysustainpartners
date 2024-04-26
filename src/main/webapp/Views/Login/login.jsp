<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <script src='https://www.google.com/recaptcha/api.js'
    integrity="sha384-oqVuAfXRKap7fdgcCY5uykM6+R9GqQ8K/uxy9rx7HNQlGYl1kPzQho1wx4JwY8wC"></script>
</head>

<body class="bg-black h-screen font-sans relative overflow-hidden">
    <h1 class="text-white text-2xl sm:text-3xl md:text-4xl font-bold absolute top-0 left-1/2 transform -translate-x-1/2 mt-10 sm:mt-20 z-20">Sustain Partners</h1>

    <video id="video-background" autoplay muted loop class="absolute top-0 left-0 w-full h-full object-cover z-0 filter blur-lg">
        <source src="Assets/login.mp4" type="video/mp4">
        <track src="captions_en.vtt" kind="captions" srclang="en" label="English">
        your browser does not support the video tag.
    </video>

    <div class="absolute inset-0 bg-black opacity-50 z-10"></div>

    <div class="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 z-30 w-full px-4">
        <div class="bg-gray-200 p-4 sm:p-8 rounded-lg shadow-md w-full max-w-md mx-auto">
            <h2 class="text-xl sm:text-2xl font-semibold mb-4 text-gray-800 text-center">Iniciar Sesión</h2>
            <form action="login" method="post" class="space-y-6">
                <div>
                    <label for="user" class="block text-sm font-medium text-gray-700">Usuario</label>
                    <input type="text" name="user" id="user" required class="mt-1 p-2 w-full border rounded-md focus:ring focus:ring-blue-200 focus:border-blue-300">
                </div>

                <div>
                    <label for="password" class="block text-sm font-medium text-gray-700">Contraseña</label>
                    <input type="password" name="password" id="password" required class="mt-1 p-2 w-full border rounded-md focus:ring focus:ring-blue-200 focus:border-blue-300">
                </div>
                <div class="items-center">
                <div class="g-recaptcha mb-4 w-full" data-sitekey="6LfVXgspAAAAAD82ltcbTlmK1rvhUfwR2R1MGCuf"></div>
                </div>
                <% if(request.getAttribute("error") != null) { %>
                    <p class="text-red-600 text-sm font-medium text-center">
                        <%= request.getAttribute("error") %>
                    </p>
                <% } %>
                <div class="flex flex-col items-center space-y-4">
                    <button type="submit" class="bg-blue-600 text-white w-full sm:w-40 p-2 rounded-md hover:bg-blue-700">Ingresar</button>
                    <a href="register" class="text-center bg-green-600 text-white w-full sm:w-40 p-2 rounded-md hover:bg-green-700">Registrarse</a>
                    <a href="recuperar" class="text-green-600 hover:underline">¿Has olvidado tu contraseña?</a>
                </div>
            </form>
        </div>
    </div>
</body>

</html>
