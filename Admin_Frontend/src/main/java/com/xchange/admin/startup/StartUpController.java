package com.xchange.admin.startup;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class StartUpController {
    @FXML
    private TextField serverAddressField;

    @FXML
    private TextField portField;

    @FXML
    private Button connectButton;

    @FXML
    private ProgressIndicator connectionProgress;

    @FXML
    private Label statusLabel;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(4))
            .build();

    @FXML
    private void initialize() {
        portField.setText("8080");
        portField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                portField.setText(newValue.replaceAll("\\D", ""));
            }
        });

        connectionProgress.setVisible(false);
    }

    @FXML
    private void onConnect() {
        String serverAddress = serverAddressField.getText().trim();
        String portText = portField.getText().trim();

        if (serverAddress.isEmpty()) {
            showError("Enter the XChange server IP address or hostname.");
            serverAddressField.requestFocus();
            return;
        }

        int port;
        try {
            port = Integer.parseInt(portText);
            if (port < 1 || port > 65535) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException exception) {
            showError("Enter a port number between 1 and 65535.");
            portField.requestFocus();
            return;
        }

        String baseUrl = normalizeBaseUrl(serverAddress, port);

        setConnecting(true);
        statusLabel.setText("Connecting to " + baseUrl + "...");

        Task<String> connectionTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                return verifyServer(baseUrl);
            }
        };

        connectionTask.setOnSucceeded(event -> {
            String verifiedAddress = connectionTask.getValue();
            setConnecting(false);
            statusLabel.getStyleClass().remove("error");
            statusLabel.getStyleClass().add("success");
            statusLabel.setText("XChange server found.");
            openAdminHome(verifiedAddress);
        });

        connectionTask.setOnFailed(event -> {
            setConnecting(false);
            Throwable failure = connectionTask.getException();

            if (failure instanceof ConnectException) {
                showError("Could not reach the XChange server. Check the IP, port, and network.");
            } else {
                showError("Server connection failed: " + failure.getMessage());
            }
        });

        Thread connectionThread = new Thread(connectionTask, "xchange-server-connect");
        connectionThread.setDaemon(true);
        connectionThread.start();
    }

    private String verifyServer(String baseUrl) throws Exception {
        String[] healthPaths = {"/api/health", "/health", "/actuator/health"};

        Exception lastFailure = null;
        for (String healthPath : healthPaths) {
            URI uri = URI.create(baseUrl + healthPath);
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .timeout(Duration.ofSeconds(4))
                    .GET()
                    .header("Accept", "application/json")
                    .build();

            try {
                HttpResponse<String> response = httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    return baseUrl;
                }

                lastFailure = new IOException(
                        "Server returned HTTP " + response.statusCode() + " from " + healthPath
                );
            } catch (Exception exception) {
                lastFailure = exception;
            }
        }

        if (lastFailure != null) {
            throw lastFailure;
        }

        throw new IOException("No XChange health endpoint responded.");
    }

    private String normalizeBaseUrl(String serverAddress, int port) {
        String address = serverAddress
                .replaceFirst("^https?://", "")
                .replaceAll("/+$", "");
        return "http://" + address + ":" + port;
    }

    private void setConnecting(boolean connecting) {
        connectButton.setDisable(connecting);
        serverAddressField.setDisable(connecting);
        portField.setDisable(connecting);
        connectionProgress.setVisible(connecting);
    }

    private void showError(String message) {
        statusLabel.getStyleClass().remove("success");
        statusLabel.getStyleClass().add("error");
        statusLabel.setText(message);
    }

    private void openAdminHome(String serverAddress) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/xchange/admin/home/AdminHome.fxml")
            );
            Parent home = loader.load();

            Stage stage = (Stage) connectButton.getScene().getWindow();
            stage.setTitle("XChange Admin");
            stage.setScene(new Scene(home));
            stage.setMinWidth(900);
            stage.setMinHeight(600);
            stage.centerOnScreen();
        } catch (IOException exception) {
            Platform.runLater(() ->
                    showError("Connected, but the admin home could not be loaded: "
                            + exception.getMessage())
            );
        }
    }
}
