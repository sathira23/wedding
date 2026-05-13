<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.wedding.model.User" %>
<jsp:useBean id="user" type="com.wedding.model.User" scope="session" />
<%
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Wedding Package Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/assets/css/app.css" rel="stylesheet" />
</head>
<body class="wedding-body">
<div class="app-shell">
    <%@ include file="/WEB-INF/views/partials/header.jspf" %>
    <div class="container page-frame">
        <div class="dashboard-grid mt-4">
            <section class="dashboard-panel">
                <span class="eyebrow">Dashboard</span>
                <div class="d-flex flex-column flex-md-row align-items-start gap-3 mt-3 mb-3">
                    <img src="${pageContext.request.contextPath}/assets/images/brand-crest.svg" alt="Wedding Package Management logo" class="brand-crest brand-crest--hero" />
                    <div>
                        <h1 class="display-6 mb-2">Create memorable wedding packages with a premium presentation.</h1>
                        <p class="section-subtitle mb-0">Use this workspace to guide clients through elegant package options, refined pricing, venue connections, and clean organizer workflows.</p>
                    </div>
                </div>
                <div class="dashboard-actions mb-4">
                    <a href="${pageContext.request.contextPath}/packages" class="btn btn-primary btn-lg">Open Package Studio</a>
                    <a href="${pageContext.request.contextPath}/packages/form" class="btn btn-secondary btn-lg">Create New Package</a>
                </div>
                <div class="hero-metrics">
                    <div class="metric-card">
                        <strong><%= user.getRole() %></strong>
                        <span>Current access role</span>
                    </div>
                    <div class="metric-card">
                        <strong>3</strong>
                        <span>Package tiers ready</span>
                    </div>
                    <div class="metric-card">
                        <strong>CRUD</strong>
                        <span>Full management flow enabled</span>
                    </div>
                </div>
            </section>

            <aside class="section-card">
                <span class="eyebrow">Quick Guide</span>
                <h2 class="h4 mt-3 mb-3">What you can do now</h2>
                <ul class="list-checks">
                    <li>Create beautiful package offers for Basic, Premium, and Luxury tiers.</li>
                    <li>Link packages to venues or events for clear coordination.</li>
                    <li>Use organizer and admin flows during the system demonstration.</li>
                    <li>Show strong input validation and stable CRUD behavior to score marks.</li>
                </ul>
                <div class="soft-rule"></div>
                <p class="muted-copy mb-0">Signed in as <strong><%= user.getFullName() %></strong>. Use the package studio for the main demonstration flow.</p>
            </aside>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
