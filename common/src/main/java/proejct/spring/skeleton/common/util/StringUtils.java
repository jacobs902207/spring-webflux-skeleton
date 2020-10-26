package proejct.spring.skeleton.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * String 관련 Util
 *
 * @author jinwoo.choi
 * @since 2017.08.01
 */
public class StringUtils {

    private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);

    /**
     * 빈문자열
     */
    public static final String EMPTY = "";
    /**
     * 공백문자열
     */
    public static final String BLANK = " ";
    /**
     * 숫자패턴
     */
    public static final String NUMBER_FORMAT_PATTERN = "#,##0.##";
    /**
     * 기본 숫자 포멧
     **/
    public static final DecimalFormat NUMBER_FORMAT;

    static {
        NUMBER_FORMAT = new DecimalFormat(NUMBER_FORMAT_PATTERN);
        NUMBER_FORMAT.setGroupingUsed(true);
    }

    /**
     * 문자열 LTrim
     *
     * @param s
     * @return
     */
    public static String ltrim(String s) {
        if (s == null) {
            return null;
        }

        return s.replaceAll("^\\s+", "");
    }

    /**
     * 문자열 RTrim
     *
     * @param s
     * @return
     */
    public static String rtrim(String s) {
        if (s == null) {
            return null;
        }

        return s.replaceAll("\\s+$", "");
    }

    /**
     * 문자열 첫글자 대문자화
     *
     * @param s
     * @return
     */
    public static String firstUpperCase(String s) {
        if (s == null) {
            return null;
        }

        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String lpad(String s, int size, char paddingChr) {
        if (s == null) {
            return null;
        } else if (s.length() >= size) {
            return s.substring(s.length() - size, size);
        }

        char[] paddingArr = new char[size - s.length()];
        Arrays.fill(paddingArr, paddingChr);

        return String.valueOf(paddingArr) + s;
    }

    public static String rpad(String s, int size, char paddingChr) {
        if (s == null) {
            return null;
        } else if (s.length() >= size) {
            return s.substring(0, size);
        }

        char[] paddingArr = new char[size - s.length()];
        Arrays.fill(paddingArr, paddingChr);

        return s + String.valueOf(paddingArr);
    }

    /**
     * String null 을 def 로 처리
     *
     * @param str
     * @param def
     * @return String
     */
    public static String nvl(String str, String def) {
        return str == null ? def : str;
    }

    /**
     * String null 을 "" 로 변환
     *
     * @param str
     * @return String
     */
    public static String nvl(String str) {
        return nvl(str, EMPTY);
    }

    /**
     * 문자열이 null 또는 Empty인 경우 지정된 문자열로 바꾸는 함수.
     * <pre>
     * String id1 = StringUtil.nvl2(request.getParameter("id1"),"unKnown");
     * 서블릿 요청 파라메터 id1에 대한 값이 null 또는 Empty 이면 "unKnown" 로 바꾼다.
     * </pre>
     *
     * @param str 원본 문자열
     * @param def null 또는 Empty 일 경우 바뀔 문자열
     * @return String
     */
    public static String nvl2(String str, String def) {
        return isNullOrEmpty(str) ? def : str;
    }

    /**
     * 문자열이 null, Empty, 공백(Blank)인 경우 지정된 문자열로 바꾸는 함수(문자열 trim()함).
     * <pre>
     * String id1 = StringUtil.nvl3(request.getParameter("id1"),"unKnown");
     * 서블릿 요청 파라메터 id1에 대한 값이 null, Empty, 공백(Blank) 이면 "unKnown" 로 바꾼다.
     * </pre>
     *
     * @param str 원본 문자열
     * @param def null 또는 Empty 일 경우 바뀔 문자열
     * @return 문자열
     */
    public static String nvl3(String str, String def) {
        if (str == null) {
            return def;
        }
        str = str.trim();
        if (str.length() == 0) {
            return def;
        }
        return str;
    }

    /**
     * 문자열이 null 또는 Empty 인지 확인하는 함수.
     * <pre>
     * boolean chk = StringUtil.isNullOrEmpty(request.getParameter("id1"));
     * </pre>
     *
     * @param str 원본 문자열
     * @return boolean
     * @see #nvl2(String, String)
     */
    public static boolean isNullOrEmpty(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 문자열이 null, Empty, 공백(Blank) 인지 확인하는 함수.
     * <pre>
     * boolean chk = StringUtil.isNullOrBlank(request.getParameter("id1"));
     * </pre>
     *
     * @param str 원본 문자열
     * @return boolean
     * @see #nvl3(String, String)
     */
    public static boolean isNullOrBlank(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return false;
    }


    /**
     * 문자열을 int 형으로 변환(콤마는 자동제거).
     * <pre>
     * String str = "1,000";
     * int rtn = StringUtil.parseInt(str,0);
     * </pre>
     *
     * @param str 원본 문자열
     * @param def 변환이 실패할 경우의 기본값
     * @return int
     */
    public static int parseInt(String str, int def) {
        if (isNullOrEmpty(str)) {
            return def;
        }
        str = str.trim();
        try {
            return Integer.parseInt(removeCommaNotNull(str));
        } catch (Throwable t) {
            return def;
        }
    }

    /**
     * 문자열을 int 형으로 변환(콤마는 자동제거).
     * <pre>
     * String str = "1,000";
     * int rtn = StringUtil.parseInt(str);
     * </pre>
     *
     * @param str 원본 문자열
     * @return int
     */
    public static int parseInt(String str) {
        return parseInt(str, 0);
    }

    /**
     * 문자열을 long 형으로 변환(콤마는 자동제거).
     * <pre>
     * String str = "1,000";
     * long rtn = StringUtil.parseLong(str,0L);
     * </pre>
     *
     * @param str 원본 문자열
     * @param def 변환이 실패할 경우의 기본값
     * @return long
     */
    public static long parseLong(String str, long def) {
        if (isNullOrEmpty(str)) {
            return def;
        }
        str = str.trim();
        try {
            return Long.parseLong(removeCommaNotNull(str));
        } catch (Throwable t) {
            return def;
        }
    }

    /**
     * 문자열을 long 형으로 변환(콤마는 자동제거).
     * <pre>
     * String str = "1,000";
     * long rtn = StringUtil.parseLong(str);
     * </pre>
     *
     * @param str 원본 문자열
     * @return long
     */
    public static long parseLong(String str) {
        return parseLong(str, 0L);
    }

    /**
     * 문자열을 금액형으로 변환(콤마는 자동제거).
     * <pre>
     * java.text.NumberFormat을 이용한 소수점 3자리 금액 표현,
     * String str = "12345";
     * String rtn = StringUtil.parseMoney(str);
     * ( rtn == "12,345" )
     * </pre>
     *
     * @param str 원본 문자열
     * @return 3자리 콤마
     */
    public static String parseMoney(String str) {
        return parseMoney(str, str);
    }

    /**
     * long 형을 금액형으로 변환
     *
     * @param value
     * @return
     */
    public static String parseMoney(long value) {
        String str = String.valueOf(value);
        return parseMoney(str, str);
    }

    /**
     * 문자열을 금액형으로 변환(콤마는 자동제거).
     * <pre>
     * java.text.NumberFormat을 이용한 소수점 3자리 금액 표현,
     * String str = "12345";
     * String rtn = StringUtil.parseMoney(str,"0");
     * ( rtn == "12,345" )
     * </pre>
     *
     * @param str 원본 문자열
     * @param def 변환이 실패할 경우의 기본값
     * @return 3자리 콤마
     */
    public static String parseMoney(String str, String def) {
        try {
            double val = parseDouble(str, 0.0d);
            return NUMBER_FORMAT.format(val);
        } catch (Throwable t) {
            return def;
        }
    }

    /**
     * 문자열을 byte[] 형으로 변환.
     * <pre>
     * String str = "1000";
     * byte[] rtn = StringUtil.parseByte(str);
     * </pre>
     *
     * @param str 원본 문자열
     * @return byte[]
     */
    public static byte[] parseByte(String str) {
        return parseByte(str, (Charset) null, null);
    }

    /**
     * 문자열을 byte[] 형으로 변환.
     * <pre>
     * String str = "1000";
     * byte[] rtn = StringUtil.parseByte(str, "EUC-KR");
     * </pre>
     *
     * @param str         원본 문자열
     * @param charsetName supported character encoding
     * @return byte[]
     */
    public static byte[] parseByte(String str, String charsetName) {
        return parseByte(str, toCharset(charsetName), null);
    }

    /**
     * 문자열을 byte[] 형으로 변환.
     * <pre>
     * String str = "1000";
     * byte[] rtn = StringUtil.parseByte(str, "EUC-KR", new byte[]{0});
     * </pre>
     *
     * @param str         원본 문자열
     * @param charsetName supported character encoding
     * @param def         변환이 실패할 경우의 기본값
     * @return byte[]
     */
    public static byte[] parseByte(String str, String charsetName, byte[] def) {
        return parseByte(str, toCharset(charsetName), def);
    }

    /**
     * 캐릭터세트에 해당하는 Charset 객체 반환.
     *
     * @param charsetName 캐릭터세트
     * @return Charset 객체
     */
    public static Charset toCharset(String charsetName) {
        if (charsetName != null) {
            try {
                return Charset.forName(charsetName);
            } catch (Throwable t) {
                logger.info("Convert charset failed", t);
            }
        }
        return null;
    }

    /**
     * 문자열을 byte[] 형으로 변환.
     * <pre>
     * String str = "1000";
     * byte[] rtn = StringUtil.parseByte(str, new byte[]{0});
     * </pre>
     *
     * @param str 원본 문자열
     * @param def 변환이 실패할 경우의 기본값
     * @return byte[]
     */
    public static byte[] parseByte(String str, byte[] def) {
        return parseByte(str, (Charset) null, def);
    }

    /**
     * 문자열을 byte[] 형으로 변환.
     * <pre>
     * String str = "1000";
     * byte[] rtn = StringUtil.parseByte(str, "EUC-KR");
     * </pre>
     *
     * @param str     원본 문자열
     * @param charset supported character encoding
     * @return byte[]
     */
    public static byte[] parseByte(String str, Charset charset) {
        return parseByte(str, charset, null);
    }

    /**
     * 문자열을 byte[] 형으로 변환.
     * <pre>
     * String str = "1000";
     * byte[] rtn = StringUtil.parseByte(str, "EUC-KR", new byte[]{0});
     * </pre>
     *
     * @param str     원본 문자열
     * @param charset supported character encoding
     * @param def     변환이 실패할 경우의 기본값
     * @return byte[]
     */
    public static byte[] parseByte(String str, Charset charset, byte[] def) {
        if (str == null) {
            return def;
        }
        try {
            if (charset == null) {
                return str.getBytes();
            }
            return str.getBytes(charset);
        } catch (Throwable t) {
            return def;
        }
    }

    /**
     * 문자열을 double 형으로 변환(콤마는 자동제거).
     * <pre>
     * String str = "1,000";
     * double rtn = StringUtil.parseDouble(str,0.0d);
     * </pre>
     *
     * @param str 원본 문자열
     * @param def 변환이 실패할 경우의 기본값
     * @return double
     */
    public static double parseDouble(String str, double def) {
        if (isNullOrEmpty(str)) {
            return def;
        }
        try {
            return Double.parseDouble(removeCommaNotNull(str));
        } catch (Throwable t) {
            return def;
        }
    }

    /**
     * 문자열을 double 형으로 변환(콤마는 자동제거).
     * <pre>
     * String str = "1,000";
     * double rtn = StringUtil.parseDouble(str);
     * </pre>
     *
     * @param str 원본 문자열
     * @return double
     */
    public static double parseDouble(String str) {
        return parseDouble(str, 0.0d);
    }

    /**
     * 문자열을 float 형으로 변환(콤마는 자동제거).
     * <pre>
     * String str = "1,000";
     * float rtn = StringUtil.parseFloat(str,0.0f);
     * </pre>
     *
     * @param str 원본 문자열
     * @param def 변환이 실패할 경우의 기본값
     * @return float
     */
    public static float parseFloat(String str, float def) {
        if (isNullOrEmpty(str)) {
            return def;
        }
        try {
            return Float.parseFloat(removeCommaNotNull(str));
        } catch (Throwable t) {
            return def;
        }
    }

    /**
     * 콤마제거 함수.
     * <pre>
     * StringUtil.removeComma(null)      = null
     * StringUtil.removeComma("")        = ""
     * StringUtil.removeComma("1,000")   = "1000"
     * StringUtil.removeComma("1,000.0") = "1000.0"
     * </pre>
     *
     * @param str 원본문자열
     * @return 바뀐 문자열
     */
    public static String removeComma(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        return removeCommaNotNull(str);
    }

    /**
     * 콤마제거 함수 처리중 null 입력 시 오류발생.
     *
     * @param str
     * @return 변환값
     * @see #removeComma(String)
     */
    public static String removeCommaNotNull(String str) {
        boolean comma = false;
        StringBuilder buff = null;
        int strLen = str.length();
        for (int ii = 0; ii < strLen; ++ii) {
            char chr = str.charAt(ii);
            if (chr == ',') {
                if (!comma) {
                    buff = new StringBuilder(strLen);
                    buff.append(str.substring(0, ii));
                    comma = true;
                }
            } else if (comma) {
                buff.append(chr);
            }
        }
        if (comma) {
            return buff.toString();
        }
        return str;
    }

    public static byte[] hexStringToByteArray(String hexString) {
        if (hexString == null || hexString.length() == 0) {
            return null;
        }

        byte[] byteArray = new byte[hexString.length() / 2];

        for (int i = 0; i < byteArray.length; i++) {
            byteArray[i] = (byte) Integer.parseInt(hexString.substring(2 * i, 2 * i + 2), 16);
        }

        return byteArray;
    }

    public static String byteArrayToHexString(byte[] byteArray) {
        if (byteArray == null || byteArray.length == 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder(byteArray.length * 2);

        String hexNumber;

        for (byte b : byteArray) {
            hexNumber = "0" + Integer.toHexString(0xff & b).toUpperCase();
            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }

        return sb.toString();
    }

    /**
     * 지정한 byte 길이만큼 문자열을 자름.
     * <pre>
     * StringUtil.cutB(null, 4)   = null
     * StringUtil.cutB("", 4)     = ""
     * StringUtil.cutB("test", 2) = "te"
     * StringUtil.cutB("test", 5) = "test"
     * </pre>
     *
     * @param str 원본문자열
     * @param len byte 길이
     * @return 변환값
     */
    public static String cutB(String str, int len) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        int total = 0;
        for (int ii = 0; ii < str.length(); ++ii) {
            total += String.valueOf(str.charAt(ii)).getBytes().length;
            if (total > len) {
                return str.substring(0, ii);
            }
        }
        return str;
    }

    /**
     * 메시지를 포멧팅한다.
     * <pre>
     * StringUtils.format("This is {} and {}.", "Apple", "Phone") = "This is Apple and Phone."
     * </pre>
     *
     * @param message
     * @param params
     * @return
     */
    public static String format(String message, Object... params) {
        if (params == null) {
            return message;
        } else {
            return MessageFormatter.arrayFormat(message, params).getMessage();
        }
    }

    /**
     * 숫자데이터만 추출.
     * <pre>
     * StringUtil.numberChar(null)        = null
     * StringUtil.numberChar("")          = ""
     * StringUtil.numberChar("123-30/33") = "1233033"
     * </pre>
     *
     * @param str 원본문자열
     * @return 변환문자열
     */
    public static String numberChar(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        int strLen = str.length();
        StringBuilder buff = new StringBuilder(strLen);
        for (int ii = 0; ii < strLen; ++ii) {
            char chr = str.charAt(ii);
            if (chr >= '0' && chr <= '9') {
                buff.append(chr);
            }
        }
        return buff.toString();
    }

    public static boolean isNumeric(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * 전달된 원본 문자열 <code>inString</code>에서 변경대상 문자열 <code>oldSubstring</code>을 찾아
     * 변경할 문자열 <code>newSubstring</code>로 모두 변경한다.
     *
     * @param inString     원본 문자열
     * @param oldSubstring 변경대상 문자열
     * @param newSubstring 변경할 문자열
     * @return 원본 문자열에서 변경대상 문자열 <code>oldSubstring</code>이 변경할 문자열
     * <code>newSubstring</code>로 모두 변경된 문자열
     */
    public static String replace(String inString, String oldSubstring, String newSubstring) {
        if (!hasLength(inString) || !hasLength(oldSubstring) || newSubstring == null) {
            return inString;
        }
        StringBuilder sb = new StringBuilder();
        int pos = 0;
        int index = inString.indexOf(oldSubstring);
        int patLen = oldSubstring.length();
        while (index >= 0) {
            sb.append(inString.substring(pos, index));
            sb.append(newSubstring);
            pos = index + patLen;
            index = inString.indexOf(oldSubstring, pos);
        }
        sb.append(inString.substring(pos));
        return sb.toString();
    }

    public static boolean isEmpty(String str) {
        String s = str;
        if (s != null) {
            s = s.trim();
        }
        return isEmpty_ch((CharSequence) s);
    }

    private static boolean hasLength_ch(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    /**
     * 문자열의 길이가 있는지 판단한다. 전달된 문자열이 <code>null</code>이 아니고 길이가 0보다 큰 경우 길이가 있는
     * 것으로 판단한다.
     *
     * @param str 검사할 문자열
     * @return 문자열의 길이가 있는지 여부
     */
    public static boolean hasLength(String str) {
        return hasLength_ch((CharSequence) str);
    }

    private static boolean isEmpty_ch(final CharSequence str) {
        if (null == str) {
            return true;
        }
        for (int i = 0, n = str.length(); i < n; ++i) {
            if (Character.isWhitespace(str.charAt(i))) {
                continue;
            }
            return false;
        }
        return true;
    }
}