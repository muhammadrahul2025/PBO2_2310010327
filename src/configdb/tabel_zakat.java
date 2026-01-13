/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package configdb;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Driver;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * 
 * @author Welcomp
 */

public class tabel_zakat {
    private String namadb = "PBO2_2310010327";
    private String url = "jdbc:mysql://localhost:3306/" + namadb;
    private String username = "root";
    private String password = "";
    private Connection koneksi;

    // Variabel penampung data
    public Integer VAR_id_zakat = null;
    public String VAR_nama = null;
    public String VAR_jenis_zakat = null;
    public String VAR_tanggal_zakat = null;
    public Double VAR_jumlah = null;
    public String VAR_catatan = null;
    public String VAR_tanggal_dibuat = null;
    public boolean validasi = false;

    // Konstruktor (buat koneksi)
    public tabel_zakat() {
        try {
            Driver mysqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);
            koneksi = DriverManager.getConnection(url, username, password);
            System.out.println("Koneksi ke database berhasil (tabel_zakat)");
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, "Koneksi gagal: " + error.getMessage());
        }
    }

    // SIMPAN
    public void simpanZakat(int id_zakat, String nama, String jenis_zakat, String tanggal_zakat, 
                            Double jumlah, String catatan, String tanggal_dibuat) {
        try {
            // Cek apakah ID sudah ada
            String cekPrimary = "SELECT * FROM tabel_zakat WHERE id_zakat = '" + id_zakat + "'";
            Statement check = koneksi.createStatement();
            ResultSet data = check.executeQuery(cekPrimary);

            if (data.next()) {
                JOptionPane.showMessageDialog(null, "ID zakat sudah terdaftar!");
                this.VAR_id_zakat = data.getInt("id_zakat");
                this.VAR_nama = data.getString("nama");
                this.VAR_jenis_zakat = data.getString("jenis_zakat");
                this.VAR_tanggal_zakat = data.getString("tanggal_zakat");
                this.VAR_jumlah = data.getDouble("jumlah");
                this.VAR_catatan = data.getString("catatan");
                this.VAR_tanggal_dibuat = data.getString("tanggal_dibuat");
                this.validasi = true;
            } else {
                this.validasi = false;
                String sql = "INSERT INTO tabel_zakat(id_zakat, nama, jenis_zakat, tanggal_zakat, jumlah, catatan, tanggal_dibuat) "
                           + "VALUES('" + id_zakat + "', '" + nama + "', '" + jenis_zakat + "', '" + tanggal_zakat + "', "
                           + "'" + jumlah + "', '" + catatan + "', '" + tanggal_dibuat + "')";
                Statement perintah = koneksi.createStatement();
                perintah.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Data zakat berhasil disimpan!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menyimpan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // UBAH
    public void ubahZakat(int id_zakat, String nama, String jenis_zakat, String tanggal_zakat, 
                          Double jumlah, String catatan, String tanggal_dibuat) {
        try {
            String sql = "UPDATE tabel_zakat SET nama=?, jenis_zakat=?, tanggal_zakat=?, jumlah=?, catatan=?, tanggal_dibuat=? WHERE id_zakat=?";
            PreparedStatement perintah = koneksi.prepareStatement(sql);
            perintah.setString(1, nama);
            perintah.setString(2, jenis_zakat);
            perintah.setString(3, tanggal_zakat);
            perintah.setDouble(4, jumlah);
            perintah.setString(5, catatan);
            perintah.setString(6, tanggal_dibuat);
            perintah.setInt(7, id_zakat);
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data zakat berhasil diubah!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengubah: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // HAPUS
    public void hapusZakat(int id_zakat) {
        try {
            String sql = "DELETE FROM tabel_zakat WHERE id_zakat='" + id_zakat + "'";
            Statement perintah = koneksi.createStatement();
            perintah.execute(sql);
            JOptionPane.showMessageDialog(null, "Data zakat berhasil dihapus!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menghapus: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // TAMPIL DATA 
    public ResultSet getDataZakat() {
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM tabel_zakat";
            Statement perintah = koneksi.createStatement();
            rs = perintah.executeQuery(sql);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data zakat: " + e.getMessage());
        }
        return rs;
    }
     public void tampilData(JTable komponenTable, String SQL){
           try {
               Statement perintah = koneksi.createStatement();
               ResultSet data = perintah.executeQuery(SQL);
               ResultSetMetaData meta = data.getMetaData();
               int jumKolom = meta.getColumnCount();
               DefaultTableModel modelTable = new DefaultTableModel();
               modelTable.addColumn("ID");
               modelTable.addColumn("Nama");
               modelTable.addColumn("Jenis Zakat");
               modelTable.addColumn("Tanggal Zakat");
               modelTable.addColumn("Jumlah");
               modelTable.addColumn("Catatan");
               modelTable.addColumn("Tanggal Dibuat");
               modelTable.getDataVector().clear();
               modelTable.fireTableDataChanged();
               while(data.next()){
                   Object[] row = new Object[jumKolom];
                   for (int i = 1; i <= jumKolom; i++) {
                        row[i - 1] = data.getObject(i);                   }
                   modelTable.addRow(row);
               }
               komponenTable.setModel(modelTable);
           } catch (Exception e) {
           
        }
     }
           public void cetakLaporan(String fileLaporan, String SQL){
        try {
            File file = new File(fileLaporan);
            JasperDesign jasDes = JRXmlLoader.load(file);
            JRDesignQuery query = new JRDesignQuery();
            query.setText(SQL);
            jasDes.setQuery(query);
            JasperReport jr = JasperCompileManager.compileReport(jasDes);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, this.koneksi);
            JasperViewer.viewReport(jp);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
}
}
