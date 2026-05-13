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
import java.util.Map;
import java.util.Optional;

@WebServlet("/packages/save")
public class PackageSaveServlet extends HttpServlet {
    private final PackageService packageService = new PackageService(new PackageDaoImpl(), new EventDaoImpl(), new VenueDaoImpl());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String rawPackageId = req.getParameter("id");
        Integer packageId = ServletUtil.parseInt(rawPackageId);
        WeddingPackage weddingPackage = new WeddingPackage();
        if (rawPackageId != null && rawPackageId.trim().length() > 0 && packageId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid package identifier.");
            return;
        }
        if (packageId != null) {
            Optional<WeddingPackage> existing = packageService.findPackage(packageId);
            if (existing.isPresent()) {
                weddingPackage = existing.get();
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Wedding package not found.");
                return;
            }
        }

        if (weddingPackage.getId() != 0 && !packageService.canManage(user, weddingPackage)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to modify this package.");
            return;
        }

        Map<String, String> errors;
        try {
            errors = packageService.validateAndSave(
                    user,
                    weddingPackage,
                    req.getParameter("name"),
                    req.getParameter("description"),
                    req.getParameter("tier"),
                    req.getParameter("price"),
                    req.getParameter("inclusions"),
                    req.getParameter("status"),
                    req.getParameter("linkedEventId"),
                    req.getParameter("linkedVenueId")
            );
        } catch (RuntimeException ex) {
            errors = Map.of("general", ex.getMessage());
        }

        if (!errors.isEmpty()) {
            weddingPackage.setName(req.getParameter("name"));
            weddingPackage.setDescription(req.getParameter("description"));
            try {
                String tierValue = req.getParameter("tier");
                if (tierValue != null && !tierValue.isEmpty()) {
                    weddingPackage.setTier(com.wedding.model.PackageTier.valueOf(tierValue));
                }
            } catch (IllegalArgumentException ignored) {
                weddingPackage.setTier(null);
            }
            try {
                String priceValue = req.getParameter("price");
                if (priceValue != null && !priceValue.trim().isEmpty()) {
                    weddingPackage.setPrice(new java.math.BigDecimal(priceValue.trim()));
                }
            } catch (NumberFormatException ignored) {
                weddingPackage.setPrice(null);
            }
            weddingPackage.setInclusions(req.getParameter("inclusions"));
            try {
                String statusValue = req.getParameter("status");
                if (statusValue != null && !statusValue.isEmpty()) {
                    weddingPackage.setStatus(com.wedding.model.PackageStatus.valueOf(statusValue));
                }
            } catch (IllegalArgumentException ignored) {
                weddingPackage.setStatus(null);
            }
            weddingPackage.setLinkedEventId(ServletUtil.parseInt(req.getParameter("linkedEventId")));
            weddingPackage.setLinkedVenueId(ServletUtil.parseInt(req.getParameter("linkedVenueId")));

            req.setAttribute("errors", errors);
            req.setAttribute("packageItem", weddingPackage);
            req.setAttribute("events", packageService.getEvents());
            req.setAttribute("venues", packageService.getVenues());
            req.getRequestDispatcher("/WEB-INF/views/package-form.jsp").forward(req, resp);
            return;
        }

        boolean created = packageId == null || packageId == 0;
        String message = created ? "Package created successfully." : "Package updated successfully.";
        resp.sendRedirect(req.getContextPath() + "/packages?success=" + java.net.URLEncoder.encode(message, java.nio.charset.StandardCharsets.UTF_8));
    }
}
