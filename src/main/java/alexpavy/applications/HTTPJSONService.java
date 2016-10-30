package alexpavy.applications;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class HTTPJSONService {

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<Object> executeRequest(String url, HttpMethod method) {

        final HttpEntity<String> request = buildRequest();
        try {
            ResponseEntity<Object> reposResponse = restTemplate.exchange(url, method, request, Object.class);
            if (reposResponse.getStatusCode() != HttpStatus.OK) {
                throw new IllegalStateException("HTTP call failed with code " + reposResponse.getStatusCode());
            }
            return reposResponse;
        } catch (HttpClientErrorException e) {
            throw new HttpClientErrorException(
                    e.getStatusCode(), "Call for " + url + " failed with message: " + e.getMessage());
        }
    }

    private HttpEntity<String> buildRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

}
