package com.zhuxc.tools.gitlab;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;



public class DashboardView extends VBox {
    private GitLabService gitLabService;

    public DashboardView(GitLabService gitLabService) {
        this.gitLabService = gitLabService;
        getStyleClass().add("dashboard");

        // 仓库概览卡片
        HBox overviewCards = new HBox(20);
        overviewCards.getStyleClass().add("overview-cards");

        overviewCards.getChildren().addAll(
                createCard("Total Repositories", "42", "repos-icon"),
                createCard("Active Repositories", "28", "active-icon"),
                createCard("Outdated Branches", "5", "outdated-icon")
        );

        // 最近活动列表
        Label recentActivityLabel = new Label("Recent Activity");
        recentActivityLabel.getStyleClass().add("section-title");

        TableView<GitActivity> activityTable = new TableView<>();
        setupActivityTable(activityTable);

        // 添加到主布局
        getChildren().addAll(overviewCards, recentActivityLabel, activityTable);
        setSpacing(20);
        setPadding(new Insets(20));
    }

    private void setupActivityTable(TableView<GitActivity> table) {
        TableColumn<GitActivity, String> repoCol = new TableColumn<>("Repository");
        repoCol.setCellValueFactory(new PropertyValueFactory<>("repository"));

        TableColumn<GitActivity, String> branchCol = new TableColumn<>("Branch");
        branchCol.setCellValueFactory(new PropertyValueFactory<>("branch"));

        TableColumn<GitActivity, String> commitCol = new TableColumn<>("Last Commit");
        commitCol.setCellValueFactory(new PropertyValueFactory<>("commitId"));

        TableColumn<GitActivity, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<GitActivity, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        table.getColumns().addAll(repoCol, branchCol, commitCol, authorCol, dateCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // 示例数据
        ObservableList<GitActivity> data = FXCollections.observableArrayList(
                new GitActivity("project-api", "main", "a1b2c3d", "John Doe", "2 hours ago"),
                new GitActivity("web-ui", "dev", "e4f5g6h", "Jane Smith", "1 day ago"),
                new GitActivity("mobile-app", "feature/login", "i7j8k9l", "Mike Johnson", "3 days ago")
        );

        table.setItems(data);
    }

    private Node createCard(String title, String value, String iconClass) {
        VBox card = new VBox(10);
        card.getStyleClass().add("card");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("card-value");

        // 图标占位符
        Region icon = new Region();
        icon.getStyleClass().addAll("card-icon", iconClass);
        icon.setMinSize(40, 40);

        card.getChildren().addAll(icon, titleLabel, valueLabel);
        card.setAlignment(Pos.CENTER);

        return card;
    }
}