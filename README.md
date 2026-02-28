# QuantityMeasurementApp
# UC3 – Generic Quantity Class (DRY Principle)

## Description
UC3 refactors separate `Feet` and `Inches` classes into a single generic `Quantity` class using a `LengthUnit` enum.  
This eliminates code duplication and follows the DRY principle.

## How It Works
- User enters value and unit (feet/inch).
- Values are converted to a common base unit (feet).
- Equality is checked using value-based comparison.

## Key Concepts
- DRY Principle
- Enum Usage
- Encapsulation
- Abstraction
- Proper equals() contract
- Cross-unit comparison (1 foot = 12 inches)
