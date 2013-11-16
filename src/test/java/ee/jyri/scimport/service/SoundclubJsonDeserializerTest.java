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
    private SoundcloudJsonDeserializer deserializer;

    @Before
    public void setUp() throws Exception
    {
        tracksJson = FileUtils.readFileToString(new File("src/test/resource/fixtures/tracks.json"));
        multipleTracksJson = FileUtils.readFileToString(new File("src/test/resource/fixtures/multiple_tracks.json"));
        deserializer = new SoundcloudJsonDeserializer();
    }


    @Test
    public void deserializeJsonWithOneTrackObject() throws Exception {
        List<Track> track = deserializer.getTracks(tracksJson);
        assertThat(track.size(), is(1));
    }

    @Test
    public void deserializeJsonWithMultipleTrackObjects() throws Exception {
        List<Track> track = deserializer.getTracks(multipleTracksJson);
        assertThat(track.size(), is(2));
    }

    @Test
    public void deserializesTrackFields() throws Exception {
        Track track = deserializer.getTracks(multipleTracksJson).get(0);

        assertThat( track.getId(), is("13158665"));
        assertThat(track.isDownloadable(), is(true));
        assertThat(track.getDownloadUrl(), is("http://api.soundcloud.com/tracks/13158665/download"));
        assertThat( track.getContentLength(), is(10211857L));
        assertThat(track.getTitle(), is("Munching at Tiannas house"));
        assertThat(track.getFormat(), is("m4a"));
    }

    @Test
    public void isMp3Track() throws Exception {
        Track track1 = deserializer.getTracks(multipleTracksJson).get(0);
        Track track2 = deserializer.getTracks(multipleTracksJson).get(1);

        assertThat(track1.isMp3(), is(false));
        assertThat(track2.isMp3(), is(true));
    }
}
