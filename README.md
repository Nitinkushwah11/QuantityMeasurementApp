# QuantityMeasurementApp

A scalable, generic measurement system built using Java Generics and interfaces.
Supports Length, Weight, Volume, and Temperature with unit conversion, equality comparison, and selective arithmetic operations.
Designed using SOLID principles, DRY architecture, functional interfaces, and type-safe generics for extensibility and maintainability.

---
# UC1-FeetEquality

## Features

- Immutable `Feet` class  
- Factory method `fromString()`  
- Custom exception `InvalidFeetException`  
- Proper `equals()` and `hashCode()` implementation  
- Unit testing using JUnit 5  

## Test Cases Covered

- Same value comparison  
- Different value comparison  
- Null comparison  
- Different type comparison  
- Valid string input  
- Invalid string input (Exception case)  

---

## Technologies Used

- Java  
- JUnit 5

🔗 *Code Link:*  
👉 [UC-1](https://github.com/Nitinkushwah11/QuantityMeasurementApp/tree/feature/UC1-FeetEquality)

---

# UC2 - Equality Comparison

This use case implements equality comparison for:

- Feet
- Inches

Objects are created using:
Feet.fromString("1.0");
Inches.fromString("1.0");

Invalid numeric input throws a custom exception.

## Equality Rules
- Same value → true
- Different value → false
- Null → false
- Same reference → true
- Different class → false

## Tech
Java, JUnit 5

🔗 *Code Link:*  
👉 [UC-2](https://github.com/Nitinkushwah11/QuantityMeasurementApp/tree/feature/UC2-InchEquality)

---

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

🔗 *Code Link:*  
👉 [UC-3](https://github.com/Nitinkushwah11/QuantityMeasurementApp/tree/feature/UC3-GenericLength)

---

# Quantity Measurement – UC4

## Supported Units

* Feet
* Inch
* Yard
* Centimeter

## Features

* Takes input from user
* Converts units internally
* Checks if two values are equal
* Supports cross-unit comparison

## Example

1 Feet = 12 Inch
3 Feet = 1 Yard
2.54 Centimeter = 1 Inch


🔗 *Code Link:*  
👉 [UC-4](https://github.com/Nitinkushwah11/QuantityMeasurementApp/tree/feature/UC4-YardEquality)

---

# UC5 – Unit-to-Unit Conversion

UC5 adds direct conversion between length units using a common base unit.

## Supported Units

FEET, INCHES, YARDS, CENTIMETERS

## Features

* Static `convert(value, source, target)` method
* Instance `convertTo()` method
* Base unit normalization
* Input validation (null, NaN, infinite)
* Immutable value object

## Formula

```
result = value × (source.factor / target.factor)
```

## Example

* 1 FEET → INCHES = 12
* 3 YARDS → FEET = 9
* 36 INCHES → YARDS = 1

🔗 *Code Link:*  
👉 [UC-5](https://github.com/Nitinkushwah11/QuantityMeasurementApp/tree/feature/UC5-UnitConvertion)

---

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

🔗 *Code Link:*  
👉 [UC-6](https://github.com/Nitinkushwah11/QuantityMeasurementApp/tree/feature/UC6-UnitAddition)

---

# Quantity Measurement – UC7

## Description

This project performs **length addition and unit conversion** using OOP principles in Java.

## Supported Units

* FEET
* INCHES
* YARDS
* CENTIMETERS

## Features

* Add two quantities
* Automatic unit conversion
* Result in chosen target unit
* Equality comparison using base unit (FEET)
* Immutable design
* Input validation

## Example

```
1 FEET + 12 INCHES

Output:
2.0 feet
24.0 inches
0.6666666666666666 yards
```

## Design

* Base unit: FEET
* Uses conversion factors
* EPSILON-based equality comparison

🔗 *Code Link:*  
👉 [UC-7](https://github.com/Nitinkushwah11/QuantityMeasurementApp/tree/feature/UC7-TargetUnitAddition)

---

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

  🔗 *Code Link:*  
👉 [UC-8](https://github.com/Nitinkushwah11/QuantityMeasurementApp/tree/feature/UC8-StandaloneUnit)

---

# UC9 – Weight Measurement

## Description
UC9 extends the Quantity Measurement Application to support **Weight Measurements** alongside Length.

Supported Units:
- **KILOGRAM (kg)** – Base unit
- **GRAM (g)** – 1 g = 0.001 kg
- **POUND (lb)** – 1 lb = 0.453592 kg

## Features
- Equality comparison (cross-unit supported)
- Unit conversion
- Addition (implicit & explicit target unit)
- Immutable design
- Type-safe (Weight ≠ Length)
- Uses epsilon (1e-6) for floating-point precision

## Conclusion
UC9 validates scalable architecture by supporting multiple measurement categories without affecting existing length functionality.

  🔗 *Code Link:*  
👉 [UC-9](https://github.com/Nitinkushwah11/QuantityMeasurementApp/tree/feature/UC9-WeightMeasurement)

---

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

  🔗 *Code Link:*  
👉 [UC-10](https://github.com/Nitinkushwah11/QuantityMeasurementApp/tree/feature/UC10-GenericQuantity)


---
