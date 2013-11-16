package ee.jyri.scimport.service;


import ee.jyri.scimport.domain.Track;
import ee.jyri.scimport.error.NoSuchUserException;

import java.util.Map;

public interface TrackService {

    Map< String, Track> findUserTracks(String username) throws Exception, NoSuchUserException;
}
