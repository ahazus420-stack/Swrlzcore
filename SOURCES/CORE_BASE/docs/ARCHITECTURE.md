# Architecture

## Project goals
- Provide a reusable Android foundation for future SWRLZ applications.
- Keep app layers separated so new products can inherit behavior and UI conventions.
- Favor modularity, clear boundaries, and maintainability over hard-coded implementation details.

## Module relationships
- app hosts the application shell and main activity.
- featurehome demonstrates how a product feature can depend on the core foundation.
- designsystem provides shared Compose primitives.
- core provides domain models and repository abstractions that feature modules can consume.

## Dependency graph
- app -> featurehome -> core
- app -> designsystem -> none
- featurehome -> designsystem -> core

## Clean architecture layers
- Presentation: Compose UI in app and featurehome.
- Domain: data classes and repository contracts in core.
- Data: repository implementations in core, ready to evolve into Room/Retrofit or other backends.

## Future expansion strategy
- Add feature modules for onboarding, settings, and account flows.
- Introduce dependency injection and repository implementations behind interfaces.
- Add instrumentation, tests, and CI automation as the product grows.

## How future SWRLZ applications inherit from this repository
Future SWRLZ applications can clone or fork this repository, keep the same module boundaries, and extend the foundation with product-specific features while reusing the core architecture and design conventions.
