package ee.jyri.scimport.service;


import com.amazonaws.services.s3.model.ObjectMetadata;
import ee.jyri.scimport.domain.Track;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.contains;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class S3TrackUploadServiceTest {
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Mock
    private AwsApiAdapter adapter;

    @InjectMocks
    private S3TrackUploadService service = new S3TrackUploadService();

    private Track track;

    @Before
    public void setUp() throws Exception {
        track = createTestTrack();

    }

    @Test
    public void throwsExceptionIfTrackIsNotDownloadable() throws Exception {
        track.setDownloadable(false);

        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("This track is not downloadable");
        service.uploadTrack(track);

        verifyZeroInteractions(adapter);
    }

    @Test
    public void uploadsTrackToAmazonS3() throws Exception {
        service.uploadTrack(track);
        verify(adapter, times(1)).upload(anyString(),
                anyString(), any(ObjectMetadata.class));
    }

    @Test
    public void downloadUrlHasSoundcloudClientIdAttached() throws Exception {
        String clientId = "1234567";
        service.setClientId(clientId);

        service.uploadTrack(track);
        String url = track.getDownloadUrlWithClientId(clientId);
        assertThat(url, containsString(Track.CLIENT_ID));
        verify(adapter, times(1)).upload(contains(url), anyString(), any(ObjectMetadata.class));
    }

    @Test
    public void ifDownloadUrlLinkHasHTTPS_ProtocolThenChangeItToHTTP() throws Exception {

        track.setDownloadUrl("https://www.test.com");
        service.uploadTrack(track);
        verify(adapter).upload(contains("http://www.test.com"), anyString(), any(ObjectMetadata.class));
    }

    @Test
    public void uploadsOnlyIfTrackIsOfMP3Format() throws Exception {
        track.setFormat("mpeg");
        expectedEx.expect(Exception.class);

        service.uploadTrack(track);

        verifyZeroInteractions(adapter);
    }

    @Test
    public void assemblesObjectMetadata() throws Exception {

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(track.getContentLength());
        meta.setContentType(S3TrackUploadService.CONTENT_TYPE);

        service.uploadTrack(track);

        ArgumentCaptor<ObjectMetadata> argument = ArgumentCaptor.forClass(ObjectMetadata.class);

        verify(adapter).upload(anyString(), anyString(), argument.capture());

        ObjectMetadata value = argument.getValue();
        assertThat(meta.getContentLength(), is(value.getContentLength()));
        assertThat(meta.getContentType(), is(value.getContentType()));
    }

    @Test
    public void addsTimestampToFilenameSoThatFilesWithSameTitleWouldNotBeOverwrittern() throws Exception {

        service.uploadTrack(track);

        verify(adapter).upload(
                anyString(),
                matches("^" + track.getTitle() + "-\\d{13}." + track.getFormat()),
                any(ObjectMetadata.class));
    }

    private Track createTestTrack() {
        Track track = new Track();
        track.setId("123");
        track.setDownloadable(true);
        track.setDownloadUrl("http://fake.url");
        track.setTitle("Title");
        track.setFormat("mp3");
        return track;
    }

}
