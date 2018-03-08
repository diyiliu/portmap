package com.diyiliu.ui.controller;

import com.diyiliu.support.model.Host;
import com.diyiliu.support.model.Pair;
import com.diyiliu.support.model.TableProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;

/**
 * Description: MainController
 * Author: DIYILIU
 * Update: 2018-03-06 12:20
 */

public class MainController {

    @FXML
    private TableView tv;

    @FXML
    private ComboBox cbxProtocol;

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

        String protocol = property.getProtocol();
        cbxProtocol.getSelectionModel().select(protocol);
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

    public ComboBox getCbxProtocol() {
        return cbxProtocol;
    }

    public Pair getPair(){

        String inIp = tfInsideHost.getText().trim();
        String inPort = tfInsidePort.getText().trim();
        String outIp = tfOutsideHost.getText().trim();
        String outPort = tfOutsidePort.getText().trim();

        if (StringUtils.isBlank(inIp) || StringUtils.isBlank(inPort) ||
                StringUtils.isBlank(outIp) || StringUtils.isBlank(outPort)) {

            return null;
        }

        String protocol = (String) cbxProtocol.getSelectionModel().getSelectedItem();
        Host inHost = new Host(inIp, inPort);
        Host outHost = new Host(outIp, outPort);

        Pair pair = new Pair();
        pair.setProtocol(protocol);
        pair.setInside(inHost);
        pair.setOutside(outHost);

        return pair;
    }
}
