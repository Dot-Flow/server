package com.samsungjeomja.dotflow.braille.service;

import com.samsungjeomja.dotflow.braille.dto.response.BrfFileResponse;
import com.samsungjeomja.dotflow.braille.repository.BrailleImageRepository;
import com.samsungjeomja.dotflow.braille.dto.request.UnicodeRequest;
import com.samsungjeomja.dotflow.braille.dto.response.TextFileResponse;
import com.samsungjeomja.dotflow.braille.utils.FileConverter;
import com.samsungjeomja.dotflow.common.s3.service.S3QueryService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class TranslationService {
    private final BrailleImageRepository brailleImageRepository;

    private final GptService gptService;

    private final S3QueryService s3QueryService;
    private final ClovaService clovaService;
    private static String url = "";

    private static final long MAX_FILE_SIZE = 1024 * 1024; // 1MB

    public TextFileResponse translateFromUnicodeToText(UnicodeRequest request) throws IOException {
        // request -> string
        String result = "hello world";
        String summary = gptService.makeSummary(result);
        byte[] textFile = FileConverter.convertStringToFile(result, "resultFile");

        return TextFileResponse.builder()
                .summary(summary)
                .result(result)
                .textFile(textFile)
                .build();

    }


    public TextFileResponse translateFromBrfToText(MultipartFile brfFile) throws IOException {
        String[] str = FileConverter.toUnicodeFromBrf(brfFile);

        UnicodeRequest request =
                UnicodeRequest.builder().unicodeArray(List.of(str)).build();

//        String response = sendRequest(request).getBody();

        // request -> string
        String result = "hello world";
        String summary = gptService.makeSummary(result);
        byte[] textFile = FileConverter.convertStringToFile(result, "resultFile");

        return TextFileResponse.builder()
                .summary(summary)
                .result(result)
                .textFile(textFile)
                .build();
    }

    public TextFileResponse translateFromImageToText(String imageUrl) throws IOException {

//        String response = sendRequest(imageUrl).getBody();

        String result = "hello world";
        String summary = gptService.makeSummary(result);
        byte[] textFile = FileConverter.convertStringToFile(result, "resultFile");

        return TextFileResponse.builder()
                .summary(summary)
                .result(result)
                .textFile(textFile)
                .build();
    }

    public BrfFileResponse translateFromTextToBrf(String request) throws IOException {

        // 더미데이터
        byte[] file = makeTestBrfFile();
        MultipartFile testFile = FileConverter.convertByteArrayToMultipartFile(file);
        String[] unicodeArray = FileConverter.toUnicodeFromBrf(testFile);

        byte[] brfFile = FileConverter.toBrfFromUnicode(unicodeArray);
        String summary = gptService.makeSummary(request);

        return BrfFileResponse.builder()
                .brfFile(brfFile)
                .unicodeArray(unicodeArray)
                .summary(summary)
                .build();
    }

    public BrfFileResponse translateFromImageToBrf(MultipartFile image) throws IOException {
        String result = clovaService.extractTextFromImage(image);
        String summary = gptService.makeSummary(result);
        String[] unicode = FileConverter.toUnicodeFromBrf(
                FileConverter.convertByteArrayToMultipartFile(makeTestBrfFile()));
        byte[] brfFile = FileConverter.toBrfFromUnicode(unicode);

        log.info(result);

        return BrfFileResponse.builder()
                .summary(summary)
                .brfFile(brfFile)
                .unicodeArray(unicode)
                .build();

    }

    private String summarizeFileContent(MultipartFile brfFile) throws IOException {
        String content = new String(brfFile.getBytes(), StandardCharsets.UTF_8);

        return gptService.getGptResponse(content);

    }

    public <T> ResponseEntity<String> sendRequest(T request) {
        // RestTemplate 인스턴스 생성
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정 (Content-Type: application/json)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // HttpEntity에 본문(request)과 헤더 설정
        HttpEntity<T> entity = new HttpEntity<>(request, headers);

        // 서버로 POST 요청 보내기
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    private byte[] makeTestBrfFile() throws IOException {
        byte[] file = FileConverter.convertResourceToByteArray("test.brf");
        MultipartFile testFile = FileConverter.convertByteArrayToMultipartFile(file);
        String[] unicodeArray = FileConverter.toUnicodeFromBrf(testFile);

        return FileConverter.toBrfFromUnicode(unicodeArray);
    }
}
