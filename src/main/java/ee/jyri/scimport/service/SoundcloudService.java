package ee.jyri.scimport.service;


import java.util.List;

public interface SoundcloudService <T> {

    List< T > findUserSongs( String username );
}
