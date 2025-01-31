package it.unisa.application.sottosistemi.GestioneRegistrazione.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/registerPage")
public class RegisterServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("RegisterServlet - Servlet chiamata con GET");
        System.out.println("RegisterServlet - Context Path: " + request.getContextPath());
        System.out.println("RegisterServlet - Request URI: " + request.getRequestURI());
        System.out.println("RegisterServlet - Servlet Path: " + request.getServletPath());
        System.out.println("RegisterServlet - Forwarding a /WEB-INF/jsp/registerPage.jsp");
        request.getRequestDispatcher("/WEB-INF/jsp/registerPage.jsp").forward(request, response);

    }
}