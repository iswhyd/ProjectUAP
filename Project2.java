package com.example.demo;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.junit.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import static org.junit.Assert.assertTrue;


public class Project2 extends Application {
    private TableView<User> table = new TableView<>();
    private boolean isUsernameValid = true;
    private ObservableList<User> data = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        stage.setTitle("Login/Register Application");
        stage.setWidth(300);
        stage.setHeight(300);

        final Label label = new Label("Login/Register");
        label.setFont(new Font("Arial", 30));

        final Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            // Redirect to login page
            showLoginPage(stage);
        });

        final Button registerButton = new Button("Registrasi");
        registerButton.setOnAction(e -> {
            // Redirect to registration page
            showRegistrationPage(stage);
        });

        final VBox vboxInput = new VBox();
        vboxInput.getChildren().addAll(loginButton, registerButton);
        vboxInput.setSpacing(10);

        final VBox vbox = new VBox();
        vbox.setSpacing(8);
        vbox.setPadding(new Insets(20, 10, 10, 10));
        vbox.getChildren().addAll(label, vboxInput);

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
    private boolean validateInputRegis(String username, String password, String email) {
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            showAlert("Tidak ada yang boleh kosong!");
            return false;
        }
        if (!email.endsWith("@webmail.umm.ac.id")) {
            showAlert("Inputan Email Salah! Pastikan Menggunakan @webmail.umm.ac.id");
            return false;
        }

        return true;
    }
    private boolean validateInputLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Tidak ada yang boleh kosong!");
            return false;
        }

        return true;
    }

    // Method to show login page
    private void showLoginPage(Stage stage) {

        loadDataFromFile();

        final TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(200);

        final PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(200);

        final TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(200);

        final Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            // Reset status validasi Username
            isUsernameValid = true;

            if (data.isEmpty()) {
                showAlert("Belum ada data pengguna. Silakan registrasi terlebih dahulu!");
            } else if (validateInputLogin(usernameField.getText(), passwordField.getText())) {
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

        final Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            // Redirect to the main page
            start(stage);
        });

        final VBox vboxInput = new VBox();
        vboxInput.getChildren().addAll(usernameField, passwordField, loginButton, backButton);
        vboxInput.setSpacing(10);

        final VBox vbox = new VBox();
        vbox.setSpacing(8);
        vbox.setPadding(new Insets(20, 10, 10, 10));
        vbox.getChildren().addAll(vboxInput);

        Scene scene = new Scene(new Group());
        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
    }

    // Method to show registration page
    private void showRegistrationPage(Stage stage) {
        final TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(200);

        final PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(200);

        final TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(200);

        final Button registerButton = new Button("Registrasi");
        registerButton.setOnAction(e -> {
            // Reset status validasi Username
            isUsernameValid = true;

            if (validateInputRegis(usernameField.getText(), passwordField.getText(), emailField.getText())) {
                // Check if the username already exists in the table
                if (findUser(data, usernameField.getText()) == null) {
                    if (passwordField.getText().length() < 8) {
                        showAlert("Password harus memiliki setidaknya 8 karakter!");
                    } else {
                        User newUser = new User(
                                usernameField.getText(),
                                passwordField.getText(),
                                emailField.getText()
                        );
                        data.add(newUser);
                        saveDataToFile(newUser);
                        showAlert("Registrasi Berhasil");
                    }
                } else {
                    showAlert("Username sudah ada yang memakai! Pilih yang berbeda!.");
                }
            }
        });

        final Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            // Redirect to the main page
            start(stage);
        });

        final VBox vboxInput = new VBox();
        vboxInput.getChildren().addAll(usernameField, passwordField, emailField, registerButton, backButton);
        vboxInput.setSpacing(10);

        final VBox vbox = new VBox();
        vbox.setSpacing(8);
        vbox.setPadding(new Insets(20, 10, 10, 10));
        vbox.getChildren().addAll(vboxInput);

        Scene scene = new Scene(new Group());
        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
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
    private void loadDataFromFile() {
        File file = new File("user_dataa.txt");

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length >= 2) {
                    data.add(new User(parts[0], parts[1], ""));
                }
            }
        } catch (IOException ex) {
            System.err.println("Error reading from file: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void saveDataToFile(User user) {
        File file = new File("user_dataa.txt");

        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            writer.println(user.getUsername() + "," + user.getPassword() + "," + user.getEmail());
        } catch (IOException ex) {
            System.err.println("Error writing to file: " + ex.getMessage());
            ex.printStackTrace();
        }
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
        private final StringProperty email;

        public User(String username, String password, String email) {
            this.username = new SimpleStringProperty(username);
            this.password = new SimpleStringProperty(password);
            this.email = new SimpleStringProperty(email);
        }

        public String getUsername() {
            return username.get();
        }

        public String getPassword() {
            return password.get();
        }
        public String getEmail() {
            return email.get();
        }

        public StringProperty usernameProperty() {
            return username;
        }

        public StringProperty passwordProperty() {
            return password;
        }


    }
    public static class ProjectTest {

        @Test
        public void testPasswordLength() {
            Project2.User user = new Project2.User("testUser", "passsword12345", "iswahyudiiskandar@gmail.com");

            assertTrue("Password harus lebih dari sama dengan 8", user.getPassword().length() >= 8);
        }
    }
}

