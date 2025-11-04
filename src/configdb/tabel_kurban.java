/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package configdb;

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

/**
 * 
 * @author Welcomp
 */

public class tabel_kurban {
    private String namadb = "pbo2_2310010327"; 
    private String url = "jdbc:mysql://localhost:3306/" + namadb;
    private String username = "root";
    private String password = "";
    private Connection koneksi;

    // Variabel penampung data
    public Integer VAR_id_kurban = null;
    public String VAR_nama = null;
    public String VAR_jenis_kurban = null;
    public String VAR_tanggal_kurban = null;
    public String VAR_catatan = null;
    public String VAR_tanggal_dibuat = null;
    public boolean validasi = false;

    // Constructor: Membuat koneksi ke database
    public tabel_kurban() {
        try {
            Driver mysqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);
            koneksi = DriverManager.getConnection(url, username, password);
            System.out.println("Koneksi ke database berhasil (tabel_kurban)");
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, "Koneksi gagal: " + error.getMessage());
        }
    }

    // SIMPAN 
    public void simpanKurban(int id_kurban, String nama, String jenis_kurban, String tanggal_kurban,
                             String catatan, String tanggal_dibuat) {
        try {
            String cekPrimary = "SELECT * FROM tabel_kurban WHERE id_kurban = '" + id_kurban + "'";
            Statement check = koneksi.createStatement();
            ResultSet data = check.executeQuery(cekPrimary);

            if (data.next()) {
                JOptionPane.showMessageDialog(null, "ID kurban sudah terdaftar!");
                this.VAR_id_kurban = data.getInt("id_kurban");
                this.VAR_nama = data.getString("nama");
                this.VAR_jenis_kurban = data.getString("jenis_kurban");
                this.VAR_tanggal_kurban = data.getString("tanggal_kurban");
                this.VAR_catatan = data.getString("catatan");
                this.VAR_tanggal_dibuat = data.getString("tanggal_dibuat");
                this.validasi = true;
            } else {
                this.validasi = false;
                String sql = "INSERT INTO tabel_kurban(id_kurban, nama, jenis_kurban, tanggal_kurban, catatan, tanggal_dibuat) "
                           + "VALUES('" + id_kurban + "', '" + nama + "', '" + jenis_kurban + "', '" + tanggal_kurban + "', '" 
                           + catatan + "', '" + tanggal_dibuat + "')";
                Statement perintah = koneksi.createStatement();
                perintah.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Data kurban berhasil disimpan!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menyimpan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // UBAH 
    public void ubahKurban(int id_kurban, String nama, String jenis_kurban, String tanggal_kurban,
                           String catatan, String tanggal_dibuat) {
        try {
            String sql = "UPDATE tabel_kurban SET nama=?, jenis_kurban=?, tanggal_kurban=?, catatan=?, tanggal_dibuat=? WHERE id_kurban=?";
            PreparedStatement perintah = koneksi.prepareStatement(sql);
            perintah.setString(1, nama);
            perintah.setString(2, jenis_kurban);
            perintah.setString(3, tanggal_kurban);
            perintah.setString(4, catatan);
            perintah.setString(5, tanggal_dibuat);
            perintah.setInt(6, id_kurban);
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data kurban berhasil diubah!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengubah: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // HAPUS
    public void hapusKurban(int id_kurban) {
        try {
            String sql = "DELETE FROM tabel_kurban WHERE id_kurban='" + id_kurban + "'";
            Statement perintah = koneksi.createStatement();
            perintah.execute(sql);
            JOptionPane.showMessageDialog(null, "Data kurban berhasil dihapus!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menghapus: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // TAMPIL DATA 
    public ResultSet getDataKurban() {
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM tabel_kurban";
            Statement perintah = koneksi.createStatement();
            rs = perintah.executeQuery(sql);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data kurban: " + e.getMessage());
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
               modelTable.addColumn("Tanggal Kurban");
               modelTable.addColumn("Jenis Kurban");
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
}
