package com.example.modul6smster3.ProjectUAP;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Project extends Application {
    private TableView<User> table = new TableView<>();
    private boolean isUsernameValid = true;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Login/Register Application");
        stage.setWidth(400);
        stage.setHeight(600);

        final Label label = new Label("Login/Register");
        label.setFont(new Font("Arial", 30));

        table.setEditable(true);

        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());

        TableColumn<User, String> passwordCol = new TableColumn<>("Password");
        passwordCol.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());

        table.getColumns().addAll(usernameCol, passwordCol);

        final ObservableList<User> data = FXCollections.observableArrayList();
        table.setItems(data);

        final TextField usernameField = new TextField();
        usernameField.setMaxWidth(usernameCol.getPrefWidth());
        usernameField.setPromptText("Username");

        final PasswordField passwordField = new PasswordField();
        passwordField.setMaxWidth(passwordCol.getPrefWidth());
        passwordField.setPromptText("Password");

        final Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            // Reset status validasi Username
            isUsernameValid = true;

            if (data.isEmpty()) {
                showAlert("Belum ada data pengguna. Silakan registrasi terlebih dahulu!");
            } else if (validateInput(usernameField.getText(), passwordField.getText())) {
                // Check if the username exists in the table
                User user = findUser(data, usernameField.getText());
                if (user != null) {
                    if (user.getPassword().equals(passwordField.getText())) {
                        showAlert("Login Berhasil!");
                    } else {
                        showAlert("Password salah untuk username ini.");
                    }
                } else {
                    showAlert("Username tidak ditemukan!");
                }
            }
        });

        final Button registerButton = new Button("Registrasi");
        registerButton.setOnAction(e -> {
            // Reset status validasi Username
            isUsernameValid = true;

            if (validateInput(usernameField.getText(), passwordField.getText())) {
                // Check if the username already exists in the table
                if (findUser(data, usernameField.getText()) == null) {
                    if (passwordField.getText().length() < 8) {
                        showAlert("Password harus memiliki setidaknya 8 karakter!!");
                    } else {
                        User newUser = new User(
                                usernameField.getText(),
                                passwordField.getText()
                        );
                        data.add(newUser);
                        showAlert("Registrasi Berhasil");
                    }
                } else {
                    showAlert("Username sudah ada yang memakai! Pilih yang berbeda.");
                }
            }
        });

        final HBox hboxInput = new HBox();
        hboxInput.getChildren().addAll(usernameField, passwordField, loginButton, registerButton);
        hboxInput.setSpacing(10);

        final VBox vbox = new VBox();
        vbox.setSpacing(8);
        vbox.setPadding(new Insets(20, 10, 10, 10));
        vbox.getChildren().addAll(label, table, hboxInput);

        Scene scene = new Scene(new Group());
        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param username
     * @param password
     * @return
     */

    //fungsi jika usernamae kosong atau password kosong maka akan mengeluarkan alert//
    private boolean validateInput(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Tidak ada yang boleh kosong!");
            return false;
        }

        return true;
    }

    /**
     *
     * @param message
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private User findUser(ObservableList<User> users, String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static class User {
        private final StringProperty username;
        private final StringProperty password;

        public User(String username, String password) {
            this.username = new SimpleStringProperty(username);
            this.password = new SimpleStringProperty(password);
        }

        public String getUsername() {
            return username.get();
        }

        public String getPassword() {
            return password.get();
        }

        public StringProperty usernameProperty() {
            return username;
        }

        public StringProperty passwordProperty() {
            return password;
        }
    }
}
