package com.samsungjeomja.dotflow.braille.repository.service;

import com.samsungjeomja.dotflow.braille.dto.request.from_server.BrlTranslationRequest;
import com.samsungjeomja.dotflow.braille.dto.request.from_server.TextTranslationRequest;
import com.samsungjeomja.dotflow.braille.dto.response.from_server.BrfFileResponse;
import com.samsungjeomja.dotflow.braille.dto.response.to_server.BrlResponse;
import com.samsungjeomja.dotflow.braille.dto.response.to_server.TextResponse;
import com.samsungjeomja.dotflow.braille.repository.BrailleImageRepository;
import com.samsungjeomja.dotflow.braille.dto.request.to_server.UnicodeRequest;
import com.samsungjeomja.dotflow.braille.dto.response.from_server.TextFileResponse;
import com.samsungjeomja.dotflow.braille.utils.FileConverter;
import com.samsungjeomja.dotflow.common.s3.service.S3QueryService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class TranslationService {
    private final ApiService apiService;

    private final GptService gptService;

    private final ClovaService clovaService;

    private final OcrService ocrService;

    public TextFileResponse translateFromUnicodeToText(UnicodeRequest request) throws IOException {
        String[] brlArray = FileConverter.convertUnicodeToBrl(request.unicodeArray());

        for (String s : brlArray) {
            System.out.println(s);
        }

        TextResponse response = apiService.sendBrlTranslateRequest(BrlTranslationRequest.builder()
                .brl(Arrays.stream(brlArray).toList())
                .build());
        String result = gptService.makePrettyString(response.text());

        String summary = gptService.makeSummary(result);
        byte[] textFile = FileConverter.convertStringToFile(result, "resultFile");

        Path resourceDir = Paths.get("src", "main", "resources");
        if (!Files.exists(resourceDir)) {
            Files.createDirectories(resourceDir); // 경로가 없다면 생성
        }
        Path filePath = resourceDir.resolve("resultFile.txt");

        // 파일 저장
        Files.write(filePath, textFile);

        return TextFileResponse.builder()
                .summary(summary)
                .result(result)
                .textFile(textFile)
                .build();

    }


    public TextFileResponse translateFromBrfToText(MultipartFile brfFile) throws IOException {
        String[] str = FileConverter.toUnicodeFromBrf(brfFile);
        for (String s : str) {
            System.out.print(s);
        }

        UnicodeRequest request =
                UnicodeRequest.builder().unicodeArray(List.of(str)).build();

        return translateFromUnicodeToText(request);
    }

    public TextFileResponse translateFromImageToText(MultipartFile image) throws IOException {

        List<String> brlList = ocrService.sendBrlImageTranslateRequest(image);

        List<String> unicodeList = new ArrayList<>();

        for (String uni : brlList) {
            StringBuilder unicodes = new StringBuilder();
            for (char c : uni.toCharArray()) { // 각 문자를 순회
                unicodes.append(String.format("%04X", (int) c)).append(" "); // 유니코드 값을 추가
            }
//            log.info("is there any problem? :{} ", unicodes);
            unicodeList.add(unicodes.toString().trim()); // 완성된 문자열을 리스트에 추가
        }

        UnicodeRequest request =
                UnicodeRequest.builder().unicodeArray(unicodeList).build();

        return translateFromUnicodeToText(request);
    }

    public BrfFileResponse translateFromTextToBrf(String request) throws IOException {
        BrlResponse brlResponse = apiService.sendTextTranslateRequest(TextTranslationRequest.builder()
                .text(FileConverter.splitByNewLine(request))
                .build());
        List<String> splitUnicode = FileConverter.splitByNewLine(brlResponse.text());
        String[] unicodeArray = FileConverter.extractUnicodeArray(splitUnicode);

        System.out.println(Arrays.toString(unicodeArray));
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
        String string = gptService.makePrettyString(result);

        log.info(string);
        return translateFromTextToBrf(result);

    }

    private byte[] makeTestBrfFile() throws IOException {
        byte[] file = FileConverter.convertResourceToByteArray("test.brf");
        MultipartFile testFile = FileConverter.convertByteArrayToMultipartFile(file);
        String[] unicodeArray = FileConverter.toUnicodeFromBrf(testFile);

        return FileConverter.toBrfFromUnicode(unicodeArray);
    }
}
