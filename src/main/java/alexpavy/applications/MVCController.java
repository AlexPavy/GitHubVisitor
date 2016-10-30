package alexpavy.applications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/", produces = "application/json")
public class MVCController {

    private static final String GITHUB_ROOT = "https://api.github.com";
    private static final String USERS_ENDPOINT = "/users";
    private static final String REPOS_ENDPOINT = "/repos";

    private final HTTPJSONService httpService;

    @Autowired
    public MVCController(HTTPJSONService httpService) {
        this.httpService = httpService;
    }

    @GetMapping
    public Object get5PublicRepositories(@PathParam(value = "user") String user) {
        if (user == null) {
            throw new IllegalArgumentException("Must provide a GitHub user as path parameter");
        }

        String url = GITHUB_ROOT + USERS_ENDPOINT + "/" + user + REPOS_ENDPOINT;
        ResponseEntity<Object> reposResponse = httpService.executeRequest(url, HttpMethod.GET);
        List<Map> repos = readRepos(reposResponse);
        return sortReposBySize(repos, Direction.DESC).subList(0, 5);
    }

    private List<Map> readRepos(ResponseEntity<Object> reposResponse) {
        final Object responseBody = reposResponse.getBody();
        if (responseBody == null || !(responseBody instanceof List)) {
            throw new IllegalStateException("GitHub response is not a list");
        }
        return new ArrayList<>((List<Map>) responseBody);
    }

    private List sortReposBySize(List<Map> repos, Direction dir) {
        Collections.sort(repos, (repo1, repo2) -> {
            final String sizeKey = "size";
            if (getIntegerValue(repo1, sizeKey) > getIntegerValue(repo2, sizeKey)) {
                return dir == Direction.ASC ? 1 : -1;
            } else if (getIntegerValue(repo1, sizeKey) < getIntegerValue(repo2, sizeKey)) {
                return dir == Direction.ASC ? -1 : 1;
            } else {
                return 0;
            }
        });
        return repos;
    }

    private Integer getIntegerValue(Map repo1, String sizeKey) {
        return (Integer) repo1.get(sizeKey);
    }

}
