package ee.jyri.scimport.web;

import ee.jyri.scimport.domain.Song;
import ee.jyri.scimport.service.SoundcloudService;
import ee.jyri.scimport.service.UploadService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

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

    public static final String EXISTING_USER = "ValidUser";

    public static final String NONEXISTING_USER = "InValidUser";

    public static final String JSON_CHARSET = "application/json;charset=UTF-8";


    @Mock
    private SoundcloudService< Song > soundcloudService;

    @Mock
    private UploadService< Song > uploadService;

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
                .andExpect(view().name("home"));
    }

    @Test
    public void searchForExistngUsersSongs_returnModelWithSongs() throws Exception {
        List<Song> songs = new ArrayList<Song>();
        songs.add( new Song("1"));
        songs.add( new Song("2"));

        when(soundcloudService.findUserSongs(anyString()))
                .thenReturn(songs);

        mockMvc.perform(get("/searchUser/" + EXISTING_USER))
                .andExpect(model().attributeExists(MODEL_SONGS))
                .andExpect(model().attribute(MODEL_SONGS, songs));

        verify(soundcloudService, times(1)).findUserSongs(EXISTING_USER);
    }

    @Test
    public void searchForNonExistingUsersSongs_returnsNothing() throws Exception {
        ArrayList<Song> emptyList = new ArrayList<Song>();
        when(soundcloudService.findUserSongs(anyString())).thenReturn(emptyList);

        mockMvc.perform(get("/searchUser/" + NONEXISTING_USER))
                .andExpect(model().attribute(MODEL_SONGS, emptyList));

        verify(soundcloudService, times(1)).findUserSongs(NONEXISTING_USER);
    }

    @Test
    public void canSaveSongToStorage_returnsSuccess() throws Exception {
        List<Song> songs = new ArrayList<Song>();
        songs.add( new Song("1"));
        songs.add( new Song("2"));

        when(soundcloudService.findUserSongs(anyString())).thenReturn(songs);

        mockMvc.perform(get("/" + EXISTING_USER + "/saveSong/1"))
            .andExpect(content().contentType(JSON_CHARSET))
            .andExpect(jsonPath("$.success").value(true));

        verify(soundcloudService, times(1)).findUserSongs(EXISTING_USER);
        verify(uploadService).uploadFile(any( Song.class ));

    }

    @Test
    public void saveSongToStorage_withNonExistingSong_returnsFailure() throws Exception {
        when(soundcloudService.findUserSongs(anyString())).thenReturn( new ArrayList<Song>());

        mockMvc.perform(get("/" + EXISTING_USER + "/saveSong/1"))
                .andExpect((content().contentType(JSON_CHARSET)))
                .andExpect(jsonPath("$.success").value(false));

        verify(soundcloudService, times(1)).findUserSongs(EXISTING_USER);
        verifyZeroInteractions(uploadService);
    }
}
