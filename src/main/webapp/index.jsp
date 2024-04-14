<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Landing Page - Sustain Partners</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>

<body class="bg-black h-screen font-sans relative overflow-hidden">

    <!-- Background Video -->
    <video id="video-background" autoplay muted loop class="absolute top-0 left-0 w-full h-full object-cover z-0 filter blur-sm">
        <source src="Assets/landing.mp4" type="video/mp4">
    </video>

    <!-- Black Overlay -->
    <div class="absolute inset-0 bg-black opacity-50 z-5"></div>

    <!-- Content -->
    <div class="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 z-10 text-center">
        <h1 class="text-4xl font-bold mb-4 text-white">Sustain Partners</h1>
        <p class="mb-8 text-gray-200 text-lg">Impulsando proyectos de desarrollo sostenible alineados con los ODS de la ONU en el Perú.</p>

        <div class="flex flex-col space-y-4">
            <a href="login" class="bg-blue-600 text-white w-full p-2 rounded-md hover:bg-blue-700 focus:ring focus:ring-blue-300">Iniciar sesión</a>
            <a href="register" class="border border-blue-600 text-blue-600 w-full p-2 rounded-md hover:bg-blue-600 hover:text-white focus:ring focus:ring-blue-300">Registrarse</a>
        </div>

        <div class="mt-16 flex justify-around w-full text-white space-x-6">
            <div class="flex flex-col items-center">
                <img src="Assets/arbol.png" alt="ODS Icon" class="w-16 h-16 mb-4">
                <span class="font-bold">ODS</span>
                <span>Objetivos de Desarrollo Sostenible</span>
            </div>
            <div class="flex flex-col items-center">
                <img src="Assets/partners.png" alt="Partners Icon" class="w-16 h-16 mb-4">
                <span class="font-bold">Partners</span>
                <span>Promovemos la colaboración entre comunidades</span>
            </div>
            <div class="flex flex-col items-center">
                <img src="Assets/projects.png" alt="Projects Icon" class="w-16 h-16 mb-4">
                <span class="font-bold">Proyectos</span>
                <span>Impulsamos iniciativas a nivel nacional</span>
            </div>
        </div>
    </div>

</body>

</html>