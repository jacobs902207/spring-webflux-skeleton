package proejct.spring.skeleton.common.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@Slf4j
@UtilityClass
public class FormatUtils {
    public static final char SPACE_FORMAT = '-';

    public static String getCardNo(String cardNo) {
        return getFmtCardNo(cardNo, false);
    }

    /**
     * 카드번호를 보기좋게 표시.
     * (문자/기호 자동제거, 15자리 미만 또는 null 일때는 빈문자열).
     * <pre>
     * String cardNo = Mas.getFmtCardNo("3234567890123456");
     * ( cardNo == "3234-5678-9012-3456" or 3234-56**-****-3456 )
     * </pre>
     *
     * @param cardNo    카드번호
     * @param isMasking 마스킹여부
     * @return 변환된 문자열
     */
    public static String getFmtCardNo(String cardNo, boolean isMasking) {
        if (cardNo != null) {
            String str = StringUtils.numberChar(cardNo);
            int len = str.length();
            if (len == 16) {
                if (isMasking) {
                    return new StringBuilder(19)
                            .append(str.substring(0, 4))
                            .append(SPACE_FORMAT)
                            .append(str.substring(4, 6))
                            .append("**")
                            .append(SPACE_FORMAT)
                            .append("****")
                            .append(SPACE_FORMAT)
                            .append(str.substring(12, 16)).toString();
                } else {
                    return new StringBuilder(19)
                            .append(str.substring(0, 4))
                            .append(SPACE_FORMAT)
                            .append(str.substring(4, 8))
                            .append(SPACE_FORMAT)
                            .append(str.substring(8, 12))
                            .append(SPACE_FORMAT)
                            .append(str.substring(12, 16)).toString();
                }
            } else if (len == 15) {
                if (isMasking) {
                    return new StringBuilder(17)
                            .append(str.substring(0, 4))
                            .append(SPACE_FORMAT)
                            .append("**")
                            .append(str.substring(6, 10))
                            .append(SPACE_FORMAT)
                            .append("***")
                            .append(str.substring(13, 15)).toString();
                } else {
                    return new StringBuilder(17)
                            .append(str.substring(0, 4))
                            .append(SPACE_FORMAT)
                            .append(str.substring(4, 10))
                            .append(SPACE_FORMAT)
                            .append(str.substring(10, 15)).toString();
                }
            }
        }
        return StringUtils.EMPTY;
    }

    public static String getFmtDate(String date, String format) {
        if (date == null || format == null) {
            return "";
        }

        if (date.length() < 1) {
            return "";
        }

        try {
            Date inputDate = new SimpleDateFormat("yyyyMMdd").parse(date);
            LocalDate localDate = inputDate.toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDate();

            return localDate.format(DateTimeFormatter.ofPattern(format)
                    .withLocale(Locale.forLanguageTag("ko")));
        } catch (ParseException ex) {
            log.error("FormatUtils#ParseException, {}, {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
            return "";
        }
    }

    public static String getFmtPhone(String phone) {
        if (phone == null || phone.length() < 1) {
            return "";
        }

        if (phone.length() == 8) {
            return phone.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
        } else if (phone.length() == 12) {
            return phone.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
        }
        return phone.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
    }

    /**
     * 사업자등록번호를 보기좋게 표시.
     * (문자/기호 자동제거, 10자리 미만 또는 null 일때는 기존정보 리턴).
     * <pre>
     * String bzNo = Mas.getFmtBzNo("1234567890");
     * ( bzNo == "123-45-67890" )
     * </pre>
     *
     * @param bzNo 사업자등록번호
     * @return 변환된 문자열
     */
    public static String getFmtBzNo(String bzNo) {
        if (bzNo != null) {
            String str = StringUtils.numberChar(bzNo);
            int len = str.length();
            if (len == 10) {
                return new StringBuilder(12)
                        .append(str.substring(0, 3))
                        .append(SPACE_FORMAT)
                        .append(str.substring(3, 5))
                        .append(SPACE_FORMAT)
                        .append(str.substring(5, 10)).toString();
            }
        }
        return bzNo;
    }

    /**
     * 한글 이름을 마스킹한다.
     *
     * @param name
     * @return
     */
    public static String getMaskingHanName(String name) {
        return getMaskingHanName(name, true);
    }

    /**
     * 한글 이름을 마스킹한다.
     *
     * @param name
     * @param isMasking
     * @return
     */
    public static String getMaskingHanName(String name, boolean isMasking) {
        if (!isMasking) {
            return name;
        } else if (StringUtils.isNullOrBlank(name)) {
            return StringUtils.EMPTY;
        } else if (name.length() == 2) {
            return name.substring(0, 1) + "*";
        } else if (name.length() >= 5) {
            return name.substring(0, name.length() - 2) + "**";
        } else {
            char[] array = name.toCharArray();

            for (int i = 1; i < name.length() - 1; i++) {
                array[i] = '*';
            }

            return new String(array);
        }
    }

    /**
     * 공백으로 구분되어 한글 이름이 포함되어 있는 이름에 마스킹한다.
     * 첫번째 공백 앞은 반드시 한글 이름
     * ex) "홍길동" -> "홍*동"
     * ex) "홍길동 직원" -> "홍*동 직원"
     *
     * @param name
     * @return
     */
    public static String getMaskingIncludeHanName(String name) {
        return getMaskingIncludeHanName(name, true);
    }

    /**
     * 공백으로 구분되어 한글 이름이 포함되어 있는 이름에 마스킹한다.
     * 첫번째 공백 앞은 반드시 한글 이름
     * ex) "홍길동" -> "홍*동"
     * ex) "홍길동 직원" -> "홍*동 직원"
     *
     * @param name
     * @param isMasking
     * @return
     */
    public static String getMaskingIncludeHanName(String name, boolean isMasking) {
        if (!isMasking) {
            return name;
        } else if (StringUtils.isNullOrBlank(name)) {
            return StringUtils.EMPTY;
        } else if (!name.contains(" ")) {
            return getMaskingHanName(name, isMasking);
        } else {
            String hanName = getMaskingHanName(name.split(" ")[0]);
            return hanName + name.substring(hanName.length(), name.length());
        }
    }

    /**
     * 영문 이름을 마스킹한다.
     *
     * @param name
     * @return
     */
    public static String getMaskingEngName(String name) {
        return getMaskingEngName(name, true);
    }

    /**
     * 영문 이름을 마스킹한다.
     *
     * @param name
     * @param isMasking
     * @return
     */
    public static String getMaskingEngName(String name, boolean isMasking) {
        if (!isMasking) {
            return name;
        } else if (StringUtils.isNullOrBlank(name)) {
            return StringUtils.EMPTY;
        }

        String[] array = name.split(" ");

        if (array.length == 1) {
            if (name.length() <= 4) {
                return name;
            } else {
                char[] array2 = name.toCharArray();

                for (int i = 4; i < name.length(); i++) {
                    array2[i] = '*';
                }

                return new String(array2);
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(array[0]).append(" ");

            for (int i = 1; i < array.length; i++) {
                for (char dummy : array[i].toCharArray()) {
                    sb.append('*');
                }

                sb.append(" ");
            }

            return sb.toString().trim();
        }
    }

    /**
     * 주민번호를 마스킹한다.
     *
     * @param juminNo
     * @return
     */
    public static String getMaskingJuminNo(String juminNo) {
        return getMaskingJuminNo(juminNo, true);
    }

    /**
     * 주민번호를 마스킹한다.
     *
     * @param juminNo
     * @param isMasking
     * @return
     */
    public static String getMaskingJuminNo(String juminNo, boolean isMasking) {
        return getMaskingJuminNo(juminNo, false, isMasking);
    }

    /**
     * 주민번호를 마스킹한다.
     *
     * @param juminNo         주민번호
     * @param isSexNotMasking 성별구분 마스킹여부
     * @param isMasking       마스킹여부
     * @return
     */
    public static String getMaskingJuminNo(String juminNo, boolean isSexNotMasking, boolean isMasking) {
        if (!isMasking) {
            return juminNo;
        } else if (StringUtils.isNullOrBlank(juminNo)) {
            return StringUtils.EMPTY;
        }

        int guide = 7;

        if (isSexNotMasking) {
            guide = 6;
        }

        char[] array = juminNo.toCharArray();

        for (int i = juminNo.length() - 1, j = 0; j < guide; j++, i--) {
            array[i] = '*';
        }

        return new String(array);
    }

    /**
     * 전화번호를 마스킹한다.
     *
     * @param telNo
     * @return
     */
    public static String getMaskingTelNo(String telNo) {
        return getMaskingTelNo(telNo, true);
    }

    /**
     * 전화번호를 마스킹한다.
     *
     * @param telNo
     * @param isMasking
     * @return
     */
    public static String getMaskingTelNo(String telNo, boolean isMasking) {
        if (!isMasking) {
            return telNo;
        } else if (StringUtils.isNullOrBlank(telNo)) {
            return StringUtils.EMPTY;
        }

        String tempTelNo;

        if (telNo.contains("-")) {
            String[] array = telNo.split("-");

            if (array.length == 3) {
                char[] array2 = array[1].toCharArray();

                for (int i = 0; i < array2.length; i++) {
                    array2[i] = '*';
                }

                return array[0] + "-" + new String(array2) + "-" + array[2];
            } else {
                tempTelNo = telNo.replace("-", "");
            }
        } else {
            tempTelNo = telNo;
        }

        char[] array = tempTelNo.toCharArray();

        if (tempTelNo.startsWith("02")) {
            for (int i = 2; i < array.length - 4; i++) {
                array[i] = '*';
            }
        } else {
            for (int i = 3; i < array.length - 4; i++) {
                array[i] = '*';
            }
        }

        return new String(array);
    }

    /**
     * 주소를 마스킹한다.
     *
     * @param address
     * @return
     */
    public static String getMaskingAddress(String address) {
        return getMaskingAddress(address, true);
    }

    /**
     * 주소를 마스킹한다.
     *
     * @param address
     * @param isMasking
     * @return
     */
    public static String getMaskingAddress(String address, boolean isMasking) {
        if (!isMasking) {
            return address;
        } else if (StringUtils.isNullOrBlank(address)) {
            return StringUtils.EMPTY;
        }

        char[] array = address.toCharArray();

        for (int i = 0; i < array.length; i++) {
            if (array[i] >= 48 && array[i] <= 57) {
                array[i] = '*';
            }
        }

        return new String(array);
    }

    /**
     * 이메일을 마스킹한다.
     *
     * @param email
     * @return
     */
    public static String getMaskingEmail(String email) {
        return getMaskingEmail(email, true);
    }

    /**
     * 이메일을 마스킹한다.
     *
     * @param email
     * @param isMasking
     * @return
     */
    public static String getMaskingEmail(String email, boolean isMasking) {
        if (!isMasking) {
            return email;
        } else if (StringUtils.isNullOrBlank(email)) {
            return StringUtils.EMPTY;
        }

        String[] splitArr = email.split("@");
        String tempEmail = splitArr[0];

        if (tempEmail.length() < 3) {
            return email;
        }

        int guide = ((int) Math.floor(tempEmail.length() / 2)) / 2;

        char[] array = tempEmail.toCharArray();

        for (int i = guide; i < array.length - guide; i++) {
            array[i] = '*';
        }

        if (splitArr.length > 1) {
            return new String(array) + "@" + splitArr[1];
        } else {
            return new String(array);
        }
    }

    /**
     * 계좌번호를 마스킹한다.
     *
     * @param accountNo
     * @return
     */
    public static String getMaskingAccountNo(String accountNo) {
        return getMaskingAccountNo(accountNo, true);
    }

    /**
     * 계좌번호를 마스킹한다.
     *
     * @param accountNo
     * @param isMasking
     * @return
     */
    public static String getMaskingAccountNo(String accountNo, boolean isMasking) {
        if (!isMasking) {
            return accountNo;
        } else if (StringUtils.isNullOrBlank(accountNo)) {
            return StringUtils.EMPTY;
        } else if (accountNo.length() <= 4) {
            return accountNo;
        } else {
            char[] array = accountNo.toCharArray();

            for (int i = 0; i < array.length - 4; i++) {
                array[i] = '*';
            }

            return new String(array);
        }
    }

}
