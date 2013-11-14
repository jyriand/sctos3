package ee.jyri.scimport.service;


import java.util.List;
import java.util.Map;

public interface SoundcloudService <T> {

    Map< String, T > findUserSongs( String username );
}
