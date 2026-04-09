# Traveller Unit Converter App

## Project Overview
The Traveller Unit Converter is a mobile application developed using Kotlin and Jetpack Compose.  
It provides a simple and user-friendly interface for performing common unit conversions required during travel.

The app supports multiple categories such as Currency, Fuel/Distance/Liquid, and Temperature, with built-in validation and real-time conversion results.

---

## Features
- Convert between multiple unit categories:
  - Currency (USD, AUD, EUR, JPY, GBP)
  - Fuel, Distance, and Liquid units
  - Temperature (Celsius, Fahrenheit, Kelvin)
- Input validation (handles empty, invalid, and negative values)
- Clean and modern UI using Jetpack Compose
- Real-time conversion results display
- Consistent colour theme across all categories

---

## Technologies Used
- Kotlin
- Jetpack Compose
- Android Studio

---

## Project Structure
- MainActivity / ConverterHomeActivity  
- UI Components:
  - JourneyConverterScreen  
  - TopBanner  
  - InputSection  
  - ResultSection  
  - InfoSection  
- Utility Functions:
  - performConversion()  
  - exchangeCurrency()  
  - convertTravelMeasure()  
  - convertHeatValue()  

---

## How It Works
1. Select a category (Currency, Fuel/Distance/Liquid, Temperature)  
2. Choose source unit and destination unit  
3. Enter a numeric value  
4. Click "Convert Now"  
5. The result is displayed instantly  

---

## Validation Rules
- Empty input is not allowed  
- Only numeric values are accepted  
- Negative values are restricted for certain categories  
- Same unit selection returns the same value  

---

## Author
Binul Bijo

---

## 📄 License
This project is developed for academic purposes.
