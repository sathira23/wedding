<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Login - Wedding Package Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/assets/css/app.css" rel="stylesheet" />
</head>
<body class="auth-body">
<div class="app-shell">
    <div class="container auth-layout">
        <div class="row justify-content-center w-100">
            <div class="col-xl-10">
                <div class="auth-shell">
                    <div class="row g-0">
                        <div class="col-lg-6 d-none d-lg-block">
                            <div class="auth-showcase h-100">
                                <span class="eyebrow">Wedding Package Studio</span>
                                <h1>Shape elegant wedding offers with confidence.</h1>
                                <p>Organize luxury, premium, and basic packages in one stylish dashboard built for planners, admins, and polished demonstrations.</p>
                                <div class="auth-pillars">
                                    <div class="pillar-card">
                                        <strong>Tiered offers</strong>
                                        <span>Present clear pricing structures for every celebration style.</span>
                                    </div>
                                    <div class="pillar-card">
                                        <strong>Venue linking</strong>
                                        <span>Connect packages to venues and events without confusion.</span>
                                    </div>
                                    <div class="pillar-card">
                                        <strong>Live CRUD flow</strong>
                                        <span>Show your viva panel a smooth, stable, real-time management experience.</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-6">
                            <div class="auth-form-panel h-100 d-flex flex-column justify-content-center">
                                <img src="${pageContext.request.contextPath}/assets/images/brand-crest.svg" alt="Wedding Package Management logo" class="brand-badge-logo" />
                                <div class="auth-copy mb-4">
                                    <h2 class="form-card-title">Sign in</h2>
                                    <p>Access your wedding package workspace and continue designing curated event experiences for clients and venues.</p>
                                </div>
                                <form action="${pageContext.request.contextPath}/login" method="post" novalidate>
                                    <div class="mb-3">
                                        <label for="username" class="form-label">Username</label>
                                        <input type="text" class="form-control" id="username" name="username" required />
                                        <div class="invalid-feedback">Username is required.</div>
                                    </div>
                                    <div class="mb-3">
                                        <label for="password" class="form-label">Password</label>
                                        <input type="password" class="form-control" id="password" name="password" required />
                                        <div class="invalid-feedback">Password is required.</div>
                                    </div>
                                    <div class="d-grid mt-4">
                                        <button class="btn btn-primary btn-lg">Enter Dashboard</button>
                                    </div>
                                    <c:if test="${not empty param.error}">
                                        <div class="alert alert-danger mt-3">${param.error}</div>
                                    </c:if>
                                </form>
                                <div class="credential-note mt-4">
                                    <strong>Quick demo access:</strong> use <strong>admin / admin</strong> for full access, or <strong>organizer1 / organizer1</strong> for organizer-only flow.
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    (() => {
        const forms = document.querySelectorAll('form');
        forms.forEach(form => {
            form.addEventListener('submit', event => {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        });
    })();
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
