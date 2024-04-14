package com.loscuchurrumines.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import org.json.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.loscuchurrumines.DAO.UsuarioDAO;
import com.loscuchurrumines.Model.Usuario;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static final String SECRET_KEY = "6LfVXgspAAAAAFOIm5PqiXZqbtYibfKzGNDlUOCx";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        request.getRequestDispatcher("Views/Login/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        String user = request.getParameter("user");
        String password = request.getParameter("password");

        Usuario authenticatedUser = usuarioDAO.authenticate(user, password);

        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        boolean isCaptchaValid = verify(gRecaptchaResponse, SECRET_KEY);

        if (isCaptchaValid) {

            if (authenticatedUser != null) {
                if (authenticatedUser.getEstado()) {
                    request.getSession().setAttribute("user", authenticatedUser);
                    response.sendRedirect("dashboard");
                } else {
                    request.setAttribute("error", "Usuario inactivo");
                    request.getRequestDispatcher("Views/Login/login.jsp").forward(request, response);
                    
                }
                
            } else {
                request.setAttribute("error", "Usuario o contraseña incorrectos");
                request.getRequestDispatcher("Views/Login/login.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Captcha inválido. Por favor, inténtelo de nuevo.");
            request.getRequestDispatcher("Views/Login/login.jsp").forward(request, response);
        }
    }

    public static boolean verify(String gRecaptchaResponse, String secretKey) throws IOException {
        if (gRecaptchaResponse == null || gRecaptchaResponse.isEmpty()) {
            return false;
        }

        String url = "https://www.google.com/recaptcha/api/siteverify",
                params = "secret=" + URLEncoder.encode(secretKey, "UTF-8") +
                        "&response=" + URLEncoder.encode(gRecaptchaResponse, "UTF-8");

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        OutputStream os = conn.getOutputStream();
        os.write(params.getBytes("UTF-8"));
        os.flush();
        os.close();

        int responseCode = conn.getResponseCode();
        InputStream is = responseCode == 200 ? conn.getInputStream() : conn.getErrorStream();

        try (Scanner scanner = new Scanner(is)) {
            String jsonResponse = scanner.useDelimiter("\\A").next();
            JSONObject json = new JSONObject(jsonResponse);
            System.out.println(jsonResponse);
            return json.getBoolean("success");
        } catch (Exception e) {
            return false;
        }
    }

}