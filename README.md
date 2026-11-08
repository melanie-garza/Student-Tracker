# Student Tracker 

**Student Tracker** is a web gradebook I built to automate my English classes. It calculates weighted averages & letter grades from raw scores, features a 1-click "Level Up" to promote groups, strictly validates data (no >100), and generates official PDF Report Cards. Built to save time and eliminate manual paperwork!

## Tech Stack
* **Backend:** Java 25, Spring Boot, Spring Data JPA
* **Database:** SQL MariaDB
* **Frontend:** HTML5, CSS3, Thymeleaf (Dark Neon Theme)
* **PDF Generation:** OpenPDF

## Key Features
* **Automated Grading:** Instantly calculates final averages based on custom weights (Attendance, Oral, Exams, Projects) and assigns American letter grades (A-F).
* **Data Validation:** Physically blocks invalid inputs (negative numbers, decimals, >100) and uses a Neon Pink "Edit Mode" to prevent accidental overwrites.
* **Level Up System:** Automatically promotes an entire group to the next term, copying students and resetting their grades to zero instantly.
* **Progress Report Cards:** Generates clean, downloadable PDF reports with institutional branding and teacher feedback.