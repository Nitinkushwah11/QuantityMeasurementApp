# UC14 – Temperature with Selective Arithmetic Support

## Description
UC14 adds **Temperature (Celsius, Fahrenheit, Kelvin)** to the Quantity system.

Temperature supports:
- Equality comparison
- Unit conversion

Temperature does NOT support:
- Addition
- Subtraction
- Division

## Key Changes
- Refactored `IMeasurable` using default methods
- Added `SupportsArithmetic` functional interface
- `TemperatureUnit` overrides arithmetic validation
- `Quantity` checks operation support before execution
- Fully backward compatible (UC1–UC13 unchanged)
