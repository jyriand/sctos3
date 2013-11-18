package ee.jyri.scimport.domain;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Track {

    public static final String CLIENT_ID = "?client_id=";
    private String id;

    private boolean downloadable;

    private String title;

    @JsonProperty("original_content_size")
    private long contentLength;

    @JsonProperty("download_url")
    private String downloadUrl;

    @JsonProperty("original_format")
    private String format;

    public boolean isMp3() {
        return format.equals("mp3");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDownloadable() {
        return downloadable;
    }

    public void setDownloadable(boolean downloadable) {
        this.downloadable = downloadable;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getKey() {
        return title + "-" + System.currentTimeMillis() + "." + format;
    }

    public String getDownloadUrlWithClientId(String clientId) {
        if (downloadUrl.contains("https")) {
            downloadUrl = downloadUrl.replace("https", "http");
        }

        return downloadUrl + CLIENT_ID + clientId;
    }

}
