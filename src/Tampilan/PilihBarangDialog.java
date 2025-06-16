package Tampilan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import koneksi.Koneksi;
import com.formdev.flatlaf.FlatLightLaf;

class PilihBarangDialog extends JDialog {
    private JTable tableBarang;
    private JTextField txtCari;
    private TableRowSorter<DefaultTableModel> sorter;
    private DefaultTableModel model;
    private String selectedBarangId;
    private String selectedBarangNama;
    private int selectedBarangStok;
    private boolean isSelected = false;
    
    public PilihBarangDialog(Frame parent) {
        super(parent, "Pilih Barang", true);
        setFlatLightLaf();
        initComponents();
        loadDataBarang();
        setupTableSorter();
    }
    
    private void setFlatLightLaf() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void initComponents() {
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setSize(700, 500);
        setLocationRelativeTo(getParent());
        getRootPane().setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel headerLabel = new JLabel("Pilih Barang", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SF Pro Display", Font.BOLD, 18));
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBorder(new EmptyBorder(15, 0, 10, 0));
        
        txtCari = new JTextField();
        txtCari.setPreferredSize(new Dimension(0, 35));
        txtCari.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
        txtCari.putClientProperty("JTextField.placeholderText", "Ketik untuk mencari...");
        txtCari.putClientProperty("JTextField.showClearButton", true);
        
        JPanel searchFieldPanel = new JPanel(new BorderLayout());
        searchFieldPanel.add(txtCari, BorderLayout.CENTER);
        
        searchPanel.add(new JLabel("Pencarian: "), BorderLayout.WEST);
        searchPanel.add(searchFieldPanel, BorderLayout.CENTER);
        
        headerPanel.add(searchPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);
        
        String[] columns = {"ID Barang", "Nama Barang", "Stok Tercatat", "Satuan", "Harga", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) return Integer.class;
                return String.class;
            }
        };
        
        tableBarang = new JTable(model);
        tableBarang.setRowHeight(30);
        tableBarang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableBarang.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
        tableBarang.getTableHeader().setFont(new Font("SF Pro Text", Font.BOLD, 12));
        tableBarang.setShowVerticalLines(false);
        tableBarang.setIntercellSpacing(new Dimension(0, 1));

        tableBarang.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableBarang.getColumnModel().getColumn(1).setPreferredWidth(250);
        tableBarang.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableBarang.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableBarang.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableBarang.getColumnModel().getColumn(5).setPreferredWidth(100);
        
        tableBarang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    selectBarang();
                }
            }
        });
        
        tableBarang.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    selectBarang();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableBarang);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        
        JButton btnPilih = new JButton("Pilih");
        JButton btnBatal = new JButton("Batal");
        
        btnPilih.setPreferredSize(new Dimension(80, 35));
        btnPilih.setFont(new Font("SF Pro Text", Font.BOLD, 12));

        btnBatal.setPreferredSize(new Dimension(80, 35));
        btnBatal.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
        
        btnPilih.addActionListener(e -> selectBarang());
        btnBatal.addActionListener(e -> dispose());
        
        buttonPanel.add(btnBatal);
        buttonPanel.add(btnPilih);
        add(buttonPanel, BorderLayout.SOUTH);
        
        getRootPane().setDefaultButton(btnPilih);
        
        setupSearchListener();
    }
    
    private void setupTableSorter() {
        sorter = new TableRowSorter<>(model);
        tableBarang.setRowSorter(sorter);
    }
    
    private void setupSearchListener() {
        txtCari.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                performSearch();
            }
        });
    }
    
    private void performSearch() {
        String searchText = txtCari.getText().trim().toLowerCase();
        
        if (searchText.isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }
        
        List<RowFilter<Object, Object>> filters = new ArrayList<>();
        
        filters.add(RowFilter.regexFilter("(?i)" + searchText, 0));
        filters.add(RowFilter.regexFilter("(?i)" + searchText, 1));
        
        RowFilter<Object, Object> combinedFilter = RowFilter.orFilter(filters);
        sorter.setRowFilter(combinedFilter);
        
        updateSearchResultsLabel();
    }
    
    private void updateSearchResultsLabel() {
        int totalRows = model.getRowCount();
        int visibleRows = tableBarang.getRowCount();
        
        if (totalRows != visibleRows) {
            setTitle("Pilih Barang - Menampilkan " + visibleRows + " dari " + totalRows + " barang");
        } else {
            setTitle("Pilih Barang - " + totalRows + " barang");
        }
    }
    
    private void loadDataBarang() {
        model.setRowCount(0);
        
        try {
            Koneksi koneksi = new Koneksi();
            Connection conn = koneksi.connect();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM barang ORDER BY nama_barang";
            ResultSet rs = stmt.executeQuery(sql);
            
            int count = 0;
            while (rs.next()) {
                Object[] row = {
                    rs.getString("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getInt("stok"),
                    rs.getString("satuan"),
                    rs.getInt("harga_jual"),
                    rs.getString("status")
                };
                model.addRow(row);
                count++;
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
            setTitle("Pilih Barang - " + count + " barang");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Gagal memuat data barang: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void selectBarang() {
        int selectedRow = tableBarang.getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = tableBarang.convertRowIndexToModel(selectedRow);
            
            selectedBarangId = model.getValueAt(modelRow, 0).toString();
            selectedBarangNama = model.getValueAt(modelRow, 1).toString();
            selectedBarangStok = Integer.parseInt(model.getValueAt(modelRow, 2).toString());
            isSelected = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Pilih barang terlebih dahulu!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            SwingUtilities.invokeLater(() -> txtCari.requestFocusInWindow());
        }
    }
    
    public String getSelectedBarangId() { return selectedBarangId; }
    public String getSelectedBarangNama() { return selectedBarangNama; }
    public int getSelectedBarangStok() { return selectedBarangStok; }
    public boolean isBarangSelected() { return isSelected; }
}

class StokOpnameThemeUtil {
    public static void setFlatLightLaf() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("TextComponent.arc", 8);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void updateComponentUI(Component component) {
        SwingUtilities.updateComponentTreeUI(component);
    }
}