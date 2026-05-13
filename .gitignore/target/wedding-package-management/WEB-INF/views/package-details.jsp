<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.wedding.model.User" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="user" type="com.wedding.model.User" scope="session" />
<jsp:useBean id="packageItem" type="com.wedding.model.WeddingPackage" scope="request" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Package Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/assets/css/app.css" rel="stylesheet" />
</head>
<body class="wedding-body">
<div class="app-shell">
<%@ include file="partials/header.jspf" %>
<div class="container page-frame">
    <section class="page-hero mb-4">
        <div class="d-flex flex-column flex-lg-row justify-content-between align-items-start gap-4">
            <div class="detail-heading hero-text">
                <span class="eyebrow">Package Details</span>
                <h1 class="display-6 mt-3 mb-3">${packageItem.name}</h1>
                <p>Review the full presentation of this wedding package, including its tier, linking, pricing, and organizer ownership before demonstrating edits or updates.</p>
            </div>
            <div class="action-row">
                <a href="${pageContext.request.contextPath}/packages/form?id=${packageItem.id}" class="btn btn-primary btn-lg">Edit Package</a>
                <a href="${pageContext.request.contextPath}/packages" class="btn btn-secondary btn-lg">Back to list</a>
            </div>
        </div>
        <div class="detail-stat-grid">
            <div class="detail-stat">
                <strong>${packageItem.tier}</strong>
                <span>Selected service tier</span>
            </div>
            <div class="detail-stat">
                <strong>${packageItem.status}</strong>
                <span>Current package status</span>
            </div>
            <div class="detail-stat">
                <strong>$${packageItem.price}</strong>
                <span>Displayed package price</span>
            </div>
        </div>
    </section>

    <div class="detail-grid">
        <div class="detail-panel">
            <div class="description-panel">
                <h2 class="h4 mb-3">Overview</h2>
                <p class="text-muted mb-4">${packageItem.description}</p>

                <dl class="row mb-0">
                    <dt class="col-sm-4 text-muted">Tier</dt>
                    <dd class="col-sm-8"><span class="badge rounded-pill text-white badge-tier-${packageItem.tier}">${packageItem.tier}</span></dd>

                    <dt class="col-sm-4 text-muted">Status</dt>
                    <dd class="col-sm-8"><span class="badge bg-${packageItem.status == 'ACTIVE' ? 'success' : packageItem.status == 'DRAFT' ? 'secondary' : 'warning'} text-uppercase">${packageItem.status}</span></dd>

                    <dt class="col-sm-4 text-muted">Price</dt>
                    <dd class="col-sm-8 fw-semibold">$${packageItem.price}</dd>

                    <dt class="col-sm-4 text-muted">Organizer</dt>
                    <dd class="col-sm-8">Organizer #${packageItem.organizerId}</dd>

                    <dt class="col-sm-4 text-muted">Linked Event</dt>
                    <dd class="col-sm-8">
                        <c:choose>
                            <c:when test="${not empty packageItem.linkedEventName}">${packageItem.linkedEventName}</c:when>
                            <c:otherwise>None</c:otherwise>
                        </c:choose>
                    </dd>

                    <dt class="col-sm-4 text-muted">Linked Venue</dt>
                    <dd class="col-sm-8">
                        <c:choose>
                            <c:when test="${not empty packageItem.linkedVenueName}">${packageItem.linkedVenueName}</c:when>
                            <c:otherwise>None</c:otherwise>
                        </c:choose>
                    </dd>

                    <dt class="col-sm-4 text-muted">Inclusions</dt>
                    <dd class="col-sm-8">${packageItem.inclusions}</dd>
                </dl>
            </div>
        </div>
        <aside class="section-card">
            <span class="eyebrow">Package Snapshot</span>
            <h2 class="h4 mt-3 mb-3">Useful talking points</h2>
            <ul class="list-checks">
                <li>Use this page to explain how tier, venue, and event links appear together.</li>
                <li>Point out the timestamps to show update tracking during the demo.</li>
                <li>Use the Edit button to transition smoothly into the update flow.</li>
            </ul>
            <div class="soft-rule"></div>
            <div class="detail-panel-soft">
                <h3 class="h6 text-uppercase text-secondary mb-3">Timestamps</h3>
                <p class="mb-3"><strong>Created:</strong><br />${packageItem.createdAt}</p>
                <p class="mb-0"><strong>Updated:</strong><br />${packageItem.updatedAt}</p>
            </div>
        </aside>
    </div>
</div>
<%@ include file="partials/footer.jspf" %>
</div>
</body>
</html>
