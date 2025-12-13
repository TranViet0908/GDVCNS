package Project.GDVCNS.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class SlugUtils {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String makeSlug(String input) {
        if (input == null)
            throw new IllegalArgumentException("Input string cannot be null");

        // 1. Chuyển đổi thủ công Tiếng Việt có dấu sang không dấu (Cách an toàn nhất)
        String slug = input.trim().toLowerCase();
        slug = slug.replaceAll("[áàảãạăắằẳẵặâấầẩẫậ]", "a");
        slug = slug.replaceAll("[éèẻẽẹêếềểễệ]", "e");
        slug = slug.replaceAll("[iíìỉĩị]", "i");
        slug = slug.replaceAll("[óòỏõọôốồổỗộơớờởỡợ]", "o");
        slug = slug.replaceAll("[úùủũụưứừửữự]", "u");
        slug = slug.replaceAll("[ýỳỷỹỵ]", "y");
        slug = slug.replaceAll("đ", "d");

        // 2. Thay khoảng trắng thành gạch ngang
        slug = WHITESPACE.matcher(slug).replaceAll("-");

        // 3. Chuẩn hóa Normalizer (để xử lý các dấu còn sót lại)
        String normalized = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(normalized).replaceAll("");

        // 4. Loại bỏ ký tự đặc biệt
        slug = NONLATIN.matcher(slug).replaceAll("");

        // 5. Xử lý gạch ngang thừa
        slug = slug.replaceAll("-+", "-");

        // Xóa gạch ngang đầu cuối
        if (slug.startsWith("-")) slug = slug.substring(1);
        if (slug.endsWith("-")) slug = slug.substring(0, slug.length() - 1);

        return slug;
    }
}