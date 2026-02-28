# UC8 – Standalone LengthUnit Refactoring

## Description

UC8 refactors the design by extracting `LengthUnit` into a standalone enum and assigning it full responsibility for unit conversions.

## Key Improvements

* `LengthUnit` handles all conversion logic
* `Quantity` handles comparison and arithmetic
* Follows Single Responsibility Principle (SRP)
* Removes circular dependency risk
* Improves scalability for future units (Weight, Volume, etc.)
* Fully backward compatible with UC1–UC7

## Supported Units

* FEET
* INCHES
* YARDS
* CENTIMETERS

## Example Output

```
Convert 1 FEET to INCHES:
12.0 inches

Add 1 FEET + 12 INCHES (in FEET):
2.0 feet

Add 1 FEET + 12 INCHES (in YARDS):
0.6666666666666666 yards

36 INCHES equals 1 YARDS?
true
```

## Architecture

* Base Unit: FEET
* Conversion delegated to `LengthUnit`
* Equality uses epsilon comparison
* Immutable design

---
