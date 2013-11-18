package ee.jyriand.sctos3.web;

import ee.jyriand.sctos3.domain.Track;
import ee.jyriand.sctos3.service.TrackService;
import ee.jyriand.sctos3.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    public static final String MODEL_TRACKS = "userTracks";
    public static final String MODEL_USER_ERROR = "userError";
    public static final String REQ_SEARCH_TRACKS = "/searchUserTracks";
    public static final String REQ_SAVE_TRACK = "/saveTrack";
    public static final String VIEW_HOME = "home";

    @Autowired
    private TrackService trackService;

    @Autowired
    private UploadService uploadService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        logger.info("Landed home");
        return VIEW_HOME;
    }

    @RequestMapping(value = REQ_SEARCH_TRACKS, method = RequestMethod.GET)
    public String searchUserTracks(@RequestParam String username, Model model) {
        logger.info("Landed searchUserTracks: username=" + username);
        try {
            model.addAttribute(MODEL_TRACKS, trackService.findUserTracks(username).values());
            model.addAttribute("username", username);
        } catch (Exception e) {
            model.addAttribute(MODEL_USER_ERROR, "User " + username + " doesn't exist.");
            logger.error("Error: " + e.getMessage());
        }
        return VIEW_HOME;
    }

    @RequestMapping(value = REQ_SAVE_TRACK, method = RequestMethod.GET)
    public
    @ResponseBody
    SimpleResponse saveTrack(@RequestParam String username, @RequestParam String trackId, Model model) {
        logger.info("Landed savingTrack: username {}, trackId: {}", username, trackId);
        try {
            Map<String, Track> userTracks = trackService.findUserTracks(username);

            if (userTracks == null || userTracks.isEmpty()) {
                return new SimpleResponse(false);
            }

            uploadService.uploadTrack(userTracks.get(trackId));
            return new SimpleResponse(true);
        } catch (Exception e) {
            logger.error("Saving track failed: " + e.getMessage());
            return new SimpleResponse(false);
        }
    }

}
