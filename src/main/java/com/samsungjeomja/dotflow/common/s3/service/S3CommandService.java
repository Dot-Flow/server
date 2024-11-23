package com.samsungjeomja.dotflow.common.s3.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.MultiObjectDeleteException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.samsungjeomja.dotflow.common.response.code.status.ErrorStatus;
import com.samsungjeomja.dotflow.common.response.exception.GeneralException;
import com.samsungjeomja.dotflow.common.s3.dto.S3RequestDto;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@PropertySource("classpath:application.yml")
@Service
public class S3CommandService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private String getFileExtension(String fileName) {
        // TODO: 파일 업로드 확장자 제한 추가 예정
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new GeneralException(ErrorStatus._FILE_EXTENSTION_ERROR);
        }
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    public String uploadFile(MultipartFile multipartFile) {
        String fileName = createFileName(multipartFile.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new GeneralException(ErrorStatus._FILE_UPLOAD_ERROR);
        }
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    public void deleteFilesByName(S3RequestDto requestDto) {
        List<KeyVersion> keys = requestDto.getFileNames().stream()
            .map(KeyVersion::new)
            .toList();

        DeleteObjectsRequest request = new DeleteObjectsRequest(bucket).withKeys(keys);

        try {
            amazonS3Client.deleteObjects(request);
        } catch (MultiObjectDeleteException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제에 실패했습니다.");
        }
    }


}