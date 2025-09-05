package com.zhuxc.tools.gitlab;

import org.gitlab4j.api.GitLabApi;

/**
 * @author zhuxuchao
 * @date 2025/8/24
 */
public class GitlabClientFactory {
    private static GitLabApi gitlabApi;
    public static GitLabApi build(String url, String token) {
        return new GitLabApi(url, token);
    }

    public static GitLabApi getGitlabApi() {
        return gitlabApi;
    }

    public static void setGitlabApi(GitLabApi gitlabApi) {
        GitlabClientFactory.gitlabApi = gitlabApi;
    }
}
