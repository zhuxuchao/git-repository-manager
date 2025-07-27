package com.zhuxc.tools.gitlab;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.MergeRequest;
import org.gitlab4j.api.models.MergeRequestParams;
import org.gitlab4j.api.models.Project;

import java.util.List;
import java.util.prefs.Preferences;

public class GitLabService {

    private static final String GITLAB_URL = "https://gitlab.example.com";
    private static final String PERSONAL_TOKEN = "your-personal-access-token";

    private GitLabApi gitLabApi;

    public GitLabService() {
        // gitLabApi = new GitLabApi(GITLAB_URL, PERSONAL_TOKEN);
        // 尝试从配置加载
        Preferences prefs = Preferences.userNodeForPackage(GitLabService.class);
        String url = prefs.get("gitlab_url", "");
        String token = prefs.get("gitlab_token", "");

        if (!url.isEmpty() && !token.isEmpty()) {
            gitLabApi = new GitLabApi(url, token);
        }
    }

    public List<Project> getAllProjects() throws GitLabApiException {
        return gitLabApi.getProjectApi().getProjects();
    }

    public List<Commit> getBranchCommits(String projectId, String branchName) throws GitLabApiException {
        return gitLabApi.getCommitsApi().getCommits(projectId, branchName, null, null);
    }

    public MergeRequest createMergeRequest(String projectId,
                                           String sourceBranch,
                                           String targetBranch,
                                           String title) throws GitLabApiException {
        MergeRequestParams params = new MergeRequestParams()
                .withSourceBranch(sourceBranch)
                .withTargetBranch(targetBranch)
                .withTitle(title);

        return gitLabApi.getMergeRequestApi().createMergeRequest(projectId, params);
    }

    public void updateConnection(String url, String token) throws GitLabApiException {
        if (gitLabApi != null) {
            gitLabApi.close(); // 关闭旧连接
        }
        gitLabApi = new GitLabApi(url, token);
    }
}