package com.zhuxc.tools.gitlab;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class CommitsView extends VBox {
    private GitLabService gitLabService;
    private ComboBox<String> branchSelector;
    private TableView<CommitInfo> commitsTable;

    public CommitsView(GitLabService gitLabService) {
        this.gitLabService = gitLabService;
        getStyleClass().add("commits-view");
        setSpacing(15);
        setPadding(new Insets(20));

        // 标题
        Label title = new Label("Latest Commits by Branch");
        title.getStyleClass().add("view-title");

        // 分支选择器
        HBox selectorBox = new HBox(10);
        selectorBox.setAlignment(Pos.CENTER_LEFT);

        Label branchLabel = new Label("Select Branch:");
        branchSelector = new ComboBox<>();
        branchSelector.getItems().addAll("main", "develop", "staging", "production");
        branchSelector.setValue("main");

        selectorBox.getChildren().addAll(branchLabel, branchSelector);

        // 提交表格
        commitsTable = new TableView<>();
        setupCommitsTable();

        // 刷新按钮
        Button refreshBtn = new Button("Refresh");
        refreshBtn.getStyleClass().add("action-btn");
        refreshBtn.setOnAction(e -> refreshCommits());

        // 添加到布局
        getChildren().addAll(title, selectorBox, commitsTable, refreshBtn);

        // 初始加载数据
        refreshCommits();
    }

    private void setupCommitsTable() {
        TableColumn<CommitInfo, String> repoCol = new TableColumn<>("Repository");
        repoCol.setCellValueFactory(new PropertyValueFactory<>("repository"));

        TableColumn<CommitInfo, String> idCol = new TableColumn<>("Commit ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("commitId"));

        TableColumn<CommitInfo, String> messageCol = new TableColumn<>("Message");
        messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));

        TableColumn<CommitInfo, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<CommitInfo, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        commitsTable.getColumns().addAll(repoCol, idCol, messageCol, authorCol, dateCol);
        commitsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void refreshCommits() {
        String selectedBranch = branchSelector.getValue();

        // 这里应该调用GitLab4J API获取数据
        // 以下是模拟数据
        ObservableList<CommitInfo> data = FXCollections.observableArrayList(
                new CommitInfo("project-api", "a1b2c3d", "Fix login bug", "John Doe", "2023-05-15"),
                new CommitInfo("web-ui", "e4f5g6h", "Update styles", "Jane Smith", "2023-05-14"),
                new CommitInfo("mobile-app", "i7j8k9l", "Add push notifications", "Mike Johnson", "2023-05-13")
        );

        commitsTable.setItems(data);
    }
}