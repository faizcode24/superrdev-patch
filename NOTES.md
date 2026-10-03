# NOTES

## Summary of changes
Detailed explanations are in `handwritten/`.

**Backend / SQL**
- Fixed `AND`/`OR` precedence in the search query. Archived tasks leaked into results, the status filter was bypassed for title matches, and the total was wrong. Same fix in `db/queries/search_tasks.sql` and the Oracle package (count and cursor).
- Removed a `Thread.sleep` of up to 1s that slowed short searches.
- Invalid `status`, `page` or `pageSize` now returns 400 instead of 500 (`pageSize` capped at 100).
- Moved pagination into SQL (`LIMIT/OFFSET` plus a count query).
- Escaped `%` and `_` so they match literally.

**Frontend**
- Fixed a race condition where stale responses overwrote newer ones.
- Fixed loading and error state: loading no longer sticks after an error, and errors clear on retry.
- Reset to page 1 when search or status changes.
- Added a 300ms search debounce.

## What I chose not to change
- Task model, schema and seed data.
- No new features, auth, or UI redesign, to keep the diff focused.
- Search still uses `LIKE '%term%'`. Full-text search would be a larger change.
- Oracle pagination keeps the pre-12c ROWNUM pattern. I only fixed the logic bug.

## Biggest remaining risk
There are no automated tests, so regressions like the precedence bug would go unnoticed. `LIKE '%term%'` also cannot use an index and will be slow on large tables.

## Assumptions
- Max page size of 100 is reasonable.
- 400 is the right response for bad input.
- Archived tasks should never appear in search.

## Tools used
I used Claude to help review the code and draft the fixes. I checked each change against the code myself and wrote the handwritten explanations on my own. The frontend builds. [Edit this line: say whether you ran the backend and which manual checks you did.]
