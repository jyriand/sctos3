package ee.jyri.scimport.service;

import com.jayway.jsonpath.JsonPath;
import ee.jyri.scimport.domain.Track;
import ee.jyri.scimport.error.NoSuchUserException;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.List;
import java.util.Map;

import static ee.jyri.scimport.TestConstants.*;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class DownloadTrackFromSoundcloudTest
{
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Mock
    private SoundcloudApiAdapter apiAdapter;

    @InjectMocks
    private SoundcloudTrackService service = new SoundcloudTrackService();

    String tracksJson;

    @Before
    public void setUp() throws Exception
    {
        tracksJson = FileUtils.readFileToString(new File("src/test/resource/fixtures/single_track.json"));
        assertNotNull(tracksJson);
    }

    @Test
    public void findUserTracks_validUsername_returnsMapOfTracks() throws Exception
    {
        when(apiAdapter.getUserTracksJSON(EXISTING_USER)).thenReturn( tracksJson );

        Map<String,Track> userTracks = service.findUserTracks(EXISTING_USER);
        assertNotNull(userTracks);
    }

    @Test
    public void validUserWhoHasNoTracksReturnsEmptyMap() throws Exception
    {
        when(apiAdapter.getUserTracksJSON(USER_WITHOUT_TRACKS)).thenReturn(EMPTY_JSON_RESPONSE);

        Map<String,Track> userTracks = service.findUserTracks(USER_WITHOUT_TRACKS);
        assertThat( userTracks.isEmpty(), is(true));
    }

    @Test
    public void findUserTracks_invalidUsername_throwsAnException() throws Exception
    {
        when(apiAdapter.getUserTracksJSON(NONEXISTING_USER)).thenReturn(NOT_FOUND_JSON);
        expectedEx.expect(NoSuchUserException.class);

        service.findUserTracks(NONEXISTING_USER);
    }

    @Test
    public void returnedMapShouldContainTracksFromResponseJson() throws Exception {
        when(apiAdapter.getUserTracksJSON(EXISTING_USER)).thenReturn(tracksJson);
        Map<String, Track> userTracks = service.findUserTracks(EXISTING_USER);

        assertThat(userTracks.size(), is(1));

        Track track = userTracks.get("13158665");
        assertThat(track.getId(), is( "13158665" ));
    }
}
