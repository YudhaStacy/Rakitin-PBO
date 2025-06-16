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
 * Form Penjualan - Sales Transaction Form
 *
 * @author angga murdika
 */
public class form_penjualan extends JFrame {
    private Connection conn;
    private String idPesanan;
    private DefaultTableModel modelDetailBarang;
    private NumberFormat currencyFormat;
    private String idPelanggan = null;
    private String namaPelanggan = "";
    private double totalBelanjaSebelumnya = 0;
    private int jumlahTransaksiSebelumnya = 0;
    private double persentaseDiskon = 0;
    private JTextField txtCariPelanggan;
    private JButton btnCariPelanggan;
    private JLabel lblInfoPelanggan, lblDiskon;
    private JTextField txtDiskon;

    private JLabel lblTitle, lblKodeTransaksi, lblTanggal, lblPelanggan, lblDetailBarang, lblTotal;
    private JTextField txtKodeTransaksi, txtTanggal, txtTotal;
    private JTable tableDetailBarang;
    private JScrollPane scrollPane;
    private JButton btnTambah, btnHapus, btnSimpan, btnKeluar;

    public form_penjualan() {
        initializeComponents();
        setupEventHandlers();
        buatIdPesananBaru();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    private Runnable onDataSaved;

    public form_penjualan(Runnable onDataSaved) {
        this();
        this.onDataSaved = onDataSaved;

    }

    private void initializeComponents() {
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        setTitle("Form Penjualan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(815, 660);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID Barang", "Nama Barang", "Harga Satuan", "Jumlah", "Subtotal"};
        modelDetailBarang = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        createComponents();
        layoutComponents();

        txtKodeTransaksi.putClientProperty("JTextField.placeholderText", "ID Pesanan");
        txtTanggal.putClientProperty("JTextField.placeholderText", "Tanggal");
        txtTotal.putClientProperty("JTextField.placeholderText", "Total");
    }

    private void createComponents() {
        lblTitle = new JLabel("FORM PENJUALAN", SwingConstants.CENTER);
        lblTitle.setFont(lblTitle.getFont().deriveFont(24f));

        lblKodeTransaksi = new JLabel("ID Pesanan:");
        lblTanggal = new JLabel("Tanggal:");
        lblPelanggan = new JLabel("Pelanggan:");
        lblDetailBarang = new JLabel("Detail Barang:");
        lblTotal = new JLabel("Total:");
        lblTotal.setFont(lblTotal.getFont().deriveFont(18f));

        txtKodeTransaksi = new JTextField();
        txtKodeTransaksi.setEditable(false);

        txtTanggal = new JTextField();
        txtTanggal.setEditable(false);
        txtTanggal.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        txtTotal = new JTextField("Rp 0");
        txtTotal.setEditable(false);
        txtTotal.setFont(txtTotal.getFont().deriveFont(18f));
        txtTotal.setHorizontalAlignment(SwingConstants.CENTER);

        tableDetailBarang = new JTable(modelDetailBarang);
        scrollPane = new JScrollPane(tableDetailBarang);

        btnTambah = new JButton("Tambah Barang");
        btnHapus = new JButton("Hapus Barang");
        btnSimpan = new JButton("Simpan Transaksi");
        btnKeluar = new JButton("Keluar");

        btnSimpan.setBackground(new java.awt.Color(0, 204, 51));
        btnSimpan.setForeground(java.awt.Color.WHITE);
        btnSimpan.setFont(btnSimpan.getFont().deriveFont(12f));

        btnKeluar.setBackground(new java.awt.Color(255, 51, 51));
        btnKeluar.setForeground(java.awt.Color.WHITE);
        btnKeluar.setFont(btnKeluar.getFont().deriveFont(12f));

        txtCariPelanggan = new JTextField();
        txtCariPelanggan.putClientProperty("JTextField.placeholderText", "Masukkan ID Pelanggan");

        btnCariPelanggan = new JButton("Cari");

        lblInfoPelanggan = new JLabel("Pelanggan: Umum");
        lblInfoPelanggan.setFont(lblInfoPelanggan.getFont().deriveFont(12f));

        lblDiskon = new JLabel("Diskon:");
        txtDiskon = new JTextField("0%");
        txtDiskon.setEditable(false);
        txtDiskon.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void layoutComponents() {
        lblTitle.setBounds(0, 20, 800, 30);
        add(lblTitle);

        lblKodeTransaksi.setBounds(50, 70, 100, 25);
        txtKodeTransaksi.setBounds(160, 70, 200, 25);
        add(lblKodeTransaksi);
        add(txtKodeTransaksi);

        lblTanggal.setBounds(50, 105, 100, 25);
        txtTanggal.setBounds(160, 105, 200, 25);
        add(lblTanggal);
        add(txtTanggal);

        lblPelanggan.setBounds(50, 140, 100, 25);
        add(lblPelanggan);

        lblPelanggan.setBounds(50, 140, 100, 25);
        txtCariPelanggan.setBounds(160, 140, 150, 25);
        btnCariPelanggan.setBounds(320, 140, 60, 25);
        lblInfoPelanggan.setBounds(160, 170, 300, 80);
        add(lblPelanggan);
        add(txtCariPelanggan);
        add(btnCariPelanggan);
        add(lblInfoPelanggan);

        lblDiskon.setBounds(450, 140, 60, 25);
        txtDiskon.setBounds(520, 140, 100, 25);
        add(lblDiskon);
        add(txtDiskon);

        lblTotal.setBounds(450, 70, 60, 25);
        txtTotal.setBounds(520, 70, 200, 50);
        add(lblTotal);
        add(txtTotal);

        btnTambah.setBounds(500, 235, 120, 30);
        btnHapus.setBounds(630, 235, 120, 30);
        add(btnTambah);
        add(btnHapus);

        lblDetailBarang.setBounds(50, 240, 150, 25);
        scrollPane.setBounds(50, 270, 700, 250);
        add(lblDetailBarang);
        add(scrollPane);

        btnSimpan.setBounds(500, 540, 120, 35);
        btnKeluar.setBounds(630, 540, 120, 35);
        add(btnSimpan);
        add(btnKeluar);
    }

    private void setupEventHandlers() {
        btnTambah.addActionListener(e -> showTambahBarangDialog());
        btnHapus.addActionListener(e -> hapusBarangTerpilih());
        btnSimpan.addActionListener(e -> simpanTransaksi());
        btnKeluar.addActionListener(e -> keluarAplikasi());

        btnCariPelanggan.addActionListener(e -> cariPelanggan());
        txtCariPelanggan.addActionListener(e -> cariPelanggan());
    }

    private void showTambahBarangDialog() {
        FormTambahBarang dialog = new FormTambahBarang(this, modelDetailBarang);
        dialog.setVisible(true);
        hitungTotal();
    }

    private void hapusBarangTerpilih() {
        int selectedRow = tableDetailBarang.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Hapus barang terpilih?", "Konfirmasi",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                modelDetailBarang.removeRow(selectedRow);
                hitungTotal();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih barang yang akan dihapus!");
        }
    }

    private void cariPelanggan() {
        String inputId = txtCariPelanggan.getText().trim();

        if (inputId.isEmpty()) {
            resetPelanggan();
            return;
        }

        try {
            Koneksi koneksi = new Koneksi();
            conn = koneksi.connect();

            String sqlPelanggan = "SELECT id_pelanggan, nama_pelanggan FROM pelanggan WHERE id_pelanggan = ?";
            PreparedStatement pstmt = conn.prepareStatement(sqlPelanggan);
            pstmt.setString(1, inputId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                idPelanggan = rs.getString("id_pelanggan");
                namaPelanggan = rs.getString("nama_pelanggan");

                ambilDataTransaksiSebelumnya();
                hitungDiskon();
                updateInfoPelanggan();
                hitungTotal();

            } else {
                JOptionPane.showMessageDialog(this, "Pelanggan dengan ID " + inputId + " tidak ditemukan!");
                resetPelanggan();
            }

            rs.close();
            pstmt.close();
            conn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error mencari pelanggan: " + e.getMessage());
            e.printStackTrace();
            resetPelanggan();
        }
    }

    private void ambilDataTransaksiSebelumnya() throws Exception {
        String sql = "SELECT COUNT(*) as jumlah_transaksi, COALESCE(SUM(total_harga), 0) as total_belanja "
                + "FROM pesanan WHERE id_pelanggan = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, idPelanggan);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            jumlahTransaksiSebelumnya = rs.getInt("jumlah_transaksi");
            totalBelanjaSebelumnya = rs.getDouble("total_belanja");
        }

        rs.close();
        pstmt.close();
    }

    private void hitungDiskon() {
        persentaseDiskon = 0;

        boolean belanjaLebih8Juta = totalBelanjaSebelumnya >= 8000000;
        boolean transaksiLebih10Kali = jumlahTransaksiSebelumnya >= 10;

        if (belanjaLebih8Juta && transaksiLebih10Kali) {
            persentaseDiskon = 15;
        } else if (belanjaLebih8Juta) {
            persentaseDiskon = 10;
        } else if (transaksiLebih10Kali) {
            persentaseDiskon = 8;
        }
    }

    private void updateInfoPelanggan() {
        String info = String.format("<html>Pelanggan: %s<br>Belanja: %s<br>Transaksi: %d kali</html>",
                namaPelanggan,
                currencyFormat.format(totalBelanjaSebelumnya),
                jumlahTransaksiSebelumnya);

        lblInfoPelanggan.setText(info);
        txtDiskon.setText(String.format("%.0f%%", persentaseDiskon));

        if (persentaseDiskon > 0) {
            txtDiskon.setBackground(new java.awt.Color(144, 238, 144));
            lblInfoPelanggan.setForeground(new java.awt.Color(0, 128, 0));
        } else {
            txtDiskon.setBackground(java.awt.Color.WHITE);
            lblInfoPelanggan.setForeground(java.awt.Color.BLACK);
        }
    }

    private void resetPelanggan() {
        idPelanggan = null;
        namaPelanggan = "";
        totalBelanjaSebelumnya = 0;
        jumlahTransaksiSebelumnya = 0;
        persentaseDiskon = 0;

        lblInfoPelanggan.setText("Pelanggan: Umum");
        lblInfoPelanggan.setForeground(java.awt.Color.BLACK);
        txtDiskon.setText("0%");
        txtDiskon.setBackground(java.awt.Color.WHITE);

        hitungTotal();
    }

    private void hitungTotal() {
        double subtotal = 0;
        for (int i = 0; i < modelDetailBarang.getRowCount(); i++) {
            String subtotalStr = modelDetailBarang.getValueAt(i, 4).toString();
            subtotal += Double.parseDouble(subtotalStr);
        }

        double nilaiDiskon = subtotal * (persentaseDiskon / 100);
        double total = subtotal - nilaiDiskon;

        if (persentaseDiskon > 0) {
            String formatTotal = String.format("%s - %s = %s",
                    currencyFormat.format(subtotal),
                    currencyFormat.format(nilaiDiskon),
                    currencyFormat.format(total));
            txtTotal.setText(formatTotal);
        } else {
            txtTotal.setText(currencyFormat.format(total));
        }
    }

    private void simpanTransaksi() {
        if (modelDetailBarang.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tambahkan barang terlebih dahulu!");
            return;
        }

        try {
            simpanPesanan();
            simpanDetailPesanan();
            updateDataBarang();

            JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan!");
            resetForm();

            if (onDataSaved != null) {
                onDataSaved.run();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan transaksi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void simpanPesanan() throws Exception {
        double subtotal = 0;
        for (int i = 0; i < modelDetailBarang.getRowCount(); i++) {
            String subtotalStr = modelDetailBarang.getValueAt(i, 4).toString();
            subtotal += Double.parseDouble(subtotalStr);
        }

        double nilaiDiskon = subtotal * (persentaseDiskon / 100);
        double totalHarga = subtotal - nilaiDiskon;

        Koneksi koneksi = new Koneksi();
        conn = koneksi.connect();

        String sql = "INSERT INTO pesanan (id_pesanan, tanggal, total_harga, id_pelanggan, id_karyawan, diskon_persen, nilai_diskon) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, idPesanan);
        pstmt.setDate(2, new java.sql.Date(new Date().getTime()));
        pstmt.setDouble(3, totalHarga);
        pstmt.setString(4, idPelanggan);
        pstmt.setString(5, null);
        pstmt.setDouble(6, persentaseDiskon);
        pstmt.setDouble(7, nilaiDiskon);

        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }

    private void updateDataBarang() throws Exception {
        Koneksi koneksi = new Koneksi();
        conn = koneksi.connect();

        String sql = "UPDATE barang SET stok = stok - ? WHERE id_barang = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        for (int i = 0; i < modelDetailBarang.getRowCount(); i++) {
            String idBarang = modelDetailBarang.getValueAt(i, 0).toString();
            int jumlah = Integer.parseInt(modelDetailBarang.getValueAt(i, 3).toString());

            pstmt.setInt(1, jumlah);
            pstmt.setString(2, idBarang);
            pstmt.executeUpdate();
        }

        pstmt.close();
        conn.close();
    }

    private void simpanDetailPesanan() throws Exception {
        Koneksi koneksi = new Koneksi();
        conn = koneksi.connect();

        String sql = "INSERT INTO detail_pesanan (id_pesanan, id_barang, harga_satuan, jumlah) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        for (int i = 0; i < modelDetailBarang.getRowCount(); i++) {
            String idBarang = modelDetailBarang.getValueAt(i, 0).toString();
            double hargaSatuan = Double.parseDouble(modelDetailBarang.getValueAt(i, 2).toString());
            int jumlah = Integer.parseInt(modelDetailBarang.getValueAt(i, 3).toString());

            pstmt.setString(1, idPesanan);
            pstmt.setString(2, idBarang);
            pstmt.setDouble(3, hargaSatuan);
            pstmt.setInt(4, jumlah);

            pstmt.executeUpdate();
        }

        pstmt.close();
        conn.close();
    }

    private void resetForm() {
        modelDetailBarang.setRowCount(0);
        txtTotal.setText(currencyFormat.format(0));
        txtCariPelanggan.setText("");
        resetPelanggan();
        buatIdPesananBaru();
    }

    private void keluarAplikasi() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Keluar dari aplikasi?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION);

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

    private void buatIdPesananBaru() {
        String prefix = "PSN";
        String tanggalHariIni = new SimpleDateFormat("yyyyMMdd").format(new Date());

        try {
            Koneksi koneksi = new Koneksi();
            conn = koneksi.connect();

            String sql = "SELECT COUNT(*) AS jumlah FROM pesanan WHERE DATE(tanggal) = CURDATE()";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            int nomorUrut = 1;
            if (rs.next()) {
                nomorUrut = rs.getInt("jumlah") + 1;
            }

            String nomorStr = String.format("%03d", nomorUrut);
            idPesanan = prefix + "-" + tanggalHariIni + "-" + nomorStr;

            txtKodeTransaksi.setText(idPesanan);

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            idPesanan = prefix + "-" + tanggalHariIni + "-001";
            txtKodeTransaksi.setText(idPesanan);
            JOptionPane.showMessageDialog(this, "Warning: Menggunakan ID default karena error database");
        }
    }

    public class FormTambahBarang extends JDialog {

        private JTextField txtId, txtNama, txtHarga, txtJumlah;
        private JButton btnCari, btnOK, btnCancel;
        private DefaultTableModel modelDetail;

        public FormTambahBarang(JFrame parent, DefaultTableModel modelDetail) {
            super(parent, "Tambah Barang", true);
            this.modelDetail = modelDetail;
            initDialog();
        }

        private void initDialog() {
            setLayout(new GridLayout(6, 2, 10, 10));
            setSize(400, 300);
            setLocationRelativeTo(getParent());

            txtId = new JTextField();
            txtId.setEditable(false);
            txtNama = new JTextField();
            txtNama.setEditable(false);
            txtHarga = new JTextField();
            txtHarga.setEditable(false);
            txtJumlah = new JTextField("1");

            btnCari = new JButton("Cari Barang");
            btnOK = new JButton("Tambah");
            btnCancel = new JButton("Batal");

            add(new JLabel("ID Barang:"));
            add(txtId);
            add(new JLabel("Nama Barang:"));
            add(txtNama);
            add(new JLabel("Harga Satuan:"));
            add(txtHarga);
            add(new JLabel("Jumlah:"));
            add(txtJumlah);
            add(btnCari);
            add(new JLabel());
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
            txtHarga.setText(formCari.getHarga());
        }

        private void tambahBarang() {
            if (txtId.getText().isEmpty() || txtJumlah.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Lengkapi data barang dan jumlah!");
                return;
            }

            try {
                double harga = Double.parseDouble(txtHarga.getText());
                int jumlah = Integer.parseInt(txtJumlah.getText());

                if (jumlah <= 0) {
                    JOptionPane.showMessageDialog(this, "Jumlah harus lebih dari 0!");
                    return;
                }
                double subtotal = harga * jumlah;

                boolean sudahAda = false;
                for (int i = 0; i < modelDetail.getRowCount(); i++) {
                    if (modelDetail.getValueAt(i, 0).toString().equals(txtId.getText())) {
                        int jumlahLama = Integer.parseInt(modelDetail.getValueAt(i, 3).toString());
                        int jumlahBaru = jumlahLama + jumlah;
                        double subtotalBaru = harga * jumlahBaru;

                        modelDetail.setValueAt(jumlahBaru, i, 3);
                        modelDetail.setValueAt(subtotalBaru, i, 4);
                        sudahAda = true;
                        break;
                    }
                }

                if (!sudahAda) {
                    modelDetail.addRow(new Object[]{
                        txtId.getText(),
                        txtNama.getText(),
                        harga,
                        jumlah,
                        subtotal
                    });
                }

                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Format jumlah tidak valid!");
            }
        }
    }

    public class FormPilihBarang extends JDialog {

        private JTable tableBarang;
        private DefaultTableModel modelBarang;
        private JTextField txtCari;
        private String id = "", nama = "", harga = "";

        public FormPilihBarang(JDialog parent) {
            super(parent, "Pilih Barang", true);
            initDialog();
            loadDataBarang();
        }

        private void initDialog() {
            setLayout(new BorderLayout(10, 10));
            setSize(600, 400);
            setLocationRelativeTo(getParent());

            JPanel panelCari = new JPanel(new BorderLayout());
            panelCari.add(new JLabel("Cari: "), BorderLayout.WEST);
            txtCari = new JTextField();
            txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    cariBarang();
                }
            });
            panelCari.add(txtCari, BorderLayout.CENTER);
            add(panelCari, BorderLayout.NORTH);

            String[] kolom = {"ID", "Nama Barang", "Harga"};
            modelBarang = new DefaultTableModel(kolom, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            tableBarang = new JTable(modelBarang);
            tableBarang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tableBarang.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        pilihBarang();
                    }
                }
            });

            JScrollPane scrollPane = new JScrollPane(tableBarang);
            add(scrollPane, BorderLayout.CENTER);

            JPanel panelTombol = new JPanel();
            JButton btnPilih = new JButton("Pilih");
            JButton btnBatal = new JButton("Batal");

            btnPilih.addActionListener(e -> pilihBarang());
            btnBatal.addActionListener(e -> dispose());

            panelTombol.add(btnPilih);
            panelTombol.add(btnBatal);
            add(panelTombol, BorderLayout.SOUTH);
        }

        private void loadDataBarang() {
            try {
                Koneksi koneksi = new Koneksi();
                Connection conn = koneksi.connect();

                String sql = "SELECT id_barang, nama_barang, harga_jual FROM barang where status = 'Aktif' ORDER BY nama_barang";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                modelBarang.setRowCount(0);
                while (rs.next()) {
                    modelBarang.addRow(new Object[]{
                        rs.getString("id_barang"),
                        rs.getString("nama_barang"),
                        rs.getDouble("harga_jual")
                    });
                }

                rs.close();
                stmt.close();
                conn.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void cariBarang() {
            String keyword = txtCari.getText().trim().toLowerCase();

            if (keyword.isEmpty()) {
                loadDataBarang();
                return;
            }

            try {
                Koneksi koneksi = new Koneksi();
                Connection conn = koneksi.connect();

                String sql = "SELECT id_barang, nama_barang, stok, harga_jual "
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
            int row = tableBarang.getSelectedRow();
            if (row >= 0) {
                id = modelBarang.getValueAt(row, 0).toString();
                nama = modelBarang.getValueAt(row, 1).toString();
                harga = modelBarang.getValueAt(row, 2).toString();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Pilih barang terlebih dahulu!");
            }
        }

        public String getId() {
            return id;
        }

        public String getNama() {
            return nama;
        }

        public String getHarga() {
            return harga;
        }
    }

    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new form_penjualan().setVisible(true);
        });
    }
}
