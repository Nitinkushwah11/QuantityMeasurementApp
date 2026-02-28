# QuantityMeasurementApp

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
