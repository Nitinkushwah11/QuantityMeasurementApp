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

---
