package Project.GDVCNS.service;

import Project.GDVCNS.dto.ContactDTO;
import Project.GDVCNS.entity.Contact;
import Project.GDVCNS.enums.ContactStatus;
import Project.GDVCNS.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // Tự động inject dependency (Repository)
public class ContactService {

    private final ContactRepository contactRepository;

    public Page<ContactDTO> getAllContacts(String keyword, ContactStatus status, Pageable pageable) {
        Page<Contact> contactPage = contactRepository.searchContacts(keyword, status, pageable);
        return contactPage.map(this::convertToDTO);
    }

    public ContactDTO getContactById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy liên hệ với ID: " + id));
        return convertToDTO(contact);
    }

    @Transactional
    public void saveContact(ContactDTO dto) {
        Contact contact;
        if (dto.getId() != null) {
            // Update: Lấy từ DB ra
            contact = contactRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy liên hệ để cập nhật"));
        } else {
            // Create: New object
            contact = new Contact();
            // createdAt tự động lo bởi Entity (@CreationTimestamp)
        }

        // Map dữ liệu (Không có cột note)
        contact.setFullName(dto.getFullName());
        contact.setPhone(dto.getPhone());
        contact.setEmail(dto.getEmail());
        contact.setSubject(dto.getSubject());
        contact.setMessage(dto.getMessage());
        contact.setStatus(dto.getStatus());

        contactRepository.save(contact);
    }

    @Transactional
    public void deleteContact(Long id) {
        if (contactRepository.existsById(id)) {
            contactRepository.deleteById(id);
        }
    }

    // Helper method chuyển đổi Entity -> DTO
    private ContactDTO convertToDTO(Contact contact) {
        return ContactDTO.builder()
                .id(contact.getId())
                .fullName(contact.getFullName())
                .phone(contact.getPhone())
                .email(contact.getEmail())
                .subject(contact.getSubject())
                .message(contact.getMessage())
                .status(contact.getStatus())
                .createdAt(contact.getCreatedAt())
                .build();
    }
}