package ee.jyri.scimport.service;


import ee.jyri.scimport.domain.Track;
import ee.jyri.scimport.helper.SoundcloudJsonDeserializer;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SoundclubJsonDeserializerTest
{
    String tracksJson;
    String multipleTracksJson;

    @Before
    public void setUp() throws Exception
    {
        tracksJson = FileUtils.readFileToString(new File("src/test/resource/fixtures/tracks.json"));
        multipleTracksJson = FileUtils.readFileToString(new File("src/test/resource/fixtures/multiple_tracks.json"));
    }


    @Test
    public void deserializeJsonWithOneTrackObject() throws Exception {
        SoundcloudJsonDeserializer deserializer = new SoundcloudJsonDeserializer();
        List<Track> track = deserializer.getTracks(tracksJson);
        assertThat(track.size(), is(1));
    }

    @Test
    public void deserializeJsonWithMultipleTrackObjects() throws Exception {
        SoundcloudJsonDeserializer deserializer = new SoundcloudJsonDeserializer();
        List<Track> track = deserializer.getTracks(multipleTracksJson);
        assertThat(track.size(), is(2));
    }
}
