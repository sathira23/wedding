package com.wedding.servlet;

import com.wedding.dao.EventDaoImpl;
import com.wedding.dao.PackageDaoImpl;
import com.wedding.dao.VenueDaoImpl;
import com.wedding.model.User;
import com.wedding.model.WeddingPackage;
import com.wedding.service.PackageService;
import com.wedding.util.ServletUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/packages/view")
public class PackageDetailsServlet extends HttpServlet {
    private final PackageService packageService = new PackageService(new PackageDaoImpl(), new EventDaoImpl(), new VenueDaoImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer packageId = ServletUtil.parseInt(req.getParameter("id"));
        if (packageId == null) {
            resp.sendRedirect(req.getContextPath() + "/packages");
            return;
        }

        Optional<WeddingPackage> optionalPackage = packageService.findPackage(packageId);
        if (optionalPackage.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Wedding package not found.");
            return;
        }

        WeddingPackage weddingPackage = optionalPackage.get();
        User user = (User) req.getSession().getAttribute("user");
        if (!packageService.canManage(user, weddingPackage)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to view this package.");
            return;
        }

        req.setAttribute("packageItem", weddingPackage);
        req.getRequestDispatcher("/WEB-INF/views/package-details.jsp").forward(req, resp);
    }
}
