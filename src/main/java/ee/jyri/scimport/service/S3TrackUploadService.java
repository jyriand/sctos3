package ee.jyri.scimport.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import ee.jyri.scimport.domain.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class S3TrackUploadService implements UploadService {

    public static final String CONTENT_TYPE = "audio/mpeg";

    @Autowired
    private AwsApiAdapter awsApiAdapter;

    @Override
    public void uploadTrack(Track track) throws Exception {

        if( !track.isDownloadable()){
            throw new Exception( "This track is not downloadable" );
        }
        else if(!track.isMp3() )
        {
            throw new Exception( "Only mp3 format is supported" );
        }

        awsApiAdapter.upload( track.getDownloadUrl(), track.getTitle(), createObjectMetadata(track));
    }

    private ObjectMetadata createObjectMetadata(Track track) {
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength( track.getContentLength());
        meta.setContentType( CONTENT_TYPE );
        return meta;
    }
}
