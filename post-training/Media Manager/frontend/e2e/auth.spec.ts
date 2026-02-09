import { test, expect } from '@playwright/test';

test.describe('Authentication', () => {
    test('Customer can login', async ({ page }) => {
        await page.goto('/');
        await page.fill('[data-testid=username]', 'customer1');
        await page.fill('[data-testid=password]', 'password');
        await page.click('[data-testid=login-button]');

        await expect(page).toHaveURL(/.*dashboard/);
        await expect(page.locator('[data-testid=user-role]')).toHaveText('Customer');
    });

    test('Employee can login', async ({ page }) => {
        await page.goto('/');
        await page.fill('[data-testid=username]', 'employee1');
        await page.fill('[data-testid=password]', 'password');
        await page.click('[data-testid=login-button]');

        await expect(page).toHaveURL(/.*dashboard/);
        await expect(page.locator('[data-testid=user-role]')).toHaveText('Employee');
    });

    test('Login failure shows error message', async ({ page }) => {
        await page.goto('/');
        await page.fill('[data-testid=username]', 'wronguser');
        await page.fill('[data-testid=password]', 'wrongpass');
        await page.click('[data-testid=login-button]');

        await expect(page.locator('[data-testid=error-message]')).toBeVisible();
        await expect(page.locator('[data-testid=error-message]')).toContainText('Invalid credentials');
    });

    test('User can logout', async ({ page }) => {
        // Login first
        await page.goto('/');
        await page.fill('[data-testid=username]', 'customer1');
        await page.fill('[data-testid=password]', 'password');
        await page.click('[data-testid=login-button]');

        await expect(page).toHaveURL(/.*dashboard/);

        await page.click('[data-testid=logout-button]');
        await expect(page).toHaveURL(/.*login/);
    });
});
