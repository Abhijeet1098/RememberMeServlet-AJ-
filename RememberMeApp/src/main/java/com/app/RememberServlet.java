package com.app;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/remember")
public class RememberServlet extends HttpServlet {

    // Handles form submission (POST)
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("username");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Input validation
        if (name == null || name.trim().isEmpty()) {
            out.println("<h3>Name cannot be empty!</h3>");
            out.println("<a href='index.html'>Go Back</a>");
            return;
        }

        // Create cookie
        Cookie cookie = new Cookie("username", name);
        cookie.setMaxAge(86400); // 1 day

        response.addCookie(cookie);

        // Display welcome message
        out.println("<h2>Welcome, " + name + "!</h2>");
        out.println("<a href='remember'>Visit Again</a><br><br>");
        out.println("<a href='remember?action=logout'>Logout</a>");
    }

    // Handles revisit & logout (GET)
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        // 🔴 LOGOUT LOGIC
        if ("logout".equals(action)) {
            Cookie cookie = new Cookie("username", "");
            cookie.setMaxAge(0); // delete cookie
            response.addCookie(cookie);

            response.sendRedirect("index.html");
            return;
        }

        Cookie[] cookies = request.getCookies();
        String name = null;

        // Find username cookie
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("username".equals(c.getName())) {
                    name = c.getValue();
                    break;
                }
            }
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (name != null) {
            out.println("<h2>Welcome back, " + name + "!</h2>");
            out.println("<br><a href='remember?action=logout'>Logout</a>");
        } else {
            response.sendRedirect("index.html");
        }
    }
}