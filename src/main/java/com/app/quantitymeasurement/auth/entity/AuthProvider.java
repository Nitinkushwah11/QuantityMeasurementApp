package com.app.quantitymeasurement.auth.entity;


/**
 * AuthProvider Enum
 *
 * This enum defines the different authentication providers/methods supported by the application.
 * It helps us track HOW and WHERE a user registered/authenticated with our system.
 *
 * Why we need this:
 * =================
 * 1. Users who registered via Google OAuth do NOT have a password stored in our database.
 *    We need to know this so we don't try to validate/update passwords for Google users.
 *
 * 2. If we mix authentication methods, we need to know which method a user used.
 *    For example, a user who registered with email/password shouldn't automatically
 *    get merged with a Google account having the same email without verification.
 *
 * 3. In the future, we can easily add more OAuth providers (GitHub, Facebook, LinkedIn)
 *    without changing our database schema.
 *
 * 4. Provides clarity in the database about how each user authenticated.
 *
 * Usage Example:
 * ==============
 * User user = new User();
 * user.setProvider(AuthProvider.GOOGLE);  // This user came from Google OAuth
 * user.setProviderId("1234567890");       // Google's unique ID for this user
 */
public enum AuthProvider
{
    LOCAL,
    GOOGLE
}