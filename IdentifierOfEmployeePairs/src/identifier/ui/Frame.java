package identifier.ui;

import javax.swing.JFrame;
import javax.swing.JTable;

public class Frame extends JFrame {
    Frame(Object[][] data) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setTitle("Pair of Employees");
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        String[] columnNames = {"Employee1_ID", "Employee2_ID", "Project_ID", "Days Worked"};

        JTable table = new JTable(data, columnNames);
        table.getTableHeader().setBounds(50, 0, 700, 50);
        table.setBounds(50, 50, 700, 500);

        this.add(table.getTableHeader());
        this.add(table);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        FileSelector fileSelector = new FileSelector();
        Frame frame = new Frame(fileSelector.getPairData());
    }
}
