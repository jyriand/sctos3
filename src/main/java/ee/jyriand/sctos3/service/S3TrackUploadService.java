package ee.jyriand.sctos3.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import ee.jyriand.sctos3.domain.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class S3TrackUploadService implements UploadService {

    private static final Logger logger = LoggerFactory.getLogger(S3TrackUploadService.class);

    public static final String CONTENT_TYPE = "audio/mp3";

    @Autowired
    private AwsApiAdapter awsApiAdapter;

    @Autowired
    private SoundcloudTrackService soundcloudTrackService;

    @Value("${soundcloud.clientId}")
    private String clientId;

    @Override
    public void uploadTrack(Track track) throws Exception {
        logger.info("Uploading track");
        if (!track.isDownloadable()) {
            throw new Exception("This track is not downloadable");
        } else if (!track.isMp3()) {
            throw new Exception("Only mp3 format is supported");
        }

        awsApiAdapter.upload(track.getDownloadUrlWithClientId(clientId), track.getKey(), createObjectMetadata(track));
    }

    private ObjectMetadata createObjectMetadata(Track track) {
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(track.getContentLength());
        meta.setContentType(CONTENT_TYPE);
        return meta;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
