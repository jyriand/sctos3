package ee.jyri.scimport.service;


import java.util.List;

public interface SoundcloudService {

    List< String > findUserSongs( String username );
}
