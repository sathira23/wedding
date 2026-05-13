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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/packages")
public class PackageServlet extends HttpServlet {
    private final PackageService packageService = new PackageService(new PackageDaoImpl(), new EventDaoImpl(), new VenueDaoImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String search = req.getParameter("search");
        String filterTier = req.getParameter("filterTier");
        String filterStatus = req.getParameter("filterStatus");
        Integer filterVenueId = ServletUtil.parseInt(req.getParameter("filterVenueId"));
        Integer filterOrganizerId = ServletUtil.parseInt(req.getParameter("filterOrganizerId"));

        List<WeddingPackage> packages = packageService.getPackages(user).stream()
                .filter(pkg -> {
                    boolean matches = true;
                    if (search != null && !search.trim().isEmpty()) {
                        String term = search.trim().toLowerCase();
                        matches &= pkg.getName().toLowerCase().contains(term)
                                || pkg.getDescription().toLowerCase().contains(term)
                                || pkg.getInclusions().toLowerCase().contains(term)
                                || (pkg.getLinkedEventName() != null && pkg.getLinkedEventName().toLowerCase().contains(term))
                                || (pkg.getLinkedVenueName() != null && pkg.getLinkedVenueName().toLowerCase().contains(term));
                    }
                    if (filterTier != null && !filterTier.isEmpty()) {
                        matches &= pkg.getTier().name().equalsIgnoreCase(filterTier);
                    }
                    if (filterStatus != null && !filterStatus.isEmpty()) {
                        matches &= pkg.getStatus().name().equalsIgnoreCase(filterStatus);
                    }
                    if (filterVenueId != null) {
                        matches &= pkg.getLinkedVenueId() != null && pkg.getLinkedVenueId().equals(filterVenueId);
                    }
                    if (filterOrganizerId != null) {
                        matches &= pkg.getOrganizerId() == filterOrganizerId;
                    }
                    return matches;
                })
                .collect(Collectors.toList());

        req.setAttribute("packages", packages);
        req.setAttribute("venues", packageService.getVenues());
        if (user.isAdmin()) {
            Set<Integer> organizerIds = packages.stream().map(WeddingPackage::getOrganizerId).collect(Collectors.toSet());
            req.setAttribute("organizerIds", organizerIds);
        }
        req.getRequestDispatcher("/WEB-INF/views/packages.jsp").forward(req, resp);
    }
}
