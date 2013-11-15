package ee.jyri.scimport.service;

import ee.jyri.scimport.domain.Track;
import ee.jyri.scimport.error.NoSuchUserException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SoundcloudTrackService implements TrackService {

    @Override
    public Map<String, Track> findUserTracks(String username) throws NoSuchUserException {

        HashMap<String, Track> songs = new HashMap<String, Track>();
        if( username.equals("InValidUser"))
        {
            throw new NoSuchUserException("User: " + username + " doesn't exist");
        }
        songs.put( "1", new Track("1"));
        return songs;
    }
}
