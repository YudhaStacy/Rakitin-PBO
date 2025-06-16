package Tampilan;


import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Locale;

public class DashboardAppAdmin extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JButton selectedButton;

    Connection conn;

    public DashboardAppAdmin() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Dashboard Rakitin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        contentPanel.add(createSupplierPanel(), "Supplier");
        contentPanel.add(createPenjualanPanel(), "Penjualan");
        contentPanel.add(createPembelianPanel(), "Pembelian");
        contentPanel.add(createBarangPanel(), "Barang");
        contentPanel.add(createStokOpnamePanel(), "Stok Opname");
        contentPanel.add(createKaryawanPanel(), "Karyawan");
        contentPanel.add(createPelangganPanel(), "Pelanggan");

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

        String[] menuItems = {"Supplier", "Penjualan", "Pembelian", "Barang", "Stok Opname", "Karyawan", "Pelanggan"};
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

    
    //SUPPLIER
    private JTable createSupplierTable() {
        String[] columns = {"ID", "Nama Supplier", "Alamat", "Telepon"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);

        try {
            Koneksi koneksi1 = new Koneksi();
            conn = koneksi1.connect();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery("SELECT * FROM pemasok");

            while (res.next()) {
                String id = res.getString("id_pemasok");
                String nama = res.getString("nama_pemasok");
                String alamat = res.getString("alamat");
                String telepon = res.getString("no_tlp");
                model.addRow(new Object[]{id, nama, alamat, telepon});
            }

            res.close();
            stm.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal load data supplier: " + e.getMessage());
        }

        return table;
    }

    private JPanel createSupplierPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel headerLabel = new JLabel("Manajemen Supplier");
        headerLabel.setFont(new Font("SF Pro Text", Font.BOLD, 28));
        panel.add(headerLabel, BorderLayout.NORTH);

        JTable table = createSupplierTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(20, 0, 0, 0));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");

        btnTambah.addActionListener(e -> {
            form_supplier form = new form_supplier(() -> {
                DefaultTableModel newModel = (DefaultTableModel) createSupplierTable().getModel();
                table.setModel(newModel);
            });
            form.setVisible(true);
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Pilih data yang akan diedit.");
                return;
            }

            String id = table.getValueAt(row, 0).toString();
            String nama = table.getValueAt(row, 1).toString();
            String alamat = table.getValueAt(row, 2).toString();
            String telp = table.getValueAt(row, 3).toString();

            form_supplier form = new form_supplier(() -> {
                DefaultTableModel modelBaru = (DefaultTableModel) createSupplierTable().getModel();
                table.setModel(modelBaru);
            });
            form.setFormData(id, nama, telp, alamat);
            form.setVisible(true);
        });

        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Pilih data yang akan dihapus.");
                return;
            }

            String id = table.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(null, "Yakin hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection conn = new Koneksi().connect();
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM pemasok WHERE id_pemasok=?");
                    stmt.setString(1, id);
                    stmt.executeUpdate();

                    DefaultTableModel modelBaru = (DefaultTableModel) createSupplierTable().getModel();
                    table.setModel(modelBaru);
                    JOptionPane.showMessageDialog(null, "Data dihapus.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Gagal hapus data: " + ex.getMessage());
                }
            }
        });
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // BARANG
    private JTable createBarangTable() {
        String[] columns = {"ID Barang", "Nama Barang", "Stok", "Satuan", "Harga Beli", "Harga Jual", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);

        try {
            Koneksi koneksi1 = new Koneksi();
            conn = koneksi1.connect();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery("SELECT * FROM barang");

            while (res.next()) {
                String id = res.getString("id_barang");
                String nama = res.getString("nama_barang");
                int stok = res.getInt("Stok");
                String satuan = res.getString("satuan");
                String hargaBeli = res.getString("harga_beli");
                String hargaJual = res.getString("harga_jual");
                String status = res.getString("status");
                model.addRow(new Object[]{id, nama, stok, satuan, hargaBeli, hargaJual, status});
            }
            res.close();
            stm.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal load data Barang: " + e.getMessage());
        }

        return table;
    }

    private JPanel createBarangPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel headerLabel = new JLabel("Manajemen Barang");
        headerLabel.setFont(new Font("SF Pro Text", Font.BOLD, 28));
        panel.add(headerLabel, BorderLayout.NORTH);

        JTable table = createBarangTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(20, 0, 0, 0));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");

        btnTambah.addActionListener(e -> {
            form_barang formbar = new form_barang(() -> {
                DefaultTableModel newModel = (DefaultTableModel) createBarangTable().getModel();
                table.setModel(newModel);
            });
            formbar.setVisible(true);
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Pilih data yang akan diedit.");
                return;
            }

            String id = table.getValueAt(row, 0).toString();
            String nama = table.getValueAt(row, 1).toString();
            int stok = Integer.parseInt(table.getValueAt(row, 2).toString());
            String satuan = table.getValueAt(row, 3).toString();
            int hargaBeli = (int) Double.parseDouble(table.getValueAt(row, 4).toString());
            int hargaJual = (int) Double.parseDouble(table.getValueAt(row, 5).toString());
            String status = table.getValueAt(row, 6).toString();

            
            form_barang form = new form_barang(() -> {
                DefaultTableModel modelBaru = (DefaultTableModel) createBarangTable().getModel();
                table.setModel(modelBaru);
            });
            form.setFormData(id, nama, hargaBeli, hargaJual, status, satuan);
            form.setVisible(true);
        });

        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Pilih data yang akan dihapus.");
                return;
            }

            String id = table.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(null, "Yakin hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection conn = new Koneksi().connect();
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM barang WHERE id_barang=?");
                    stmt.setString(1, id);
                    stmt.executeUpdate();

                    DefaultTableModel modelBaru = (DefaultTableModel) createBarangTable().getModel();
                    table.setModel(modelBaru);
                    JOptionPane.showMessageDialog(null, "Data dihapus.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Gagal hapus data: " + ex.getMessage());
                }
            }
        });
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JTable createStokOpnameTable() {
        String[] columns = {"ID", "Tanggal", "Barang", "Stok Fisik", "Stok Tercatat", "Selisih", "Keterangan"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);

        try {
            Koneksi koneksi1 = new Koneksi();
            conn = koneksi1.connect();
            Statement stm = conn.createStatement();
            String sql = """
                SELECT so.id_stok_opname, so.tanggal, b.nama_barang, so.stok_fisik, b.stok, 
                       (so.stok_fisik - b.stok) AS selisih, so.keterangan
                FROM stok_opname so
                JOIN barang b ON so.id_barang = b.id_barang
                ORDER BY so.tanggal DESC
                """;
            ResultSet res = stm.executeQuery(sql);

            while (res.next()) {
                String id = res.getString("id_stok_opname");
                String tanggal = res.getString("tanggal");
                String namaBarang = res.getString("nama_barang");
                int stokFisik = res.getInt("stok_fisik");
                int stokTercatat = res.getInt("stok");
                int selisih = res.getInt("selisih");
                String keterangan = res.getString("keterangan");

                 System.out.println("ID: " + id + ", Stok Fisik: " + stokFisik + ", Stok Tercatat: " + stokTercatat + ", Selisih: " + selisih);
                model.addRow(new Object[]{id, tanggal, namaBarang, stokFisik, stokTercatat, selisih, keterangan});
            }

            res.close();
            stm.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal load data stok opname: " + e.getMessage());
        }
        return table;
    }


    private JPanel createStokOpnamePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel headerLabel = new JLabel("Stok Opname");
        headerLabel.setFont(new Font("SF Pro Text", Font.BOLD, 28));
        panel.add(headerLabel, BorderLayout.NORTH);

        JTable table = createStokOpnameTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(20, 0, 0, 0));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");

        btnTambah.addActionListener(e -> {
            stokOpname formStokOpname = new stokOpname();
            formStokOpname.setLocationRelativeTo(null);
            formStokOpname.setVisible(true);

            formStokOpname.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    DefaultTableModel modelBaru = (DefaultTableModel) createStokOpnameTable().getModel();
                    table.setModel(modelBaru);
                }
            });
        });

        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Pilih data yang akan dihapus.");
                return;
            }

            String id = table.getValueAt(row, 0).toString();

            int confirm = JOptionPane.showConfirmDialog(null, "Yakin hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection conn = new Koneksi().connect();
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM stok_opname WHERE id_stok_opname=?");
                    stmt.setString(1, id);
                    stmt.executeUpdate();

                    DefaultTableModel modelBaru = (DefaultTableModel) createStokOpnameTable().getModel();
                    table.setModel(modelBaru);
                    JOptionPane.showMessageDialog(null, "Data dihapus.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Gagal hapus data: " + ex.getMessage());
                }
            }
        });

        
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }
    
    
    //KARYAWAN
    private JTable createKaryawanTable() {
        String[] columns = {"ID Karyawan", "email", "password", "Nama Karyawan", "Telepon", "role", "status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);

        try {
            Koneksi koneksi1 = new Koneksi();
            conn = koneksi1.connect();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery("SELECT * FROM karyawan");

            while (res.next()) {
                String id = res.getString("id_karyawan");
                String email = res.getString("email");
                String password = res.getString("password");
                String nama = res.getString("nama_karyawan");
                String telepon = res.getString("no_tlp");
                String role = res.getString("role");
                String status = res.getString("status");

                model.addRow(new Object[]{id, email, password, nama, telepon, role, status});
            }

            res.close();
            stm.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal load data karyawan: " + e.getMessage());
        }
        return table;
    }

    private JPanel createKaryawanPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel headerLabel = new JLabel("Manajemen Karyawan");
        headerLabel.setFont(new Font("SF Pro Text", Font.BOLD, 28));
        panel.add(headerLabel, BorderLayout.NORTH);

        JTable table = createKaryawanTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(20, 0, 0, 0));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");

        btnTambah.addActionListener(e -> {
            form_karyawan form = new form_karyawan(() -> {
                DefaultTableModel newModel = (DefaultTableModel) createKaryawanTable().getModel();
                table.setModel(newModel);
            });
            form.setVisible(true);
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Pilih data karyawan yang akan diedit.");
                return;
            }

            String id = table.getValueAt(row, 0).toString();
            String email = table.getValueAt(row, 1).toString();
            String password = table.getValueAt(row, 2).toString();
            String nama = table.getValueAt(row, 3).toString();
            String telp = table.getValueAt(row, 4).toString();
            String role = table.getValueAt(row, 5).toString();
            String status = table.getValueAt(row, 6).toString();
           
            form_karyawan form = new form_karyawan(() -> {
                DefaultTableModel modelBaru = (DefaultTableModel) createKaryawanTable().getModel();
                table.setModel(modelBaru);
            });

            form.setFormData(id, email, password, nama, telp, role, status);
            form.setVisible(true);
        });

        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Pilih data yang akan dihapus.");
                return;
            }

            String id = table.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(null, "Yakin hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection conn = new Koneksi().connect();
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM karyawan WHERE id_karyawan=?");
                    stmt.setString(1, id);
                    stmt.executeUpdate();

                    DefaultTableModel modelBaru = (DefaultTableModel) createKaryawanTable().getModel();
                    table.setModel(modelBaru);
                    JOptionPane.showMessageDialog(null, "Data dihapus.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Gagal hapus data: " + ex.getMessage());
                }
            }
        });

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    //PELANGGAN
    private JTable createPelangganTable() {
        String[] columns = {"ID Pelanggan", "Nama Pelanggan", "Alamat", "Telepon"}; //total transaksi?
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);

        try {
            Koneksi koneksi1 = new Koneksi();
            conn = koneksi1.connect();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery("SELECT * FROM pelanggan");

            while (res.next()) {
                String id = res.getString("id_pelanggan");
                String nama = res.getString("nama_pelanggan");
                String alamat = res.getString("alamat");
                String telepon = res.getString("no_tlp");
                model.addRow(new Object[]{id, nama, alamat, telepon});
            }

            res.close();
            stm.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal load data supplier: " + e.getMessage());
        }

        return table;
    }

    private JPanel createPelangganPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel headerLabel = new JLabel("Manajemen Pelanggan");
        headerLabel.setFont(new Font("SF Pro Text", Font.BOLD, 28));
        panel.add(headerLabel, BorderLayout.NORTH);

        JTable table = createPelangganTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(20, 0, 0, 0));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");

        btnTambah.addActionListener(e -> {
                form_pelanggan form = new form_pelanggan(() -> { 
                DefaultTableModel newModel = (DefaultTableModel) createPelangganTable().getModel();
                table.setModel(newModel);
            });
            form.setVisible(true);
        });

        //edit
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Pilih data yang akan diedit.");
                return;
            }

            String id = table.getValueAt(row, 0).toString();
            String nama = table.getValueAt(row, 1).toString();
            String alamat = table.getValueAt(row, 2).toString();
            String telp = table.getValueAt(row, 3).toString();

            form_pelanggan form = new form_pelanggan(() -> {
                DefaultTableModel modelBaru = (DefaultTableModel) createPelangganTable().getModel();
                table.setModel(modelBaru);
            });
            form.setFormData(id, nama, telp, alamat);
            form.setVisible(true);
        });

        //hapus
        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Pilih data yang akan dihapus.");
                return;
            }

            String id = table.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(null, "Yakin hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection conn = new Koneksi().connect();
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM pelanggan WHERE id_pelanggan=?");
                    stmt.setString(1, id);
                    stmt.executeUpdate();
                    
                    DefaultTableModel modelBaru = (DefaultTableModel) createPelangganTable().getModel();
                    table.setModel(modelBaru);
                    JOptionPane.showMessageDialog(null, "Data dihapus.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Gagal hapus data: " + ex.getMessage());
                }
            }
        });

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
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
            new DashboardAppAdmin().setVisible(true);
        });
    }
}
