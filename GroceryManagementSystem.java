import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GroceryManagementSystem extends JFrame {
    private JTextField itemNameField, quantityField, priceField, searchField;
    private JLabel totalLabel;
    private DefaultTableModel tableModel;
    private JTable table;

    public GroceryManagementSystem() {
        setTitle("Grocery Management System");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 5, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add/Edit Item"));

        itemNameField = new JTextField();
        quantityField = new JTextField();
        priceField = new JTextField();

        inputPanel.add(new JLabel("Item Name:"));
        inputPanel.add(itemNameField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("Price per Unit:"));
        inputPanel.add(priceField);

        JButton addButton = new JButton("Add Item");
        JButton editButton = new JButton("Edit Item");

        inputPanel.add(addButton);
        inputPanel.add(editButton);

        // Table
        tableModel = new DefaultTableModel(new Object[]{"Item Name", "Quantity", "Price", "Total"}, 0);
        table = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(table);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        JButton removeButton = new JButton("Remove Selected");
        JButton clearButton = new JButton("Clear All");
        bottomPanel.add(removeButton);
        bottomPanel.add(clearButton);

        totalLabel = new JLabel("Total Bill: ₹0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bottomPanel.add(totalLabel);

        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Item"));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Item Name:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        add(inputPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        add(searchPanel, BorderLayout.WEST);

        // Button Actions
        addButton.addActionListener(e -> addItem());
        removeButton.addActionListener(e -> removeSelected());
        clearButton.addActionListener(e -> clearAll());
        searchButton.addActionListener(e -> searchItem());
        editButton.addActionListener(e -> editSelected());

        setVisible(true);
    }

    private void addItem() {
        String name = itemNameField.getText().trim();
        String qtyStr = quantityField.getText().trim();
        String priceStr = priceField.getText().trim();

        if (name.isEmpty() || qtyStr.isEmpty() || priceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            int qty = Integer.parseInt(qtyStr);
            double price = Double.parseDouble(priceStr);
            double total = qty * price;

            tableModel.addRow(new Object[]{name, qty, price, total});
            clearInputs();
            updateTotalLabel();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantity and Price must be numeric.");
        }
    }

    private void editSelected() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            try {
                String name = itemNameField.getText().trim();
                int qty = Integer.parseInt(quantityField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());
                double total = qty * price;

                tableModel.setValueAt(name, row, 0);
                tableModel.setValueAt(qty, row, 1);
                tableModel.setValueAt(price, row, 2);
                tableModel.setValueAt(total, row, 3);

                clearInputs();
                updateTotalLabel();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid number format.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select an item to edit.");
        }
    }

    private void removeSelected() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            tableModel.removeRow(row);
            updateTotalLabel();
        } else {
            JOptionPane.showMessageDialog(this, "Select a row to remove.");
        }
    }

    private void clearAll() {
        tableModel.setRowCount(0);
        updateTotalLabel();
    }

    private void searchItem() {
        String searchText = searchField.getText().trim().toLowerCase();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String itemName = tableModel.getValueAt(i, 0).toString().toLowerCase();
            if (itemName.contains(searchText)) {
                table.setRowSelectionInterval(i, i);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Item not found.");
    }

    private void clearInputs() {
        itemNameField.setText("");
        quantityField.setText("");
        priceField.setText("");
    }

    private void updateTotalLabel() {
        double total = 0.0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            total += Double.parseDouble(tableModel.getValueAt(i, 3).toString());
        }
        totalLabel.setText("Total Bill: ₹" + String.format("%.2f", total));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GroceryManagementSystem::new);
    }
}
