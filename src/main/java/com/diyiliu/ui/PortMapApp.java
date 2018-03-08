package com.diyiliu.ui;

import com.diyiliu.support.config.Constant;
import com.diyiliu.support.model.MappingResult;
import com.diyiliu.support.model.Pair;
import com.diyiliu.support.util.SpringUtil;
import com.diyiliu.ui.controller.MainController;
import com.diyiliu.ui.service.DataLoadService;
import com.diyiliu.ui.service.OperateService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Description: PortMapApp
 * Author: DIYILIU
 * Update: 2018-03-06 10:37
 */

public class PortMapApp extends Application {

    public Parent createContent(Stage stage, DataLoadService service, OperateService optService) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ClassLoader.getSystemResource("fxml/mainScene.fxml"));
        AnchorPane root = fxmlLoader.load();
        ScrollPane scrollPane = (ScrollPane) root.lookup("#scrPn");
        scrollPane.setStyle("-fx-background-insets: 0;");

        TableView tv = (TableView) scrollPane.getContent();
        tv.getStyleClass().add("no-border");

        ObservableList<TableColumn> tvColumns = tv.getColumns();
        for (int i = 0; i < tvColumns.size(); i++) {
            TableColumn tc = tvColumns.get(i);
            tc.setCellValueFactory(new PropertyValueFactory(tc.getId()));
        }

        // 左侧 遮罩
        Region veil = new Region();
        veil.setStyle("-fx-background-color: rgba(0,0,0,0.4)");
        veil.setLayoutX(10);
        veil.setPrefSize(508, 510);

        ProgressIndicator p = new ProgressIndicator();
        p.setPrefSize(120, 120);
        p.setLayoutX(200);
        p.setLayoutY(200);

        p.progressProperty().bind(service.progressProperty());
        veil.visibleProperty().bind(service.runningProperty());
        p.visibleProperty().bind(service.runningProperty());
        tv.itemsProperty().bind(service.valueProperty());

        // 右侧 遮罩
        Region rVeil = new Region();
        rVeil.setStyle("-fx-background-color: rgba(0,0,0,0.4)");
        rVeil.setLayoutX(510);
        rVeil.setPrefSize(270, 510);

        ProgressBar rp = new ProgressBar();
        rp.setPrefSize(120, 10);
        rp.setLayoutX(580);
        rp.setLayoutY(260);

        rp.progressProperty().bind(optService.progressProperty());
        rVeil.visibleProperty().bind(optService.runningProperty());
        rp.visibleProperty().bind(optService.runningProperty());

        root.getChildren().addAll(veil, p, rVeil, rp);

        MainController controller = fxmlLoader.getController();
        Text optTip = controller.getOptTip();
        optTip.getStyleClass().add("tip");


        ComboBox cbProtocol = controller.getCbxProtocol();
        cbProtocol.getItems().addAll("TCP", "UDP");
        cbProtocol.getSelectionModel().selectFirst();

        Button btnRefresh = controller.getBtnRefresh();
        btnRefresh.setOnAction(event -> {
            if (veil.isVisible()) {
                showTip(optTip, "请稍后...");
                return;
            }
            service.restart();
        });

        Button btnOpt = controller.getBtnOpt();
        btnOpt.setOnAction(event -> {
            if (veil.isVisible()) {
                showTip(optTip, "请稍后...");
                return;
            }

            Pair pair = controller.getPair();
            if (pair == null) {
                alertTip("输入内容有误!", Alert.AlertType.WARNING, stage, null);
                return;
            }
            String text = btnOpt.getText().trim();
            if (text.equals("删除")) {
                pair.setNo(true);
            }

            if (pair.isNo() || optCheck(pair, stage)) {
                optService.setPair(pair);

                String msg = "删除 " + pair.getInside().getIp() + " " + pair.getInside().getPort() + " 外网映射?";
                alertTip(msg, Alert.AlertType.CONFIRMATION, stage, optService);
            }
        });

        optService.valueProperty().addListener((ObservableValue observable,
                                                Object oldValue, Object newValue) -> {
            if (newValue != null){
                ObservableList<MappingResult> list = (ObservableList) newValue;
                if (list.size() > 0){
                    MappingResult result = list.get(0);
                    if (result.getResult() == 0){

                        alertException("操作失败!", result.getMessage(), stage);
                    }else {
                        controller.resetDetail();
                        alertTip("操作成功, 更新映射端口?", Alert.AlertType.CONFIRMATION, stage, service);
                    }
                }
            }
        });

        service.start();

        return root;
    }

    /**
     * 显示窗体
     */
    public void display() {
        launch(null);
    }

    @Override
    public void start(Stage stage) throws Exception {
        DataLoadService service = SpringUtil.getBean("dataLoadService");
        OperateService optService = SpringUtil.getBean("operateService");

        Scene scene = new Scene(createContent(stage, service, optService));
        scene.getStylesheets().add("style/main.css");

        stage.setTitle("端口映射");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * 校验映射信息
     *
     * @return
     */
    public boolean optCheck(Pair pair, Stage stage) {
        String in = pair.getInside().getIp() + ":" + pair.getInside().getPort();
        String out = pair.getOutside().getIp() + ":" + pair.getOutside().getPort();
        if (Constant.insideCacheProvider.containsKey(in) ||
                Constant.outsideCacheProvider.containsKey(out)) {

            alertTip("映射端口已存在!", Alert.AlertType.INFORMATION, stage, null);
            return false;
        }

        return true;
    }

    /**
     * 弹出提示信息
     *
     * @param msg
     * @param type
     * @param stage
     */
    public void alertTip(String msg, Alert.AlertType type, Stage stage, Service service) {
        Alert alert = new Alert(type, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        alert.getDialogPane().setContentText(msg);
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait().filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    if (type == Alert.AlertType.CONFIRMATION) {

                        service.restart();
                    }
                });
    }

    /**
     * 操作异常
     *
     * @param content
     * @param message
     * @param stage
     */
    public void alertException(String content, String message, Stage stage) {
        Dialog<ButtonType> dialog = new Dialog();
        dialog.setTitle("错误");


        final DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK);
        dialogPane.setContentText(content);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        Label label = new Label("内容:");

        TextArea textArea = new TextArea(message);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane root = new GridPane();
        root.setVisible(false);
        root.setMaxWidth(Double.MAX_VALUE);
        root.add(label, 0, 0);
        root.add(textArea, 0, 1);
        dialogPane.setExpandableContent(root);
        dialog.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> System.out.println("The exception was approved"));
    }

    /**
     * 显示提示信息
     *
     * @param t
     * @param msg
     */
    public void showTip(Text t, String msg) {
        t.setText(msg);
        new Timeline(new KeyFrame(Duration.millis(2500),
                e -> t.setText(""))
        ).play();
    }
}
