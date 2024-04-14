<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.loscuchurrumines.Model.Persona"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Perfil</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Newsreader:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body class="bg-gray-100 font-['Newsreader']">

<div class="container mx-auto px-10 py-6">
    <div class="bg-white shadow rounded-lg p-6">
        <div class="mb-4 border-b pb-4">
            <h1 class="text-2xl font-semibold text-gray-800">Editar Perfil</h1>
        </div>

        <div class="edit-form">
            <%
                Persona persona = (Persona) session.getAttribute("persona");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date fechaNacDate = sdf.parse(persona.getFechaNacimiento());
                String fechaNac = sdf.format(fechaNacDate); 

            %>
            <form action="persona?action=update" method="post">
                <input type="hidden" name="idPersona" value="<%= persona.getIdPersona() %>">
                <div class="mb-4">
                    <label for="nombre" class="block text-gray-700 text-sm font-semibold mb-2">Nombre:</label>
                    <input type="text" id="nombre" name="nombre" value="<%= persona.getNombre() %>" required class="border rounded-md px-3 py-2 w-full focus:ring focus:ring-green-200 transition">
                </div>
                <div class="mb-4">
                    <label for="apellido" class="block text-gray-700 text-sm font-semibold mb-2">Apellido:</label>
                    <input type="text" id="apellido" name="apellido" value="<%= persona.getApellido() %>" required class="border rounded-md px-3 py-2 w-full focus:ring focus:ring-green-200 transition">
                </div>
                <div class="mb-4">
                    <label for="celular" class="block text-gray-700 text-sm font-semibold mb-2">Celular:</label>
                    <input type="text" id="celular" name="celular" value="<%= persona.getCelular() %>" required class="border rounded-md px-3 py-2 w-full focus:ring focus:ring-green-200 transition">
                </div>
                <div class="mb-4">
                    <label for="fechaNacimiento" class="block text-gray-700 text-sm font-semibold mb-2">Fecha de Nacimiento:</label>
                    <input type="date" id="fechaNacimiento" name="fechaNacimiento" value="<%= fechaNac %>" required class="border rounded-md px-3 py-2 w-full focus:ring focus:ring-green-200 transition">
                </div>
                <div class="mb-6">
                    <label for="sexo" class="block text-gray-700 text-sm font-semibold mb-2">Sexo:</label>
                    <select id="sexo" name="sexo" class="border rounded-md px-3 py-2 w-full focus:ring focus:ring-green-200 transition">
                        <option value="M" <%= "M".equals(persona.getSexo()) ? "selected" : "" %>>Masculino</option>
                        <option value="F" <%= "F".equals(persona.getSexo()) ? "selected" : "" %>>Femenino</option>
                    </select>
                </div>
                <div class="text-right">
                    <button type="submit" class="text-xs sm:text-sm bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600 focus:outline-none focus:ring focus:ring-green-200 transition">Actualizar Perfil</button>
                    <button class="text-xs sm:text-sm bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600 focus:outline-none focus:ring focus:ring-green-200 transition" onclick = "location.href='dashboard'">Volver al Inicio</button><br>

                </div>
            </form>
            
        </div>
        
    </div>
</div>


</body>
</html>
