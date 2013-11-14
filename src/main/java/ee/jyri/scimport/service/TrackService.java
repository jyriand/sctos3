package ee.jyri.scimport.service;


import ee.jyri.scimport.domain.Track;

import java.util.List;
import java.util.Map;

public interface TrackService {

    Map< String, Track> findUserTracks(String username);
}
