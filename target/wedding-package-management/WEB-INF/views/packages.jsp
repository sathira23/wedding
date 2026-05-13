<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.wedding.model.User" %>
<%@ page import="com.wedding.model.WeddingPackage" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:useBean id="user" type="com.wedding.model.User" scope="session" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Wedding Packages</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/assets/css/app.css" rel="stylesheet" />
</head>
<body class="wedding-body">
<div class="app-shell">
<%@ include file="partials/header.jspf" %>
<div class="container page-frame">
    <section class="page-hero mb-4">
        <div class="d-flex flex-column flex-lg-row justify-content-between align-items-start gap-4">
            <div class="hero-text">
                <span class="eyebrow">Package Studio</span>
                <div class="d-flex flex-column flex-md-row align-items-start gap-3 mt-3 mb-3">
                    <img src="${pageContext.request.contextPath}/assets/images/brand-crest.svg" alt="Wedding Package Management logo" class="brand-crest brand-crest--hero" />
                    <div>
                        <h1 class="display-6 mb-2">Curate wedding packages with a luxury-first presentation.</h1>
                        <p class="mb-0">Manage event-ready offers, refine pricing tiers, and keep every organizer workflow clean enough for a confident system demo.</p>
                    </div>
                </div>
            </div>
            <div class="action-row">
                <a href="${pageContext.request.contextPath}/packages/form" class="btn btn-success btn-lg">Create Package</a>
                <a href="${pageContext.request.contextPath}/packages" class="btn btn-secondary btn-lg">Refresh List</a>
            </div>
        </div>
        <div class="hero-metrics">
            <div class="metric-card">
                <strong>${fn:length(packages)}</strong>
                <span>Packages visible now</span>
            </div>
            <div class="metric-card">
                <strong>${fn:length(venues)}</strong>
                <span>Venues ready to link</span>
            </div>
            <div class="metric-card">
                <strong>${user.role}</strong>
                <span>Signed-in access level</span>
            </div>
        </div>
    </section>

    <%@ include file="partials/messages.jspf" %>

    <div class="card filter-panel mb-4">
        <div class="card-body">
            <form class="row gy-3 gx-3 align-items-end" method="get" action="${pageContext.request.contextPath}/packages">
                <div class="col-lg-4 col-md-6">
                    <label for="search" class="form-label">Search packages</label>
                    <input type="search" class="form-control" id="search" name="search" value="${param.search}" placeholder="Filter by name, description or inclusion" />
                </div>
                <div class="col-lg-2 col-md-4">
                    <label for="filterTier" class="form-label">Tier</label>
                    <select class="form-select" id="filterTier" name="filterTier">
                        <option value="">All tiers</option>
                        <option value="BASIC" <c:if test="${param.filterTier == 'BASIC'}">selected</c:if>>Basic</option>
                        <option value="PREMIUM" <c:if test="${param.filterTier == 'PREMIUM'}">selected</c:if>>Premium</option>
                        <option value="LUXURY" <c:if test="${param.filterTier == 'LUXURY'}">selected</c:if>>Luxury</option>
                    </select>
                </div>
                <div class="col-lg-2 col-md-4">
                    <label for="filterStatus" class="form-label">Status</label>
                    <select class="form-select" id="filterStatus" name="filterStatus">
                        <option value="">All statuses</option>
                        <option value="ACTIVE" <c:if test="${param.filterStatus == 'ACTIVE'}">selected</c:if>>Active</option>
                        <option value="DRAFT" <c:if test="${param.filterStatus == 'DRAFT'}">selected</c:if>>Draft</option>
                        <option value="ARCHIVED" <c:if test="${param.filterStatus == 'ARCHIVED'}">selected</c:if>>Archived</option>
                    </select>
                </div>
                <div class="col-lg-2 col-md-4">
                    <label for="filterVenueId" class="form-label">Venue</label>
                    <select class="form-select" id="filterVenueId" name="filterVenueId">
                        <option value="">All venues</option>
                        <c:forEach items="${venues}" var="venue">
                            <option value="${venue.id}" <c:if test="${param.filterVenueId == venue.id}">selected</c:if>>${venue.name}</option>
                        </c:forEach>
                    </select>
                    <c:if test="${empty venues}">
                        <div class="form-text text-danger">No venue records available yet. Venue filtering will appear once venues are added.</div>
                    </c:if>
                </div>
                <c:if test="${user.role == 'ADMIN'}">
                    <div class="col-lg-2 col-md-4">
                        <label for="filterOrganizerId" class="form-label">Organizer</label>
                        <select class="form-select" id="filterOrganizerId" name="filterOrganizerId">
                            <option value="">All organizers</option>
                            <c:forEach items="${organizerIds}" var="organizerId">
                                <option value="${organizerId}" <c:if test="${param.filterOrganizerId == organizerId}">selected</c:if>>Organizer #${organizerId}</option>
                            </c:forEach>
                        </select>
                    </div>
                </c:if>
                <div class="col-lg-2 col-md-4 text-end">
                    <button type="submit" class="btn btn-primary w-100">Apply filters</button>
                </div>
            </form>
        </div>
    </div>

    <div class="card table-panel">
        <div class="card-body">
            <div class="d-flex flex-column flex-md-row justify-content-between align-items-center mb-3 gap-3">
                <div>
                    <h2 class="h5 mb-1">Package collection</h2>
                    <p class="text-muted mb-0">${fn:length(packages)} package card(s) shown across the current filters and role permissions.</p>
                </div>
            </div>
            <c:choose>
                <c:when test="${not empty packages}">
                    <div class="package-grid">
                        <c:forEach var="item" items="${packages}">
                            <article class="package-card">
                                <div class="package-card__top">
                                    <div class="package-card__brand">
                                        <span class="package-card__icon package-card__icon--${fn:toLowerCase(item.tier)}">${fn:substring(item.tier, 0, 1)}</span>
                                        <div>
                                            <h3 class="package-card__title">${item.name}</h3>
                                            <p class="package-card__meta">Updated ${item.updatedAt}</p>
                                        </div>
                                    </div>
                                    <div class="package-card__price">$${item.price}</div>
                                </div>

                                <div class="package-card__chips">
                                    <span class="badge rounded-pill text-white badge-tier-${item.tier}">${item.tier}</span>
                                    <span class="package-chip">${item.status}</span>
                                    <c:if test="${user.role == 'ADMIN'}">
                                        <span class="package-chip">Organizer #${item.organizerId}</span>
                                    </c:if>
                                </div>

                                <p class="package-card__description">${item.description}</p>

                                <div class="package-card__detail-grid">
                                    <div class="package-card__detail">
                                        <span class="package-card__detail-icon">LK</span>
                                        <div>
                                            <span class="package-card__detail-label">Linked Destination</span>
                                            <span class="package-card__detail-value">
                                                <c:choose>
                                                    <c:when test="${not empty item.linkedEventName}">${item.linkedEventName} (Event)</c:when>
                                                    <c:when test="${not empty item.linkedVenueName}">${item.linkedVenueName} (Venue)</c:when>
                                                    <c:otherwise>Not linked yet</c:otherwise>
                                                </c:choose>
                                            </span>
                                        </div>
                                    </div>
                                    <div class="package-card__detail">
                                        <span class="package-card__detail-icon">IN</span>
                                        <div>
                                            <span class="package-card__detail-label">Inclusions</span>
                                            <span class="package-card__detail-value">${item.inclusions}</span>
                                        </div>
                                    </div>
                                </div>

                                <div class="package-card__footer">
                                    <div class="package-card__inclusions">
                                        Crafted for planners who need clear package scope, stylish presentation, and confident demo flow.
                                    </div>
                                    <div class="package-card__actions">
                                        <a class="btn btn-sm btn-outline-secondary" href="${pageContext.request.contextPath}/packages/view?id=${item.id}">View</a>
                                        <a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/packages/form?id=${item.id}">Edit</a>
                                        <button type="button" class="btn btn-sm btn-outline-danger" onclick="confirmDelete(${item.id}, '${fn:replace(item.name, "'", "&#39;")}')">Delete</button>
                                    </div>
                                </div>
                            </article>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="section-card empty-state text-center">
                        <img src="${pageContext.request.contextPath}/assets/images/brand-crest.svg" alt="Wedding Package Management logo" class="brand-crest mb-3" />
                        <h3 class="h5 mb-2">No packages match the selected filters.</h3>
                        <p class="mb-4">Try changing the search criteria or create a new package to start building your collection.</p>
                        <a href="${pageContext.request.contextPath}/packages/form" class="btn btn-primary">Create Your First Package</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<div class="modal fade" id="deletePackageModal" tabindex="-1" aria-labelledby="deletePackageModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/packages/delete" method="post">
                <div class="modal-header">
                    <h5 class="modal-title" id="deletePackageModalLabel">Delete package</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body"></div>
                <input type="hidden" name="id" value="" />
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-danger">Delete</button>
                </div>
            </form>
        </div>
    </div>
</div>

<%@ include file="partials/footer.jspf" %>
</div>
</body>
</html>
