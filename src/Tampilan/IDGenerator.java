/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tampilan;

import koneksi.Koneksi;
import java.sql.*;
/**
 *
 * @author Yudaaa
 */
public class IDGenerator {
    public static String generateAutoID(Connection conn, String tableName, String idColumn, String prefix, int numberLength) {
        String newID = "";
        try {
            String sql = "SELECT " + idColumn + " FROM " + tableName + " WHERE " + idColumn + " LIKE ? ORDER BY " + idColumn + " DESC LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, prefix + "%");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String lastID = rs.getString(1);
                String numberPart = lastID.substring(prefix.length());
                int number = Integer.parseInt(numberPart) + 1;
                String formattedNumber = String.format("%0" + numberLength + "d", number);
                newID = prefix + formattedNumber;
            } else {
                String formattedNumber = String.format("%0" + numberLength + "d", 1);
                newID = prefix + formattedNumber;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newID;
    }
}
