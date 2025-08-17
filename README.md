📖 Project Description: LibrarySystemGUI

LibrarySystemGUI is a desktop-based Library Management System built in Java Swing.
It provides an interactive graphical interface for managing library operations such as:

📚 Adding new books with details (name, category).

🗑️ Removing books (with safeguards if the book is currently borrowed).

📖 Borrowing books with specified duration and borrow date validation.

🔄 Returning borrowed books.

⏳ Extending borrowing periods automatically by 7 days.

📊 Viewing the library’s real-time status, including:

Borrowed vs. available books.

Category-wise book counts.

Total number of books and capacity usage.

The application ensures user-friendly interaction with validation checks, error handling, and status reporting through dialogs and a central status panel.

Key features:

GUI-based interface using Java Swing (JFrame, JMenuBar, JPanel, JButton, JTextArea, etc.).

Validation for borrow dates (ISO format YYYY-MM-DD, no future borrow dates).

Dynamic capacity setting (user-defined or defaults to 100).

Robust error-handling and confirmation dialogs for safe operations.

Clear separation of business logic (Library, Book classes) and UI logic (LibrarySystemGUI).

This project demonstrates the application of Object-Oriented Programming (OOP) principles, GUI development, and basic data handling in Java.
