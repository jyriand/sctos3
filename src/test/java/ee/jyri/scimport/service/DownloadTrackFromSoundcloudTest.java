package ee.jyri.scimport.service;

import ee.jyri.scimport.domain.Track;
import ee.jyri.scimport.error.NoSuchUserException;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.Map;

import static ee.jyri.scimport.TestConstants.EXISTING_USER;
import static ee.jyri.scimport.TestConstants.NONEXISTING_USER;
import static junit.framework.Assert.assertNotNull;


@RunWith(MockitoJUnitRunner.class)
public class DownloadTrackFromSoundcloudTest
{
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    String tracksJson;

    SoundcloudTrackService service = new SoundcloudTrackService();


    @Before
    public void setUp() throws Exception
    {
        tracksJson = FileUtils.readFileToString(new File("src/test/resource/fixtures/tracks.json"));
        assertNotNull(tracksJson);
    }

    @Test
    public void findUserTracks_validUsername_returnsMapOfTracks() throws Exception
    {

        Map<String,Track> userTracks = service.findUserTracks(EXISTING_USER);
        assertNotNull(userTracks);
    }

    @Test
    public void findUserTracks_invalidUsername_throwsAnException() throws Exception
    {
        expectedEx.expect(NoSuchUserException.class);
        expectedEx.expectMessage("User: " + NONEXISTING_USER + " doesn't exist");

        service.findUserTracks(NONEXISTING_USER);
    }
}
