package Project.GDVCNS.service;

import Project.GDVCNS.common.ConfigKey;
import Project.GDVCNS.entity.SystemConfig;
import Project.GDVCNS.repository.SystemConfigRepository;
import Project.GDVCNS.utils.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SystemConfigService {

    private final SystemConfigRepository configRepository;

    public Map<String, String> getAllConfigs() {
        List<SystemConfig> configs = configRepository.findAll();
        return configs.stream()
                .collect(Collectors.toMap(SystemConfig::getConfigKey, SystemConfig::getConfigValue));
    }

    @Transactional
    public void saveConfig(Map<String, String> params, MultipartFile logoFile, MultipartFile faviconFile) throws IOException {
        // 1. Lưu Text
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (isValidConfigKey(key)) {
                updateOrCreateConfig(key, value);
            }
        }

        // 2. Lưu Logo
        if (logoFile != null && !logoFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(logoFile.getOriginalFilename());
            String uploadDir = "uploads/system";
            FileUploadUtils.saveFile(uploadDir, "logo_" + fileName, logoFile);
            updateOrCreateConfig(ConfigKey.SITE_LOGO, "/" + uploadDir + "/logo_" + fileName);
        }

        // 3. Lưu Favicon
        if (faviconFile != null && !faviconFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(faviconFile.getOriginalFilename());
            String uploadDir = "uploads/system";
            FileUploadUtils.saveFile(uploadDir, "favicon_" + fileName, faviconFile);
            updateOrCreateConfig(ConfigKey.SITE_FAVICON, "/" + uploadDir + "/favicon_" + fileName);
        }
    }

    private void updateOrCreateConfig(String key, String value) {
        SystemConfig config = configRepository.findById(key)
                .orElse(new SystemConfig(key, value, "Cấu hình hệ thống"));
        config.setConfigValue(value);
        configRepository.save(config);
    }

    // CẬP NHẬT: Thêm các key mới vào danh sách hợp lệ
    private boolean isValidConfigKey(String key) {
        return key.equals(ConfigKey.COMPANY_NAME_VI) ||
                key.equals(ConfigKey.COMPANY_NAME_EN) ||
                key.equals(ConfigKey.EMAIL) ||
                key.equals(ConfigKey.HEADQUARTERS) ||
                key.equals(ConfigKey.HOTLINE) ||
                key.equals(ConfigKey.REPRESENTATIVE) ||
                key.equals(ConfigKey.TAX_CODE) ||
                key.equals(ConfigKey.CHARTER_CAPITAL) ||
                key.equals(ConfigKey.FOUNDED_DATE) ||
                // Các key mới
                key.equals(ConfigKey.SITE_INTRO) ||
                key.equals(ConfigKey.SITE_VISION) ||
                key.equals(ConfigKey.SITE_MISSION) ||
                key.equals(ConfigKey.SITE_CORE_VALUES);
    }
}