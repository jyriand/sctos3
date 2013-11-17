package ee.jyri.scimport.service;


import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AwsApiAdapter
{

    @Value("${s3.bucketName}")
    private String bucketName;

    public void upload( String url, String key, ObjectMetadata metaData )
    {

    }

}
