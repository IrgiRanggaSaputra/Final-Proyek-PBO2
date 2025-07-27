package com.mycompany.peminjamanbarang.controller;

import com.mycompany.peminjamanbarang.dao.BarangDAO;
import com.mycompany.peminjamanbarang.model.Barang;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class DataBarangController {

    public Parent getView() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));

        Label title = new Label("Data Barang");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button addBtn = new Button("+ Barang");
        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        TableView<Barang> tableView = createTableView();
        refreshTable(tableView);

        addBtn.setOnAction(_ -> showForm(null, () -> refreshTable(tableView)));

        container.getChildren().addAll(title, addBtn, tableView);
        return container;
    }

    @SuppressWarnings("unchecked")
    private TableView<Barang> createTableView() {
        TableView<Barang> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tableView.setPrefHeight(400);

        TableColumn<Barang, String> kodeCol = new TableColumn<>("Kode Barang");
        kodeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getKode()));

        TableColumn<Barang, String> namaCol = new TableColumn<>("Nama Barang");
        namaCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNama()));

        TableColumn<Barang, String> kondisiCol = new TableColumn<>("Kondisi");
        kondisiCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getKondisi()));

        TableColumn<Barang, Void> aksiCol = new TableColumn<>("Aksi");
        aksiCol.setCellFactory(_ -> new TableCell<>() {
            private final Hyperlink edit = new Hyperlink("Edit");
            private final Hyperlink delete = new Hyperlink("Delete");
            {
                edit.setStyle("-fx-text-fill: blue;");
                delete.setStyle("-fx-text-fill: red;");
                edit.setOnAction(_ -> {
                    Barang b = getTableView().getItems().get(getIndex());
                    showForm(b, () -> refreshTable(getTableView()));
                });
                delete.setOnAction(_ -> {
                    Barang b = getTableView().getItems().get(getIndex());
                    
                    // Konfirmasi delete
                    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmAlert.setTitle("Konfirmasi Hapus");
                    confirmAlert.setHeaderText("Hapus Barang");
                    confirmAlert.setContentText("Apakah Anda yakin ingin menghapus barang '" + b.getNama() + "'?\nTindakan ini tidak dapat dibatalkan.");
                    
                    confirmAlert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            try {
                                BarangDAO.deleteBarang(b.getId());
                                refreshTable(getTableView());
                                
                                // Success notification
                                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                                successAlert.setTitle("Berhasil");
                                successAlert.setHeaderText("Barang Terhapus");
                                successAlert.setContentText("Barang '" + b.getNama() + "' berhasil dihapus.");
                                successAlert.showAndWait();
                                
                            } catch (Exception e) {
                                // Error handling
                                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                                errorAlert.setTitle("Error");
                                errorAlert.setHeaderText("Gagal Menghapus Barang");
                                errorAlert.setContentText("Terjadi kesalahan: " + e.getMessage() + 
                                                         "\n\nKemungkinan barang sedang dipinjam atau digunakan.");
                                errorAlert.showAndWait();
                            }
                        }
                    });
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(10, edit, new Label("/"), delete);
                    setGraphic(box);
                }
            }
        });

        tableView.getColumns().addAll(kodeCol, namaCol, kondisiCol, aksiCol);
        return tableView;
    }

    private void refreshTable(TableView<Barang> tableView) {
        tableView.getItems().setAll(BarangDAO.getAllBarang());
    }

    private void showForm(Barang b, Runnable refresh) {
        Stage stage = new Stage();
        stage.setTitle(b == null ? "Tambah Barang" : "Edit Barang");
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: #4CAF50;");
        form.setAlignment(Pos.CENTER);

        TextField namaField = new TextField(b != null ? b.getNama() : "");
        TextField kodeField = new TextField(b != null ? b.getKode() : "");
        TextField kondisiField = new TextField(b != null ? b.getKondisi() : "");

        namaField.setPromptText("Nama Barang");
        kodeField.setPromptText("Kode Barang");
        kondisiField.setPromptText("Kondisi");

        Button btnSave = new Button("Simpan");
        btnSave.setStyle("-fx-background-color: white; -fx-text-fill: black;");

        btnSave.setOnAction(_ -> {
            Barang newB = (b == null)
                    ? new Barang(namaField.getText(), kodeField.getText(), kondisiField.getText())
                    : new Barang(b.getId(), namaField.getText(), kodeField.getText(), kondisiField.getText());

            if (b == null) BarangDAO.insertBarang(newB);
            else BarangDAO.updateBarang(newB);
            refresh.run();
            stage.close();
        });

        form.getChildren().addAll(
                new Label("Nama Barang"), namaField,
                new Label("Kode Barang"), kodeField,
                new Label("Kondisi"), kondisiField,
                btnSave
        );

        stage.setScene(new Scene(form, 300, 350));
        stage.show();
    }
}
