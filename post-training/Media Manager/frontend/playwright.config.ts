import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
    testDir: './e2e',
    fullyParallel: false,
    forbidOnly: !!process.env.CI,
    retries: process.env.CI ? 2 : 0,
    workers: 1,
    reporter: 'html',
    timeout: 30000,
    use: {
        baseURL: 'http://localhost:4200',
        trace: 'on-first-retry',
    },
    webServer: {
        command: 'cd ../backend && ./gradlew bootRun --args="--spring.profiles.active=h2"',
        url: 'http://localhost:8080/api/auth/login',
        reuseExistingServer: true,
        timeout: 60000,
    },
    projects: [
        {
            name: 'chromium',
            use: { ...devices['Desktop Chrome'] },
        },
    ],
});
