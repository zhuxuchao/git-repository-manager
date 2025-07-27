package com.zhuxc.tools.gitlab;

public class GitActivity {
    private String repository;
    private String branch;
    private String commitId;
    private String author;
    private String date;

    public GitActivity(String repository, String branch, String commitId, String author, String date) {
        this.repository = repository;
        this.branch = branch;
        this.commitId = commitId;
        this.author = author;
        this.date = date;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
