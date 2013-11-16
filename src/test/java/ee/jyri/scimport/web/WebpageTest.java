package ee.jyri.scimport.web;

import ee.jyri.scimport.domain.Track;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isNull;
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
    public void searchForExistngUsersSongs_returnModelWithSongs() throws Exception {
        Map<String, Track> songs = createSongs();
        when(trackService.findUserTracks(anyString()))
                .thenReturn(songs);

        mockMvc.perform(get(REQ_SEARCH_TRACKS).param(USERNAME, EXISTING_USER))
                .andExpect(model().attributeExists(MODEL_TRACKS))
                .andExpect(model().attribute(MODEL_TRACKS, songs.values()))
                .andExpect(model().attribute(USERNAME, EXISTING_USER));

        verify(trackService, times(1)).findUserTracks(EXISTING_USER);
    }

    @Test
    public void searchForNonExistingUsersSongs_returnsNothing() throws Exception {
        HashMap<String, Track> emptyMap = new HashMap<String, Track>();
        when(trackService.findUserTracks(anyString())).thenReturn( emptyMap );

        mockMvc.perform(get(REQ_SEARCH_TRACKS).param(USERNAME, NONEXISTING_USER))
                .andExpect(model().attribute(MODEL_TRACKS, emptyMap.values()));

        verify(trackService, times(1)).findUserTracks(NONEXISTING_USER);
    }

    @Test
    public void canSaveSongToStorage_returnsSuccess() throws Exception {
        when(trackService.findUserTracks(anyString())).thenReturn(createSongs());

        mockMvc.perform(get(REQ_SAVE_TRACK).param(USERNAME, EXISTING_USER).param(TRACK_ID, "1"))
            .andExpect(content().contentType(JSON_CHARSET))
            .andExpect(jsonPath("$.success").value(true));

        verify(trackService, times(1)).findUserTracks(EXISTING_USER);
        verify(uploadService).uploadTrack(any(Track.class));

    }

    @Test
    public void saveSongToStorage_withNonExistingSong_returnsFailure() throws Exception {
        when(trackService.findUserTracks(anyString())).thenReturn( new HashMap<String, Track>());

        mockMvc.perform(get(REQ_SAVE_TRACK).param(USERNAME, EXISTING_USER).param(TRACK_ID, "1"))
                .andExpect((content().contentType(JSON_CHARSET)))
                .andExpect(jsonPath("$.success").value(false));

        verify(trackService, times(1)).findUserTracks(EXISTING_USER);
        verifyZeroInteractions(uploadService);
    }

    private Map< String, Track> createSongs(){
        HashMap<String, Track> songs = new HashMap<String, Track>();
        songs.put( "1", new Track("1"));
        songs.put("2", new Track("2"));
        return songs;
    }


}
