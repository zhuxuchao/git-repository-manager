package com.zhuxc.tools.gitlab;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.utils.DateUtils;
import org.gitlab4j.api.CommitsApi;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CommitsView extends VBox {
    private static final Log log = LogFactory.getLog(CommitsView.class);
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private TextField branchField;
    private TextField sinceField;
    private TextField projectField;
    private TableView<CommitInfo> commitsTable;

    public CommitsView() {
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
        branchField = new TextField("dev");
        Label sinceLabel = new Label("Since Date:");
        String defaultSince = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).format(DATE_TIME_FORMATTER);
        sinceField = new TextField(defaultSince);
        Label projectLabel = new Label("Project Name:");
        projectField = new TextField("xf-");
        // TextField 宽度设置代码
        branchField.setPrefWidth(100);  // 设置推荐宽度为100像素
        branchField.setMaxWidth(150);   // 设置最大宽度为150像素

        sinceField.setPrefWidth(150);   // 设置推荐宽度为150像素
        sinceField.setMaxWidth(150);    // 设置最大宽度为200像素

        projectField.setPrefWidth(120); // 设置推荐宽度为120像素
        projectField.setMaxWidth(200);  // 设置最大宽度为200像素
        selectorBox.getChildren().addAll(branchLabel, branchField, sinceLabel, sinceField, projectLabel, projectField);

        // 提交表格
        commitsTable = new TableView<>();
        setupCommitsTable();

        // 刷新按钮
        Button refreshBtn = new Button("查询");
        refreshBtn.getStyleClass().add("action-btn");
        refreshBtn.setOnAction(e -> refreshCommits());

        // 添加到布局
        getChildren().addAll(title, selectorBox, refreshBtn, commitsTable);

        // 初始加载数据
        // refreshCommits();
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
        commitsTable.setPlaceholder(new Label("No commits found."));
        // 这里怎么设置表格高度自适应？？
        commitsTable.setPrefHeight(TableView.USE_COMPUTED_SIZE);
        commitsTable.getItems().addListener((ListChangeListener<CommitInfo>) c -> {
            int rowCount = commitsTable.getItems().size();
            double rowHeight = 24.0; // 假设每行高度为24像素
            double headerHeight = 30.0; // 表头高度
            double newHeight = headerHeight + (rowCount * rowHeight);
            commitsTable.setPrefHeight(Math.max(newHeight, 100)); // 最小高度100
        });
        // 设置选中行的样式
        String tableStyle =
                ".table-row-cell:selected { " +
                        "-fx-background-color: #2196f3; " +  // 蓝色背景
                        "} " +
                        ".table-row-cell:selected .table-cell { " +
                        "-fx-text-fill: white; " +  // 白色文字
                        "} " +
                        ".table-row-cell:selected:focused { " +
                        "-fx-background-color: #1976d2; " +  // 焦点状态下的深蓝色背景
                        "}";

        commitsTable.getStylesheets().add("data:text/css," + tableStyle);
    }

    private void adjustTableHeight() {
        int rowCount = commitsTable.getItems().size();
        double rowHeight = commitsTable.getFixedCellSize();
        if (rowHeight <= 0) {
            rowHeight = 24.0; // 默认行高
        }
        double headerHeight = 30.0;
        double newHeight = headerHeight + (rowCount * rowHeight);
        commitsTable.setPrefHeight(Math.max(newHeight, 150)); // 设置最小高度
    }

    private void refreshCommits() {
        commitsTable.getItems().clear();
        GitLabApi gitLabApi = GitlabClientFactory.getGitlabApi();
        if (gitLabApi == null) {
            commitsTable.setPlaceholder(new Label("GitLabApi not init..."));
            return;
        }
        String selectedBranch = branchField.getText();
        String sinceDateStr = sinceField.getText();
        Date sinceDate = sinceDateStr != null ? DateUtils.parseDate(sinceDateStr, new String[]{"yyyy-MM-dd HH:mm:ss"}) : null;

        try {
            String text = projectField.getText();
            List<Project> projects = gitLabApi.getProjectApi().getProjects(text);
            CommitsApi commitsApi = gitLabApi.getCommitsApi();
            // 使用线程安全的集合
            List<CommitInfo> commits = Collections.synchronizedList(new ArrayList<>());
            // 或者使用 CopyOnWriteArrayList
            // List<CommitInfo> commits = new CopyOnWriteArrayList<>();

            // 使用自定义线程池来控制并发数量
            List<CompletableFuture<Void>> futures = new ArrayList<>();

            for (Project project : projects) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        Long projectId = project.getId();
                        commitsApi.getCommits(projectId, selectedBranch, sinceDate, null, 1, 1)
                                .stream()
                                .findFirst()
                                .ifPresent(commit -> {
                                    String commitDate = LocalDateTime.ofInstant(
                                                    commit.getCommittedDate().toInstant(),
                                                    ZoneId.systemDefault())
                                            .format(DATE_TIME_FORMATTER);
                                    commits.add(new CommitInfo(
                                            project.getName(),
                                            commit.getId(),
                                            commit.getMessage(),
                                            commit.getAuthorName(),
                                            commitDate));
                                });
                    } catch (GitLabApiException ex) {
                        log.error("查询项目 " + project.getName() + " 提交记录异常", ex);
                    }
                }, MainApp.executor);
                futures.add(future);
            }

            // 等待所有任务完成
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            // 排序并更新UI
            commits.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
            Platform.runLater(() -> {
                commitsTable.getItems().addAll(commits);
                adjustTableHeight();
            });
        } catch (GitLabApiException e) {
            log.error("获取项目列表异常", e);
        }
    }


}