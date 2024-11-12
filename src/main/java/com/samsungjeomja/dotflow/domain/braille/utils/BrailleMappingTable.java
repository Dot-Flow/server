package com.samsungjeomja.dotflow.domain.braille.utils;

import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BrailleMappingTable {
    private static final String[] brailleMap = new String[0x5f - 0x20 + 1];

    static {
        brailleMap[0] = "2800"; // 공백
        brailleMap[1] = "2816"; // !
        brailleMap[2] = "2810"; // "
        brailleMap[3] = "283F"; // #
        brailleMap[4] = "282E"; // $
        brailleMap[5] = "2829"; // %
        brailleMap[6] = "282F"; // &
        brailleMap[7] = "2802"; // '
        brailleMap[8] = "2837"; // (
        brailleMap[9] = "283E"; // )
        brailleMap[10] = "2821"; // *
        brailleMap[11] = "282B"; // +
        brailleMap[12] = "2820"; // ,
        brailleMap[13] = "2824"; // -
        brailleMap[14] = "2828"; // .
        brailleMap[15] = "2814"; // /
        brailleMap[16] = "2834"; // 0
        brailleMap[17] = "2802"; // 1
        brailleMap[18] = "2806"; // 2
        brailleMap[19] = "2812"; // 3
        brailleMap[20] = "2832"; // 4
        brailleMap[21] = "2822"; // 5
        brailleMap[22] = "2816"; // 6
        brailleMap[23] = "2836"; // 7
        brailleMap[24] = "2826"; // 8
        brailleMap[25] = "281A"; // 9
        brailleMap[26] = "2831"; // :
        brailleMap[27] = "2830"; // ;
        brailleMap[28] = "2823"; // <
        brailleMap[29] = "283F"; // =
        brailleMap[30] = "281C"; // >
        brailleMap[31] = "2839"; // ?
        brailleMap[32] = "2808"; // @
        brailleMap[33] = "2801"; // A
        brailleMap[34] = "2803"; // B
        brailleMap[35] = "2809"; // C
        brailleMap[36] = "2819"; // D
        brailleMap[37] = "2811"; // E
        brailleMap[38] = "280B"; // F
        brailleMap[39] = "281B"; // G
        brailleMap[40] = "2813"; // H
        brailleMap[41] = "280A"; // I
        brailleMap[42] = "281A"; // J
        brailleMap[43] = "2805"; // K
        brailleMap[44] = "2807"; // L
        brailleMap[45] = "280D"; // M
        brailleMap[46] = "281D"; // N
        brailleMap[47] = "2815"; // O
        brailleMap[48] = "280F"; // P
        brailleMap[49] = "281F"; // Q
        brailleMap[50] = "2817"; // R
        brailleMap[51] = "280E"; // S
        brailleMap[52] = "281E"; // T
        brailleMap[53] = "2825"; // U
        brailleMap[54] = "2827"; // V
        brailleMap[55] = "283A"; // W
        brailleMap[56] = "282D"; // X
        brailleMap[57] = "283D"; // Y
        brailleMap[58] = "282F"; // Z
        brailleMap[59] = "2833"; // [
        brailleMap[60] = "282C"; // Backslash
        brailleMap[61] = "2834"; // ]
        brailleMap[62] = "2821"; // ^
        brailleMap[63] = "2820"; // _
    }

    // 점자 유니코드를 가져오는 메서드
    public static String getBrailleUnicode(int asciiCode) {
        if (asciiCode >= 0x20 && asciiCode <= 0x5F) {
            return brailleMap[asciiCode - 0x20];
        }
        if (asciiCode == 0x0c) {
            return "000C";
        } else {
            log.warn("no mapping :0x{}", String.format("%02X", asciiCode));
            return "xx"; // 유효하지 않은 아스키 코드
        }
    }

    public static Integer getAscii(String unicode) {
        return IntStream.range(0, brailleMap.length)
                .filter(i -> brailleMap[i].equals(unicode))
                .findFirst()
                .orElse(-1);
    }
}
