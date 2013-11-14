package ee.jyri.scimport.web;

import java.text.DateFormat;
import java.util.*;

import ee.jyri.scimport.domain.SimpleResponse;
import ee.jyri.scimport.domain.Song;
import ee.jyri.scimport.service.SoundcloudService;
import ee.jyri.scimport.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    public static final String MODEL_SONGS = "userSongs";

    @Autowired
    private SoundcloudService< Song > soundcloudService;

    @Autowired
    private UploadService< Song > uploadService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home( Model model) {
        logger.info("Welcome home! ");
        return "home";
    }

    @RequestMapping(value = "/searchUserSongs", method = RequestMethod.GET)
    public String searchUser(@RequestParam String username, Model model) {
        model.addAttribute(MODEL_SONGS, soundcloudService.findUserSongs(username).values());
        model.addAttribute("username", username );
        return "home";
    }

    @RequestMapping(value = "/saveSong", method = RequestMethod.GET)
    public @ResponseBody
    SimpleResponse saveSongToStorage(@RequestParam String username, @RequestParam String songId, Model model) {

        Map<String, Song> userSongs = soundcloudService.findUserSongs(username);

        if( userSongs == null || userSongs.isEmpty() ){
            return new SimpleResponse( false );
        }

        uploadService.uploadFile( userSongs.get(songId) );
        return new SimpleResponse(true);
    }

}
