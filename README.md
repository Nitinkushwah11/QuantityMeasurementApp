# UC6 – Addition of Two Length Units

UC6 extends UC5 by adding support for **addition of two length measurements** (same category).

## Supported Units

FEET, INCHES, YARDS, CENTIMETERS

## Features

* Add two `Length` objects
* Automatic unit conversion before addition
* Result returned in unit of first operand
* Uses base unit normalization (FEET)
* Immutable design (returns new object)
* Input validation for null, NaN, infinite values

## Logic

1. Convert both lengths to base unit (FEET)
2. Add values
3. Convert sum back to first operand’s unit
4. Return new `Length` object

## Example

* 1 FEET + 2 FEET = 3 FEET
* 1 FEET + 12 INCHES = 2 FEET
* 12 INCHES + 1 FEET = 24 INCHES
* 1 YARD + 3 FEET = 2 YARDS

---
