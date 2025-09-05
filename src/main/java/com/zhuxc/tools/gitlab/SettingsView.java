package com.zhuxc.tools.gitlab;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.prefs.Preferences;

public class SettingsView extends VBox {

    private TextField gitLabUrlField;
    private PasswordField tokenField;
    private CheckBox saveConfigCheckbox;
    public SettingsView() {
        initialize();
    }

    private void initialize() {
        getStyleClass().add("settings-view");
        setSpacing(20);
        setPadding(new Insets(20));

        // 标题
        Label title = new Label("Settings");
        title.getStyleClass().add("view-title");

        // 表单容器
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(15);
        formGrid.setPadding(new Insets(10));

        // GitLab URL
        Label urlLabel = new Label("GitLab Server URL:");
        gitLabUrlField = new TextField();
        gitLabUrlField.setPromptText("https://gitlab.example.com");
        formGrid.addRow(0, urlLabel, gitLabUrlField);

        // Access Token
        Label tokenLabel = new Label("Personal Access Token:");
        tokenField = new PasswordField();
        tokenField.setPromptText("Paste your token here");
        formGrid.addRow(1, tokenLabel, tokenField);

        // 保存配置选项
        saveConfigCheckbox = new CheckBox("Save configuration locally");
        saveConfigCheckbox.setSelected(true);
        formGrid.add(saveConfigCheckbox, 1, 2);

        // 测试连接按钮
        Button testConnectionBtn = new Button("Test Connection");
        testConnectionBtn.getStyleClass().add("action-btn");
        testConnectionBtn.setOnAction(e -> testConnection());

        // 保存按钮
        Button saveBtn = new Button("Save Settings");
        saveBtn.getStyleClass().add("action-btn");
        saveBtn.setOnAction(e -> saveSettings());

        // 按钮容器
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(testConnectionBtn, saveBtn);

        // 状态标签
        Label statusLabel = new Label();
        statusLabel.getStyleClass().add("status-label");

        // 添加到主布局
        getChildren().addAll(title, formGrid, buttonBox, statusLabel);

        // 加载已保存的配置
        loadSavedSettings();
    }

    private void testConnection() {
        String url = gitLabUrlField.getText().trim();
        String token = tokenField.getText().trim();

        if (url.isEmpty() || token.isEmpty()) {
            showStatus("Please enter both URL and token", "error");
            return;
        }

        try {
            GitLabApi gitLabApi = GitlabClientFactory.build(url, token);
            // 测试连接
            gitLabApi.getProjectApi().getProjects("xf-",1, 1);
            // 更新服务
            // GitlabClientFactory.setGitlabApi(gitLabApi);
            showStatus("Connection successful!", "success");
        } catch (GitLabApiException e) {
            showStatus("Connection failed: " + e.getMessage(), "error");
        }
    }

    private void saveSettings() {
        String url = gitLabUrlField.getText().trim();
        String token = tokenField.getText().trim();

        if (url.isEmpty() || token.isEmpty()) {
            showStatus("Please enter both URL and token", "error");
            return;
        }

        try {
            // 保存到配置
            Preferences prefs = Preferences.userNodeForPackage(SettingsView.class);
            if (saveConfigCheckbox.isSelected()) {
                prefs.put("gitlab_url", url);
                prefs.put("gitlab_token", token); // 注意: 这不是最安全的方式
            } else {
                prefs.remove("gitlab_url");
                prefs.remove("gitlab_token");
            }
            GitLabApi gitLabApi = GitlabClientFactory.build(url, token);
            // 测试连接
            gitLabApi.getProjectApi().getProjects("xf-",1, 1);
            // 更新服务
            GitlabClientFactory.setGitlabApi(gitLabApi);
            showStatus("Settings saved successfully!", "success");
        } catch (Exception e) {
            showStatus("Error saving settings: " + e.getMessage(), "error");
        }
    }

    private void loadSavedSettings() {
        Preferences prefs = Preferences.userNodeForPackage(SettingsView.class);
        String url = prefs.get("gitlab_url", "");
        String token = prefs.get("gitlab_token", "");

        if (!url.isEmpty()) {
            gitLabUrlField.setText(url);
            saveConfigCheckbox.setSelected(true);
        }

        if (!token.isEmpty()) {
            tokenField.setText(token);
        }
    }

    private void showStatus(String message, String type) {
        // 在实际应用中，您可以使用Notification控件或Alert对话框
        Label statusLabel = (Label) getChildren().get(getChildren().size() - 1);
        statusLabel.setText(message);

        statusLabel.getStyleClass().removeAll("error", "success");
        statusLabel.getStyleClass().add(type);
    }
}