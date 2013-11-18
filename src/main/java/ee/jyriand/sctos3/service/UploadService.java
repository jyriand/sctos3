package ee.jyriand.sctos3.service;


import ee.jyriand.sctos3.domain.Track;

public interface UploadService {

    void uploadTrack(Track fileName) throws Exception;
}
