package com.zhuxc.tools.gitlab;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MergeView extends VBox {
    private ComboBox<String> sourceBranchSelector;
    private ComboBox<String> targetBranchSelector;
    private ListView<String> reposList;

    public MergeView() {
        getStyleClass().add("merge-view");
        setSpacing(15);
        setPadding(new Insets(20));

        // 标题
        Label title = new Label("Merge Branches Across Repositories");
        title.getStyleClass().add("view-title");

        // 分支选择部分
        GridPane branchGrid = new GridPane();
        branchGrid.setHgap(10);
        branchGrid.setVgap(10);

        Label sourceLabel = new Label("Source Branch:");
        sourceBranchSelector = new ComboBox<>();
        sourceBranchSelector.getItems().addAll("feature/login", "bugfix/header", "dev");

        Label targetLabel = new Label("Target Branch:");
        targetBranchSelector = new ComboBox<>();
        targetBranchSelector.getItems().addAll("main", "develop", "staging");

        branchGrid.addRow(0, sourceLabel, sourceBranchSelector);
        branchGrid.addRow(1, targetLabel, targetBranchSelector);

        // 仓库选择部分
        Label reposLabel = new Label("Select Repositories:");
        reposList = new ListView<>();
        reposList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        reposList.getItems().addAll(
                "project-api",
                "web-ui",
                "mobile-app",
                "authentication-service",
                "database-migration"
        );

        // 操作按钮
        HBox buttonBox = new HBox(10);
        Button previewBtn = new Button("Preview Merge");
        previewBtn.getStyleClass().add("action-btn");

        Button executeBtn = new Button("Execute Merge");
        executeBtn.getStyleClass().add("danger-btn");

        buttonBox.getChildren().addAll(previewBtn, executeBtn);

        // 添加到布局
        getChildren().addAll(title, branchGrid, reposLabel, reposList, buttonBox);
    }

}