package com.zhuxc.tools.gitlab;

import javafx.application.Application;
import javafx.stage.Stage;
import org.gitlab4j.api.GitLabApi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.prefs.Preferences;

public class MainApp extends Application {
    public static ExecutorService executor;
    @Override
    public void init() throws Exception {
        super.init();
        Preferences prefs = Preferences.userNodeForPackage(SettingsView.class);
        String url = prefs.get("gitlab_url", "http://192.168.1.66/");
        String token = prefs.get("gitlab_token", "Y3qMnj-CR3zaNXkdJays");
        System.out.println("url:" + url);
        System.out.println("token:" + token);
        if (!url.isEmpty() && !token.isEmpty()) {
            GitLabApi gitLabApi = GitlabClientFactory.build(url, token);
            // 更新服务
            GitlabClientFactory.setGitlabApi(gitLabApi);
        }
        executor = Executors.newFixedThreadPool(10);
    }

    @Override
    public void start(Stage primaryStage) {
        GitLabManager manager = new GitLabManager();
        manager.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
        }
    }
}