package com.xchange.admin.startup;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

public class StartUpView {
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

    public TextField getServerAddressField() {
        return serverAddressField;
    }

    public TextField getPortField() {
        return portField;
    }

    public Button getConnectButton() {
        return connectButton;
    }

    public ProgressIndicator getConnectionProgress() {
        return connectionProgress;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }
}
