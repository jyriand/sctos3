package ee.jyri.scimport.service;


import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Http;
import com.soundcloud.api.Request;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SoundcloudApiAdapter {

    public static final String SOUNDCLOUD_URL = "http://soundcloud.com/";

    @Value("${soundcloud.clientId}")
    private String clientId;

    public String getUserTracksJSON(String username) {

        String tracksJson = null;
        try {
            ApiWrapper wrapper = new ApiWrapper(clientId, null, null, null);

            long userId = wrapper.resolve(SOUNDCLOUD_URL + username);

            HttpResponse response = wrapper.get(Request.to("/users/%d/tracks/", userId));
            tracksJson = Http.formatJSON(Http.getString(response));
        } catch (IOException e) {
            System.out.println("Error");
        }

        return tracksJson;
    }
}
