package com.diyiliu.controller;

import com.diyiliu.model.TableProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Description: MainController
 * Author: DIYILIU
 * Update: 2018-03-06 12:20
 */

public class MainController {

    @FXML
    private TableView tv;

    @FXML
    private Button btnOpt;

    @FXML
    private Button btnRefresh;

    @FXML
    private TextField tfInsideHost;

    @FXML
    private TextField tfInsidePort;

    @FXML
    private TextField tfOutsideHost;

    @FXML
    private TextField tfOutsidePort;

    @FXML
    private Text optTip;


    public void optHandle(){
/*        Alert.AlertType type = Alert.AlertType.INFORMATION;
        Alert alert = new Alert(type, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.getDialogPane().setContentText(type + " text.");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> System.out.println("The alert was approved"));*/
    }

    public void onTvMouseClicked(MouseEvent e) {
        MouseButton mb = e.getButton();

        switch (mb) {

            // 左键
            case PRIMARY:
                resetDetail();
                break;

            // 右键
            case SECONDARY:
                showDetail();
                break;
            default:
                break;
        }
    }




    public void resetDetail() {
        btnOpt.setText("添加");
        setDetail("192.168.1.", "",
                "218.3.247.227", "");
    }

    public void showDetail() {
        btnOpt.setText("删除");
        TablePosition tp = (TablePosition) tv.getSelectionModel().getSelectedCells().get(0);
        TableProperty property = (TableProperty) tv.getItems().get(tp.getRow());
        setDetail(property.getInsideHost(), property.getInsidePort(),
                property.getOutsideHost(), property.getOutsidePort());
    }

    public void setDetail(String insideHost, String insidePort, String outsideHost, String outsidePort) {
        tfInsideHost.setText(insideHost);
        tfInsidePort.setText(insidePort);
        tfOutsideHost.setText(outsideHost);
        tfOutsidePort.setText(outsidePort);
    }

    public Button getBtnRefresh() {
        return btnRefresh;
    }

    public Button getBtnOpt() {
        return btnOpt;
    }

    public Text getOptTip() {
        return optTip;
    }
}
