package com.diyiliu.ui;

import com.diyiliu.support.util.SpringUtil;
import com.diyiliu.ui.controller.MainController;
import com.diyiliu.ui.service.DataLoadService;
import com.diyiliu.ui.service.OperateService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Description: PortMapApp
 * Author: DIYILIU
 * Update: 2018-03-06 10:37
 */

public class PortMapApp extends Application {

    public Parent createContent(Service service, Service optService) throws Exception {
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
        rVeil.setLayoutX(520);
        rVeil.setPrefSize(260, 510);

        ProgressBar rp = new ProgressBar();
        rp.setPrefSize(100, 10);
        rp.setLayoutX(600);
        rp.setLayoutY(200);

        rp.progressProperty().bind(optService.progressProperty());
        rVeil.visibleProperty().bind(optService.runningProperty());
        rp.visibleProperty().bind(optService.runningProperty());

        root.getChildren().addAll(veil, p, rVeil, rp);


        MainController controller = fxmlLoader.getController();
        Text optTip = controller.getOptTip();
        optTip.getStyleClass().add("tip");

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

        Scene scene = new Scene(createContent(service, optService));
        scene.getStylesheets().add("style/main.css");

        stage.setTitle("端口映射");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void showTip(Text t, String msg) {
        t.setText(msg);
        new Timeline(new KeyFrame(Duration.millis(2500),
                e -> t.setText(""))
        ).play();
    }
}
