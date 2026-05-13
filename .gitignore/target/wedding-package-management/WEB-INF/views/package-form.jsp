<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.wedding.model.User" %>
<%@ page import="com.wedding.model.WeddingPackage" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:useBean id="user" type="com.wedding.model.User" scope="session" />
<jsp:useBean id="packageItem" type="com.wedding.model.WeddingPackage" scope="request" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>
        <c:choose>
            <c:when test="${packageItem.id == 0}">Create Package</c:when>
            <c:otherwise>Edit Package</c:otherwise>
        </c:choose>
    </title>
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
                <span class="eyebrow">Package Editor</span>
                <h1 class="display-6 mt-3 mb-3">
                    <c:choose>
                        <c:when test="${packageItem.id == 0}">Create a polished new wedding package.</c:when>
                        <c:otherwise>Refine your package details with confidence.</c:otherwise>
                    </c:choose>
                </h1>
                <p>Build a package that feels clear, premium, and easy to explain during your demonstration, from pricing and tier selection to venue or event linking.</p>
            </div>
            <a class="btn btn-secondary btn-lg" href="${pageContext.request.contextPath}/packages">Back to list</a>
        </div>
        <div class="hero-metrics">
            <div class="metric-card">
                <strong>Tiered</strong>
                <span>Basic, Premium, Luxury</span>
            </div>
            <div class="metric-card">
                <strong>Validated</strong>
                <span>Required fields and pricing checks</span>
            </div>
            <div class="metric-card">
                <strong>Linked</strong>
                <span>Attach to events or venues</span>
            </div>
        </div>
    </section>

    <%@ include file="partials/messages.jspf" %>
    <c:if test="${not empty loadWarning}">
        <div class="alert alert-info alert-dismissible fade show" role="alert">
            <strong>Note:</strong> ${loadWarning}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="row g-4">
        <div class="col-xl-8">
            <div class="card detail-panel">
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/packages/save" method="post" class="row g-4 needs-validation" novalidate>
                        <input type="hidden" name="id" value="${packageItem.id}" />

                        <div class="col-lg-6">
                            <label for="name" class="form-label">Package Name</label>
                            <input type="text" class="form-control" id="name" name="name" value="${packageItem.name}" required maxlength="120" placeholder="e.g. Celebration Package" />
                            <div class="form-text">Enter a unique package name for easy organizer recognition.</div>
                            <div class="invalid-feedback">Please enter a package name.</div>
                        </div>

                        <div class="col-lg-6">
                            <label for="tier" class="form-label">Package Tier</label>
                            <select id="tier" name="tier" class="form-select" required>
                                <option value="">Select a tier</option>
                                <option value="BASIC" <c:if test="${packageItem.tier == 'BASIC'}">selected</c:if>>Basic</option>
                                <option value="PREMIUM" <c:if test="${packageItem.tier == 'PREMIUM'}">selected</c:if>>Premium</option>
                                <option value="LUXURY" <c:if test="${packageItem.tier == 'LUXURY'}">selected</c:if>>Luxury</option>
                            </select>
                            <div class="form-text">Choose the tier that best matches the service level.</div>
                            <div class="invalid-feedback">Please select a tier.</div>
                        </div>

                        <div class="col-12">
                            <label for="description" class="form-label">Description</label>
                            <textarea id="description" name="description" class="form-control" rows="5" required placeholder="Enter package description">${packageItem.description}</textarea>
                            <div class="form-text">Provide a concise, customer-facing description.</div>
                            <div class="invalid-feedback">A description is required.</div>
                        </div>

                        <div class="col-lg-4">
                            <label for="price" class="form-label">Price</label>
                            <input type="number" min="1" step="0.01" class="form-control" id="price" name="price" value="${packageItem.price}" required placeholder="0.00" />
                            <div class="form-text">Enter the package price in dollars.</div>
                            <div class="invalid-feedback">Please enter a valid positive price.</div>
                        </div>

                        <div class="col-lg-4">
                            <label for="status" class="form-label">Status</label>
                            <select id="status" name="status" class="form-select" required>
                                <option value="">Select status</option>
                                <option value="ACTIVE" <c:if test="${packageItem.status == 'ACTIVE'}">selected</c:if>>Active</option>
                                <option value="DRAFT" <c:if test="${packageItem.status == 'DRAFT'}">selected</c:if>>Draft</option>
                                <option value="ARCHIVED" <c:if test="${packageItem.status == 'ARCHIVED'}">selected</c:if>>Archived</option>
                            </select>
                            <div class="form-text">Only active packages are shown to event planners.</div>
                            <div class="invalid-feedback">Please select a status.</div>
                        </div>

                        <div class="col-lg-4">
                            <label for="inclusions" class="form-label">Inclusions</label>
                            <input type="text" class="form-control" id="inclusions" name="inclusions" value="${packageItem.inclusions}" required placeholder="e.g. photography, catering" />
                            <div class="form-text">Describe key services included in the package.</div>
                            <div class="invalid-feedback">Please list the package inclusions.</div>
                        </div>

                        <div class="col-md-6">
                            <label for="linkedEventId" class="form-label">Linked Event</label>
                            <select id="linkedEventId" name="linkedEventId" class="form-select">
                                <option value="">None</option>
                                <c:forEach items="${events}" var="event">
                                    <option value="${event.id}" <c:if test="${event.id == packageItem.linkedEventId}">selected</c:if>>${event.name}</option>
                                </c:forEach>
                            </select>
                            <div class="form-text">Choose an event to connect this package with.</div>
                            <c:if test="${empty events}">
                                <div class="form-text text-danger">No event records available. Create an event first to link it.</div>
                            </c:if>
                        </div>

                        <div class="col-md-6">
                            <label for="linkedVenueId" class="form-label">Linked Venue</label>
                            <select id="linkedVenueId" name="linkedVenueId" class="form-select">
                                <option value="">None</option>
                                <c:forEach items="${venues}" var="venue">
                                    <option value="${venue.id}" <c:if test="${venue.id == packageItem.linkedVenueId}">selected</c:if>>${venue.name}</option>
                                </c:forEach>
                            </select>
                            <div class="form-text">Choose a venue to connect this package with.</div>
                            <c:if test="${empty venues}">
                                <div class="form-text text-danger">No venue records available. Create a venue first to link it.</div>
                            </c:if>
                        </div>

                        <div class="col-12 text-end">
                            <button type="submit" class="btn btn-primary btn-lg">Save Package</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-xl-4">
            <div class="section-card h-100">
                <span class="eyebrow">Design Checklist</span>
                <h2 class="h4 mt-3 mb-3">Keep the package demo-ready.</h2>
                <ul class="list-checks">
                    <li>Use a distinctive package name that is easy to recognize during the demo.</li>
                    <li>Match the tier to the expected service experience and pricing level.</li>
                    <li>Choose either an event or a venue to keep links clean and understandable.</li>
                    <li>Write inclusions in customer-friendly wording so the interface feels polished.</li>
                </ul>
                <div class="soft-rule"></div>
                <div class="detail-stat-grid">
                    <div class="detail-stat">
                        <strong>${fn:length(events)}</strong>
                        <span>Events available</span>
                    </div>
                    <div class="detail-stat">
                        <strong>${fn:length(venues)}</strong>
                        <span>Venues available</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="partials/footer.jspf" %>
</div>
</body>
</html>
