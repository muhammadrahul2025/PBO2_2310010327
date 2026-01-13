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

public class tabel_surat_masuk {
    private String namadb = "PBO2_2310010327";
    private String url = "jdbc:mysql://localhost:3306/" + namadb;
    private String username = "root";
    private String password = "";
    private Connection koneksi;

    // Variabel penampung data
    public Integer VAR_id_surat_masuk = null;
    public String VAR_no_surat = null;
    public String VAR_tanggal_surat = null;
    public String VAR_tanggal_diterima = null;
    public String VAR_pengirim = null;
    public String VAR_perihal = null;
    public String VAR_keterangan = null;
    public String VAR_file_surat = null;
    public String VAR_tanggal_dibuat = null;
    public boolean validasi = false;

    // Constructor: koneksi ke database
    public tabel_surat_masuk() {
        try {
            Driver mysqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);
            koneksi = DriverManager.getConnection(url, username, password);
            System.out.println("Koneksi ke database berhasil (tabel_surat_masuk)");
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, "Koneksi gagal: " + error.getMessage());
        }
    }

    // SIMPAN 
    public void simpanSuratMasuk(int id_surat_masuk, String no_surat, String tanggal_surat, String tanggal_diterima,
                                 String pengirim, String perihal, String keterangan, String file_surat, String tanggal_dibuat) {
        try {
            String cekPrimary = "SELECT * FROM tabel_surat_masuk WHERE id_surat_masuk = '" + id_surat_masuk + "'";
            Statement check = koneksi.createStatement();
            ResultSet data = check.executeQuery(cekPrimary);

            if (data.next()) {
                JOptionPane.showMessageDialog(null, "ID surat masuk sudah terdaftar!");
                this.VAR_id_surat_masuk = data.getInt("id_surat_masuk");
                this.VAR_no_surat = data.getString("no_surat");
                this.VAR_tanggal_surat = data.getString("tanggal_surat");
                this.VAR_tanggal_diterima = data.getString("tanggal_diterima");
                this.VAR_pengirim = data.getString("pengirim");
                this.VAR_perihal = data.getString("perihal");
                this.VAR_keterangan = data.getString("keterangan");
                this.VAR_file_surat = data.getString("file_surat");
                this.VAR_tanggal_dibuat = data.getString("tanggal_dibuat");
                this.validasi = true;
            } else {
                this.validasi = false;
                String sql = "INSERT INTO tabel_surat_masuk(id_surat_masuk, no_surat, tanggal_surat, tanggal_diterima, pengirim, perihal, keterangan, file_surat, tanggal_dibuat) "
                           + "VALUES('" + id_surat_masuk + "', '" + no_surat + "', '" + tanggal_surat + "', '" + tanggal_diterima + "', '"
                           + pengirim + "', '" + perihal + "', '" + keterangan + "', '" + file_surat + "', '" + tanggal_dibuat + "')";
                Statement perintah = koneksi.createStatement();
                perintah.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Data surat masuk berhasil disimpan!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menyimpan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // UBAH 
    public void ubahSuratMasuk(int id_surat_masuk, String no_surat, String tanggal_surat, String tanggal_diterima,
                               String pengirim, String perihal, String keterangan, String file_surat, String tanggal_dibuat) {
        try {
            String sql = "UPDATE tabel_surat_masuk SET no_surat=?, tanggal_surat=?, tanggal_diterima=?, pengirim=?, perihal=?, keterangan=?, file_surat=?, tanggal_dibuat=? WHERE id_surat_masuk=?";
            PreparedStatement perintah = koneksi.prepareStatement(sql);
            perintah.setString(1, no_surat);
            perintah.setString(2, tanggal_surat);
            perintah.setString(3, tanggal_diterima);
            perintah.setString(4, pengirim);
            perintah.setString(5, perihal);
            perintah.setString(6, keterangan);
            perintah.setString(7, file_surat);
            perintah.setString(8, tanggal_dibuat);
            perintah.setInt(9, id_surat_masuk);
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data surat masuk berhasil diubah!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengubah: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // HAPUS 
    public void hapusSuratMasuk(Integer id_surat_masuk) {
        try {
            String sql = "DELETE FROM tabel_surat_masuk WHERE id_surat_masuk='" + id_surat_masuk + "'";
            Statement perintah = koneksi.createStatement();
            perintah.execute(sql);
            JOptionPane.showMessageDialog(null, "Data surat masuk berhasil dihapus!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menghapus: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // TAMPIL DATA 
    public ResultSet getDataSuratMasuk() {
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM tabel_surat_masuk";
            Statement perintah = koneksi.createStatement();
            rs = perintah.executeQuery(sql);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data surat masuk: " + e.getMessage());
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
               modelTable.addColumn("No Surat");
               modelTable.addColumn("Tanggal Surat");
               modelTable.addColumn("Tanggal Diterima");
               modelTable.addColumn("Pengirim");
               modelTable.addColumn("Perihal");
               modelTable.addColumn("Keterangan");
               modelTable.addColumn("File Surat");
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
