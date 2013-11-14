package ee.jyri.scimport.service;


import java.io.File;

public interface UploadService< T > {

    void uploadFile( T fileName );
}
