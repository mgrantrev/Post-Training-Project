import { test, expect } from '@playwright/test';

test.describe('Employee Management', () => {
    test.beforeEach(async ({ page }) => {
        // Login as employee
        await page.goto('/');
        await page.fill('[data-testid=username]', 'employee1');
        await page.fill('[data-testid=password]', 'password');
        await page.click('[data-testid=login-button]');
        await expect(page).toHaveURL(/.*dashboard/);
    });

    test('Employee can see sales metrics', async ({ page }) => {
        await expect(page.locator('[data-testid=sales-metrics-view]')).toBeVisible();
        await expect(page.locator('[data-testid=sales-item]')).toBeVisible();
    });

    test('Employee can manage support tickets', async ({ page }) => {
        await page.click('[data-testid=manage-tickets-button]');
        await expect(page.locator('[data-testid=employee-ticket-list]')).toBeVisible();

        const firstTicket = page.locator('[data-testid=employee-ticket-item]').first();
        await firstTicket.click();

        await page.fill('[data-testid=ticket-response-input]', 'I am looking into this.');
        await page.click('[data-testid=submit-response-button]');
        await page.click('[data-testid=close-ticket-button]');

        await expect(firstTicket.locator('[data-testid=ticket-status]')).toHaveText('Closed');
    });
});
