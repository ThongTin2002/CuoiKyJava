package server;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.AdminDAO;

import java.awt.*;

public class SimpleTableExample {
	private AdminDAO adminDAO;
	
	public SimpleTableExample(java.util.List<String[]> users, AdminDAO adminDAO) {
		this.adminDAO = adminDAO;
        // Tạo JFrame
        JFrame frame = new JFrame("Quản lý user");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Tạo bảng với 2 cột
        Object[][] userData = new Object[users.size()][2];
        for (int i = 0; i < users.size(); i++) {
            userData[i][0] = users.get(i)[0];
            userData[i][1] = "Xóa";
        }

        DefaultTableModel model = new DefaultTableModel(userData, new String[]{"Tên người dùng", "Thao tác"});
        JTable table = new JTable(model);

        // Tạo JScrollPane để thêm bảng vào, cho phép cuộn
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        // Thêm JScrollPane vào JFrame
        frame.add(scrollPane, BorderLayout.CENTER);

        // Hiển thị JFrame
        frame.setVisible(true);
    }

}

