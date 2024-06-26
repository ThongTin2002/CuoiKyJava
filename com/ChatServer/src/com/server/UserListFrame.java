package server;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;

import dao.AdminDAO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserListFrame extends JFrame {

    private JTable userTable;
    private AdminDAO adminDAO;

    public UserListFrame(java.util.List<String[]> users, AdminDAO adminDAO) {
        this.adminDAO = adminDAO;

        setTitle("Quản lý user");
        setSize(400, 300);
        setLocationRelativeTo(null);

        Object[][] userData = new Object[users.size()][2];
        for (int i = 0; i < users.size(); i++) {
            userData[i][0] = users.get(i)[0];
            userData[i][1] = "Xóa";
        }

        DefaultTableModel model = new DefaultTableModel(userData, new String[]{"Tên người dùng", "Thao tác"});
        userTable = new JTable(model);

        // Thêm renderer và editor cho cột "Thao tác"
        userTable.getColumn("Thao tác").setCellRenderer(new ButtonRenderer());
        userTable.getColumn("Thao tác").setCellEditor(new ButtonEditor(new JCheckBox()));

        userTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(userTable);

        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
            setBackground(Color.RED);
            setForeground(Color.WHITE);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Xóa" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(Color.RED);
            button.setForeground(Color.WHITE);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "Xóa" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Thực hiện hành động xóa user khi nhấn nút
                int selectedRow = userTable.getSelectedRow();
                String username = (String) userTable.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(button, "Bạn có chắc chắn muốn xóa người dùng " + username + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    adminDAO.deleteUser(username);
                    ((DefaultTableModel) userTable.getModel()).removeRow(selectedRow);
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}
