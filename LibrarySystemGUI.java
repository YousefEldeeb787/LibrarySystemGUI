package librarysystemgui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

class Book {
    int id;
    String name;
    String category;
    boolean isBorrowed;
    int borrowPeriod;
    String borrowDate;

    public Book(int id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category.toLowerCase();
        this.isBorrowed = false;
        this.borrowPeriod = 0;
        this.borrowDate = "";
    }
}

class Library {
    ArrayList<Book> books;
    int capacity;
    int nextId;

    Library(int cap) {
        capacity = cap;
        books = new ArrayList<>();
        nextId = 1;
    }

    boolean addBook(String name, String category) {
        if (books.size() >= capacity) {
            JOptionPane.showMessageDialog(null, "Library is full", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        Book book = new Book(nextId++, name, category.toLowerCase());
        books.add(book);
        return true;
    }

    Book findBook(int id) {
        for (Book book : books) {
            if (book.id == id) {
                return book;
            }
        }
        return null;
    }

    boolean removeBook(int id) {
        Book book = findBook(id);
        if (book == null) {
            JOptionPane.showMessageDialog(null, "Book not found", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (book.isBorrowed) {
            JOptionPane.showMessageDialog(null, 
                "Book is borrowed " + book.borrowPeriod + " days.", 
                "Cannot Remove", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        books.remove(book);
        return true;
    }

    boolean borrowBook(int id, int period, String date) {
        Book book = findBook(id);
        if (book == null) {
            JOptionPane.showMessageDialog(null, "Book not found", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (book.isBorrowed) {
            JOptionPane.showMessageDialog(null, 
                "Book is borrowed until " + book.borrowDate + 
                " plus " + book.borrowPeriod + " days.", 
                "Cannot Borrow", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        book.isBorrowed = true;
        book.borrowPeriod = period;
        book.borrowDate = date;
        return true;
    }

    boolean returnBook(int id) {
        Book book = findBook(id);
        if (book == null) {
            JOptionPane.showMessageDialog(null, "Book not found", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!book.isBorrowed) {
            JOptionPane.showMessageDialog(null, "Book is not borrowed", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        book.isBorrowed = false;
        book.borrowPeriod = 0;
        book.borrowDate = "";
        return true;
    }

    boolean extendBorrowPeriod(int id) {
        Book book = findBook(id);
        if (book == null) {
            return false;
        }
        if (!book.isBorrowed) {
            return false;
        }
        int bonusDays = 7;
        book.borrowPeriod += bonusDays;
        return true;
    }

    String getStatusText() {
        StringBuilder sb = new StringBuilder();
        int biologyCount = 0, mathsCount = 0, historyCount = 0, 
            chemistryCount = 0, politicsCount = 0;
        int borrowedCount = 0;

        sb.append("Library Status:\n\n");
        for (Book book : books) {
            sb.append("ID: ").append(book.id).append(", Name: ").append(book.name)
              .append(", Category: ").append(book.category).append("\n");
            if (book.isBorrowed) {
                sb.append("Status: Borrowed, Period: ").append(book.borrowPeriod)
                  .append(" days, Borrow Date: ").append(book.borrowDate).append("\n");
                borrowedCount++;
            } else {
                sb.append("Status: Not Borrowed\n");
            }
            sb.append("\n");
            
            switch (book.category) {
                case "biology":
                    biologyCount++;
                    break;
                case "maths":
                    mathsCount++;
                    break;
                case "history":
                    historyCount++;
                    break;
                case "chemistry":
                    chemistryCount++;
                    break;
                case "politics":
                    politicsCount++;
                    break;
            }
        }
        sb.append("Category Counts:\n");
        sb.append("Biology: ").append(biologyCount).append("\n");
        sb.append("Maths: ").append(mathsCount).append("\n");
        sb.append("History: ").append(historyCount).append("\n");
        sb.append("Chemistry: ").append(chemistryCount).append("\n");
        sb.append("Politics: ").append(politicsCount).append("\n\n");
        sb.append("Total Borrowed Books: ").append(borrowedCount).append("\n");
        sb.append("Total Books: ").append(books.size()).append("\n");
        sb.append("Library Capacity: ").append(capacity).append("\n");

        return sb.toString();
    }
}

public class LibrarySystemGUI extends JFrame {
    private final Library library;
    private final JTextArea statusArea;
    private final JTextField nameField;
    private final JTextField idField;
    private final JTextField periodField;
    private final JTextField dateField;
    private final JComboBox<String> categoryCombo;
    
    public LibrarySystemGUI(int capacity) {
        library = new Library(capacity);

        setTitle("Library Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic(KeyEvent.VK_X);
        exitItem.setToolTipText("Exit application");
        exitItem.addActionListener(e -> exitApplication());
        fileMenu.add(exitItem);
        
        menuBar.add(fileMenu);
        
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.setMnemonic(KeyEvent.VK_A);
        aboutItem.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, 
                "Library Management System\nVersion 1.0", 
                "About", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);
        
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Book Operations"));
        
        JLabel nameLabel = new JLabel("Book Name:");
        nameField = new JTextField();
        
        JLabel categoryLabel = new JLabel("Category:");
        String[] categories = {"Biology", "Maths", "History", "Chemistry", "Politics"};
        categoryCombo = new JComboBox<>(categories);
        
        JLabel idLabel = new JLabel("Book ID (for borrow/remove/return/extend):");
        idField = new JTextField();
        
        JLabel periodLabel = new JLabel("Borrow Period (days):");
        periodField = new JTextField();
        
        JLabel dateLabel = new JLabel("Borrow Date (YYYY-MM-DD):");
        dateField = new JTextField();
        
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(categoryLabel);
        inputPanel.add(categoryCombo);
        inputPanel.add(idLabel);
        inputPanel.add(idField);
        inputPanel.add(periodLabel);
        inputPanel.add(periodField);
        inputPanel.add(dateLabel);
        inputPanel.add(dateField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton addButton = new JButton("Add Book");
        addButton.addActionListener(e -> addBook());
        
        JButton removeButton = new JButton("Remove Book");
        removeButton.addActionListener(e -> removeBook());
        
        JButton borrowButton = new JButton("Borrow Book");
        borrowButton.addActionListener(e -> borrowBook());
        
        JButton returnButton = new JButton("Return Book");
        returnButton.addActionListener(e -> returnBook());
        
        JButton extendButton = new JButton("Extend Borrow Period");
        extendButton.setToolTipText("Extend the borrowing period for a book by 7 days");
        extendButton.addActionListener(e -> extendBorrowPeriod());
        
        JButton statusButton = new JButton("View Status");
        statusButton.setToolTipText("Refresh the library status display");
        statusButton.addActionListener(e -> viewStatus());
        
        JButton exitButton = new JButton("Exit System");
        exitButton.setToolTipText("Exit the library system");
        exitButton.addActionListener(e -> exitApplication());
        
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(extendButton);
        buttonPanel.add(statusButton);
        buttonPanel.add(exitButton);
        
        statusArea = new JTextArea();
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        statusArea.setText("");
        JScrollPane scrollPane = new JScrollPane(statusArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Library Status"));
        
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void addBook() {
        String name = nameField.getText().trim();
        String category = (String) categoryCombo.getSelectedItem();
        
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a book name", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (library.addBook(name, category)) {
            JOptionPane.showMessageDialog(this, "Book added successfully", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            nameField.setText("");
        }
    }
    
    private void removeBook() {
        String idText = idField.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a book ID", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int id = Integer.parseInt(idText);
            if (library.removeBook(id)) {
                JOptionPane.showMessageDialog(this, "Book removed successfully", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                idField.setText("");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid book ID", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void borrowBook() {
        String idText = idField.getText().trim();
        String periodText = periodField.getText().trim();
        String date = dateField.getText().trim();

        if (idText.isEmpty() || periodText.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            int period = Integer.parseInt(periodText);

            LocalDate borrowDate;
            try {
                borrowDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate today = LocalDate.now();
            if (borrowDate.isAfter(today)) {
                JOptionPane.showMessageDialog(this, "Borrow date cannot be in the future", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (period <= 0) {
                JOptionPane.showMessageDialog(this, "Borrow period must be positive", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (library.borrowBook(id, period, date)) {
                JOptionPane.showMessageDialog(this, "Book borrowed successfully", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                idField.setText("");
                periodField.setText("");
                dateField.setText("");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for ID and period", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void returnBook() {
        String idText = idField.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a book ID", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int id = Integer.parseInt(idText);
            if (library.returnBook(id)) {
                JOptionPane.showMessageDialog(this, "Book returned successfully", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                idField.setText("");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid book ID", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void extendBorrowPeriod() {
        String idText = idField.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a book ID", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int id = Integer.parseInt(idText);
            if (library.extendBorrowPeriod(id)) {
                JOptionPane.showMessageDialog(this, 
                    "Borrow period extended by 7 days.", 
                    "Bonus Applied", JOptionPane.INFORMATION_MESSAGE);
                idField.setText("");
            } else {
                Book book = library.findBook(id);
                if (book == null) {
                    JOptionPane.showMessageDialog(this, "Book not found", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Book is not borrowed", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid book ID", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void viewStatus() {
        SwingUtilities.invokeLater(() -> {
            statusArea.setText(library.getStatusText());
            System.out.println("viewStatus() called at " + LocalDateTime.now());
        });
    }
    
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit the Library System?",
            "Exit Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int capacity = -1;
            while (capacity <= 0) {
                String input = JOptionPane.showInputDialog("Enter library capacity:");
                if (input == null) {
                    JOptionPane.showMessageDialog(null, "Using default capacity of 100.", 
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                    capacity = 100;
                    break;
                }
                try {
                    capacity = Integer.parseInt(input);
                    if (capacity <= 0) {
                        JOptionPane.showMessageDialog(null, "Capacity must be positive.", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            LibrarySystemGUI gui = new LibrarySystemGUI(capacity);
            gui.setVisible(true);
        });
    }
}