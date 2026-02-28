# UC13 – Centralized Arithmetic (Refactored)

## Description
UC13 refactors `Quantity<U extends IMeasurable>` to centralize arithmetic logic using enum-based dispatch and DRY principles.

## Features
- Generic Quantity class
- Centralized validation
- Centralized base conversion
- ADD, SUBTRACT → return Quantity
- DIVIDE → returns double
- Cross-category protection
- Division by zero handling
- Immutable design

## Example Output
9.50 FEET  
4.50 LITRE  
5.0  
0.9999999999999998  
Error: Division by zero  

## Conclusion
UC13 improves maintainability, removes duplication, and keeps full backward compatibility.
