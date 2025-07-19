package com.mycompany.peminjamanbarang.controller;

import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import javafx.stage.Stage;

public class RoleSelectionView {

    public Scene getView(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: white; -fx-padding: 30;");

        Image logoImg = new Image(getClass().getResourceAsStream("/img/injempanjang.png"), 200, 300, true, true);
        ImageView logoView = new ImageView(logoImg);


        HBox header = new HBox(10, logoView);
        header.setAlignment(Pos.CENTER);

        // Button gambar Admin
        Image adminImg = new Image(getClass().getResourceAsStream("/img/admin.png"), 100, 100, true, true);
        ImageView adminView = new ImageView(adminImg);
        VBox adminBox = new VBox(10, adminView);
        adminBox.setAlignment(Pos.CENTER);
        adminBox.setOnMouseClicked(_ -> {
            LoginView loginView = new LoginView("admin");
            primaryStage.setScene(loginView.getLoginScene(primaryStage));
        });

        // Button gambar Mahasiswa
        Image mhsImg = new Image(getClass().getResourceAsStream("/img/mahasiswa.png"), 100, 100, true, true);
        ImageView mhsView = new ImageView(mhsImg);
        VBox mhsBox = new VBox(10, mhsView);
        mhsBox.setAlignment(Pos.CENTER);
        mhsBox.setOnMouseClicked(_ -> {
            LoginView loginView = new LoginView("mahasiswa");
            primaryStage.setScene(loginView.getLoginScene(primaryStage));
        });

        HBox options = new HBox(40, adminBox, mhsBox);
        options.setAlignment(Pos.CENTER);

        root.getChildren().addAll(header, options);

        return new Scene(root, 500, 350);
    }
}
