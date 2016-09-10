package com.qunar.fresh.librarysystem.utils;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将汉字转换成拼音
 *
 * @author hang.gao
 */
public class ChineseToPinyin {

    private static final Logger logger = LoggerFactory.getLogger(ChineseToPinyin.class);

    /**
     * 汉字
     */
    private final String CHINESE_CHARACTER = "[\u4E00-\u9FA5]+";

    /**
     * 拼音的格式
     */
    private final HanyuPinyinOutputFormat format;

    /**
     * 单例
     */
    private static final ChineseToPinyin INSTANCE = new ChineseToPinyin();

    /**
     * 转换汉字
     *
     * @param chinese
     * @return
     */
    public static final String transform(String chinese) {
        return INSTANCE.trans(chinese);
    }

    private ChineseToPinyin() {
        format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
    }

    /**
     * 执行转换，将汉字转换成拼音
     *
     * @param chinese 输入的包含汉字的串
     * @return 转换后的拼音
     */
    private String trans(String chinese) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(chinese));
        char[] input = chinese.trim().toCharArray();
        StringBuilder output = new StringBuilder("");

        try {
            for (int i = 0; i < input.length; i++) {
                if (Character.toString(input[i]).matches(CHINESE_CHARACTER)) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
                    output.append(temp[0]);
                } else {
                    output.append(Character.toString(input[i]));
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            logger.error("转换出错", e);
        }
        return output.toString();
    }
}
