package ee.jyri.scimport.service;


import ee.jyri.scimport.domain.Track;

public interface UploadService {

    void uploadTrack(Track fileName) throws Exception;
}
