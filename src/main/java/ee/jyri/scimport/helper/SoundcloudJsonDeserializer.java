package ee.jyri.scimport.helper;


import ee.jyri.scimport.domain.Track;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.List;

public class SoundcloudJsonDeserializer {

    public List<Track> getTracks(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return mapper.readValue(json, new TypeReference<List<Track>>() {
        });
    }
}
