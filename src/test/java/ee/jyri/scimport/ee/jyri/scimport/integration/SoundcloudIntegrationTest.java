package ee.jyri.scimport.ee.jyri.scimport.integration;

import ee.jyri.scimport.domain.Track;
import ee.jyri.scimport.service.SoundcloudTrackService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml")
public class SoundcloudIntegrationTest
{
    @Autowired
    private SoundcloudTrackService trackService;

    @Test
    @Ignore
    public void testName() throws Exception {
        Map<String,Track> tracks = trackService.findUserTracks("apitest");
        assertThat(tracks.size(), greaterThan(1));
    }
}
