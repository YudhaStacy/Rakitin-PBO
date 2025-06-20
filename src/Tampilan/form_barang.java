/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Tampilan;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import koneksi.Koneksi;

/**
 *
 * @author angga murdika
 */

public class form_barang extends javax.swing.JFrame {

    /**
     * Creates new form form_barang
     */
    Connection conn;
    public form_barang() {
        initComponents();
        Koneksi koneksi1 = new Koneksi();
        conn = koneksi1.connect();
        txtNamaBarang.putClientProperty("JTextField.placeholderText", "Masukkan Nama");
    }

    private Runnable onDataSaved;

    public form_barang(Runnable onDataSaved) {
        this();
        this.onDataSaved = onDataSaved;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    public void setFormData(String id, String nama, int hargaBeli, int hargaJual, String status, String satuan) {
        txtNamaBarang.setText(nama);
        sHargaBeli.setValue(hargaBeli);
        sHargaJual.setValue(hargaJual);
        pilihStatus.setSelectedItem(status);
        pilihSatuan.setSelectedItem(satuan);
        btnSimpan.setText("Update");

        for (ActionListener al : btnSimpan.getActionListeners()) {
            btnSimpan.removeActionListener(al);
        }

        btnSimpan.addActionListener(evt -> {
            try {
                String namaBaru = txtNamaBarang.getText().trim();
                int hargaBeliBaru = (int) sHargaBeli.getValue();
                int hargaJualBaru = (int) sHargaJual.getValue();
                String statusBaru = (String) pilihStatus.getSelectedItem();
                String satuanBaru = (String) pilihSatuan.getSelectedItem();

                if (namaBaru.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nama barang tidak boleh kosong.");
                    return;
                }

                if (conn == null || conn.isClosed()) {
                    conn = new Koneksi().connect();
                }

                String sql = "UPDATE barang SET nama_barang=?, harga_beli=?, harga_jual=?, status=?, satuan=? WHERE id_barang=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, namaBaru);
                stmt.setInt(2, hargaBeliBaru);
                stmt.setInt(3, hargaJualBaru);
                stmt.setString(4, statusBaru);
                stmt.setString(5, satuanBaru);
                stmt.setString(6, id);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Data berhasil diupdate.");
                if (onDataSaved != null) onDataSaved.run();
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal update data: " + ex.getMessage());
            }
        });
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        lbTab = new javax.swing.JLabel();
        lbNamaBarang = new javax.swing.JLabel();
        lbHarga = new javax.swing.JLabel();
        sHargaBeli = new javax.swing.JSpinner();
        txtNamaBarang = new javax.swing.JTextField();
        btnSimpan = new javax.swing.JButton();
        pilihStatus = new javax.swing.JComboBox<>();
        lbHarga1 = new javax.swing.JLabel();
        pilihSatuan = new javax.swing.JComboBox<>();
        lbNamaBarang1 = new javax.swing.JLabel();
        lbHarga2 = new javax.swing.JLabel();
        sHargaJual = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lbTab.setFont(new java.awt.Font("SF Pro Text", 1, 24)); // NOI18N
        lbTab.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTab.setText("Barang");
        lbTab.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lbNamaBarang.setFont(new java.awt.Font("SF Pro Text", 0, 12)); // NOI18N
        lbNamaBarang.setText("Nama Barang");

        lbHarga.setFont(new java.awt.Font("SF Pro Text", 0, 12)); // NOI18N
        lbHarga.setText("Harga Beli");

        sHargaBeli.setFont(new java.awt.Font("SF Pro Text", 0, 12)); // NOI18N
        sHargaBeli.setAlignmentX(0.0F);
        sHargaBeli.setAlignmentY(0.0F);

        txtNamaBarang.setFont(new java.awt.Font("SF Pro Text", 0, 12)); // NOI18N
        txtNamaBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaBarangActionPerformed(evt);
            }
        });

        btnSimpan.setBackground(new java.awt.Color(0, 204, 51));
        btnSimpan.setFont(new java.awt.Font("SF Pro Text", 1, 12)); // NOI18N
        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        pilihStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Aktif", "Tidak Aktif" }));

        lbHarga1.setFont(new java.awt.Font("SF Pro Text", 0, 12)); // NOI18N
        lbHarga1.setText("Status");

        pilihSatuan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "pcs", "unit" }));

        lbNamaBarang1.setFont(new java.awt.Font("SF Pro Text", 0, 12)); // NOI18N
        lbNamaBarang1.setText("Nama Barang");

        lbHarga2.setFont(new java.awt.Font("SF Pro Text", 0, 12)); // NOI18N
        lbHarga2.setText("Harga Jual");

        sHargaJual.setFont(new java.awt.Font("SF Pro Text", 0, 12)); // NOI18N
        sHargaJual.setAlignmentX(0.0F);
        sHargaJual.setAlignmentY(0.0F);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lbTab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbHarga, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbNamaBarang, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lbHarga2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbHarga1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                                    .addComponent(pilihStatus, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(sHargaJual)))
                            .addGap(69, 69, 69)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(sHargaBeli, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(pilihSatuan, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbNamaBarang1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                        .addComponent(txtNamaBarang, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(7, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(lbTab)
                .addGap(41, 41, 41)
                .addComponent(lbNamaBarang)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbNamaBarang1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pilihSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbHarga)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sHargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbHarga2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbHarga1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pilihStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtNamaBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaBarangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaBarangActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        String Nama = txtNamaBarang.getText();
        int hargaBeli = (int) sHargaBeli.getValue();
        int hargaJual = (int) sHargaJual.getValue();
        String idBaru = IDGenerator.generateAutoID(conn, "barang", "id_barang", "BRG", 3);
        String status = pilihStatus.getSelectedItem().toString();
        String satuan = pilihSatuan.getSelectedItem().toString();
        String sql = "INSERT INTO barang (id_barang, nama_barang, harga_beli, harga_jual, status, satuan) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idBaru);
            stmt.setString(2, Nama);
            stmt.setInt(3, hargaBeli);
            stmt.setInt(4, hargaJual);
            stmt.setString(5, status);
            stmt.setString(6, satuan);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
                txtNamaBarang.setText("");
                sHargaBeli.setValue(0);

                if (onDataSaved != null) {
                    onDataSaved.run();
                }
                this.dispose(); 
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan data: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    FlatLightLaf.setup();

    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new form_barang().setVisible(true);
        }
    });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSimpan;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel lbHarga;
    private javax.swing.JLabel lbHarga1;
    private javax.swing.JLabel lbHarga2;
    private javax.swing.JLabel lbNamaBarang;
    private javax.swing.JLabel lbNamaBarang1;
    private javax.swing.JLabel lbTab;
    private javax.swing.JComboBox<String> pilihSatuan;
    private javax.swing.JComboBox<String> pilihStatus;
    private javax.swing.JSpinner sHargaBeli;
    private javax.swing.JSpinner sHargaJual;
    private javax.swing.JTextField txtNamaBarang;
    // End of variables declaration//GEN-END:variables
}
