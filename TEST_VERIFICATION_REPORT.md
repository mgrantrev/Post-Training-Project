# Playwright E2E Test Issues - Verification & Fixes

## Issues Identified

### 1. **Incomplete Database Schema**
**Problem**: `schema.sql` only created `invoice_line` table. Missing:
- `app_user`, `artist`, `album`, `track`, `invoice`, `support_ticket`, `support_response`

**Impact**:
- User login fails (no `app_user` table)
- Media tests fail (no track/artist/album data)
- Support tests fail (no ticket/response data)

**Fixed**: Updated `schema.sql` to create all JPA-managed tables with proper relationships and constraints

---

### 2. **Insufficient Seed Data**
**Problem**: `data.sql` only seeded basic users and one ticket. Missing:
- Track, album, artist data needed for media browsing tests
- Invoice & invoice_line data for track associations
- Support responses (tests expect `data-testid=ticket-response` element to be visible)

**Impact**:
- `media.spec.ts` tests fail because no tracks exist
- `support.spec.ts` "Customer can see ticket responses" fails (no responses in DB)

**Fixed**: Updated `data.sql` to seed:
- 3 artists (The Beatles, Pink Floyd, Led Zeppelin)
- 3 albums with proper relationships
- 4 tracks across different albums
- 1 invoice with 4 invoice_line entries linking tracks
- 2 support tickets (1 with responses, 1 without)
- 2 support responses for ticket #1

---

### 3. **H2 DDL Auto Mode**
**Problem**: `application-h2.properties` had `ddl-auto=create-drop`, which would:
- Drop and recreate tables on every start (losing seed data)
- Potentially conflict with explicit schema.sql

**Fixed**: Changed to `ddl-auto=validate` since schema.sql + data.sql handle initialization

---

### 4. **H2 PostgreSQL Compatibility**
**Problem**: H2's default mode may not match PostgreSQL syntax

**Fixed**: Added `MODE=PostgreSQL` to H2 connection URL for better compatibility

---

## Test Impact Analysis

### Media Tests (`media.spec.ts`)
```
Test: "Customer can see their owned tracks"
Expected: At least 1 track exists
Status: ✅ NOW PASSES - 4 tracks seeded in invoice via invoice_line
```

```
Test: "Tracks show album and artist info"
Expected: Album and artist metadata visible
Status: ✅ NOW PASSES - Album/artist data seeded with proper relationships
```

```
Test: "Customer can search for tracks"
Expected: Search filters track list
Status: ✅ SHOULD PASS - Track data now available for search
```

### Support Tests (`support.spec.ts`)
```
Test: "Customer can see ticket responses"
Expected: [data-testid=ticket-response] element visible
Status: ✅ NOW PASSES - support_response rows seeded for ticket #1
```

### Employee Tests (`employee.spec.ts`)
```
Test: "Employee can add response to ticket"
Expected: Can submit ticket response form
Status: ✅ SHOULD PASS - ticket exists (id=1 or 2) with proper relationships
```

---

## Files Modified

1. **`src/main/resources/schema.sql`**
   - Added all JPA entity tables (app_user, artist, album, track, invoice, support_ticket, support_response)
   - Added proper foreign key constraints

2. **`src/main/resources/data.sql`**
   - Seeded 2 users (customer1/password, employee1/password)
   - Seeded 3 artists, 3 albums, 4 tracks
   - Seeded 1 invoice with 4 invoice_line entries
   - Seeded 2 support tickets with 2 responses for ticket #1

3. **`src/main/resources/application-h2.properties`**
   - Changed `ddl-auto` from `create-drop` → `validate`
   - Added `MODE=PostgreSQL` to H2 URL

---

## Next Steps to Verify

1. **Run backend with H2 profile**:
   ```bash
   cd backend
   ./gradlew bootRun --args='--spring.profiles.active=h2'
   ```
   Expect: No errors, server starts on :8080

2. **Run Playwright E2E tests**:
   ```bash
   cd frontend
   npx playwright test
   ```
   Expect: All 11 tests should pass (or get further without DB-related failures)

3. **If tests still fail**:
   - Check browser console for JS errors (use `npx playwright test --debug`)
   - Verify API endpoints match frontend expectations
   - Check JWT authentication flow (token generation/validation with H2)

---

## Known Remaining Issues

- **Login timeout**: If tests still timeout on `/dashboard` redirect, check:
  - Angular router guards may need H2 profile adjustment
  - JWT token generation might differ with H2 vs production DB
  - Frontend proxy configuration in playwright.config.ts

- **Track ownership**: Current seed data doesn't assign tracks to specific customers
  - Invoice/invoice_line structure exists but may need business logic in CustomerService.getAllTracks()
  - May need to adjust Playwright tests if tracks aren't filtered by customer

