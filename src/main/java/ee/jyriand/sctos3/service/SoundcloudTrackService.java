package ee.jyriand.sctos3.service;

import ee.jyriand.sctos3.domain.Track;
import ee.jyriand.sctos3.error.NoSuchUserException;
import ee.jyriand.sctos3.helper.SoundcloudJsonDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class SoundcloudTrackService implements TrackService {

    public static final String ERRORS = "errors";

    @Autowired
    private SoundcloudApiAdapter apiAdapter;

    @Override
    public Map<String, Track> findUserTracks(String username) throws NoSuchUserException {

        HashMap<String, Track> songs = new HashMap<String, Track>();
        SoundcloudJsonDeserializer ds = new SoundcloudJsonDeserializer();

        String response = apiAdapter.getUserTracksJSON(username);

        try {
            if (response.contains(ERRORS)) {
                throw new NoSuchUserException("User: " + username + " doesn't exist");
            }

            for (Track track : ds.getTracks(response)) {
                songs.put(track.getId(), track);
            }
        } catch (IOException e) {

        }

        return songs;
    }
}
