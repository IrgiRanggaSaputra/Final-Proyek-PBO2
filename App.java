package com.mycompany.peminjamanbarang;


import com.mycompany.peminjamanbarang.controller.RoleSelectionView;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
    RoleSelectionView roleView = new RoleSelectionView();
    primaryStage.setTitle("Injemkeun Login");
    primaryStage.setScene(roleView.getView(primaryStage));
    primaryStage.show();
}

    

    public static void main(String[] args) {
        launch(args);
    }
    
    
}
