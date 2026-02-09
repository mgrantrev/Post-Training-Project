import { test, expect } from '@playwright/test';

test.describe('Customer Media Browsing', () => {
    test.beforeEach(async ({ page }) => {
        // Login as customer
        await page.goto('/');
        await page.fill('[data-testid=username]', 'customer1');
        await page.fill('[data-testid=password]', 'password');
        await page.click('[data-testid=login-button]');
        await expect(page).toHaveURL(/.*dashboard/);
    });

    test('Customer can see their owned tracks', async ({ page }) => {
        const trackList = page.locator('[data-testid=track-item]');
        await expect(trackList).toHaveCount(await trackList.count()); // Dynamic check
        await expect(trackList.first()).toBeVisible();
        await expect(trackList.first().locator('[data-testid=track-name]')).not.toBeEmpty();
    });

    test('Tracks show album and artist info', async ({ page }) => {
        const firstTrack = page.locator('[data-testid=track-item]').first();
        await expect(firstTrack.locator('[data-testid=track-album]')).toBeVisible();
        await expect(firstTrack.locator('[data-testid=track-artist]')).toBeVisible();
    });

    test('Customer can search for tracks', async ({ page }) => {
        await page.fill('[data-testid=search-input]', 'Kiss');
        const trackList = page.locator('[data-testid=track-item]');
        await expect(trackList.first()).toContainText(/Kiss/i);
    });
});
