<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Participar en Proyecto</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Newsreader:wght@400;500;600;700&display=swap" rel="stylesheet">
    <script>
        function toggleDonationInput(selectedValue) {
            var donationInput = document.getElementById('monto');
            donationInput.disabled = selectedValue != '1';
            if (donationInput.disabled) {
                donationInput.value = ''; 
            }
        }
    </script>
</head>
<body class="bg-gray-100 font-['Newsreader']">
    <div class="container mx-auto px-10 py-6">
    <button class="bg-green-500 text-white px-8 py-2 rounded-full" onclick = "location.href='dashboard'">Volver al Inicio</button><br>
        <form action="/participar?id=<%=request.getParameter("id")%>" method="post" class="bg-white p-6 rounded-lg shadow-lg space-y-4">
            <div class="form-group">
                <span class="block text-gray-700 font-semibold">¿Cómo desea contribuir?</span> <br>
                <% 
                List<Integer> modalidades = (List<Integer>) request.getSession().getAttribute("modalidades");
                for(Integer modalidad : modalidades) {
                    if(modalidad == 1) {
                %>
                <label class="inline-flex items-center mt-3">
                    <input type="radio" id="modalidad1" name="modalidad" value="1" class="form-radio h-5 w-5 text-green-600" onchange="toggleDonationInput(this.value)"><span class="ml-2 text-gray-700">Donaciones</span>
                </label>
                <%
                    } else if(modalidad == 2) {
                %>
                <label class="inline-flex items-center mt-3">
                    <input type="radio" id="modalidad2" name="modalidad" value="2" class="form-radio h-5 w-5 text-green-600" onchange="toggleDonationInput(this.value)"><span class="ml-2 text-gray-700">Voluntariado</span>
                </label>
                <%
                    }
                }
                %>
            </div>
            <div class="form-group">
                <label for="monto" class="block text-gray-700 font-semibold">¿Cuánto desea donar?</label> <br>
                <input type="number" id="monto" name="monto" class="border border-gray-300 rounded-md px-4 py-2 focus:outline-none focus:ring focus:ring-green-200" required disabled>
            </div>
            <button type="submit" class="bg-green-500 text-white px-8 py-2 rounded-full hover:bg-green-600 transition duration-300 ease-in-out">Participar</button>
            
        </form>
        
    </div>
</body>
</html>
