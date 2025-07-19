package com.mycompany.peminjamanbarang.controller;

import com.mycompany.peminjamanbarang.controller.DashboardController;
import com.mycompany.peminjamanbarang.controller.MahasiswaDashboardController;
import com.mycompany.peminjamanbarang.dao.userDAO;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class LoginView {

    private String role;

    public LoginView(String role) {
        this.role = role;
    }

// Removed misplaced loginButton handler; correct handler is set in getLoginScene method below.


    public Scene getLoginScene(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: white;");

        Label title = new Label("Login sebagai " + role.toUpperCase());
        title.setFont(Font.font("Arial", 18));

        TextField usernameField = new TextField();
        usernameField.setPromptText("User name");
        usernameField.setMaxWidth(200);
        usernameField.setStyle("-fx-background-color: lightgray;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(200);
        passwordField.setStyle("-fx-background-color: lightgray;");

        Button loginBtn = new Button("Login");
        loginBtn.setStyle("-fx-text-fill: blue; -fx-background-color: transparent;");

        Button backBtn = new Button("Kembali");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: red;");

        loginBtn.setOnAction(_ -> {
    String user = usernameField.getText();
    String pass = passwordField.getText();

    // Validasi input kosong
    if (user.isEmpty() || pass.isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Validasi", "Username dan Password tidak boleh kosong.");
        return;
    }

    if (userDAO.login(user, pass, role)) {
        showAlert(Alert.AlertType.INFORMATION, "Login Berhasil", "Selamat datang, " + user + "!");

        // Panggil dashboard berdasarkan role
        if (role.equalsIgnoreCase("admin")) {
            DashboardController dashboard = new DashboardController(user);
            Scene dashboardScene = new Scene(dashboard.getView(primaryStage), 900, 600);
            primaryStage.setScene(dashboardScene);
        } else if (role.equalsIgnoreCase("mahasiswa")) {
            int userId = userDAO.getUserId(user); // Ambil id user dari database
            MahasiswaDashboardController mahasiswaDashboard = new MahasiswaDashboardController(user, userId);
            Scene dashboardScene = new Scene(mahasiswaDashboard.getView(primaryStage), 900, 600);
            primaryStage.setScene(dashboardScene);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Role tidak dikenali.");
            return;
        }
        primaryStage.show();

    } else {
        showAlert(Alert.AlertType.ERROR, "Login Gagal", "Username/password salah.");
    }
});

        backBtn.setOnAction(_ -> {
            RoleSelectionView roleView = new RoleSelectionView();
            primaryStage.setScene(roleView.getView(primaryStage));
        });

        root.getChildren().addAll(title, usernameField, passwordField, loginBtn, backBtn);
        return new Scene(root, 300, 400);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
