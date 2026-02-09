import { test, expect } from '@playwright/test';

test('Debug: API login directly', async ({ request }) => {
    const response = await request.post('http://localhost:8080/api/auth/login', {
        data: { username: 'customer1', password: 'password' },
    });
    console.log('Status:', response.status());
    console.log('Body:', await response.text());
    expect(response.status()).toBe(200);
});

test('Debug: API login via proxy', async ({ request }) => {
    const response = await request.post('/api/auth/login', {
        data: { username: 'customer1', password: 'password' },
    });
    console.log('Proxy Status:', response.status());
    console.log('Proxy Body:', await response.text());
    expect(response.status()).toBe(200);
});
