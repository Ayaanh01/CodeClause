package saba;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LibrarySystem extends Frame implements ActionListener {
    private Label titleLabel, studentLabel, bookLabel, issueDateLabel, returnDateLabel, studentNameLabel;
    private TextField studentField, bookField, issueDateField, returnDateField, studentNameField;
    private Button issueButton, returnButton;

    private Connection connection;

    public LibrarySystem() {
        setTitle("Library System");
        setSize(400, 300); 
        setLayout(new FlowLayout());

        titleLabel = new Label("Library System");
        studentLabel = new Label("Student ID: ");
        bookLabel = new Label("Book ID: ");
        issueDateLabel = new Label("Issue Date (yyyy-MM-dd): ");
        returnDateLabel = new Label("Return Date (yyyy-MM-dd): ");
        studentNameLabel = new Label("Student Name: "); 

        studentField = new TextField(10);
        bookField = new TextField(10);
        issueDateField = new TextField(10);
        returnDateField = new TextField(10);
        studentNameField = new TextField(10);

        issueButton = new Button("Issue Book");
        returnButton = new Button("Return Book");

        issueButton.addActionListener(this);
        returnButton.addActionListener(this);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLUE);
        issueButton.setForeground(Color.GREEN);
        returnButton.setForeground(Color.RED);

        add(titleLabel);
        add(studentLabel);
        add(studentField);
        add(studentNameLabel);
        add(studentNameField); 
        add(bookLabel);
        add(bookField);
        add(issueDateLabel);
        add(issueDateField);
        add(returnDateLabel);
        add(returnDateField);
        add(issueButton);
        add(returnButton);

        connection = connectToDatabase();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
                closeDatabaseConnection();
                System.exit(0);
            }
        });

        setVisible(true);
    }

    public Connection connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3307/saba"; 
            String username = "root";
            String password = "sabakhan";
            Connection con = DriverManager.getConnection(url, username, password);
            return con;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closeDatabaseConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == issueButton) {
            issueBook();
        } else if (e.getSource() == returnButton) {
            returnBook();
        }
    }

    public void issueBook() {
        int studentID = Integer.parseInt(studentField.getText());
        int bookID = Integer.parseInt(bookField.getText());
        String issueDateStr = issueDateField.getText();
        String studentName = studentNameField.getText(); 

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date issueDate = dateFormat.parse(issueDateStr);

            String query = "INSERT INTO transactions (student_id, student_name, book_id, issue_date) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, studentID);
            preparedStatement.setString(2, studentName); 
            preparedStatement.setInt(3, bookID);
            preparedStatement.setDate(4, new java.sql.Date(issueDate.getTime()));
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void returnBook() {
        int studentID = Integer.parseInt(studentField.getText());
        int bookID = Integer.parseInt(bookField.getText());
        String returnDateStr = returnDateField.getText();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date returnDate = dateFormat.parse(returnDateStr);

            String query = "UPDATE transactions SET return_date = ? WHERE student_id = ? AND book_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, new java.sql.Date(returnDate.getTime()));
            preparedStatement.setInt(2, studentID);
            preparedStatement.setInt(3, bookID);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LibrarySystem();
    }
}

