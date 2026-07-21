# ScanAngular

Angular frontend for the Scan Manager API (Spring Boot backend).

## Commands

```bash
# Install dependencies
cd frontend/scanAngular
npm install

# Start development server
ng serve
# App available at http://localhost:4200

# Build for production
ng build

# Run unit tests
ng test
```

**Prerequisites:** Node.js 18+, Angular CLI (`npm install -g @angular/cli`)

## Backend URL Configuration

The backend API base URL is configured in:

```
src/app/environments/environment.ts
```

Default: `http://localhost:9090`

Update the `apiBaseUrl` property to point to your backend instance. The backend must have CORS enabled (it does by default with `@CrossOrigin("*")`).

## Implemented Screens

| Screen | Route | Backend Endpoints |
|--------|-------|-------------------|
| **Home (Dashboard)** | `/` | `GET /scan` - Shows total count, quick nav, recent scans |
| **All Scans** | `/scans` | `GET /scan` - Lists all active scans |
| **Add Scan** | `/scans/add` | `POST /scan` - Creates a new scan record |
| **Scan Details** | `/scans/:id` | `GET /scan/{id}` - View a single scan |
| **Search Scans** | `/scans/search` | `GET /scan/search/{domainName}?orderBy=` - Search by domain with sorting |
| **Health** | `/health` | `GET /health`, `GET /ready` - Backend health/readiness checks |
| **Delete** (from list) | — | `DELETE /scan/{id}` - Soft-deletes a scan |
| **Not Found** | `/**` | Friendly 404 page for unknown routes |

## Features

- Bootstrap 5 responsive UI
- Standalone component architecture (no NgModules)
- Angular Signals for local state management (loading, error, success)
- Reusable shared components (Navbar, Footer, Loading, Empty State)
- Form validation with user-friendly error messages
- Loading indicators during API calls
- Success/error feedback alerts
- HTTP logging interceptor (logs all requests/responses to browser console)
- HTTP error interceptor (converts backend errors to user-friendly messages)
- Consistent error handling for 4xx/5xx and connection errors
- Confirm dialog before deletion
- Auto-refresh list after deletion
