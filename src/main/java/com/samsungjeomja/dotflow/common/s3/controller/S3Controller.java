package com.samsungjeomja.dotflow.common.s3.controller;

import com.samsungjeomja.dotflow.common.response.code.status.ErrorStatus;
import com.samsungjeomja.dotflow.common.response.exception.GeneralException;
import com.samsungjeomja.dotflow.common.s3.dto.S3RequestDto;
import com.samsungjeomja.dotflow.common.s3.service.S3CommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@Tag(name = "S3")
public class S3Controller {

    private final S3CommandService s3Service;

    @PostMapping(
        value = "api/files",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "", description = "S3에 이미지를 업로드하는 기능입니다. 여러개의 파일을 동시에 업로드 가능합니다.")
    public ResponseEntity<String> uploadFile(
        @Parameter(
            description = "multipart/form-data 형식의 이미지 리스트를 input으로 받습니다. 이때 key 값은 multipartFile 입니다.",
            content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
        )
        @RequestPart(value = "files") MultipartFile file) {
        try {
            return ResponseEntity.ok(s3Service.uploadFile(file));
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._FILE_UPLOAD_ERROR);
        }
    }

    @DeleteMapping("api/file")
    @Operation(summary = "", description = "S3에서 이미지를 삭제하는 기능입니다. 여러개의 파일을 동시에 삭제 가능합니다.")
    public ResponseEntity<String> deleteFileByName(
        @RequestBody S3RequestDto requestDto) {
        try {
            s3Service.deleteFilesByName(requestDto);
            return ResponseEntity.ok("삭제를 완료했습니다.");
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._FILE_UPLOAD_ERROR);
        }
    }
}