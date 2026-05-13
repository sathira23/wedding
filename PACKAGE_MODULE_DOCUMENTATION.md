# Wedding Package Management Module

## 1. Design patterns used

- MVC (Model-View-Controller)
  - Controller: servlets such as `PackageServlet`, `PackageFormServlet`, `PackageSaveServlet`, `PackageDeleteServlet`, and `PackageDetailsServlet` handle HTTP requests, apply authorization, and forward data to views.
  - Model: classes like `WeddingPackage`, `Event`, `Venue`, `User`, and enums such as `PackageTier` and `PackageStatus` represent domain data.
  - View: JSP pages such as `packages.jsp`, `package-form.jsp`, and `package-details.jsp` render UI and use JSTL to display dynamic content.

- DAO (Data Access Object)
  - Interfaces: `PackageDao`, `EventDao`, `VenueDao` define persistence contracts.
  - Implementations: `PackageDaoImpl`, `EventDaoImpl`, `VenueDaoImpl` encapsulate SQL statements and JDBC access.
  - Purpose: decouples SQL persistence from business logic and makes testing/replacement easier.

- Service layer
  - `PackageService` contains business rules, validation orchestration, and linking logic.
  - Purpose: protects controllers from low-level data access details and keeps package-specific rules in one place.

## 2. Validation rules

- Package name is required, trimmed, and limited to 120 characters.
- Description must be provided.
- Tier must be one of the supported enum values: `BASIC`, `PREMIUM`, `LUXURY`.
- Price must be a valid number greater than zero.
- Inclusions must be provided.
- Status must be one of the supported enum values: `ACTIVE`, `DRAFT`, `ARCHIVED`.
- Package must link to either an event or a venue, not both.
- Selected event or venue IDs must exist in the database when provided.
- Package names must be unique across records, with existing package updates exempting the current record.
- Users can only modify packages they own, unless they have `ADMIN` access.

## 3. Database relationships

- `wedding_package.linked_event_id` is an optional foreign key to `event.id`.
- `wedding_package.linked_venue_id` is an optional foreign key to `venue.id`.
- `wedding_package.organizer_id` is a foreign key to the user or organizer owning the package.
- The package module uses `LEFT JOIN` to safely retrieve linked event and venue names for display.
- The relationship is one-to-many from event/venue to packages: each package can link to one event or one venue.

## 4. Likely viva questions and answers

**Q: What patterns are you using in this module?**
A: This module uses MVC with servlets as controllers, JSPs as views, and domain classes as models. It also uses DAO for database access and a service layer for business validation and transaction coordination.

**Q: Why do we need a service layer separate from DAO?**
A: The service layer contains business rules, validation, and security checks, while DAO focuses only on persistence. This separation makes the code easier to maintain and test.

**Q: How does package linking work?**
A: A package can link to either an event or a venue. The service layer validates the selected ID and DAO queries the referenced event or venue to ensure it exists before saving.

**Q: How do you handle invalid IDs and null values?**
A: The validation util safely parses numeric IDs and returns null for invalid values. The service then rejects invalid linked target IDs with clear error messages.

**Q: What happens if a linked event or venue is removed from the database?**
A: The DAO uses left joins for display so the package still loads safely. When saving, the service ensures only valid existing IDs are allowed, preventing broken references.

**Q: How do you manage database errors?**
A: Low-level SQL `SQLException`s are wrapped into a `DataAccessException`, and the service logs the error before surface-level controllers display a user-friendly message.

**Q: How is the package update process protected from unauthorized changes?**
A: The service checks package ownership, and controllers reject edits when the current user is not the organizer and not an admin.

**Q: How is reuse achieved in validation?**
A: `ValidationUtil` contains reusable methods such as `isEmpty`, `parseInt`, and `parsePositiveBigDecimal`, plus form-specific validation for package fields.
