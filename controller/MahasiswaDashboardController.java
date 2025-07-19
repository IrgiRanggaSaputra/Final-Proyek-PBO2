package com.mycompany.peminjamanbarang.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import com.mycompany.peminjamanbarang.model.PermintaanModel;
import com.mycompany.peminjamanbarang.dao.PermintaanDAO;
import com.mycompany.peminjamanbarang.dao.BarangDAO;
import com.mycompany.peminjamanbarang.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


public class MahasiswaDashboardController {
    private final String nama;
    private final int userId;

    public MahasiswaDashboardController(String nama, int userId) {
        this.nama = nama;
        this.userId = userId;
    }

    public Parent getView(Stage stage) {
        BorderPane root = new BorderPane();

        // Sidebar kiri
        VBox sidebar = new VBox(15);
        sidebar.setStyle("-fx-background-color: #4CAF50;");
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(180);
        sidebar.setAlignment(Pos.TOP_LEFT);

        // Logo dan nama
        HBox profileBox = new HBox(10);
        profileBox.setAlignment(Pos.CENTER_LEFT);
        ImageView profileImg = new ImageView(new Image(getClass().getResourceAsStream("/img/mahasiswa.png"), 30, 30, true, true));
        Label nameLabel = new Label(nama);
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        profileBox.getChildren().addAll(profileImg, nameLabel);


        // kanggo logout dina logo
        profileBox.setOnMouseClicked(_ -> {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Konfirmasi Logout");
        alert.setContentText("Apakah Anda yakin ingin logout?");

        alert.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
            RoleSelectionView roleView = new RoleSelectionView();
            Scene roleScene = roleView.getView(stage);
            stage.setScene(roleScene);
        }
    });
});


        // Menu mahasiswa dengan event handler
        VBox menuBox = new VBox(15);
        menuBox.setAlignment(Pos.TOP_LEFT);
        menuBox.setPadding(new Insets(20, 0, 0, 0));

        String[] menuItems = {"Dashboard", "Daftar Barang", "Pengajuan", "Riwayat"};
        String[] iconPaths = {"/img/dashboard.png", "/img/databarang.png", "/img/permintaan.png", "/img/riwayat.png"};

        StackPane contentArea = new StackPane();
        contentArea.setPadding(new Insets(20));

        for (int i = 0; i < menuItems.length; i++) {
            HBox menu = new HBox(10);
            menu.setAlignment(Pos.CENTER_LEFT);
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPaths[i]), 18, 18, true, true));
            Label label = new Label(menuItems[i]);
            label.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand;");
            menu.getChildren().addAll(icon, label);

            final int idx = i;
            menu.setOnMouseClicked(_ -> {
                if (idx == 0) { // Dashboard
                    VBox content = new VBox(10);
                    content.setPadding(new Insets(20));
                    content.setAlignment(Pos.TOP_LEFT);
                    Label title = new Label("Dashboard");
                    title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
                    Label welcome = new Label("Selamat datang " + nama + " (Mahasiswa),\n\n"
                            + "Injemkeun adalah sebuah aplikasi mobile yang dirancang untuk memudahkan proses peminjaman dan pengelolaan barang secara digital.\n"
                            + "Aplikasi ini memungkinkan pengguna untuk meminjam, meminjamkan, serta melacak status barang dengan cepat dan efisien,\n"
                            + "baik untuk keperluan pribadi, komunitas, maupun organisasi.");
                    welcome.setWrapText(true);
                    content.getChildren().addAll(title, welcome);
                    contentArea.getChildren().setAll(content);
                } else if (idx == 1) { // Daftar Barang
                    // Tampilkan daftar barang yang bisa diajukan peminjaman
                    TableView<com.mycompany.peminjamanbarang.model.Barang> tableView = new TableView<>();
                    tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
                    tableView.setPrefHeight(400);

                    TableColumn<com.mycompany.peminjamanbarang.model.Barang, String> kodeCol = new TableColumn<>("Kode Barang");
                    kodeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getKode()));

                    TableColumn<com.mycompany.peminjamanbarang.model.Barang, String> namaCol = new TableColumn<>("Nama Barang");
                    namaCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNama()));

                    TableColumn<com.mycompany.peminjamanbarang.model.Barang, String> kondisiCol = new TableColumn<>("Kondisi");
                    kondisiCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getKondisi()));

                    TableColumn<com.mycompany.peminjamanbarang.model.Barang, Void> aksiCol = new TableColumn<>("Aksi");
                    aksiCol.setCellFactory(_ -> new TableCell<>() {
                        private final Button ajukanBtn = new Button("Ajukan Pinjam");
                        {
                            ajukanBtn.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white; -fx-font-size: 12px;");
                            ajukanBtn.setOnAction(_ -> {
                                com.mycompany.peminjamanbarang.model.Barang barang = getTableView().getItems().get(getIndex());
                                // Insert pengajuan ke database
                                try (Connection conn = DBUtil.getConnection();
                                     PreparedStatement ps = conn.prepareStatement(
                                         "INSERT INTO pengajuan (user_id, barang_id, tanggal_pengajuan, status) VALUES (?, ?, NOW(), 'Menunggu')")) {
                                    ps.setInt(1, userId);
                                    ps.setInt(2, barang.getId());
                                    ps.executeUpdate();
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pengajuan peminjaman untuk '" + barang.getNama() + "' berhasil diajukan.\nStatus: Menunggu ACC Admin.");
                                    alert.showAndWait();
                                } catch (SQLException e) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR, "Gagal mengajukan pinjam: " + e.getMessage());
                                    alert.showAndWait();
                                }
                            });
                        }
                        @Override
                        protected void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(ajukanBtn);
                            }
                        }
                    });

                    tableView.getColumns().add(kodeCol);
                    tableView.getColumns().add(namaCol);
                    tableView.getColumns().add(kondisiCol);
                    tableView.getColumns().add(aksiCol);
                    tableView.getItems().setAll(BarangDAO.getAllBarang());

                    Label daftarLabel = new Label("Daftar Barang");
                    daftarLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                    VBox daftarBarangBox = new VBox(10, daftarLabel, tableView);
                    daftarBarangBox.setPadding(new Insets(10));
                    contentArea.getChildren().setAll(daftarBarangBox);
                } else if (idx == 2) { // Pengajuan
                    // Tabel pengajuan dengan status (ambil dari database)
                    TableView<PermintaanModel> pengajuanTable = new TableView<>();
                    pengajuanTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
                    pengajuanTable.setPrefHeight(400);

                    TableColumn<PermintaanModel, String> namaCol = new TableColumn<>("Nama");
                    namaCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNamaPengaju()));

                    TableColumn<PermintaanModel, String> barangCol = new TableColumn<>("Barang");
                    barangCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNamaBarang()));

                    TableColumn<PermintaanModel, String> kodeCol = new TableColumn<>("Kode Barang");
                    kodeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getKodeBarang()));

                    TableColumn<PermintaanModel, String> statusCol = new TableColumn<>("Status");
                    statusCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

                    TableColumn<PermintaanModel, String> tanggalCol = new TableColumn<>("Tanggal Pengajuan");
                    tanggalCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTanggalPengajuanFormatted()));

                    pengajuanTable.getColumns().addAll(namaCol, barangCol, kodeCol, statusCol, tanggalCol);
                    // Filter pengajuan milik user ini berdasarkan user_id
                    List<PermintaanModel> pengajuanList = PermintaanDAO.getPermintaanByUserId(userId, "Menunggu");
                    pengajuanTable.getItems().setAll(pengajuanList);

                    Label pengajuanLabel = new Label("Daftar Pengajuan Barang");
                    pengajuanLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                    VBox pengajuanBox = new VBox(10, pengajuanLabel, pengajuanTable);
                    pengajuanBox.setPadding(new Insets(10));
                    contentArea.getChildren().setAll(pengajuanBox);
                } else if (idx == 3) { // Riwayat
                    // Tabel riwayat pengajuan yang sudah diacc/ditolak (ambil dari database)
                    TableView<PermintaanModel> riwayatTable = new TableView<>();
                    riwayatTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
                    riwayatTable.setPrefHeight(400);

                    TableColumn<PermintaanModel, String> namaCol = new TableColumn<>("Nama");
                    namaCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNamaPengaju()));

                    TableColumn<PermintaanModel, String> barangCol = new TableColumn<>("Barang");
                    barangCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNamaBarang()));

                    TableColumn<PermintaanModel, String> kodeCol = new TableColumn<>("Kode Barang");
                    kodeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getKodeBarang()));

                    TableColumn<PermintaanModel, String> statusCol = new TableColumn<>("Status");
                    statusCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

                    TableColumn<PermintaanModel, String> tanggalCol = new TableColumn<>("Tanggal Pengajuan");
                    tanggalCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTanggalPengajuanFormatted()));

                    riwayatTable.getColumns().addAll(namaCol, barangCol, kodeCol, statusCol, tanggalCol);
                    // Filter riwayat milik user ini berdasarkan user_id
                    List<PermintaanModel> riwayatList = PermintaanDAO.getPermintaanByUserId(userId, "Riwayat");
                    riwayatTable.getItems().setAll(riwayatList);

                    Label riwayatLabel = new Label("Riwayat Pengajuan Barang");
                    riwayatLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                    VBox riwayatBox = new VBox(10, riwayatLabel, riwayatTable);
                    riwayatBox.setPadding(new Insets(10));
                    contentArea.getChildren().setAll(riwayatBox);
                }
            });
            menuBox.getChildren().add(menu);
        }

        sidebar.getChildren().addAll(profileBox, menuBox);
        root.setLeft(sidebar);
        root.setCenter(contentArea);

        // Tampilkan dashboard sebagai default
        menuBox.getChildren().get(0).fireEvent(new javafx.scene.input.MouseEvent(javafx.scene.input.MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, javafx.scene.input.MouseButton.PRIMARY, 1, false, false, false, false, true, false, false, true, false, false, null));

        return root;
    }
}
