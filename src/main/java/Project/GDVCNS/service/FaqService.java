package Project.GDVCNS.service;

import Project.GDVCNS.entity.Faq;
import Project.GDVCNS.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;

    // [CẬP NHẬT] Thêm tham số keyword
    public List<Faq> getAllFaqs(String keyword) {
        Sort sort = Sort.by(Sort.Direction.ASC, "orderIndex");

        if (StringUtils.hasText(keyword)) {
            return faqRepository.findByQuestionContainingIgnoreCaseOrAnswerContainingIgnoreCase(keyword, keyword, sort);
        }
        return faqRepository.findAll(sort);
    }

    public Optional<Faq> getFaqById(Long id) {
        return faqRepository.findById(id);
    }

    public void saveFaq(Faq faq) {
        faqRepository.save(faq);
    }

    public void deleteFaq(Long id) {
        faqRepository.deleteById(id);
    }
}