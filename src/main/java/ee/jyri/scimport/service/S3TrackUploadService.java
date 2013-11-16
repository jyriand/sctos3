package ee.jyri.scimport.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import ee.jyri.scimport.domain.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class S3TrackUploadService implements UploadService {

    @Autowired
    private AwsApiAdapter awsApiAdapter;

    @Override
    public void uploadTrack(Track track) throws Exception {

        if( ! track.isDownloadable() ){
            throw new Exception( "This track is not downloadable" );
        }

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength( track.getContentLength());
        meta.setContentType( "application/mp3" );

        awsApiAdapter.upload( track.getDownloadUrl(), meta );
    }
}
