package jp.co.goalist.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class S3Handler {

	private AmazonS3 s3Client;

	public S3Handler(String region){
		this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region) //リージョンをセット
                .build();

	}

	public Path downloadObject(String keyName, String bucketName, String dest) {

		 System.out.format("Downloading %s from S3 bucket %s...\n", keyName, bucketName);

	     try {
	         S3Object o = this.s3Client.getObject(bucketName, keyName);
	         S3ObjectInputStream s3is = o.getObjectContent();
	         FileOutputStream fos = new FileOutputStream(new File(dest));
	         byte[] read_buf = new byte[1024];
	         int read_len = 0;
	         while ((read_len = s3is.read(read_buf)) > 0) {
	             fos.write(read_buf, 0, read_len);
	         }
	         s3is.close();
	         fos.close();
	     } catch (AmazonServiceException e) {
	         System.err.println(e.getErrorMessage());
	         System.exit(1);
	     } catch (FileNotFoundException e) {
	         System.err.println(e.getMessage());
             System.exit(1);
	     } catch (IOException e) {
	         System.err.println(e.getMessage());
             System.exit(1);
	     }

	     System.out.println("Done!");
	     Path path = Paths.get(dest);
	     return path;
	}


	public void uploadObject(String keyName, String bucketName, String up) {

        System.out.format("Uploading %s to S3 bucket %s...\n", up, bucketName);

        try {
            this.s3Client.putObject(bucketName, keyName, new File(up));
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        System.out.println("Done!");
	}

}
