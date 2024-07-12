package com.loscuchurrumines.controller;


import com.loscuchurrumines.dao.UsuarioDAO;
import com.loscuchurrumines.model.Usuario;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet("/login")
public class LoginController extends HttpServlet {

    private static final String UTF_8 = "UTF-8";
    private static final String ERROR = "error";
    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());
    private static final Dotenv dotenv = Dotenv.load();

    public static final String SECRET_KEY = dotenv.get("RECAPTCHA_PRIVATE_KEY");

    private static final String DEFAULT_REQUEST_VIEWER = "Views/Login/login.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            request.getRequestDispatcher(DEFAULT_REQUEST_VIEWER).forward(request, response);
        } catch (IOException | ServletException e) {
            LOGGER.log(Level.SEVERE, "Exception caught in doGet method", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UsuarioDAO usuarioDAO = getUsuarioDAO();
        try {
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
                        request.setAttribute(ERROR, "Usuario inactivo");
                        request.getRequestDispatcher(DEFAULT_REQUEST_VIEWER).forward(request, response);
                    }
                } else {
                    request.setAttribute(ERROR, "Usuario o contraseña incorrectos");
                    request.getRequestDispatcher(DEFAULT_REQUEST_VIEWER).forward(request, response);
                }
            } else {
                request.setAttribute(ERROR, "Captcha inválido. Por favor, inténtelo de nuevo.");
                request.getRequestDispatcher(DEFAULT_REQUEST_VIEWER).forward(request, response);
            }
        } catch (IOException | ServletException e) {
            LOGGER.log(Level.SEVERE, "Exception caught in doPost method", e);
        }
    }

    protected UsuarioDAO getUsuarioDAO() {
        return new UsuarioDAO();
    }

    public boolean verify(String gRecaptchaResponse, String secretKey) {
        if (gRecaptchaResponse == null || gRecaptchaResponse.isEmpty()) {
            return false;
        }

        HttpURLConnection conn = null;
        OutputStream os = null;
        InputStream is = null;
        Scanner scanner = null;
        try {
            String url = "https://www.google.com/recaptcha/api/siteverify";
            String params = "secret=" + URLEncoder.encode(secretKey, UTF_8) + "&response=" + URLEncoder.encode(gRecaptchaResponse, UTF_8);

            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            os = conn.getOutputStream();
            os.write(params.getBytes(StandardCharsets.UTF_8));

            int responseCode = conn.getResponseCode();
            is = responseCode == 200 ? conn.getInputStream() : conn.getErrorStream();

            scanner = new Scanner(is);
            String jsonResponse = scanner.useDelimiter("\\A").next();
            JSONObject json = new JSONObject(jsonResponse);
            return json.getBoolean("success");
        } catch (IOException | JSONException e) {
            LOGGER.log(Level.SEVERE, "Exception", e);
            return false;
        }
    }

    public void handleGetForTest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void handlePostForTest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
