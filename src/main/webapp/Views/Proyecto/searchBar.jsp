<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.loscuchurrumines.Model.Proyecto"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Buscar Proyecto</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Newsreader:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
    .newsreader-font {
        font-family: 'Newsreader', serif;
    }
</style>
</head>
<body class="bg-gray-100 font-['Newsreader']">
    <div class="text-center mb-10 relative pt-6">
      <!--<div class="text-lime-800 font-medium newsreader-font leading-none text-5xl">Sustain Partners</div>-->
 
  <!--<div class="h-1 bg-gray-900 w-1/3 mx-auto my-2"></div>-->
    
    <div class="container mx-auto px-10 py-6">
        <!-- Search Bar -->
        <div class="flex justify-center mb-10">
            <form action="" method="get" class="pr-6 w-full max-w-xl relative">
                <input type="text" placeholder="Buscar proyecto..." name="query"
                    class=" border border-gray-300 rounded-md w-full px-6 py-2 focus:outline-none focus:ring focus:ring-green-200"
                    value="<%=request.getParameter("query")%>">
                
            </form>
        
            <button class="bg-green-500 text-white px-6 py-2 rounded-lg" onclick = "location.href='dashboard'">Volver al Inicio</button><br>
        </div>
        
        <!-- Projects Display -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <%
                List<Proyecto> proyectos = (List<Proyecto>) request.getAttribute("proyectosSearchBar");
                if (proyectos != null && !proyectos.isEmpty()) {
                    for (Proyecto proyecto : proyectos) {
            %>
                    <a href="proyecto?vista=detalleProyecto&id=<%= proyecto.getIdProyecto() %>" class="block">

                        <div class="bg-white rounded-lg shadow-lg p-6">

                            <h2 class="text-xl text-gray-800 font-semibold mb-2"><%= proyecto.getNombre() %></h2>
                            <p class="text-gray-600"><%= proyecto.getDescripcion() %></p>
                        </div>
            <%
                    }
                } else {
            %>
                <p class="text-center text-gray-800">No se encontraron proyectos.</p>
            <%
                }
            %>
        </div>
    </div>

</body>
</html>
