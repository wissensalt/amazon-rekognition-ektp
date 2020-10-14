/**
 * Copyright 2010-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * This file is licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License. A copy of
 * the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.wissensalt.rnd.amazon.rekognition.ektp;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;

import java.util.List;

/**
 * @author : <a href="mailto:wissensalt@gmail.com">Achmad Fauzi</a>
 * @since : 2020-10-13
 **/
public class Main {
    public static void main(String[] args) {
        String accessKey = "";
        String secretKey = "";

        String image = "";
        String bucket = "";

        AWSStaticCredentialsProvider credentialsProvider = getAWSCredential(accessKey, secretKey);
        try {
            DetectTextResult result = getAmazonRekognitionClient(credentialsProvider)
                    .detectText(buildAmazonRekognitionRequest(credentialsProvider, image, bucket));

            List<TextDetection> textDetections = result.getTextDetections();
            System.out.println("Detected lines and words for " + image);
            for (TextDetection text: textDetections) {
                System.out.println("Detected: " + text.getDetectedText());
                System.out.println("Confidence: " + text.getConfidence().toString());
                System.out.println("Id : " + text.getId());
                System.out.println("Parent Id: " + text.getParentId());
                System.out.println("Type: " + text.getType());
                System.out.println();
            }
        } catch(AmazonRekognitionException e) {
            e.printStackTrace();
        }
    }

    private static AWSStaticCredentialsProvider getAWSCredential(String accessKey, String secretKey) {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                accessKey, secretKey
        ));
    }

    private static AmazonRekognition getAmazonRekognitionClient(AWSStaticCredentialsProvider awsStaticCredentialsProvider) {
        return AmazonRekognitionClientBuilder
                .standard()
                .withCredentials(awsStaticCredentialsProvider)
                .build();
    }

    private static DetectTextRequest buildAmazonRekognitionRequest(AWSStaticCredentialsProvider awsStaticCredentialsProvider,
                                                                   String image, String bucket) {
        DetectTextRequest request = new DetectTextRequest();
        request.setRequestCredentialsProvider(awsStaticCredentialsProvider);
        request.withImage(new Image()
                .withS3Object(new S3Object()
                        .withName(image)
                        .withBucket(bucket)));

        return request;
    }
}
