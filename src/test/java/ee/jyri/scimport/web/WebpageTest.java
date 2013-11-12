package ee.jyri.scimport.web;

import ee.jyri.scimport.service.SoundcloudService;
import ee.jyri.scimport.service.UploadService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static ee.jyri.scimport.web.HomeController.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class WebpageTest {

    private MockMvc mockMvc;

    public static final String VALID_USERNAME = "ValidUser";

    public static final String INVALID_USERNAME = "InValidUser";

    @Mock
    private SoundcloudService soundcloudService;

    @Mock
    private UploadService uploadService;

    @InjectMocks
    private HomeController homeController;

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
    public void searchForExistngUsersSongs_shouldReturnModelWithSongs() throws Exception {
        List<String> songs = new ArrayList<String>();
        songs.add( "First song");
        songs.add( "Second song");

        when(soundcloudService.findUserSongs(anyString()))
                .thenReturn(songs);

        mockMvc.perform(get("/searchUser/" + VALID_USERNAME))
                .andExpect(model().attributeExists(MODEL_SONGS))
                .andExpect(model().attribute(MODEL_SONGS, songs));

        verify(soundcloudService, times(1)).findUserSongs(VALID_USERNAME);
    }

    @Test
    public void searchForNonExistingUsersSongs_shouldReturnNothing() throws Exception {
        ArrayList<String> emptyList = new ArrayList<String>();
        when(soundcloudService.findUserSongs(anyString())).thenReturn(emptyList);

        mockMvc.perform(get("/searchUser/" + INVALID_USERNAME))
                .andExpect(model().attribute(MODEL_SONGS, emptyList));

        verify(soundcloudService, times(1)).findUserSongs(INVALID_USERNAME);
    }

    @Test
    public void canSaveSongToStorage() throws Exception {
        List<String> songs = new ArrayList<String>();
        songs.add( "First song");
        songs.add( "Second song");

        when(soundcloudService.findUserSongs(anyString())).thenReturn(songs);

        mockMvc.perform(get("/"+ VALID_USERNAME + "/saveSong/1"));

        verify(soundcloudService, times(1)).findUserSongs(VALID_USERNAME);
        verify(uploadService).uploadFile(anyString());

    }

}
