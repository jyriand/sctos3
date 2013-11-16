package ee.jyri.scimport.service;

import ee.jyri.scimport.domain.Track;
import org.springframework.stereotype.Service;

@Service
public class S3UploadService implements UploadService<Track> {
    @Override
    public void uploadFile(Track fileName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
