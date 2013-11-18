package ee.jyri.scimport.ee.jyri.scimport.integration;

import com.amazonaws.services.s3.transfer.TransferManager;
import ee.jyri.scimport.domain.Track;
import ee.jyri.scimport.service.AwsApiAdapter;
import ee.jyri.scimport.service.S3TrackUploadService;
import ee.jyri.scimport.service.SoundcloudTrackService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml")
public class IntegrationTest {
    @Autowired
    private SoundcloudTrackService trackService;

    @Autowired
    private S3TrackUploadService uploadService;

    @Autowired
    private AwsApiAdapter awsApiAdapter;

    @Value("${s3.bucketName}")
    private String bucketName;


    @Test
    public void soundcloudAdapterCanFetchUserTracksJson() throws Exception {
        Map<String, Track> tracks = trackService.findUserTracks("apitest");
        assertThat(tracks.size(), greaterThan(1));
    }

    @Test
    public void awsApiAdapterHasBucketNamePropertyInjected() throws Exception {
        String bucket = (String) ReflectionTestUtils.getField(awsApiAdapter, "bucketName");
        assertThat(bucket, notNullValue());
        assertThat(bucket, is(bucketName));
    }

    @Test
    public void awsApiAdapterHasTransferManagerInjected() throws Exception {
        TransferManager transferManager = (TransferManager) ReflectionTestUtils.getField(awsApiAdapter, "transferManager");
        assertThat(transferManager, notNullValue());
    }
}
