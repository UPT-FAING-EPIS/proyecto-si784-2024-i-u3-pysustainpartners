<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Crear Proyecto</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Newsreader:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body class="bg-gray-100 font-['Newsreader']">



    <div class="container mx-auto px-10 py-6">

    <div class="pt-6 flex flex-col sm:flex-row justify-between item-center mb-4">
        <h1 class="text-3xl text-gray-700 font-semibold mb-4">Crear Proyecto</h1>
        <button class="text-sm bg-green-500 text-white px-4 sm:px-6 py-2 rounded-lg hover:bg-green-600 mb-6" onclick="location.href='dashboard'">Volver al Inicio</button>
    </div>

        <form action="/proyecto" method="post" class="space-y-4 bg-white p-6 rounded-lg shadow-lg">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div class="form-group">
                    <label for="nombre" class="block text-gray-700 font-semibold">Nombre:</label>
                    <input type="text" name="nombre" id="nombre" class="border border-gray-300 rounded-md w-full px-4 py-2 focus:outline-none focus:ring focus:ring-green-200" required>
                </div>
                <div class="form-group">
                    <label for="descripcion" class="block text-gray-700 font-semibold">Descripcion:</label>
                    <input type="text" name="descripcion" id="descripcion" class="border border-gray-300 rounded-md w-full px-4 py-2 focus:outline-none focus:ring focus:ring-green-200" required>
                </div>
            </div>
            <div class="form-group">
                <label for="objetivo" class="block text-gray-700 font-semibold">Objetivo:</label>
                <textarea id="objetivo" name="objetivo" class="border border-gray-300 rounded-md w-full px-4 py-2 focus:outline-none focus:ring focus:ring-green-200" required></textarea>
            </div>
            <div class="form-group">
                <label for="foto" class="block text-gray-700 font-semibold">Foto:</label>
                <input type="text" id="foto" name="foto" class="border border-gray-300 rounded-md w-full px-4 py-2 focus:outline-none focus:ring focus:ring-green-200" required>
            </div>
            <div class="form-group">
                <label for="region" class="block text-gray-700 font-semibold">Región:</label>
                <select id="region" name="region" class="border border-gray-300 rounded-md w-full px-4 py-2 focus:outline-none focus:ring focus:ring-green-200">
                <option value="1">Amazonas</option>
                <option value="2">Apurimac</option>
                <option value="3">Ancash</option>
                <option value="4">Arequipa</option>
                <option value="5">Ayacucho</option>
                <option value="6">Cajamarca</option>
                <option value="7">Callao</option>
                <option value="8">Cuzco</option>
                <option value="9">Huancavelica</option>
                <option value="10">Huanuco</option>
                <option value="11">Ica</option>
                <option value="12">Junin</option>
                <option value="13">La Libertad</option>
                <option value="14">Lambayeque</option>
                <option value="15">Lima Metropolitana</option>
                <option value="16">Lima Provincias</option>
                <option value="17">Loreto</option>
                <option value="18">Madre de Dios</option>
                <option value="19">Moquegua</option>
                <option value="20">Pasco</option>
                <option value="21">Piura</option>
                <option value="22">Puno</option>
                <option value="23">San Martin</option>
                <option value="24">Tacna</option>
                <option value="25">Tumbes</option>
                <option value="26">Ucayali</option<>
            </select>
            </div>
            <div class="form-group">
                <span class="block text-gray-700 font-semibold">¿De qué manera se podrá contribuir con tu proyecto?</span>
            <input type="checkbox" id="modalidad1" name="modalidades[]" value="1">
            <label for="modalidad1">Donaciones</label>
            <input type="checkbox" id="modalidad2" name="modalidades[]" value="2">
            <label for="modalidad2">Voluntariado</label>
        </div>
            <div class="form-group">
                <span class="block text-gray-700 font-semibold">¿A que ODS esta enfocado este proyecto?</span>
            <input type="checkbox" id="categoria1" name="categorias[]" value="1">
            <label for="categoria1">Fin de la probreza</label><br>

            <input type="checkbox" id="categoria2" name="categorias[]" value="2">
            <label for="categoria2">Hambre cero</label><br>

            <input type="checkbox" id="categoria3" name="categorias[]" value="3">
            <label for="categoria3">Salud y bienestar</label><br>

            <input type="checkbox" id="categoria4" name="categorias[]" value="4">
            <label for="categoria4">Educación de calidad</label><br>
            
            <input type="checkbox" id="categoria5" name="categorias[]" value="5">
            <label for="categoria5">Igualdad de género</label><br>
            
            <input type="checkbox" id="categoria6" name="categorias[]" value="6">
            <label for="categoria6">Agua limpia y saneamiento</label><br>

            <input type="checkbox" id="categoria7" name="categorias[]" value="7">
            <label for="categoria7">Energia asequible y no contaminante</label><br>

            <input type="checkbox" id="categoria8" name="categorias[]" value="8">
            <label for="categoria8">Trabajo decente y crecimiento economico</label><br>
            
            <input type="checkbox" id="categoria9" name="categorias[]" value="9">
            <label for="categoria9">Industria, innovacion e infraestructura</label><br>
            
            <input type="checkbox" id="categoria10" name="categorias[]" value="10">
            <label for="categoria10">Reduccion de las desigualdades</label><br>

            <input type="checkbox" id="categoria11" name="categorias[]" value="11">
            <label for="categoria11">Ciudades y comunidades sostenibles</label><br>

            <input type="checkbox" id="categoria12" name="categorias[]" value="12">
            <label for="categoria12">Produccion y consumo responsable</label><br>
            
            <input type="checkbox" id="categoria13" name="categorias[]" value="13">
            <label for="categoria13">Accion por el clima</label><br>
            
            <input type="checkbox" id="categoria14" name="categorias[]" value="14">
            <label for="categoria14">Vida submarina</label><br>
            
            <input type="checkbox" id="categoria15" name="categorias[]" value="15">
            <label for="categoria15">Vida de ecosistemas terrestres</label><br>
            
            <input type="checkbox" id="categoria16" name="categorias[]" value="16">
            <label for="categoria16">Paz, justicia e instituciones solidas</label><br>
            
            <input type="checkbox" id="categoria17" name="categorias[]" value="17">
            <label for="categoria17">Alianzas para lograr los objetivos</label><br>
        </div>
            <div class="form-group">
                <label for="monto" class="block text-gray-700 font-semibold">Monto:</label>
                <input type="number" id="monto" name="monto" class="border border-gray-300 rounded-md w-full px-4 py-2 focus:outline-none focus:ring focus:ring-green-200" required>
            </div>
            <button type="submit" class="text-sm bg-green-500 text-white px-4 sm:px-6 py-2 rounded-lg hover:bg-green-600 mb-6">Crear proyecto</button>
        </form>
    </div>

</body>
</html>