package usoft.cdm.electronics_market.util;


import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.text.Normalizer;
import java.util.regex.Pattern;

@Slf4j
@UtilityClass
public class TextUtil {
    public String slug(String text) {
        String normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String result = pattern.matcher(normalizedText).replaceAll("");
        String slug = result.toLowerCase() // chuyển thành chữ thường
                .replaceAll("\\s", "-") // thay thế khoảng trắng bằng dấu gạch ngang
                .replaceAll("[^\\p{L}\\p{Nd}]+", "-") // thay thế các ký tự không phải là chữ cái hoặc số bằng dấu gạch ngang
                .replaceAll("-+", "-") // loại bỏ các dấu gạch ngang liên tiếp
                .replaceAll("^-|-$", ""); // loại bỏ dấu gạch ngang ở đầu và cuối chuỗi
        return slug;
    }
}
