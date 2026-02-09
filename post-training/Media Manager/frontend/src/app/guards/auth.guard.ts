import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';

export const AuthGuard: CanActivateFn = (route, state) => {
    const router = inject(Router);
    const token = localStorage.getItem('jwt');

    if (token) {
        return true;
    }

    // Redirect to login if not authenticated
    router.navigate(['/login']);
    return false;
};
