package ee.jyri.scimport.web;

import ee.jyri.scimport.domain.Track;
import ee.jyri.scimport.error.NoSuchUserException;
import ee.jyri.scimport.service.TrackService;
import ee.jyri.scimport.service.UploadService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static ee.jyri.scimport.TestConstants.*;
import static ee.jyri.scimport.web.HomeController.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class WebpageTest {

    private MockMvc mockMvc;

    @Mock
    private TrackService trackService;

    @Mock
    private UploadService uploadService;

    @InjectMocks
    private HomeController homeController = new HomeController();

    @Before
    public void setUp(){
        this.mockMvc = standaloneSetup(homeController)
                .alwaysExpect(status().isOk())
                .build();
    }

    @Test
    public void rootPathReturnsStatusOK() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(view().name(VIEW_HOME));
    }

    @Test
    public void searchForExistngUsersSongsReturnModelWithTracks() throws Exception {
        Map<String, Track> songs = createTracks();
        when(trackService.findUserTracks(anyString()))
                .thenReturn(songs);

        mockMvc.perform(get(REQ_SEARCH_TRACKS).param(USERNAME, EXISTING_USER))
                .andExpect(model().attributeExists(MODEL_TRACKS))
                .andExpect(model().attribute(MODEL_TRACKS, songs.values()))
                .andExpect(model().attribute(USERNAME, EXISTING_USER));

        verify(trackService, times(1)).findUserTracks(EXISTING_USER);
    }

    @Test
    public void searchForNonExistingUsersTracksReturnsErrorMessageInModel() throws Exception {
        when(trackService.findUserTracks(anyString())).thenThrow( NoSuchUserException.class );

        mockMvc.perform(get(REQ_SEARCH_TRACKS).param(USERNAME, NONEXISTING_USER))
                .andExpect(model().attribute(MODEL_USER_ERROR, "User " +NONEXISTING_USER+" doesn't exist."));

        verify(trackService, times(1)).findUserTracks(NONEXISTING_USER);
    }

    @Test
    public void savingTrackToStorageReturnsJsonWithSuccess() throws Exception {
        when(trackService.findUserTracks(anyString())).thenReturn(createTracks());

        mockMvc.perform(get(REQ_SAVE_TRACK).param(USERNAME, EXISTING_USER).param(TRACK_ID, "1"))
            .andExpect(content().contentType(JSON_CHARSET))
            .andExpect(jsonPath("$.success").value(true));

        verify(trackService, times(1)).findUserTracks(EXISTING_USER);
        verify(uploadService).uploadTrack(any(Track.class));

    }

    @Test
    public void savingTrackToStorageWithNonExistingTrackReturnsFailure() throws Exception {
        when(trackService.findUserTracks(anyString())).thenThrow( Exception.class );

        mockMvc.perform(get(REQ_SAVE_TRACK).param(USERNAME, EXISTING_USER).param(TRACK_ID, "1"))
                .andExpect((content().contentType(JSON_CHARSET)))
                .andExpect(jsonPath("$.success").value(false));

        verify(trackService, times(1)).findUserTracks(EXISTING_USER);
        verifyZeroInteractions(uploadService);
    }

    private Map< String, Track> createTracks(){
        HashMap<String, Track> songs = new HashMap<String, Track>();
        Track track1 = new Track();
        track1.setId( "1" );
        Track track2 = new Track();
        track1.setId( "2" );
        songs.put( track1.getId(), track1);
        songs.put( track2.getId(), track2);
        return songs;
    }


}
