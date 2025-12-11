package Project.GDVCNS.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class SlugUtils {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String makeSlug(String input) {
        if (input == null)
            throw new IllegalArgumentException("Input string cannot be null");

        // Bỏ dấu tiếng Việt (Ninh Bình -> Ninh Binh)
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(normalized).replaceAll("");

        // Chuyển thành chữ thường và bỏ ký tự lạ
        slug = slug.toLowerCase();
        slug = NONLATIN.matcher(slug).replaceAll("");

        // Loại bỏ nhiều dấu gạch ngang liên tiếp (giao--duc -> giao-duc)
        slug = slug.replaceAll("-+", "-");

        // Xóa gạch ngang ở đầu và cuối
        if (slug.startsWith("-")) slug = slug.substring(1);
        if (slug.endsWith("-")) slug = slug.substring(0, slug.length() - 1);

        return slug;
    }
}