/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tampilan;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;

/**
 * Form Pembelian - Purchase Transaction Form
 * Improved version with proper data saving and stock updates
 *
 * @author angga murdika
 */
public class form_pembelian extends JFrame {

    private Connection conn;
    private String idPembelian;
    private DefaultTableModel modelDetailBarang;
    private NumberFormat currencyFormat;
    private String idPemasok = null;
    private String namaPemasok = "";
    private JTextField txtCariPemasok;
    private JButton btnCariPemasok;
    private JLabel lblInfoPemasok;

    private JLabel lblTitle, lblKodePembelian, lblTanggal, lblPemasok, lblDetailBarang, lblTotal;
    private JTextField txtKodePembelian, txtTanggal, txtTotal;
    private JTable tableDetailBarang;
    private JScrollPane scrollPane;
    private JButton btnTambah, btnHapus, btnSimpan, btnKeluar, btnReset;

    public form_pembelian() {
        initializeComponents();
        setupEventHandlers();
        buatIdPembelianBaru();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); // HANYA tutup form
    }

    private Runnable onDataSaved;

    public form_pembelian(Runnable onDataSaved) {
        this();
        this.onDataSaved = onDataSaved;
    }
    
    private void initializeComponents() {
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        setTitle("Form Pembelian - Purchase Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(850, 700);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID Barang", "Nama Barang", "Harga Beli", "Harga Jual", "Jumlah", "Subtotal"};
        modelDetailBarang = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 2: case 3: case 5: return Double.class; 
                    case 4: return Integer.class; 
                    default: return String.class;
                }
            }
        };

        createComponents();
        layoutComponents();
        setPlaceholders();
    }

    private void createComponents() {
        lblTitle = new JLabel("FORM PEMBELIAN", SwingConstants.CENTER);
        lblTitle.setFont(lblTitle.getFont().deriveFont(24f));
        lblTitle.setForeground(new java.awt.Color(51, 102, 153));

        lblKodePembelian = new JLabel("ID Pembelian:");
        lblTanggal = new JLabel("Tanggal:");
        lblPemasok = new JLabel("Pemasok:");
        lblDetailBarang = new JLabel("Detail Barang:");
        lblTotal = new JLabel("Total:");
        lblTotal.setFont(lblTotal.getFont().deriveFont(18f));

        txtKodePembelian = new JTextField();
        txtKodePembelian.setEditable(false);
        txtKodePembelian.setBackground(new java.awt.Color(245, 245, 245));

        txtTanggal = new JTextField();
        txtTanggal.setEditable(false);
        txtTanggal.setBackground(new java.awt.Color(245, 245, 245));
        txtTanggal.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        txtTotal = new JTextField("Rp 0");
        txtTotal.setEditable(false);
        txtTotal.setFont(txtTotal.getFont().deriveFont(18f));
        txtTotal.setHorizontalAlignment(SwingConstants.CENTER);
        txtTotal.setBackground(new java.awt.Color(245, 245, 245));

        tableDetailBarang = new JTable(modelDetailBarang);
        tableDetailBarang.setRowHeight(25);
        tableDetailBarang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane = new JScrollPane(tableDetailBarang);

        btnTambah = new JButton("Tambah Barang");
        btnHapus = new JButton("Hapus Barang");
        btnSimpan = new JButton("Simpan Transaksi");
        btnKeluar = new JButton("Keluar");
        btnReset = new JButton("Reset Form");

        btnSimpan.setBackground(new java.awt.Color(0, 153, 51));
        btnSimpan.setForeground(java.awt.Color.WHITE);
        btnSimpan.setFont(btnSimpan.getFont().deriveFont(12f));

        btnKeluar.setBackground(new java.awt.Color(204, 51, 51));
        btnKeluar.setForeground(java.awt.Color.WHITE);
        btnKeluar.setFont(btnKeluar.getFont().deriveFont(12f));

        btnReset.setBackground(new java.awt.Color(255, 153, 51));
        btnReset.setForeground(java.awt.Color.WHITE);
        btnReset.setFont(btnReset.getFont().deriveFont(12f));

        txtCariPemasok = new JTextField();
        btnCariPemasok = new JButton("Cari");
        lblInfoPemasok = new JLabel("Pemasok: -");
        lblInfoPemasok.setFont(lblInfoPemasok.getFont().deriveFont(12f));
    }

    private void layoutComponents() {
        lblTitle.setBounds(0, 20, 850, 30);
        add(lblTitle);

        lblKodePembelian.setBounds(50, 70, 100, 25);
        txtKodePembelian.setBounds(160, 70, 200, 25);
        add(lblKodePembelian);
        add(txtKodePembelian);

        lblTanggal.setBounds(50, 105, 100, 25);
        txtTanggal.setBounds(160, 105, 200, 25);
        add(lblTanggal);
        add(txtTanggal);

        lblPemasok.setBounds(50, 140, 100, 25);
        txtCariPemasok.setBounds(160, 140, 150, 25);
        btnCariPemasok.setBounds(320, 140, 60, 25);
        lblInfoPemasok.setBounds(160, 170, 300, 25);
        add(lblPemasok);
        add(txtCariPemasok);
        add(btnCariPemasok);
        add(lblInfoPemasok);

        lblTotal.setBounds(480, 70, 60, 25);
        txtTotal.setBounds(550, 70, 200, 50);
        add(lblTotal);
        add(txtTotal);

        btnTambah.setBounds(500, 210, 120, 30);
        btnHapus.setBounds(630, 210, 120, 30);
        add(btnTambah);
        add(btnHapus);

        lblDetailBarang.setBounds(50, 215, 150, 25);
        scrollPane.setBounds(50, 245, 750, 280);
        add(lblDetailBarang);
        add(scrollPane);

        btnSimpan.setBounds(450, 550, 120, 35);
        btnReset.setBounds(580, 550, 120, 35);
        btnKeluar.setBounds(710, 550, 120, 35);
        add(btnSimpan);
        add(btnReset);
        add(btnKeluar);
    }

    private void setPlaceholders() {
        txtKodePembelian.putClientProperty("JTextField.placeholderText", "ID Pembelian");
        txtTanggal.putClientProperty("JTextField.placeholderText", "Tanggal");
        txtTotal.putClientProperty("JTextField.placeholderText", "Total");
        txtCariPemasok.putClientProperty("JTextField.placeholderText", "Cari nama pemasok...");
    }

    private void setupEventHandlers() {
        btnTambah.addActionListener(e -> showTambahBarangDialog());
        btnHapus.addActionListener(e -> hapusBarangTerpilih());
        btnSimpan.addActionListener(e -> simpanTransaksi());
        btnKeluar.addActionListener(e -> keluarAplikasi());
        btnReset.addActionListener(e -> resetForm());

        btnCariPemasok.addActionListener(e -> cariPemasok());
        txtCariPemasok.addActionListener(e -> cariPemasok());

        txtCariPemasok.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                if (txtCariPemasok.getText().trim().isEmpty()) {
                    resetPemasok();
                }
            }
        });
    }

    private void showTambahBarangDialog() {
        FormTambahBarang dialog = new FormTambahBarang(this, modelDetailBarang);
        dialog.setVisible(true);
        hitungTotal();
    }

    private void hapusBarangTerpilih() {
        int selectedRow = tableDetailBarang.getSelectedRow();
        if (selectedRow >= 0) {
            String namaBarang = modelDetailBarang.getValueAt(selectedRow, 1).toString();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Hapus barang '" + namaBarang + "' dari daftar?", 
                    "Konfirmasi Penghapusan",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                modelDetailBarang.removeRow(selectedRow);
                hitungTotal();
                JOptionPane.showMessageDialog(this, "Barang berhasil dihapus dari daftar!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih barang yang akan dihapus terlebih dahulu!");
        }
    }

    private void cariPemasok() {
        String inputNama = txtCariPemasok.getText().trim();

        if (inputNama.isEmpty()) {
            resetPemasok();
            return;
        }

        try {
            Koneksi koneksi = new Koneksi();
            conn = koneksi.connect();

            String sqlPemasok = "SELECT id_pemasok, nama_pemasok FROM pemasok WHERE nama_pemasok LIKE ? LIMIT 1";
            PreparedStatement pstmt = conn.prepareStatement(sqlPemasok);
            pstmt.setString(1, "%" + inputNama + "%");
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                idPemasok = rs.getString("id_pemasok");
                namaPemasok = rs.getString("nama_pemasok");
                updateInfoPemasok();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Pemasok dengan nama '" + inputNama + "' tidak ditemukan!",
                    "Pemasok Tidak Ditemukan", 
                    JOptionPane.WARNING_MESSAGE);
                resetPemasok();
            }

            rs.close();
            pstmt.close();
            conn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error mencari pemasok: " + e.getMessage(),
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            resetPemasok();
        }
    }

    private void updateInfoPemasok() {
        lblInfoPemasok.setText("Pemasok: " + namaPemasok + " (ID: " + idPemasok + ")");
        lblInfoPemasok.setForeground(new java.awt.Color(0, 128, 0));
    }

    private void resetPemasok() {
        idPemasok = null;
        namaPemasok = "";
        lblInfoPemasok.setText("Pemasok: -");
        lblInfoPemasok.setForeground(java.awt.Color.BLACK);
    }

    private void hitungTotal() {
        double total = 0;
        for (int i = 0; i < modelDetailBarang.getRowCount(); i++) {
            Object subtotalObj = modelDetailBarang.getValueAt(i, 5);
            if (subtotalObj != null) {
                double subtotal = (subtotalObj instanceof Double) ? 
                    (Double) subtotalObj : 
                    Double.parseDouble(subtotalObj.toString());
                total += subtotal;
            }
        }
        txtTotal.setText(currencyFormat.format(total));
    }

    private void simpanTransaksi() {
        if (modelDetailBarang.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Tidak ada barang yang akan dibeli!\nTambahkan barang terlebih dahulu.",
                "Data Tidak Lengkap", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (idPemasok == null || idPemasok.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Pemasok belum dipilih!\nPilih pemasok terlebih dahulu.",
                "Data Tidak Lengkap", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menyimpan transaksi pembelian ini?\n" +
                "ID Pembelian: " + idPembelian + "\n" +
                "Pemasok: " + namaPemasok + "\n" +
                "Total Items: " + modelDetailBarang.getRowCount() + "\n" +
                "Total Harga: " + txtTotal.getText(),
                "Konfirmasi Penyimpanan",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Koneksi koneksi = new Koneksi();
            conn = koneksi.connect();
            conn.setAutoCommit(false); 

            simpanPembelian();

            simpanDetailPembelian();

            updateStokBarang();

            conn.commit(); 
            conn.setAutoCommit(true);
            conn.close();

            JOptionPane.showMessageDialog(this, 
                "Transaksi pembelian berhasil disimpan!\n" +
                "ID Pembelian: " + idPembelian + "\n" +
                "Data tersimpan di database dan stok barang telah diperbarui.",
                "Transaksi Berhasil", 
                JOptionPane.INFORMATION_MESSAGE);
            
            if (onDataSaved != null) {
                    onDataSaved.run();
            }
            resetForm();

        } catch (Exception e) {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.rollback();
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            JOptionPane.showMessageDialog(this, 
                "Gagal menyimpan transaksi pembelian!\n" +
                "Error: " + e.getMessage() + "\n" +
                "Silakan coba lagi atau hubungi administrator.",
                "Error Database", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void simpanPembelian() throws Exception {
        double totalHarga = 0;
        for (int i = 0; i < modelDetailBarang.getRowCount(); i++) {
            Object subtotalObj = modelDetailBarang.getValueAt(i, 5);
            double subtotal = (subtotalObj instanceof Double) ? 
                (Double) subtotalObj : 
                Double.parseDouble(subtotalObj.toString());
            totalHarga += subtotal;
        }

        String sql = "INSERT INTO pembelian (id_pembelian, tanggal, total_harga, id_pemasok) VALUES (?, NOW(), ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, idPembelian);
        pstmt.setDouble(2, totalHarga);
        pstmt.setString(3, idPemasok);

        int result = pstmt.executeUpdate();
        pstmt.close();
        
        if (result == 0) {
            throw new Exception("Gagal menyimpan data pembelian utama");
        }
    }

    private void simpanDetailPembelian() throws Exception {
        String sql = "INSERT INTO detail_pembelian (id_pembelian, id_barang, jumlah) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        for (int i = 0; i < modelDetailBarang.getRowCount(); i++) {
            String idBarang = modelDetailBarang.getValueAt(i, 0).toString();
            Object jumlahObj = modelDetailBarang.getValueAt(i, 4);
            int jumlah = (jumlahObj instanceof Integer) ? 
                (Integer) jumlahObj : 
                Integer.parseInt(jumlahObj.toString());

            pstmt.setString(1, idPembelian);
            pstmt.setString(2, idBarang);
            pstmt.setInt(3, jumlah);

            pstmt.addBatch();
        }

        int[] results = pstmt.executeBatch();
        pstmt.close();
        
        for (int result : results) {
            if (result == 0) {
                throw new Exception("Gagal menyimpan detail pembelian");
            }
        }
    }

    private void updateStokBarang() throws Exception {
        String sqlUpdate = "UPDATE barang SET stok = stok + ?, harga_beli = ?, harga_jual = ? WHERE id_barang = ?";
        PreparedStatement pstmt = conn.prepareStatement(sqlUpdate);

        for (int i = 0; i < modelDetailBarang.getRowCount(); i++) {
            String idBarang = modelDetailBarang.getValueAt(i, 0).toString();
            
            Object hargaBeliObj = modelDetailBarang.getValueAt(i, 2);
            double hargaBeli = (hargaBeliObj instanceof Double) ? 
                (Double) hargaBeliObj : 
                Double.parseDouble(hargaBeliObj.toString());
                
            Object hargaJualObj = modelDetailBarang.getValueAt(i, 3);
            double hargaJual = (hargaJualObj instanceof Double) ? 
                (Double) hargaJualObj : 
                Double.parseDouble(hargaJualObj.toString());
                
            Object jumlahObj = modelDetailBarang.getValueAt(i, 4);
            int jumlah = (jumlahObj instanceof Integer) ? 
                (Integer) jumlahObj : 
                Integer.parseInt(jumlahObj.toString());

            pstmt.setInt(1, jumlah);
            pstmt.setDouble(2, hargaBeli);
            pstmt.setDouble(3, hargaJual);
            pstmt.setString(4, idBarang);

            pstmt.addBatch();
        }

        int[] results = pstmt.executeBatch();
        pstmt.close();

        for (int result : results) {
            if (result == 0) {
                throw new Exception("Gagal memperbarui stok barang");
            }
        }
    }

    private void resetForm() {
        modelDetailBarang.setRowCount(0);
        txtTotal.setText(currencyFormat.format(0));
        txtCariPemasok.setText("");
        resetPemasok();
        buatIdPembelianBaru();
        
        JOptionPane.showMessageDialog(this,
            "Form telah direset!\nSiap untuk transaksi pembelian baru.",
            "Form Reset",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void keluarAplikasi() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin keluar dari aplikasi?\n" +
                "Pastikan semua transaksi telah disimpan.",
                "Konfirmasi Keluar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.exit(0);
        }
    }

    private void buatIdPembelianBaru() {
        String prefix = "PMB";
        String tanggalHariIni = new SimpleDateFormat("yyyyMMdd").format(new Date());

        try {
            Koneksi koneksi = new Koneksi();
            conn = koneksi.connect();

            String sql = "SELECT COUNT(*) AS jumlah FROM pembelian WHERE DATE(tanggal) = CURDATE()";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            int nomorUrut = 1;
            if (rs.next()) {
                nomorUrut = rs.getInt("jumlah") + 1;
            }

            String nomorStr = String.format("%03d", nomorUrut);
            idPembelian = prefix + "-" + tanggalHariIni + "-" + nomorStr;

            txtKodePembelian.setText(idPembelian);

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            idPembelian = prefix + "-" + tanggalHariIni + "-001";
            txtKodePembelian.setText(idPembelian);
            JOptionPane.showMessageDialog(this, 
                "Warning: Menggunakan ID default karena error database\n" +
                "ID: " + idPembelian,
                "Database Warning",
                JOptionPane.WARNING_MESSAGE);
        }
    }

    public class FormTambahBarang extends JDialog {
        private JTextField txtId, txtNama, txtHargaBeli, txtHargaJual, txtJumlah;
        private JButton btnCari, btnOK, btnCancel;
        private DefaultTableModel modelDetail;

        public FormTambahBarang(JFrame parent, DefaultTableModel modelDetail) {
            super(parent, "Tambah Barang ke Pembelian", true);
            this.modelDetail = modelDetail;
            initDialog();
        }

        private void initDialog() {
            setLayout(new GridLayout(8, 2, 10, 10));
            setSize(450, 400);
            setLocationRelativeTo(getParent());

            txtId = new JTextField();
            txtId.setEditable(false);
            txtId.setBackground(new java.awt.Color(245, 245, 245));
            
            txtNama = new JTextField();
            txtNama.setEditable(false);
            txtNama.setBackground(new java.awt.Color(245, 245, 245));
            
            txtHargaBeli = new JTextField("0");
            txtHargaJual = new JTextField("0");
            txtJumlah = new JTextField("1");

            btnCari = new JButton("Pilih Barang");
            btnOK = new JButton("Tambah ke Daftar");
            btnCancel = new JButton("Batal");

            add(new JLabel("ID Barang:"));
            add(txtId);
            add(new JLabel("Nama Barang:"));
            add(txtNama);
            add(new JLabel("Harga Beli (Rp):"));
            add(txtHargaBeli);
            add(new JLabel("Harga Jual (Rp):"));
            add(txtHargaJual);
            add(new JLabel("Jumlah Beli:"));
            add(txtJumlah);
            add(btnCari);
            add(new JLabel(""));
            add(btnOK);
            add(btnCancel);

            btnCari.addActionListener(e -> cariBarang());
            btnOK.addActionListener(e -> tambahBarang());
            btnCancel.addActionListener(e -> dispose());
        }

        private void cariBarang() {
            FormPilihBarang formCari = new FormPilihBarang(this);
            formCari.setVisible(true);

            txtId.setText(formCari.getId());
            txtNama.setText(formCari.getNama());
            txtHargaBeli.setText(formCari.getHargaBeli());
            txtHargaJual.setText(formCari.getHargaJual());
        }

        private void tambahBarang() {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih barang terlebih dahulu!");
                return;
            }

            if (txtJumlah.getText().trim().isEmpty() || 
                txtHargaBeli.getText().trim().isEmpty() || 
                txtHargaJual.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Lengkapi semua data barang!");
                return;
            }

            try {
                double hargaBeli = Double.parseDouble(txtHargaBeli.getText().trim());
                double hargaJual = Double.parseDouble(txtHargaJual.getText().trim());
                int jumlah = Integer.parseInt(txtJumlah.getText().trim());

                if (jumlah <= 0) {
                    JOptionPane.showMessageDialog(this, "Jumlah harus lebih dari 0!");
                    return;
                }

                if (hargaBeli <= 0 || hargaJual <= 0) {
                    JOptionPane.showMessageDialog(this, "Harga harus lebih dari 0!");
                    return;
                }

                if (hargaJual <= hargaBeli) {
                    int confirm = JOptionPane.showConfirmDialog(this,
                        "Harga jual lebih kecil atau sama dengan harga beli!\n" +
                        "Lanjutkan menambah barang?",
                        "Peringatan Harga",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                double subtotal = hargaBeli * jumlah;

                boolean sudahAda = false;
                for (int i = 0; i < modelDetail.getRowCount(); i++) {
                    if (modelDetail.getValueAt(i, 0).toString().equals(txtId.getText())) {
                        int jumlahLama = Integer.parseInt(modelDetail.getValueAt(i, 4).toString());
                        int jumlahBaru = jumlahLama + jumlah;
                        double subtotalBaru = hargaBeli * jumlahBaru;

                        modelDetail.setValueAt(hargaBeli, i, 2);
                        modelDetail.setValueAt(hargaJual, i, 3);
                        modelDetail.setValueAt(jumlahBaru, i, 4);
                        modelDetail.setValueAt(subtotalBaru, i, 5);
                        sudahAda = true;
                        break;
                    }
                }

                if (!sudahAda) {
                    modelDetail.addRow(new Object[]{
                        txtId.getText(),
                        txtNama.getText(),
                        hargaBeli,
                        hargaJual,
                        jumlah,
                        subtotal
                    });
                }

                JOptionPane.showMessageDialog(this, 
                    "Barang berhasil ditambahkan ke daftar pembelian!");
                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Format angka tidak valid!\nPastikan harga dan jumlah berupa angka yang benar.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public class FormPilihBarang extends JDialog {

        private String selectedId = "";
        private String selectedNama = "";
        private String selectedHargaBeli = "0";
        private String selectedHargaJual = "0";

        private JTextField txtCariBarang;
        private JTable tableBarang;
        private DefaultTableModel modelBarang;
        private JButton btnPilih, btnCancel;

        public FormPilihBarang(JDialog parent) {
            super(parent, "Pilih Barang", true);
            initDialog();
            loadDataBarang();
        }

        private void initDialog() {
            setLayout(new BorderLayout());
            setSize(700, 500);
            setLocationRelativeTo(getParent());

            JPanel searchPanel = new JPanel();
            searchPanel.add(new JLabel("Cari Barang:"));
            txtCariBarang = new JTextField(20);
            searchPanel.add(txtCariBarang);

            String[] columnNames = {"ID Barang", "Nama Barang", "Stok", "Harga Beli", "Harga Jual"};
            modelBarang = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            tableBarang = new JTable(modelBarang);
            tableBarang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tableBarang.setRowHeight(25);

            tableBarang.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        pilihBarang();
                    }
                }
            });

            JScrollPane scrollPane = new JScrollPane(tableBarang);

            JPanel buttonPanel = new JPanel();
            btnPilih = new JButton("Pilih Barang");
            btnCancel = new JButton("Batal");

            btnPilih.addActionListener(e -> pilihBarang());
            btnCancel.addActionListener(e -> dispose());

            buttonPanel.add(btnPilih);
            buttonPanel.add(btnCancel);

            txtCariBarang.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    cariBarang();
                }
            });

            add(searchPanel, BorderLayout.NORTH);
            add(scrollPane, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void loadDataBarang() {
            try {
                Koneksi koneksi = new Koneksi();
                Connection conn = koneksi.connect();

                String sql = "SELECT id_barang, nama_barang, stok, harga_beli, harga_jual FROM barang ORDER BY nama_barang";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                modelBarang.setRowCount(0);
                while (rs.next()) {
                    modelBarang.addRow(new Object[]{
                        rs.getString("id_barang"),
                        rs.getString("nama_barang"),
                        rs.getInt("stok"),
                        rs.getDouble("harga_beli"),
                        rs.getDouble("harga_jual")
                    });
                }

                rs.close();
                stmt.close();
                conn.close();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error loading data barang: " + e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        private void cariBarang() {
            String keyword = txtCariBarang.getText().trim().toLowerCase();

            if (keyword.isEmpty()) {
                loadDataBarang();
                return;
            }

            try {
                Koneksi koneksi = new Koneksi();
                Connection conn = koneksi.connect();

                String sql = "SELECT id_barang, nama_barang, stok, harga_beli, harga_jual "
                        + "FROM barang WHERE LOWER(nama_barang) LIKE ? OR LOWER(id_barang) LIKE ? "
                        + "ORDER BY nama_barang";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "%" + keyword + "%");
                pstmt.setString(2, "%" + keyword + "%");

                ResultSet rs = pstmt.executeQuery();

                modelBarang.setRowCount(0);
                while (rs.next()) {
                    modelBarang.addRow(new Object[]{
                        rs.getString("id_barang"),
                        rs.getString("nama_barang"),
                        rs.getInt("stok"),
                        rs.getDouble("harga_beli"),
                        rs.getDouble("harga_jual")
                    });
                }

                rs.close();
                pstmt.close();
                conn.close();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error searching barang: " + e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        private void pilihBarang() {
            int selectedRow = tableBarang.getSelectedRow();
            if (selectedRow >= 0) {
                selectedId = modelBarang.getValueAt(selectedRow, 0).toString();
                selectedNama = modelBarang.getValueAt(selectedRow, 1).toString();
                selectedHargaBeli = modelBarang.getValueAt(selectedRow, 3).toString();
                selectedHargaJual = modelBarang.getValueAt(selectedRow, 4).toString();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Pilih barang dari tabel terlebih dahulu!",
                        "Pilihan Tidak Valid",
                        JOptionPane.WARNING_MESSAGE);
            }
        }

        public String getId() {
            return selectedId;
        }

        public String getNama() {
            return selectedNama;
        }

        public String getHargaBeli() {
            return selectedHargaBeli;
        }

        public String getHargaJual() {
            return selectedHargaJual;
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            try {
                new form_pembelian().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Error starting application: " + e.getMessage(),
                        "Startup Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}
