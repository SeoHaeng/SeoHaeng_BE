package com.seohaeng.backend.global.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.seohaeng.backend.global.configuration.AmazonConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {

    private final AmazonS3 amazonS3;

    private final AmazonConfig amazonConfig;

    public String uploadFile(String keyName, MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try {
            amazonS3.putObject(
                    new PutObjectRequest(amazonConfig.getBucket(), keyName, file.getInputStream(), objectMetadata));
        } catch (IOException e) {
            log.error("error at AmazonS3Manager uploadFile : {}", (Object) e.getStackTrace());
        }
        return amazonS3.getUrl(amazonConfig.getBucket(), keyName).toString();
    }

    public String generateBookChallengeProofKeyName(String uuid) {
        return amazonConfig.getBookChallengePath() + '/' + uuid;
    }
}
