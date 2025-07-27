package com.zhuxc.tools.gitlab;

public class CommitInfo {


    private String repository;
    private String commitId;
    private String message;
    private String author;
    private String date;

    public CommitInfo(String repository, String commitId, String message, String author, String date) {
        this.repository = repository;
        this.commitId = commitId;
        this.message = message;
        this.author = author;
        this.date = date;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
