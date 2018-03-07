package com.diyiliu;

import com.diyiliu.controller.MainController;
import com.diyiliu.service.MainService;
import com.diyiliu.service.OperateService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
 * Description: MainApp
 * Author: DIYILIU
 * Update: 2018-03-06 10:37
 */
public class MainApp extends Application {

    private ScrollPane scrollPane;

    private final MainService service = new MainService();

    private final OperateService optService = new OperateService();

    public Parent createContent() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ClassLoader.getSystemResource("mainScene.fxml"));
        AnchorPane root = fxmlLoader.load();
        scrollPane = (ScrollPane) root.lookup("#scrPn");

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
        veil.setPrefSize(510, 510);

        ProgressIndicator p = new ProgressIndicator();
        p.setPrefSize(120, 120);
        p.setLayoutX(200);
        p.setLayoutY(200);

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

        p.progressProperty().bind(service.progressProperty());
        veil.visibleProperty().bind(service.runningProperty());
        p.visibleProperty().bind(service.runningProperty());
        tv.itemsProperty().bind(service.valueProperty());

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

    public static void main(String[] args) {

        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent());
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
