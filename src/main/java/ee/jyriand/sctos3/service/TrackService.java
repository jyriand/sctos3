package ee.jyriand.sctos3.service;


import ee.jyriand.sctos3.domain.Track;
import ee.jyriand.sctos3.error.NoSuchUserException;

import java.util.Map;

public interface TrackService {

    Map<String, Track> findUserTracks(String username) throws Exception, NoSuchUserException;
}
