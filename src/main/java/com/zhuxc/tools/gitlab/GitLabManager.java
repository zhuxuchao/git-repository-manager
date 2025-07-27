package com.zhuxc.tools.gitlab;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GitLabManager extends Application {
    private GitLabService gitLabService;
    private BorderPane root;
    private Scene scene;
    private DashboardView dashboard;
    private CommitsView commitsView;
    private MergeView mergeView;
    private SettingsView settingsView;

    public GitLabManager(GitLabService gitLabService) {
        this.gitLabService = gitLabService;
        this.dashboard = new DashboardView(this.gitLabService);
        this.commitsView = new CommitsView(this.gitLabService);
        this.mergeView = new MergeView(this.gitLabService);
        this.settingsView = new SettingsView(this.gitLabService);

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
        showView(this.dashboard);
        
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
        Button dashboardBtn = createSidebarButton("Dashboard", "dashboard-icon");
        Button commitsBtn = createSidebarButton("Latest Commits", "commits-icon");
        Button mergeBtn = createSidebarButton("Merge Branches", "merge-icon");
        Button settingsBtn = createSidebarButton("Settings", "settings-icon");
        
        // 添加按钮到侧边栏
        sidebar.getChildren().addAll(dashboardBtn, commitsBtn, mergeBtn, settingsBtn);
        dashboardBtn.setOnAction(handler -> {
            showView(this.dashboard);
        });
        commitsBtn.setOnAction(handler -> {
            showView(this.commitsView);
        });
        mergeBtn.setOnAction( handler -> {
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