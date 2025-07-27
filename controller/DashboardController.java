package com.mycompany.peminjamanbarang.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
//import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
//import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;

import java.util.List;
import com.mycompany.peminjamanbarang.service.PeminjamanService;
import java.sql.SQLException;
import com.mycompany.peminjamanbarang.dao.PermintaanDAO;
import com.mycompany.peminjamanbarang.model.PermintaanModel;

public class DashboardController {
    private final String role;

    public DashboardController(String role) {
        this.role = role;
    }

    public Parent getView(Stage stage) {
        BorderPane root = new BorderPane();

        VBox sidebar = new VBox(15);
        sidebar.setStyle("-fx-background-color: #4CAF50;");
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(180);
        sidebar.setAlignment(Pos.TOP_LEFT);

        HBox profileBox = new HBox(10);
        profileBox.setAlignment(Pos.CENTER_LEFT);
        ImageView profileImg = new ImageView(new Image(getClass().getResourceAsStream("/img/" + (role.equals("admin") ? "admin.png" : "mahasiswa.png")), 30, 30, true, true));
        Label roleLabel = new Label(role.equals("admin") ? "Administrator" : "Mahasiswa");
        roleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        profileImg.setStyle("-fx-cursor: hand;");
        profileImg.setOnMouseClicked(_ -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText("Konfirmasi Logout");
            alert.setContentText("Apakah Anda yakin ingin logout?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    RoleSelectionView roleView = new RoleSelectionView();
                    stage.setScene(roleView.getView(stage));
                }
            });
        });
        profileBox.getChildren().addAll(profileImg, roleLabel);

        VBox menuBox = new VBox(15);
        menuBox.setAlignment(Pos.TOP_LEFT);
        menuBox.setPadding(new Insets(20, 0, 0, 0));

        StackPane contentArea = new StackPane();
        contentArea.setPadding(new Insets(20));

        Label dashboardMenu = new Label("Dashboard");
        dashboardMenu.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand;");
        dashboardMenu.setOnMouseClicked(_ -> contentArea.getChildren().setAll(getDashboardContent()));

        Label dataBarangMenu = new Label("Data Barang");
        dataBarangMenu.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand;");
        dataBarangMenu.setOnMouseClicked(_ -> {
            DataBarangController controller = new DataBarangController();
            contentArea.getChildren().setAll(controller.getView());
        });

        Label permintaanMenu = new Label("Permintaan");
        permintaanMenu.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand;");
        permintaanMenu.setOnMouseClicked(_ -> contentArea.getChildren().setAll(getPermintaanTable()));

        Label riwayatMenu = new Label("Riwayat");
        riwayatMenu.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand;");
        riwayatMenu.setOnMouseClicked(_ -> contentArea.getChildren().setAll(getRiwayatTable()));

        menuBox.getChildren().addAll(dashboardMenu, dataBarangMenu, permintaanMenu, riwayatMenu);
        sidebar.getChildren().addAll(profileBox, menuBox);
        root.setLeft(sidebar);
        root.setCenter(contentArea);

        dashboardMenu.fireEvent(new javafx.scene.input.MouseEvent(javafx.scene.input.MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, javafx.scene.input.MouseButton.PRIMARY, 1, false, false, false, false, true, false, false, true, false, false, null));

        return root;
    }

    private VBox getDashboardContent() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_LEFT);
        Label title = new Label("Dashboard");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label welcome = new Label("Selamat datang " + (role.equals("admin") ? "Administrator" : "Mahasiswa") + ",\n\n"
                + "Injemkeun adalah sebuah aplikasi mobile yang dirancang untuk memudahkan proses peminjaman dan pengelolaan barang secara digital.\n"
                + "Aplikasi ini memungkinkan pengguna untuk meminjam, meminjamkan, serta melacak status barang dengan cepat dan efisien,\n"
                + "baik untuk keperluan pribadi, komunitas, maupun organisasi.");
        welcome.setWrapText(true);
        content.getChildren().addAll(title, welcome);
        return content;
    }

    private VBox getPermintaanTable() {
        TableView<PermintaanModel> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefHeight(400);

        TableColumn<PermintaanModel, String> namaCol = new TableColumn<>("Nama Pengajuan");
        namaCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNamaPengaju()));

        TableColumn<PermintaanModel, String> barangCol = new TableColumn<>("Barang");
        barangCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNamaBarang()));

        TableColumn<PermintaanModel, String> kodeCol = new TableColumn<>("Kode Barang");
        kodeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getKodeBarang()));

        TableColumn<PermintaanModel, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        TableColumn<PermintaanModel, Void> aksiCol = new TableColumn<>("Aksi");
        aksiCol.setCellFactory(col -> new TableCell<>() {
            private final Button accBtn = new Button("ACC");
            private final Button rejectBtn = new Button("Reject");
            {
                accBtn.setStyle("-fx-background-color: #388E3C; -fx-text-fill: white; -fx-font-size: 12px;");
                rejectBtn.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white; -fx-font-size: 12px;");
                accBtn.setOnAction(_ -> handleUpdateStatus(getTableView().getItems().get(getIndex()), "Diacc", tableView));
                rejectBtn.setOnAction(_ -> handleUpdateStatus(getTableView().getItems().get(getIndex()), "Ditolak", tableView));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(10, accBtn, rejectBtn);
                    setGraphic(box);
                }
            }
        });

        tableView.getColumns().addAll(namaCol, barangCol, kodeCol, statusCol, aksiCol);
        tableView.getItems().setAll(PermintaanDAO.getPermintaanMenunggu());

        VBox permintaanBox = new VBox(10, new Label("Daftar Permintaan Mahasiswa"), tableView);
        permintaanBox.setPadding(new Insets(10));
        return permintaanBox;
    }

    private VBox getRiwayatTable() {
        TableView<PermintaanModel> riwayatTable = new TableView<>();
        riwayatTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        riwayatTable.setPrefHeight(400);

        TableColumn<PermintaanModel, String> namaCol = new TableColumn<>("Nama Pengajuan");
        namaCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNamaPengaju()));

        TableColumn<PermintaanModel, String> barangCol = new TableColumn<>("Barang");
        barangCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNamaBarang()));

        TableColumn<PermintaanModel, String> kodeCol = new TableColumn<>("Kode Barang");
        kodeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getKodeBarang()));

        TableColumn<PermintaanModel, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        riwayatTable.getColumns().addAll(namaCol, barangCol, kodeCol, statusCol);
        riwayatTable.getItems().setAll(PermintaanDAO.getPermintaanRiwayat());

        VBox riwayatBox = new VBox(10, new Label("Riwayat Permintaan Mahasiswa"), riwayatTable);
        riwayatBox.setPadding(new Insets(10));
        return riwayatBox;
    }

    private void handleUpdateStatus(PermintaanModel p, String status, TableView<PermintaanModel> tableView) {
        if (!p.getStatus().equals("Menunggu")) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Permintaan sudah diproses.");
            alert.showAndWait();
            return;
        }
        
        try {
            // Update status permintaan
            PermintaanDAO.updateStatus(p.getId(), status);
            
            // Jika di-ACC, buat record peminjaman baru menggunakan OOP pattern
            if ("Diacc".equals(status)) {
                PeminjamanService peminjamanService = new PeminjamanService();
                
                // Ambil data dari permintaan untuk membuat peminjaman
                // Untuk saat ini, kita perlu mendapatkan user_id dan barang_id dari permintaan
                // Ini bisa diperbaiki dengan menambah method di PermintaanDAO untuk mengambil detail lengkap
                peminjamanService.createFromApprovedRequest(
                    getUserIdFromPermintaan(p.getId()), // Method helper untuk mendapatkan user_id
                    getBarangIdFromPermintaan(p.getId()), // Method helper untuk mendapatkan barang_id
                    p.getNamaPengaju(),
                    p.getNamaBarang(),
                    p.getKodeBarang()
                );
                
                System.out.println("✅ OOP Implementation: Peminjaman record created successfully using PeminjamanService!");
            }
            
            // Refresh table
            tableView.getItems().setAll(PermintaanDAO.getPermintaanMenunggu());
            
            // Show success message
            String action = status.equals("Diacc") ? "ACC" : "Reject";
            String message = "Permintaan dari '" + p.getNamaPengaju() + "' untuk barang '" + p.getNamaBarang() + "' di-" + action + ".";
            
            if ("Diacc".equals(status)) {
                message += "\nRecord peminjaman telah dibuat secara otomatis.";
            }
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
            alert.showAndWait();
            
        } catch (SQLException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Error Processing Request");
            errorAlert.setContentText("Terjadi kesalahan saat memproses permintaan: " + e.getMessage());
            errorAlert.showAndWait();
        }
    }
    
    // Helper methods untuk mendapatkan user_id dan barang_id dari permintaan
    private int getUserIdFromPermintaan(int permintaanId) throws SQLException {
        try (java.sql.Connection conn = com.mycompany.peminjamanbarang.util.DBUtil.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement("SELECT user_id FROM pengajuan WHERE id = ?")) {
            ps.setInt(1, permintaanId);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
            throw new SQLException("User ID not found for permintaan ID: " + permintaanId);
        }
    }
    
    private int getBarangIdFromPermintaan(int permintaanId) throws SQLException {
        try (java.sql.Connection conn = com.mycompany.peminjamanbarang.util.DBUtil.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement("SELECT barang_id FROM pengajuan WHERE id = ?")) {
            ps.setInt(1, permintaanId);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("barang_id");
            }
            throw new SQLException("Barang ID not found for permintaan ID: " + permintaanId);
        }
    }
}
