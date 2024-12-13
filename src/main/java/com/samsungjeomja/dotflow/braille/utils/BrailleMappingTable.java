package com.samsungjeomja.dotflow.braille.utils;

import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BrailleMappingTable {
    private static final String[] brailleMap = new String[0x5f - 0x20 + 1];

    static {
        brailleMap[0] = "2800"; // 공백
        brailleMap[1] = "282E"; // 2-3-4-6
        brailleMap[2] = "2810"; // 5
        brailleMap[3] = "283C"; // 3-4-5-6
        brailleMap[4] = "282B"; // 1-2-4-6
        brailleMap[5] = "2829"; // 1-4-6
        brailleMap[6] = "282F"; // 1-2-3-4-6
        brailleMap[7] = "2804"; // 3
        brailleMap[8] = "2837"; // 1-2-3-5-6
        brailleMap[9] = "283E"; // 2-3-4-5-6
        brailleMap[10] = "2821"; // 1-6
        brailleMap[11] = "282C"; // 3-4-6
        brailleMap[12] = "2820"; // 6
        brailleMap[13] = "2824"; // 3-6
        brailleMap[14] = "2828"; // 4-6
        brailleMap[15] = "280C"; // 3-4
        brailleMap[16] = "2834"; // 3-5-6
        brailleMap[17] = "2802"; // 2
        brailleMap[18] = "2806"; // 2-3
        brailleMap[19] = "2812"; // 2-5
        brailleMap[20] = "2832"; // 2-5-6
        brailleMap[21] = "2822"; // 2-6
        brailleMap[22] = "2816"; // 2-3-5
        brailleMap[23] = "2836"; // 2-3-5-6
        brailleMap[24] = "2826"; // 2-3-6
        brailleMap[25] = "2814"; // 3-5
        brailleMap[26] = "2831"; // 1-5-6
        brailleMap[27] = "2830"; // 5-6
        brailleMap[28] = "2823"; // 1-2-6
        brailleMap[29] = "283F"; // 1-2-3-4-5-6
        brailleMap[30] = "281C"; // 3-4-5
        brailleMap[31] = "2839"; // 1-4-5-6
        brailleMap[32] = "2808"; // 4
        brailleMap[33] = "2801"; // 1
        brailleMap[34] = "2803"; // 1-2
        brailleMap[35] = "2809"; // 1-4
        brailleMap[36] = "2819"; // 1-4-5
        brailleMap[37] = "2811"; // 1-5
        brailleMap[38] = "280B"; // 1-2-4
        brailleMap[39] = "281B"; // 1-2-4-5
        brailleMap[40] = "2813"; // 1-2-5
        brailleMap[41] = "280A"; // 2-4
        brailleMap[42] = "281A"; // 2-4-5
        brailleMap[43] = "2805"; // 1-3
        brailleMap[44] = "2807"; // 1-2-3
        brailleMap[45] = "280D"; // 1-3-4
        brailleMap[46] = "281D"; // 1-3-4-5
        brailleMap[47] = "2815"; // 1-3-5
        brailleMap[48] = "280F"; // 1-2-3-4
        brailleMap[49] = "281F"; // 1-2-3-4-5
        brailleMap[50] = "2817"; // 1-2-3-5
        brailleMap[51] = "280E"; // 2-3-4
        brailleMap[52] = "281E"; // 2-3-4-5
        brailleMap[53] = "2825"; // 1-3-6
        brailleMap[54] = "2827"; // 1-2-3-6
        brailleMap[55] = "283A"; // 2-4-5-6
        brailleMap[56] = "282D"; // 1-3-4-6
        brailleMap[57] = "283D"; // 1-3-4-5-6
        brailleMap[58] = "2835"; // 1-3-5-6
        brailleMap[59] = "282A"; // 2-4-6
        brailleMap[60] = "2833"; // 1-2-5-6
        brailleMap[61] = "283B"; // 1-2-4-5-6
        brailleMap[62] = "2818"; // 4-5
        brailleMap[63] = "2838"; // _
    }

    // 점자 유니코드를 가져오는 메서드
    public static String getBrailleUnicode(int asciiCode) {
        if (asciiCode >= 0x20 && asciiCode <= 0x5F) {
            return brailleMap[asciiCode - 0x20];
        }
        if (asciiCode == 0x0c) {
            return "000C";
        }
        if (asciiCode == 0x60){
            return "2804";
        }
        else {
            log.warn("no mapping :0x{}", String.format("%02X", asciiCode));
            return ""; // 유효하지 않은 아스키 코드
        }
    }

    public static Integer getAscii(String unicode) {
        if(unicode.toUpperCase().equals("0027")){
            return -2;
        }
        if (unicode.toUpperCase().equals("000C")) {
            return 0x0C;
        }
        if(unicode.toUpperCase().equals("2027")){
            unicode = "2824";
        }
        String finalUnicode = unicode;
        return IntStream.range(0, brailleMap.length)
                .filter(i -> brailleMap[i].equals(finalUnicode))
                .map(i -> i + 0x20)
                .findFirst()
                .orElse(-1);
    }
}
