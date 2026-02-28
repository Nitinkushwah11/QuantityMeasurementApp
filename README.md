# UC12 – Subtraction & Division on Quantity

## Description
UC12 extends the generic `Quantity<U extends IMeasurable>` system by adding:

- Subtraction → returns new `Quantity<U>`
- Division → returns dimensionless `double`

No architectural changes required.

## Features
- Cross-unit arithmetic (same category)
- Explicit & implicit target unit
- Cross-category prevention
- Division by zero handling
- Immutability preserved
- Works for Length, Weight, Volume
