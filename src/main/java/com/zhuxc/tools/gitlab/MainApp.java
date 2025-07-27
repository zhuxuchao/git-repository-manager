package com.zhuxc.tools.gitlab;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    private GitLabService gitLabService;

    @Override
    public void init() throws Exception {
        super.init();
        gitLabService = new GitLabService();
    }

    @Override
    public void start(Stage primaryStage) {
        GitLabManager manager = new GitLabManager(gitLabService);
        manager.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}