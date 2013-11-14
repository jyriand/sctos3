package ee.jyri.scimport.service;

import ee.jyri.scimport.domain.Song;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SouncloudServiceImpl implements SoundcloudService<Song> {

    @Override
    public Map<String, Song> findUserSongs(String username) {
        HashMap<String, Song> songs = new HashMap<String, Song>();
        songs.put( "1", new Song("1"));
        songs.put("2", new Song("2"));

        return songs;
    }
}
