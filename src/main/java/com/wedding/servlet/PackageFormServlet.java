package com.wedding.servlet;

import com.wedding.dao.EventDaoImpl;
import com.wedding.dao.PackageDaoImpl;
import com.wedding.dao.VenueDaoImpl;
import com.wedding.model.Event;
import com.wedding.model.User;
import com.wedding.model.Venue;
import com.wedding.model.WeddingPackage;
import com.wedding.service.PackageService;
import com.wedding.util.ServletUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/packages/form")
public class PackageFormServlet extends HttpServlet {
    private final PackageService packageService = new PackageService(new PackageDaoImpl(), new EventDaoImpl(), new VenueDaoImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String rawPackageId = req.getParameter("id");
        Integer packageId = ServletUtil.parseInt(rawPackageId);
        if (rawPackageId != null && rawPackageId.trim().length() > 0 && packageId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid package identifier.");
            return;
        }
        WeddingPackage weddingPackage = new WeddingPackage();
        if (packageId != null) {
            Optional<WeddingPackage> existing = packageService.findPackage(packageId);
            if (existing.isPresent()) {
                weddingPackage = existing.get();
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Wedding package not found.");
                return;
            }
        }
        User user = (User) req.getSession().getAttribute("user");
        if (weddingPackage.getId() != 0 && !packageService.canManage(user, weddingPackage)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to edit this package.");
            return;
        }

        List<Event> events = packageService.getEvents();
        List<Venue> venues = packageService.getVenues();
        req.setAttribute("packageItem", weddingPackage);
        req.setAttribute("events", events);
        req.setAttribute("venues", venues);
        if (events.isEmpty() && venues.isEmpty()) {
            req.setAttribute("loadWarning", "No event or venue records are available. Package linking will be limited until one is created.");
        }
        req.getRequestDispatcher("/WEB-INF/views/package-form.jsp").forward(req, resp);
    }
}
