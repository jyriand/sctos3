package ee.jyri.scimport.service;


import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
public class AwsApiAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AwsApiAdapter.class);

    @Value("${s3.bucketName}")
    private String bucketName;

    @Autowired
    private TransferManager transferManager;

    public void upload(String url, String key, ObjectMetadata metaData) throws IOException, InterruptedException {
        logger.info("Uploading file {} to {} bucket", key, bucketName);
        InputStream inputStream = new URL(url).openStream();
        Upload upload = transferManager.upload(bucketName, key, inputStream, metaData);
    }
}
