package ee.jyri.scimport.web;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ee.jyri.scimport.service.SoundcloudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    public static final String MODEL_SONGS = "userSongs";
    @Autowired
    private SoundcloudService soundcloudService;

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


}
