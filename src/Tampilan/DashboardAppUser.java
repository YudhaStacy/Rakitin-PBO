package Tampilan;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;

public class DashboardAppUser extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JButton selectedButton;
    
    Connection conn;
    
    public DashboardAppUser() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Dashboard XYZ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());
        
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);
        
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);
        
        contentPanel.add(createPembelianPanel(), "Pembelian");
        contentPanel.add(createPenjualanPanel(), "Penjualan");
        
        add(contentPanel, BorderLayout.CENTER);
        
        cardLayout.show(contentPanel, "Dashboard");
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(248, 249, 250));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));
        
        JLabel titleLabel = new JLabel("XYZ");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(108, 117, 125));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(titleLabel);
        
        sidebar.add(Box.createVerticalStrut(30));
        
        JLabel featuresLabel = new JLabel("Features");
        featuresLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        featuresLabel.setForeground(new Color(108, 117, 125));
        featuresLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(featuresLabel);
        
        sidebar.add(Box.createVerticalStrut(10));
        
        String[] menuItems = {"Pembelian","Penjualan"};

        for (int i = 0; i < menuItems.length; i++) {
            JButton menuButton = createMenuButton(menuItems[i], menuItems[i]);
            if (i == 0) {
                menuButton.setBackground(new Color(13, 110, 253));
                menuButton.setForeground(Color.WHITE);
                selectedButton = menuButton;
            }
            sidebar.add(menuButton);
            sidebar.add(Box.createVerticalStrut(5));
        }
        
        sidebar.add(Box.createVerticalGlue());
        
        JButton logoutButton = new JButton("Log Out");
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        logoutButton.addActionListener(e -> System.exit(0));
        sidebar.add(logoutButton);
        
        return sidebar;
    }
    
    private JButton createMenuButton(String text, String panelName) {
        JButton button = new JButton(text);
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(33, 37, 41));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedButton != null) {
                    selectedButton.setBackground(Color.WHITE);
                    selectedButton.setForeground(new Color(33, 37, 41));
                }
                
                button.setBackground(new Color(13, 110, 253));
                button.setForeground(Color.WHITE);
                selectedButton = button;
                
                cardLayout.show(contentPanel, panelName);
            }
        });
        
        return button;
    }
    
    private JTable createPembelianTable() {
        String[] columns = {"ID", "Nama Supplier", "Tanggal", "Total Transaksi"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);

        try {
            Koneksi koneksi1 = new Koneksi();
            conn = koneksi1.connect();
            Statement stm = conn.createStatement();
            String sql = """
                SELECT pb.id_pembelian, pb.tanggal, pb.total_harga,
                       ps.id_pemasok, ps.nama_pemasok
                FROM pembelian pb
                LEFT JOIN pemasok ps ON pb.id_pemasok = ps.id_pemasok
            """;
            ResultSet res = stm.executeQuery(sql);

            while (res.next()) {
                String id = res.getString("id_pembelian");
                String pemasok = res.getString("nama_pemasok");
                String tgl = res.getString("tanggal");
                String total = res.getString("total_harga");
                model.addRow(new Object[]{id, pemasok, tgl, total});
            }

            res.close();
            stm.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal load data supplier: " + e.getMessage());
        }

        return table;
    }

    private JPanel createPembelianPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel headerLabel = new JLabel("Data Pembelian");
        headerLabel.setFont(new Font("SF Pro Text", Font.BOLD, 28));
        panel.add(headerLabel, BorderLayout.NORTH);

        JTable table = createPembelianTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(20, 0, 0, 0));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnTambah = new JButton("Tambah");
        JButton btnDetail = new JButton("Detail");
        
        btnDetail.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String idPembelian = table.getValueAt(selectedRow, 0).toString();
                showPembelianDetail(idPembelian);
            } else {
                JOptionPane.showMessageDialog(this, "Silakan pilih data pembelian terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnDetail);

        btnTambah.addActionListener(e -> {
            form_pembelian form = new form_pembelian(() -> {
                DefaultTableModel newModel = (DefaultTableModel) createPembelianTable().getModel();
                table.setModel(newModel);
            });
            form.setVisible(true);
        });
        
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JTable createPenjualanTable() {
        String[] columns = {"ID", "Pelanggan", "Tanggal", "Total Harga", "Diskon", "Nilai Diskon"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);

        try {
            Koneksi koneksi1 = new Koneksi();
            conn = koneksi1.connect();
            Statement stm = conn.createStatement();
            String sql = """
                SELECT p.id_pesanan, p.tanggal, p.total_harga, 
                       p.diskon_persen, p.nilai_diskon,
                       pel.id_pelanggan, pel.nama_pelanggan
                FROM pesanan p
                LEFT JOIN pelanggan pel ON p.id_pelanggan = pel.id_pelanggan
            """;
            ResultSet res = stm.executeQuery(sql);

            while (res.next()) {
                String id = res.getString("id_pesanan");
                String pelanggan = res.getString("nama_pelanggan");
                String tgl = res.getString("tanggal");
                String total = res.getString("total_harga");
                String diskon = res.getString("diskon_persen");
                String nilaidiskon = res.getString("nilai_diskon");
                model.addRow(new Object[]{id, pelanggan, tgl, total, diskon, nilaidiskon});
            }

            res.close();
            stm.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal load data supplier: " + e.getMessage());
        }

        return table;
    }

    private JPanel createPenjualanPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel headerLabel = new JLabel("Data Penjualan");
        headerLabel.setFont(new Font("SF Pro Text", Font.BOLD, 28));
        panel.add(headerLabel, BorderLayout.NORTH);

        JTable table = createPenjualanTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(20, 0, 0, 0));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnTambah = new JButton("Tambah");
        JButton btnDetail = new JButton("Detail");
        
        btnDetail.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String idPenjualan = table.getValueAt(selectedRow, 0).toString();
                showPenjualanDetail(idPenjualan);
            } else {
                JOptionPane.showMessageDialog(this, "Silakan pilih data penjualan terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnDetail);

        btnTambah.addActionListener(e -> {
            form_penjualan form = new form_penjualan(() -> {
                DefaultTableModel newModel = (DefaultTableModel) createPenjualanTable().getModel();
                table.setModel(newModel);
            });
            form.setVisible(true);
        });
        
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }
    
    private void showPembelianDetail(String idPembelian) {
        JDialog detailDialog = new JDialog(this, "Detail Pembelian", true);
        detailDialog.setSize(650, 550);
        detailDialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        JLabel headerLabel = new JLabel("Detail Pembelian ID: " + idPembelian);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        try {
            Koneksi koneksi1 = new Koneksi();
            conn = koneksi1.connect();

            String query = "SELECT * FROM pembelian WHERE id_pembelian = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, idPembelian);
            ResultSet res = pst.executeQuery();

            if (res.next()) {
                addDetailRow(infoPanel, gbc, 0, "ID Pembelian:", res.getString("id_pembelian"));
                addDetailRow(infoPanel, gbc, 1, "ID Pemasok:", res.getString("id_pemasok"));
                addDetailRow(infoPanel, gbc, 2, "Tanggal:", res.getString("tanggal"));

                String totalHarga = res.getString("total_harga");
                if (totalHarga != null && !totalHarga.isEmpty()) {
                    try {
                        double total = Double.parseDouble(totalHarga);
                        addDetailRow(infoPanel, gbc, 3, "Total Harga:",
                                "Rp " + NumberFormat.getInstance(new Locale("id", "ID")).format(total));
                    } catch (NumberFormatException e) {
                        addDetailRow(infoPanel, gbc, 3, "Total Harga:", totalHarga);
                    }
                } else {
                    addDetailRow(infoPanel, gbc, 3, "Total Harga:", "-");
                }
            }
            res.close();
            pst.close();

            String[] columns = {"Nama Barang", "Jumlah"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            JTable detailTable = new JTable(model);
            detailTable.setRowHeight(25);
            detailTable.setFont(new Font("Arial", Font.PLAIN, 13));
            detailTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

            String detailQuery = """
                SELECT dp.jumlah, b.nama_barang
                FROM detail_pembelian dp
                LEFT JOIN barang b ON dp.id_barang = b.id_barang
                WHERE dp.id_pembelian = ?
            """;
            pst = conn.prepareStatement(detailQuery);
            pst.setString(1, idPembelian);
            res = pst.executeQuery();

            while (res.next()) {
                String jumlah = res.getString("jumlah");
                String namaBarang = res.getString("nama_barang");
                model.addRow(new Object[]{
                        namaBarang != null ? namaBarang : "-",
                        jumlah != null ? jumlah : "-"
                });
            }

            res.close();
            pst.close();
            conn.close();

            JScrollPane tableScrollPane = new JScrollPane(detailTable);
            tableScrollPane.setPreferredSize(new Dimension(580, 220));

            JLabel tableLabel = new JLabel("Detail Barang:");
            tableLabel.setFont(new Font("Arial", Font.BOLD, 15));
            tableLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel tablePanel = new JPanel(new BorderLayout());
            tablePanel.setBackground(Color.WHITE);
            tablePanel.add(tableScrollPane, BorderLayout.CENTER);
            tablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            centerPanel.add(infoPanel);
            centerPanel.add(Box.createVerticalStrut(10));
            centerPanel.add(tableLabel);
            centerPanel.add(Box.createVerticalStrut(5));
            centerPanel.add(tablePanel);

            mainPanel.add(centerPanel, BorderLayout.CENTER);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(detailDialog, "Error loading detail: " + e.getMessage());
            e.printStackTrace();
        }

        JButton closeButton = new JButton("Tutup");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 13));
        closeButton.addActionListener(e -> detailDialog.dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        detailDialog.add(mainPanel);
        detailDialog.setVisible(true);
    }

    private void showPenjualanDetail(String idPesanan) {
        JDialog detailDialog = new JDialog(this, "Detail Penjualan", true);
        detailDialog.setSize(650, 550);
        detailDialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel headerLabel = new JLabel("Detail Penjualan ID: " + idPesanan);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        try {
            Koneksi koneksi1 = new Koneksi();
            conn = koneksi1.connect();

            String query = """
                SELECT p.id_pesanan, p.tanggal, p.total_harga, p.diskon_persen, p.nilai_diskon,
                       pel.id_pelanggan, pel.nama_pelanggan
                FROM pesanan p
                LEFT JOIN pelanggan pel ON p.id_pelanggan = pel.id_pelanggan
                WHERE p.id_pesanan = ?
            """;

            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, idPesanan);
            ResultSet res = pst.executeQuery();

            if (res.next()) {
                addDetailRow(infoPanel, gbc, 0, "ID Pesanan:", res.getString("id_pesanan"));
                addDetailRow(infoPanel, gbc, 1, "Tanggal:", res.getString("tanggal"));

                double total = res.getDouble("total_harga");
                addDetailRow(infoPanel, gbc, 2, "Total Harga:", "Rp " + NumberFormat.getInstance(new Locale("id", "ID")).format(total));

                String diskonPersen = res.getString("diskon_persen");
                String nilaiDiskon = res.getString("nilai_diskon");
                addDetailRow(infoPanel, gbc, 3, "Diskon (%):", diskonPersen != null ? diskonPersen + "%" : "-");
                addDetailRow(infoPanel, gbc, 4, "Nilai Diskon:", nilaiDiskon != null ? "Rp " + NumberFormat.getInstance(new Locale("id", "ID")).format(Double.parseDouble(nilaiDiskon)) : "-");

                String idPelanggan = res.getString("id_pelanggan");
                String namaPelanggan = res.getString("nama_pelanggan");
                addDetailRow(infoPanel, gbc, 5, "Pelanggan:", idPelanggan + " / " + namaPelanggan);
            }
            res.close();
            pst.close();

           String[] columns = {"Nama Barang", "Jumlah", "Harga Satuan", "Subtotal"};
           DefaultTableModel model = new DefaultTableModel(columns, 0);
           JTable detailTable = new JTable(model);
           detailTable.setRowHeight(25);
           detailTable.setFont(new Font("Arial", Font.PLAIN, 13));
           detailTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

           String detailQuery = """
               SELECT dp.jumlah, b.harga_jual, b.nama_barang
               FROM detail_pesanan dp
               LEFT JOIN barang b ON dp.id_barang = b.id_barang
               WHERE dp.id_pesanan = ?
           """;
           pst = conn.prepareStatement(detailQuery);
           pst.setString(1, idPesanan);
           res = pst.executeQuery();

           while (res.next()) {
               String namaBarang = res.getString("nama_barang");
               int jumlah = res.getInt("jumlah");
               double hargaJual = res.getDouble("harga_jual");
               double subtotal = jumlah * hargaJual;

               model.addRow(new Object[]{
                   namaBarang != null ? namaBarang : "-",
                   jumlah,
                   "Rp " + NumberFormat.getInstance(new Locale("id", "ID")).format(hargaJual),
                   "Rp " + NumberFormat.getInstance(new Locale("id", "ID")).format(subtotal)
               });
           }

            res.close();
            pst.close();
            conn.close();

            JScrollPane tableScrollPane = new JScrollPane(detailTable);
            tableScrollPane.setPreferredSize(new Dimension(580, 220));

            JLabel tableLabel = new JLabel("Detail Barang:");
            tableLabel.setFont(new Font("Arial", Font.BOLD, 15));
            tableLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel tablePanel = new JPanel(new BorderLayout());
            tablePanel.add(tableScrollPane, BorderLayout.CENTER);
            tablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            centerPanel.add(infoPanel);
            centerPanel.add(Box.createVerticalStrut(10));
            centerPanel.add(tableLabel);
            centerPanel.add(Box.createVerticalStrut(5));
            centerPanel.add(tablePanel);

            mainPanel.add(centerPanel, BorderLayout.CENTER);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(detailDialog, "Error loading detail: " + e.getMessage());
            e.printStackTrace();
        }

        detailDialog.add(mainPanel);
        detailDialog.setVisible(true);
    }

    private void addDetailRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1;
        JLabel lblValue = new JLabel(value != null ? value : "-");
        panel.add(lblValue, gbc);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new DashboardAppUser().setVisible(true);
        });
    }
}