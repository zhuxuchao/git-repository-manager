package com.zhuxc.tools.gitlab;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GitLabManager extends Application {
    private BorderPane root;
    private Scene scene;
    private CommitsView commitsView;
    private MergeView mergeView;
    private SettingsView settingsView;

    public GitLabManager() {
        this.commitsView = new CommitsView();
        this.mergeView = new MergeView();
        this.settingsView = new SettingsView();
    }

    @Override
    public void start(Stage primaryStage) {
        // 创建主容器
        root = new BorderPane();
        StackPane mainContent = new StackPane();
        mainContent.getStyleClass().add("main-content");
        root.setCenter(mainContent);
        scene = new Scene(root, 1000, 700);

        // 应用CSS样式
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // 设置顶部导航
        setupTopNavigation();

        // 设置侧边栏
        setupSidebar();

        // 设置主内容区
        showView(this.commitsView);

        // 配置舞台
        primaryStage.setTitle("GitLab Repository Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupTopNavigation() {
        HBox topBar = new HBox();
        topBar.getStyleClass().add("top-bar");

        Label title = new Label("GitLab Repository Manager");
        title.getStyleClass().add("title");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // 添加搜索框
        TextField searchField = new TextField();
        searchField.setPromptText("Search repositories...");
        searchField.getStyleClass().add("search-field");

        topBar.getChildren().addAll(title, spacer, searchField);
        root.setTop(topBar);
    }

    private void setupSidebar() {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");

        // 功能按钮
        Button commitsBtn = createSidebarButton("Latest Commits", "commits-icon");
        Button mergeBtn = createSidebarButton("Merge Branches", "merge-icon");
        Button settingsBtn = createSidebarButton("Settings", "settings-icon");

        // 添加按钮到侧边栏
        sidebar.getChildren().addAll(commitsBtn, mergeBtn, settingsBtn);
        commitsBtn.setOnAction(handler -> {
            showView(this.commitsView);
        });
        mergeBtn.setOnAction(handler -> {
            showView(this.mergeView);
        });
        settingsBtn.setOnAction(handler -> {
            showView(this.settingsView);
        });

        // 底部添加一些空间
        VBox.setVgrow(sidebar, Priority.ALWAYS);

        root.setLeft(sidebar);
    }

    private Button createSidebarButton(String text, String iconClass) {
        Button btn = new Button(text);
        btn.getStyleClass().addAll("sidebar-btn", iconClass);
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    private void showView(Node view) {
        StackPane mainContent = (StackPane) root.getCenter();
        mainContent.getChildren().clear();
        mainContent.getChildren().add(view);
    }

    public static void main(String[] args) {
        launch(args);
    }
}