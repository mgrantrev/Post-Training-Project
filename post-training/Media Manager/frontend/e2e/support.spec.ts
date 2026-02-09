import { test, expect } from '@playwright/test';

test.describe('Customer Support Tickets', () => {
    test.beforeEach(async ({ page }) => {
        // Login as customer
        await page.goto('/');
        await page.fill('[data-testid=username]', 'customer1');
        await page.fill('[data-testid=password]', 'password');
        await page.click('[data-testid=login-button]');
        await expect(page).toHaveURL(/.*dashboard/);
        // Navigate to support page
        await page.click('[data-testid=get-support-button]');
        await expect(page).toHaveURL(/.*support/);
    });

    test('Customer can create a support ticket', async ({ page }) => {
        await page.click('[data-testid=new-ticket-button]');
        await page.fill('[data-testid=ticket-subject]', 'Help with login');
        await page.fill('[data-testid=ticket-description]', 'I cannot login to my account.');
        await page.click('[data-testid=submit-ticket-button]');

        await expect(page.locator('[data-testid=ticket-item]').last()).toContainText('Help with login');
    });

    test('Customer can see ticket responses', async ({ page }) => {
        const firstTicket = page.locator('[data-testid=ticket-item]').first();
        await firstTicket.click();
        await expect(page.locator('[data-testid=ticket-detail-view]')).toBeVisible();
    });
});
