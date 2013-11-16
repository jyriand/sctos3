package ee.jyri.scimport.service;


import ee.jyri.scimport.domain.Track;

import java.io.File;

public interface UploadService {

    void uploadTrack(Track fileName);
}
