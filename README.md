# Fitness App
Simple mobile app as a team programming project.

## How to run the project
1. Just clone the repository and open it in Android Studio.

## Code style
- https://developer.android.com/kotlin/style-guide

## Project structure
The project uses a mix of common practices used in Android development (clean architecture).
The goal was to create something that is not too "abstraction-heavy" and burdensome but
makes it easier to navigate the codebase and understand the project.
The structure is meant to be a guideline and can be adjusted to fit the needs of the project.

### Package structure
- Each root package represents a feature (e.g. a home screen, exercises)
- Each feature is split into 3 subpackages
  - ui - User interface. It does not contain any business logic. It just displays data and handles user input.
  - data - Source of data (e.g. database, network), business logic

![clean architecture](https://developer.android.com/static/topic/libraries/architecture/images/mad-arch-overview.png)
Read more about the structure [here](https://developer.android.com/topic/architecture).