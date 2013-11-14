package ee.jyri.scimport.web;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ee.jyri.scimport.domain.SimpleResponse;
import ee.jyri.scimport.domain.Song;
import ee.jyri.scimport.service.SoundcloudService;
import ee.jyri.scimport.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(value = "/searchUser/{username}", method = RequestMethod.GET)
    public String searchUser(@PathVariable String username, Model model) {
        model.addAttribute(MODEL_SONGS, soundcloudService.findUserSongs(username));
        return "home";
    }

    @RequestMapping(value = "/{username}/saveSong/{songId}", method = RequestMethod.GET)
    public @ResponseBody
    SimpleResponse saveSongToStorage(@PathVariable String username, @PathVariable String songId, Model model) {

        List<Song> userSongs = soundcloudService.findUserSongs(username);

        if( userSongs == null || userSongs.isEmpty() ){
            return new SimpleResponse( false );
        }

        uploadService.uploadFile( userSongs.get(Integer.valueOf(songId)) );
        return new SimpleResponse(true);
    }

}
