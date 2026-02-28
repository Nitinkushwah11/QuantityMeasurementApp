# UC10 – Generic Quantity

## Description
UC10 refactors UC9 into a single generic class:

`Quantity<U extends IMeasurable>`

This removes duplication and makes the system scalable and maintainable.

---

## Improvements
- Single generic `Quantity` class
- Common `IMeasurable` interface
- Supports multiple categories (Length, Weight)
- No duplicate logic (DRY)
- Type-safe and immutable

---

## Features
- Equality comparison
- Unit conversion
- Addition (with optional target unit)
- Prevents cross-category comparison

---
