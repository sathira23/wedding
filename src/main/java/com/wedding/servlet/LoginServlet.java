package com.wedding.servlet;

import com.wedding.model.User;
import com.wedding.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            resp.sendRedirect("login.jsp?error=Username+and+password+are+required");
            return;
        }

        User user = authService.authenticate(username, password).orElse(null);
        if (user == null) {
            resp.sendRedirect("login.jsp?error=Invalid+credentials");
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);
        resp.sendRedirect(req.getContextPath() + "/packages");
    }
}
