/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsdata.dao;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.List;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.client.GitHubClient;
import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_REPOS;
import org.eclipse.egit.github.core.client.PagedRequest;
import org.eclipse.egit.github.core.service.GitHubService;

/**
 *
 * @author Jonskichov
 */
public class ReleaseService extends GitHubService {
    
    private static final String SEGMENT_RELEASES = "/releases";
    
    
    public ReleaseService(GitHubClient client) {
        super(client);
    }
    
    /**
     * Get releases in given repository
     *
     * @param repository
     * @return list of releases
     * @throws IOException
     */
    public List<Release> getReleases(IRepositoryIdProvider repository) throws IOException {
        String id = getId(repository);
        StringBuilder uri = new StringBuilder(SEGMENT_REPOS);
        uri.append('/').append(id);
        uri.append(SEGMENT_RELEASES);
        PagedRequest<Release> request = createPagedRequest();
        request.setUri(uri);
        request.setType(new TypeToken<List<Release>>() {}.getType());
        return getAll(request);
    }
    
    public Release getLatestRelease(IRepositoryIdProvider repository) throws IOException {
        return getLatestRelease(getReleases(repository));
    }
    
    public Release getLatestRelease(List<Release> releases) throws IOException {
        Release latestRelease = null;
        for (Release release : releases) {
            if (latestRelease == null || release.getPublishedAt().after(latestRelease.getPublishedAt())) {
                latestRelease = release;
            }
        }
        return latestRelease;
    }
}