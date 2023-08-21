package msUsers.services;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AllowedUrlsProvider {

    public List<String> getAllowedUrls() {
        return Arrays.asList(
                "/api/getImage/*.jpeg",
                "/api/getImage/*.jpg",
                "/api/getImage/*.png");
    }
}
