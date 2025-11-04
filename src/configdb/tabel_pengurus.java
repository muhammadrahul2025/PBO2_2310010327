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

public class tabel_pengurus {
    private String namadb = "pbo2_2310010327"; 
    private String url = "jdbc:mysql://localhost:3306/" + namadb;
    private String username = "root";
    private String password = "";
    private Connection koneksi;

    // variabel penampung data
    public Integer VAR_id = null;
    public String VAR_nama = null;
    public String VAR_jabatan = null;
    public String VAR_foto = null;
    public String VAR_tanggal_dibuat = null;
    public boolean validasi = false;

    // constructor untuk koneksi
    public tabel_pengurus() {
        try {
            Driver mysqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);
            koneksi = DriverManager.getConnection(url, username, password);
            System.out.println("Koneksi ke database berhasil (tabel_pengurus)");
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, "Koneksi gagal: " + error.getMessage());
        }
    }

    // SIMPAN
    public void simpanPengurus(int id, String nama, String jabatan, String foto, String tanggal_dibuat) {
    try {
        String cekPrimary = "SELECT * FROM tabel_pengurus WHERE id = '" + id + "'";
        Statement check = koneksi.createStatement();
        ResultSet data = check.executeQuery(cekPrimary);

        if (data.next()) {
            JOptionPane.showMessageDialog(null, "ID Pengurus sudah ada!");
        } else {
            String sql = "INSERT INTO tabel_pengurus (id, nama, foto, jabatan, tanggal_dibuat) "
                       + "VALUES ('" + id + "', '" + nama + "', '" + foto+ "', '" + jabatan + "', '" + tanggal_dibuat + "')";
            Statement perintah = koneksi.createStatement();
            perintah.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "Data pengurus berhasil disimpan!");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menyimpan: " + e.getMessage());
        e.printStackTrace();
    }
}

    // UBAH
    public void ubahPengurus(Integer id, String nama, String jabatan, String foto, String tanggal_dibuat) {
        try {
            String sql = "UPDATE tabel_pengurus SET nama=?, jabatan=?, foto=?, tanggal_dibuat=? WHERE id=?";
            PreparedStatement perintah = koneksi.prepareStatement(sql);
            perintah.setString(1, nama);
            perintah.setString(2, jabatan);
            perintah.setString(3, foto);
            perintah.setString(4, tanggal_dibuat);
            perintah.setInt(5, id);
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data pengurus berhasil diubah!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengubah: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // HAPUS
    public void hapusPengurus(Integer id) {
        try {
            String sql = "DELETE FROM tabel_pengurus WHERE id='" + id + "'";
            Statement perintah = koneksi.createStatement();
            perintah.execute(sql);
            JOptionPane.showMessageDialog(null, "Data pengurus berhasil dihapus!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menghapus: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // TAMPIL DATA
    public ResultSet getDataPengurus() {
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM tabel_pengurus";
            Statement perintah = koneksi.createStatement();
            rs = perintah.executeQuery(sql);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data pengurus: " + e.getMessage());
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
               modelTable.addColumn("Jabatan");
               modelTable.addColumn("Foto");
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
