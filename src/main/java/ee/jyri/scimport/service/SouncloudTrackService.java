package ee.jyri.scimport.service;

import ee.jyri.scimport.domain.Track;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SouncloudTrackService implements TrackService {

    @Override
    public Map<String, Track> findUserTracks(String username) {

        HashMap<String, Track> songs = new HashMap<String, Track>();
        songs.put( "1", new Track("1"));
        songs.put("2", new Track("2"));

        return songs;
    }
}
