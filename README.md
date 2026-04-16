# Java ATM Interface: Enterprise Architecture Milestone

## 📌 Project Overview
[cite_start]This project is a **Digital Twin** of a physical ATM system[cite: 111]. [cite_start]Developed as part of the **DecodeLabs Industrial Training Kit
(Batch 2026)**, it transitions from procedural scripting to a resilient, object-oriented system design[cite: 2, 3, 29].

[cite_start]The system manages financial transactions—including deposits, withdrawals, and balance inquiries—while maintaining absolute data integrity through
strict encapsulation[cite: 7, 9, 217].

## 🏗️ Architectural Principles
Following professional software engineering standards, this project implements:

* [cite_start]**Strict Encapsulation (The Data Vault)**: Account balances are protected using `private` access modifiers and controlled via mutator methods that act as security checkpoints[cite: 217, 227].
* [cite_start]**Separation of Concerns (SoC)**: The logic is decoupled from the user interface, ensuring the `BankAccount` class remains untouched even if the UI scales from a Console to a GUI[cite: 241, 254, 275].
* [cite_start]**Input-Process-Output (IPO) Model**: Every interaction is filtered through a validation gate to prevent system crashes from malformed user input[cite: 153, 173].
* [cite_start]**Resilient Input Validation**: Utilizes "look-ahead" logic (e.g., `hasNextDouble()`) to handle human errors gracefully without throwing exceptions[cite: 184, 185].

## 🚀 Key Features
* [cite_start]**Secure Withdrawals**: Validates withdrawal requests against current balances and prevents negative transactions[cite: 25, 197].
* [cite_start]**Real-time Balance Inquiries**: Provides read-only access to account states via secure Getters[cite: 228].
* [cite_start]**Robust Error Handling**: Implements a "Validation Gate" to filter out invalid data types (e.g., text instead of numbers)[cite: 173, 179].
* [cite_start]**Scalable Design**: Prepared for future extension into State Design Patterns or JavaFX integration[cite: 275, 354].

## 🛠️ Technologies Used
* [cite_start]**Language**: Java (JDK 8+) [cite: 1]
* [cite_start]**Paradigm**: Object-Oriented Programming (OOP) [cite: 6]
* [cite_start]**Tools**: Java Scanner Class, Encapsulation, Business Logic Mapping [cite: 28, 171]

## 📝 Conclusion
[cite_start]This milestone proves the ability to bridge the gap between abstract logic and functional software through pure programmatic architecture[cite: 8].

---
*Powered by DecodeLabs | Project 3: ATM Interface*
