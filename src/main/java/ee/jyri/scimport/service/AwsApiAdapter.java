package ee.jyri.scimport.service;


import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class AwsApiAdapter
{

    @Value("${s3.bucketName}")
    private String bucketName;

    @Autowired
    private TransferManager transferManager;

    public void upload( String url, String key, ObjectMetadata metaData ) throws IOException {
        transferManager.upload( bucketName, key, new URL(url).openStream(), metaData);
    }
}
