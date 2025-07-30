import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudentGUI extends JFrame {
    private JTextField idField, nameField;
    private JButton addButton, displayButton, deleteButton;
    private JTextArea displayArea;
    private StudentManager studentManager;

    public StudentGUI() {
        studentManager = new StudentManager();
        setTitle("Student Management System");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Entry"));

        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        addButton = new JButton("Add Student");
        inputPanel.add(addButton);

        deleteButton = new JButton("Delete by ID");
        inputPanel.add(deleteButton);

        add(inputPanel, BorderLayout.NORTH);

        // Display Area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // Display Button
        displayButton = new JButton("Display All Students");
        add(displayButton, BorderLayout.SOUTH);

        // Listeners
        idField.addActionListener(e -> nameField.requestFocusInWindow());

        addButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();
                Student student = new Student();
                student.setId(id);
                student.setName(name);
                String result = studentManager.saveStudent(student);
                JOptionPane.showMessageDialog(this, result);
                idField.setText("");
                nameField.setText("");
                displayData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
            }
        });

        deleteButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                boolean result = studentManager.deleteById(id);
                if (result) {
                    JOptionPane.showMessageDialog(this, "Student deleted.");
                } else {
                    JOptionPane.showMessageDialog(this, "Student ID not found.");
                }
                displayData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter valid ID.");
            }
        });

        displayButton.addActionListener(e -> displayData());

        setVisible(true);
    }

    private void displayData() {
        java.util.List<String> records = studentManager.readAll();
        displayArea.setText("");  // clear old data
        for (String record : records) {
            displayArea.append(record + "\n");
        }
    }

    public static void main(String[] args) {
        new StudentGUI();
    }
}
