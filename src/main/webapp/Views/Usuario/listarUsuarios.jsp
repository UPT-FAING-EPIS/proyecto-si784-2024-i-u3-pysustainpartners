<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.loscuchurrumines.Model.Usuario"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Administrar Usuarios</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Newsreader:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body class="bg-gray-100 font-['Newsreader']">
    <main class="container mx-auto p-4">
        <h1 class="pt-4 text-3xl text-center font-semibold mb-8">Administrar Usuarios</h1>

        <!-- Responsive grid container -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
            <%
                List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuariosSearch");
                if (usuarios != null && !usuarios.isEmpty()) {
                    for (Usuario usuario : usuarios) {
            %>

            <!-- User card -->
            <div class="bg-white rounded-lg shadow-lg p-4">
                <h2 class="text-lg text-gray-800 font-semibold mb-3 text-center"><%= usuario.getUser() %></h2>

                <form action="usuario" method="post" class="text-center">
                    <input type="hidden" name="action" value="changeRole">
                    <input type="hidden" name="userId" value="<%= usuario.getIdUser() %>">
                    <input type="hidden" name="newRole" value="2">
                    <button type="submit" class="text-sm bg-blue-500 text-white w-full md:w-auto px-4 py-2 rounded hover:bg-blue-600 transition duration-300 ease-in-out">Hacer Administrador</button>
                </form>
            </div>
            <%
                    }
                } else {
            %>
                <!-- No user message -->
                <p class="text-center text-gray-800">No se encontraron usuarios.</p>
            <%
                }
            %>
        </div>

        <!-- Return to dashboard button -->
        <div class="text-center mt-8">
            <button class="text-sm bg-green-500 text-white px-4 sm:px-6 py-2 rounded-lg hover:bg-green-600 mb-6" onclick="location.href='dashboard'">Volver al Inicio</button>
        </div>
    </main>
</body>
</html>