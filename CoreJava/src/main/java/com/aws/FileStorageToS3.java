package com.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;

public class FileStorageToS3 {
    public static void main(String args[]) throws Exception{
        Regions clientRegion = Regions.DEFAULT_REGION;
        String bucketName = "svs-tillster";
        String keyName = "*** Key name ***";
        String filePath = "*** Path to file to upload ***";


        AWSCredentials credentials = new BasicAWSCredentials(
        "",
""
        );

        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .withForceGlobalBucketAccessEnabled(true)
                .build();



        List<Bucket> buckets = s3client.listBuckets();
        for(Bucket bucket : buckets) {
            System.out.println(bucket.getName());
        }

        if(s3client.doesBucketExistV2(bucketName)) {
            s3client.putObject(
                    bucketName,
                    "Reconfile.txt",
                    new File("/Users/deeptiarora/Tillster/frd188/S3/input/Reconfile.txt")
            );
        }

        //listing objects

        ObjectListing objectListing = s3client.listObjects(bucketName);
        for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
              System.out.println(os.getKey());
        }

        //Downloading objects
        S3Object s3object = s3client.getObject(bucketName, "Reconfile.txt");
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        FileUtils.copyInputStreamToFile(inputStream, new File("/Users/deeptiarora/Tillster/frd188/S3/output/Reconfile.txt"));
    }
}
