package ee.jyri.scimport.web;

import java.util.*;

import ee.jyri.scimport.domain.SimpleResponse;
import ee.jyri.scimport.domain.Track;
import ee.jyri.scimport.service.TrackService;
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

    public static final String MODEL_TRACKS = "userTracks";
    public static final String REQ_SEARCH_TRACKS = "/searchUserTracks";
    public static final String REQ_SAVE_TRACK = "/saveTrack";
    public static final String VIEW_HOME = "home";

    @Autowired
    private TrackService trackService;

    @Autowired
    private UploadService<Track> uploadService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home( Model model) {
        logger.info("Welcome home! ");
        return VIEW_HOME;
    }

    @RequestMapping(value = REQ_SEARCH_TRACKS, method = RequestMethod.GET)
    public String searchUser(@RequestParam String username, Model model) {
        model.addAttribute(MODEL_TRACKS, trackService.findUserTracks(username).values());
        model.addAttribute("username", username );
        return VIEW_HOME;
    }

    @RequestMapping(value = REQ_SAVE_TRACK, method = RequestMethod.GET)
    public @ResponseBody
    SimpleResponse saveSongToStorage(@RequestParam String username, @RequestParam String trackId, Model model) {

        Map<String, Track> userTracks = trackService.findUserTracks(username);

        if( userTracks == null || userTracks.isEmpty() ){
            return new SimpleResponse( false );
        }

        uploadService.uploadFile(userTracks.get(trackId));
        return new SimpleResponse(true);
    }

}
