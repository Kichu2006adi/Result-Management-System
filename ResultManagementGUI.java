import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Student {
    int rollNo;
    String name;
    int[] marks = new int[3];
    int total;
    float percentage;

    public Student(int rollNo, String name, int[] marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.marks = marks;
        calculate();
    }

    private void calculate() {
        total = 0;
        for (int m : marks) {
            total += m;
        }
        percentage = total / 3.0f;
    }

    public Object[] toTableRow() {
        return new Object[]{
            rollNo, name, marks[0], marks[1], marks[2], total, String.format("%.2f", percentage),
            (percentage >= 35 ? "PASS" : "FAIL")
        };
    }
}

public class ResultManagementGUI extends JFrame {
    private final JTextField rollField = new JTextField(10);
    private final JTextField nameField = new JTextField(15);
    private final JTextField[] markFields = {new JTextField(5), new JTextField(5), new JTextField(5)};
    private final DefaultTableModel tableModel;
    private final java.util.List<Student> students = new ArrayList<>();

    public ResultManagementGUI() {
        setTitle("Result Management System");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Student"));

        inputPanel.add(new JLabel("Roll No:"));
        inputPanel.add(rollField);

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Marks (3 Subjects):"));
        JPanel markPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (JTextField tf : markFields) markPanel.add(tf);
        inputPanel.add(markPanel);

        JButton addBtn = new JButton("Add Student");
        inputPanel.add(addBtn);

        JButton searchBtn = new JButton("Search by Roll No");
        inputPanel.add(searchBtn);

        // Table
        String[] columns = {"Roll No", "Name", "Sub1", "Sub2", "Sub3", "Total", "Percentage", "Result"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(table);

        // Actions
        addBtn.addActionListener(e -> addStudent());
        searchBtn.addActionListener(e -> searchStudent());

        // Add components
        setLayout(new BorderLayout(10, 10));
        add(inputPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
    }

    private void addStudent() {
        try {
            int roll = Integer.parseInt(rollField.getText().trim());
            String name = nameField.getText().trim();
            int[] marks = new int[3];

            for (int i = 0; i < 3; i++) {
                marks[i] = Integer.parseInt(markFields[i].getText().trim());
                if (marks[i] < 0 || marks[i] > 100) throw new NumberFormatException();
            }

            Student s = new Student(roll, name, marks);
            students.add(s);
            tableModel.addRow(s.toTableRow());
            JOptionPane.showMessageDialog(this, "Student added successfully!");

            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid roll number and marks (0-100).");
        }
    }

    private void searchStudent() {
        String input = JOptionPane.showInputDialog(this, "Enter Roll No to search:");
        if (input == null || input.isEmpty()) return;

        try {
            int roll = Integer.parseInt(input);
            for (Student s : students) {
                if (s.rollNo == roll) {
                    JOptionPane.showMessageDialog(this,
                        "Name: " + s.name + "\nMarks: " + s.marks[0] + ", " + s.marks[1] + ", " + s.marks[2] +
                        "\nTotal: " + s.total + "\nPercentage: " + s.percentage + "%\nResult: " +
                        (s.percentage >= 35 ? "PASS" : "FAIL"));
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Student not found.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid roll number.");
        }
    }

    private void clearFields() {
        rollField.setText("");
        nameField.setText("");
        for (JTextField tf : markFields) tf.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ResultManagementGUI().setVisible(true));
    }
}
