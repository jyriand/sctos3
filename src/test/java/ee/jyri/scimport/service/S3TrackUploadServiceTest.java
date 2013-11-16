package ee.jyri.scimport.service;


import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sun.tools.internal.ws.wsdl.document.mime.MIMEContent;
import com.sun.xml.internal.xsom.impl.Ref;
import ee.jyri.scimport.domain.Track;
import org.apache.http.entity.mime.MIME;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.activation.MimeType;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class S3TrackUploadServiceTest
{
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
    public void throwsExceptionIfTrackCannotBeDownloaded() throws Exception {
        track.setDownloadable( false );

        expectedEx.expect( Exception.class );
        expectedEx.expectMessage( "This track is not downloadable");
        service.uploadTrack( track );
    }

    @Test
    public void uploadsTrackToAmazonS3() throws Exception {
        service.uploadTrack( track );
        verify(adapter, times(1)).upload(anyString(), any(ObjectMetadata.class));
    }

    @Test
    public void uploadsTrackFromCorrectDownloadUrl() throws Exception {
        service.uploadTrack( track );
        verify(adapter, times(1)).upload(matches(track.getDownloadUrl()), any(ObjectMetadata.class) );
    }


    @Test
    public void assemblesObjectMetadata() throws Exception {

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength( track.getContentLength());
        meta.setContentType( "application/mp3" );

        service.uploadTrack( track );

        ArgumentCaptor<ObjectMetadata> argument = ArgumentCaptor.forClass(ObjectMetadata.class);

        verify(adapter).upload(anyString(), argument.capture());
        ObjectMetadata value = argument.getValue();
        assertThat( meta.getContentLength(), is(value.getContentLength()));
        assertThat( meta.getContentType(), is(value.getContentType()));
    }

    private Track createTestTrack() {
        Track track = new Track();
        track.setId("123");
        track.setDownloadable( true );
        track.setDownloadUrl("http://fake.url");
        return track;
    }

}
