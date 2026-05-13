package com.wedding.servlet;

import com.wedding.dao.EventDaoImpl;
import com.wedding.dao.PackageDaoImpl;
import com.wedding.dao.VenueDaoImpl;
import com.wedding.model.User;
import com.wedding.service.PackageService;
import com.wedding.util.ServletUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/packages/delete")
public class PackageDeleteServlet extends HttpServlet {
    private final PackageService packageService = new PackageService(new PackageDaoImpl(), new EventDaoImpl(), new VenueDaoImpl());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        Integer packageId = ServletUtil.parseInt(req.getParameter("id"));
        String message;
        boolean success;
        try {
            if (packageId != null && packageService.deletePackage(user, packageId)) {
                message = "Package deleted successfully.";
                success = true;
            } else {
                message = "Unable to delete package. Please check your permissions or whether the package exists.";
                success = false;
            }
        } catch (RuntimeException ex) {
            message = "Deletion failed because of a database error. Please try again later.";
            success = false;
        }

        String encodedMessage = java.net.URLEncoder.encode(message, java.nio.charset.StandardCharsets.UTF_8);
        if (success) {
            resp.sendRedirect(req.getContextPath() + "/packages?success=" + encodedMessage);
        } else {
            resp.sendRedirect(req.getContextPath() + "/packages?error=" + encodedMessage);
        }
    }
}
