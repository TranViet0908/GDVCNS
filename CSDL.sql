-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: gdvcns
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `slug` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` enum('POST','COURSE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `parent_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `slug` (`slug`),
  KEY `parent_id` (`parent_id`),
  CONSTRAINT `categories_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `categories` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Giáo dục & Đào tạo','giao-duc-dao-tao','COURSE',NULL),(2,'Công nghệ thông tin & Chuyển đổi số','cntt-chuyen-doi-so','COURSE',NULL),(3,'Tin tức & Sự kiện','tin-tuc-su-kien','POST',NULL),(4,'Dự án tiêu biểu','du-an-tieu-bieu','POST',NULL);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contacts`
--

DROP TABLE IF EXISTS `contacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contacts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `full_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `subject` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `status` enum('NEW','PROCESSING','DONE') COLLATE utf8mb4_unicode_ci DEFAULT 'NEW',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contacts`
--

LOCK TABLES `contacts` WRITE;
/*!40000 ALTER TABLE `contacts` DISABLE KEYS */;
INSERT INTO `contacts` VALUES (1,'Nguyễn Văn A','0901234567','a.nguyen@gmail.com','Tư vấn khóa AI','Tôi muốn hỏi học phí khóa AI nông dân.','NEW','2025-12-06 21:32:56'),(2,'Trần Thị B','0902345678','b.tran@gmail.com','Hợp tác đào tạo','UBND xã X muốn liên kết tổ chức lớp.','PROCESSING','2025-12-06 21:32:56'),(3,'Lê Văn C','0903456789','c.le@gmail.com','Hỏi về lịch học','Khóa phụ nữ AI học buổi tối không?','NEW','2025-12-06 21:32:56'),(4,'Phạm Thị D','0904567890','d.pham@gmail.com','Xin tài liệu','Cho tôi xin slide bài giảng hôm qua.','DONE','2025-12-06 21:32:56'),(5,'Hoàng Văn E','0905678901','e.hoang@gmail.com','Báo giá dự án','Báo giá gói chuyển đổi số cấp xã.','NEW','2025-12-06 21:32:56'),(6,'Vũ Thị F','0906789012','f.vu@gmail.com','Tuyển dụng','Tôi muốn ứng tuyển vị trí Content.','DONE','2025-12-06 21:32:56'),(7,'Đặng Văn G','0907890123','g.dang@gmail.com','Lỗi truy cập','Tôi không vào được website học tập.','PROCESSING','2025-12-06 21:32:56'),(8,'Bùi Thị H','0908901234','h.bui@gmail.com','Đăng ký học','Đăng ký cho 5 cán bộ xã Y.','NEW','2025-12-06 21:32:56'),(9,'Đỗ Văn I','0909012345','i.do@gmail.com','Góp ý','Giao diện web hơi khó nhìn trên điện thoại.','NEW','2025-12-06 21:32:56'),(10,'Hồ Thị K','0910123456','k.ho@gmail.com','Liên hệ làm đối tác','Công ty ABC muốn hợp tác.','NEW','2025-12-06 21:32:56'),(11,'Dương Văn L','0911234567','l.duong@gmail.com','Tư vấn phần mềm','Công ty có bán phần mềm quản lý không?','NEW','2025-12-06 21:32:56'),(12,'Lý Thị M','0912345678','m.ly@gmail.com','Hỏi đường','Văn phòng công ty ở đâu Linh Đàm?','DONE','2025-12-06 21:32:56'),(13,'Mai Văn N','0913456789','n.mai@gmail.com','Tư vấn','Cần tư vấn gấp.','NEW','2025-12-06 21:32:56'),(14,'Ngô Thị O','0914567890','o.ngo@gmail.com','Khóa học miễn phí','Có khóa nào free không?','DONE','2025-12-06 21:32:56'),(15,'Đào Văn P','0915678901','p.dao@gmail.com','Hỗ trợ kỹ thuật','Quên mật khẩu admin.','PROCESSING','2025-12-06 21:32:56'),(16,'Cao Thị Q','0916789012','q.cao@gmail.com','Đặt lịch hẹn','Muốn gặp TGĐ bàn công việc.','NEW','2025-12-06 21:32:56'),(17,'Đinh Văn R','0917890123','r.dinh@gmail.com','Chào hàng','Bán văn phòng phẩm.','DONE','2025-12-06 21:32:56'),(18,'Trương Thị S','0918901234','s.truong@gmail.com','Tư vấn pháp lý','Hỏi về hợp đồng đào tạo.','NEW','2025-12-06 21:32:56'),(19,'Lâm Văn T','0919012345','t.lam@gmail.com','Xin thực tập','Sinh viên năm cuối xin thực tập.','NEW','2025-12-06 21:32:56'),(20,'Phan Thị U','0920123456','u.phan@gmail.com','Khen ngợi','Dịch vụ rất tốt.','DONE','2025-12-06 21:32:56');
/*!40000 ALTER TABLE `contacts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `slug` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `thumbnail_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `duration` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `target_audience` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `training_format` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `view_count` bigint DEFAULT '0',
  `category_id` bigint DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `slug` (`slug`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `courses_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES (1,'Ứng dụng AI nâng cao hiệu suất công việc cho cán bộ Công chức xã phường','ung-dung-ai-cho-can-bo-xa-phuong',NULL,'Nâng cao năng lực số cho hành chính công.','Nội dung chi tiết...','05 buổi','Cán bộ xã phường','Trực tiếp + Online',1,4,1,'2025-12-06 21:32:56','2025-12-11 17:22:34'),(2,'Bình dân học vụ số','binh-dan-hoc-vu-so',NULL,'Phổ cập kỹ năng số cho người dân.','Nội dung chi tiết...','05 buổi','Người dân, Cán bộ','Trực tiếp + Online',1,4,1,'2025-12-06 21:32:56','2025-12-11 22:47:54'),(3,'Nâng cao năng lực ứng dụng CNTT từ tỉnh đến cơ sở','nang-cao-nang-luc-cntt-truyen-thong',NULL,'Đào tạo chiến lược truyền thông số.','Nội dung chi tiết...','05 buổi','Cán bộ tỉnh/xã','Trực tiếp + Online',1,3,2,'2025-12-06 21:32:56','2025-12-11 17:22:31'),(4,'Nâng cao năng lực phụ nữ với AI','phu-nu-voi-ai',NULL,'Giúp phụ nữ làm chủ công nghệ.','Nội dung chi tiết...','05 buổi','Phụ nữ','Trực tiếp + Online',1,0,1,'2025-12-06 21:32:56','2025-12-06 21:32:56'),(5,'Nông dân thời đại số ứng dụng AI','nong-dan-thoi-dai-so',NULL,'Ứng dụng AI vào nông nghiệp.','Nội dung chi tiết...','05 buổi','Nông dân','Trực tiếp + Online',1,1,1,'2025-12-06 21:32:56','2025-12-11 17:40:52'),(6,'Cựu chiến binh làm chủ công nghệ AI','cuu-chien-binh-lam-chu-ai',NULL,'Hỗ trợ CCB hội nhập số.','Nội dung chi tiết...','05 buổi','Cựu chiến binh','Trực tiếp + Online',1,0,1,'2025-12-06 21:32:56','2025-12-06 21:32:56');
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `faqs`
--

DROP TABLE IF EXISTS `faqs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `faqs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `question` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `answer` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `order_index` int DEFAULT '0',
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `faqs`
--

LOCK TABLES `faqs` WRITE;
/*!40000 ALTER TABLE `faqs` DISABLE KEYS */;
INSERT INTO `faqs` VALUES (1,'Làm thế nào để đăng ký khóa học tại GDVCNS?','Bạn có thể đăng ký trực tiếp trên website bằng cách nhấn nút \"Đăng ký\" tại trang chi tiết khóa học, hoặc liên hệ qua Hotline/Zalo: 0926 262 898 để được hỗ trợ nhanh nhất.',1,1,'2025-12-11 18:02:56','2025-12-11 18:02:56'),(2,'Hình thức học tập là Online hay Offline?','Chúng tôi cung cấp linh hoạt cả hai hình thức. Với các khóa đào tạo kỹ năng số cho nông dân hoặc người cao tuổi, chúng tôi ưu tiên tổ chức Offline tại địa phương kết hợp hỗ trợ Online qua Zalo.',2,1,'2025-12-11 18:02:56','2025-12-11 18:02:56'),(3,'Sau khóa học có được cấp chứng chỉ không?','Có. Sau khi hoàn thành khóa học và bài kiểm tra cuối khóa, học viên sẽ được cấp Giấy chứng nhận hoàn thành khóa học từ Công ty Cổ phần Giáo dục và Công nghệ số Việt Nam.',3,1,'2025-12-11 18:02:56','2025-12-11 18:02:56'),(4,'Tôi có thể bảo lưu khóa học không?','Học viên được phép bảo lưu khóa học trong vòng 06 tháng kể từ ngày đăng ký. Vui lòng liên hệ bộ phận giáo务 để làm thủ tục.',4,1,'2025-12-11 18:02:56','2025-12-11 18:02:56'),(5,'Phương thức thanh toán học phí như thế nào?','Chúng tôi chấp nhận chuyển khoản ngân hàng, quét mã QR hoặc thanh toán tiền mặt trực tiếp tại văn phòng công ty.',5,1,'2025-12-11 18:02:56','2025-12-11 18:02:56');
/*!40000 ALTER TABLE `faqs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `files`
--

DROP TABLE IF EXISTS `files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `files` (
  `id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `original_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `file_path` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `file_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `file_size` bigint DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `files`
--

LOCK TABLES `files` WRITE;
/*!40000 ALTER TABLE `files` DISABLE KEYS */;
INSERT INTO `files` VALUES ('f1','avatar_admin.jpg','/uploads/users/avatar_admin.jpg','image/jpeg',1024,'2025-12-06 21:32:56'),('f10','img_demo_03.jpg','/uploads/dummy/03.jpg','image/jpeg',1500,'2025-12-06 21:32:56'),('f11','img_demo_04.jpg','/uploads/dummy/04.jpg','image/jpeg',1500,'2025-12-06 21:32:56'),('f12','img_demo_05.jpg','/uploads/dummy/05.jpg','image/jpeg',1500,'2025-12-06 21:32:56'),('f13','img_demo_06.jpg','/uploads/dummy/06.jpg','image/jpeg',1500,'2025-12-06 21:32:56'),('f14','img_demo_07.jpg','/uploads/dummy/07.jpg','image/jpeg',1500,'2025-12-06 21:32:56'),('f15','img_demo_08.jpg','/uploads/dummy/08.jpg','image/jpeg',1500,'2025-12-06 21:32:56'),('f16','img_demo_09.jpg','/uploads/dummy/09.jpg','image/jpeg',1500,'2025-12-06 21:32:56'),('f17','img_demo_10.jpg','/uploads/dummy/10.jpg','image/jpeg',1500,'2025-12-06 21:32:56'),('f18','video_intro.mp4','/uploads/videos/intro.mp4','video/mp4',50000,'2025-12-06 21:32:56'),('f19','cv_ungvien.pdf','/uploads/hr/cv.pdf','application/pdf',2000,'2025-12-06 21:32:56'),('f2','course_ai_nongdan.png','/uploads/courses/ai_nongdan.png','image/png',2048,'2025-12-06 21:32:56'),('f20','logo_company.png','/uploads/system/logo.png','image/png',500,'2025-12-06 21:32:56'),('f3','banner_main.jpg','/uploads/banners/main.jpg','image/jpeg',5000,'2025-12-06 21:32:56'),('f4','tailieu_tap_huan.pdf','/uploads/docs/tailieu.pdf','application/pdf',10240,'2025-12-06 21:32:56'),('f5','sukien_kyket_01.jpg','/uploads/news/kyket01.jpg','image/jpeg',3000,'2025-12-06 21:32:56'),('f6','sukien_kyket_02.jpg','/uploads/news/kyket02.jpg','image/jpeg',3100,'2025-12-06 21:32:56'),('f7','hoithao_chuyendoiso.jpg','/uploads/news/hoithao.jpg','image/jpeg',4000,'2025-12-06 21:32:56'),('f8','img_demo_01.jpg','/uploads/dummy/01.jpg','image/jpeg',1500,'2025-12-06 21:32:56'),('f9','img_demo_02.jpg','/uploads/dummy/02.jpg','image/jpeg',1500,'2025-12-06 21:32:56');
/*!40000 ALTER TABLE `files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `posts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `slug` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `thumbnail_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `status` enum('DRAFT','PENDING','PUBLISHED','HIDDEN','REJECTED') COLLATE utf8mb4_unicode_ci DEFAULT 'DRAFT',
  `is_featured` tinyint(1) DEFAULT '0',
  `view_count` bigint DEFAULT '0',
  `published_at` datetime DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `course_id` bigint DEFAULT NULL,
  `author_id` bigint DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `slug` (`slug`),
  KEY `category_id` (`category_id`),
  KEY `course_id` (`course_id`),
  KEY `author_id` (`author_id`),
  CONSTRAINT `posts_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  CONSTRAINT `posts_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE SET NULL,
  CONSTRAINT `posts_ibfk_3` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `posts`
--

LOCK TABLES `posts` WRITE;
/*!40000 ALTER TABLE `posts` DISABLE KEYS */;
INSERT INTO `posts` VALUES (1,'Lễ ký kết hợp tác đào tạo Chuyển đổi số','le-ky-ket-hop-tac',NULL,'Sự kiện đánh dấu bước ngoặt hợp tác.','Nội dung chi tiết...','PUBLISHED',1,122,'2025-12-06 21:32:56',3,NULL,1,'2025-12-06 21:32:56','2025-12-12 00:56:38'),(2,'Tổng kết khóa đào tạo AI cho nông dân huyện A','tong-ket-khoa-ai-nong-dan',NULL,'Hơn 500 bà con nông dân tham gia.','Nội dung chi tiết...','PUBLISHED',0,88,'2025-12-06 21:32:56',4,5,2,'2025-12-06 21:32:56','2025-12-11 17:41:01'),(3,'Thông báo tuyển sinh khóa Phụ nữ với AI K12','tuyen-sinh-phu-nu-ai-k12',NULL,'Cơ hội cho chị em phụ nữ tiếp cận công nghệ.','Nội dung chi tiết...','PUBLISHED',1,303,'2025-12-06 21:32:56',3,4,3,'2025-12-06 21:32:56','2025-12-12 00:56:40'),(4,'GDVCNS đồng hành cùng chương trình chuyển đổi số quốc gia','gdvcns-dong-hanh-cds',NULL,'Cam kết của công ty với chính phủ.','Nội dung chi tiết...','PUBLISHED',1,501,'2025-12-06 21:32:56',3,NULL,1,'2025-12-06 21:32:56','2025-12-12 00:56:43'),(5,'Tại sao cán bộ xã cần học kỹ năng Prompt Engineering?','tai-sao-can-bo-can-hoc-prompt',NULL,'Bài phân tích chuyên sâu.','Nội dung chi tiết...','PUBLISHED',0,47,'2025-12-06 21:32:56',3,1,4,'2025-12-06 21:32:56','2025-12-11 17:40:08'),(6,'Góc nhìn chuyên gia: Xu hướng EdTech 2026','xu-huong-edtech-2026',NULL,'Phỏng vấn TGĐ Phạm Thị Hà Phương.','Nội dung chi tiết...','DRAFT',0,0,NULL,3,NULL,2,'2025-12-06 21:32:56','2025-12-06 21:32:56'),(7,'Hội thảo: Ứng dụng Big Data trong quản lý hành chính','hoi-thao-big-data',NULL,'Diễn ra tại TP.HCM vào tháng tới.','Nội dung chi tiết...','PENDING',0,0,NULL,3,3,5,'2025-12-06 21:32:56','2025-12-06 21:32:56'),(8,'Dự án Bình dân học vụ số tại tỉnh B','du-an-binh-dan-hoc-vu-so-tinh-b',NULL,'Triển khai thành công tại 10 xã.','Nội dung chi tiết...','PUBLISHED',1,215,'2025-12-06 21:32:56',4,2,1,'2025-12-06 21:32:56','2025-12-12 00:56:51'),(9,'Feedback học viên khóa Cựu chiến binh','feedback-ccb-khoa-ai',NULL,'Những chia sẻ đầy cảm xúc.','Nội dung chi tiết...','PUBLISHED',0,60,'2025-12-06 21:32:56',4,6,3,'2025-12-06 21:32:56','2025-12-06 21:32:56'),(10,'Công nghệ Blockchain trong giáo dục','blockchain-trong-giao-duc',NULL,'Bài nghiên cứu mới.','Nội dung chi tiết...','HIDDEN',0,10,'2025-12-06 21:32:56',3,NULL,2,'2025-12-06 21:32:56','2025-12-06 21:32:56'),(11,'Hoạt động thiện nguyện: Máy tính cho em','may-tinh-cho-em',NULL,'Trao tặng 50 bộ máy tính.','Nội dung chi tiết...','PUBLISHED',1,450,'2025-12-06 21:32:56',3,NULL,4,'2025-12-06 21:32:56','2025-12-06 21:32:56'),(12,'Hướng dẫn đăng ký tài khoản học trực tuyến','huong-dan-dang-ky',NULL,'Các bước đơn giản.','Nội dung chi tiết...','PUBLISHED',0,1001,'2025-12-06 21:32:56',3,NULL,1,'2025-12-06 21:32:56','2025-12-11 17:27:20'),(13,'Thông báo lịch nghỉ lễ Quốc khánh','thong-bao-nghi-le',NULL,'Lịch nghỉ của văn phòng công ty.','Nội dung chi tiết...','PUBLISHED',0,50,'2025-12-06 21:32:56',3,NULL,1,'2025-12-06 21:32:56','2025-12-06 21:32:56'),(14,'Review khóa học AI Nông nghiệp','review-khoa-ai-nong-nghiep',NULL,'Đánh giá từ chuyên gia.','Nội dung chi tiết...','REJECTED',0,0,NULL,3,5,2,'2025-12-06 21:32:56','2025-12-06 21:32:56'),(15,'Cập nhật chính sách bảo mật thông tin','chinh-sach-bao-mat',NULL,'Văn bản mới nhất.','Nội dung chi tiết...','PUBLISHED',0,20,'2025-12-06 21:32:56',3,NULL,1,'2025-12-06 21:32:56','2025-12-06 21:32:56'),(16,'Dự án số hóa tài liệu lưu trữ xã X','du-an-so-hoa-xa-x',NULL,'Hoàn thành 100% tiến độ.','Nội dung chi tiết...','PUBLISHED',0,90,'2025-12-06 21:32:56',4,NULL,3,'2025-12-06 21:32:56','2025-12-06 21:32:56'),(17,'Tuyển dụng chuyên viên Content Marketing','tuyen-dung-content',NULL,'Mức lương hấp dẫn.','Nội dung chi tiết...','PUBLISHED',0,300,'2025-12-06 21:32:56',3,NULL,5,'2025-12-06 21:32:56','2025-12-06 21:32:56'),(18,'Hợp tác quốc tế với đối tác Singapore','hop-tac-singapore',NULL,'Mở rộng thị trường.','Nội dung chi tiết...','PENDING',1,0,NULL,3,NULL,1,'2025-12-06 21:32:56','2025-12-06 21:32:56'),(19,'Chương trình khuyến học tháng 10','khuyen-hoc-thang-10',NULL,'Giảm 20% học phí.','Nội dung chi tiết...','DRAFT',0,0,NULL,3,NULL,2,'2025-12-06 21:32:56','2025-12-06 21:32:56'),(20,'Lễ ra mắt nền tảng học tập số GDVCNS','ra-mat-nen-tang-so',NULL,'Sản phẩm công nghệ mới.','Nội dung chi tiết...','PUBLISHED',1,600,'2025-12-06 21:32:56',4,NULL,1,'2025-12-06 21:32:56','2025-12-06 21:32:56');
/*!40000 ALTER TABLE `posts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_configs`
--

DROP TABLE IF EXISTS `system_configs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_configs` (
  `config_key` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_configs`
--

LOCK TABLES `system_configs` WRITE;
/*!40000 ALTER TABLE `system_configs` DISABLE KEYS */;
INSERT INTO `system_configs` VALUES ('charter_capital','2.000.000.000','Vốn điều lệ (VNĐ)'),('company_name_en','VIET NAM EDUCATION AND DIGITAL TECHNOLOGY JOINT STOCK COMPANY','Tên tiếng Anh'),('company_name_vi','CÔNG TY CỔ PHẦN GIÁO DỤC VÀ CÔNG NGHỆ SỐ VIỆT NAM','Tên công ty tiếng Việt'),('email','haanh12899@gmail.com','Email liên hệ'),('founded_date','2025-09-12','Ngày thành lập'),('headquarters','Số 5A khu BT5, Bán đảo Linh Đàm, Phường Hoàng Liệt, TP. Hà Nội','Trụ sở chính'),('hotline','0926262898','Số điện thoại liên hệ'),('representative','PHẠM THỊ HÀ PHƯƠNG','Tổng giám đốc'),('site_core_values','<div class=\"row\">\n    <div class=\"col-md-6 mb-3\">\n        <h5 class=\"fw-bold text-primary\">Chất lượng</h5>\n        <p>Thước đo hàng đầu trong mọi hoạt động, cam kết giải pháp hiệu quả và bền vững.</p>\n    </div>\n    <div class=\"col-md-6 mb-3\">\n        <h5 class=\"fw-bold text-primary\">Trách nhiệm & Uy tín</h5>\n        <p>Đặt lợi ích khách hàng lên trên hết, minh bạch và tận tâm trong mọi cam kết.</p>\n    </div>\n    <div class=\"col-md-6 mb-3\">\n        <h5 class=\"fw-bold text-primary\">Đổi mới sáng tạo</h5>\n        <p>Tiên phong cập nhật công nghệ mới, khuyến khích tư duy đột phá.</p>\n    </div>\n    <div class=\"col-md-6 mb-3\">\n        <h5 class=\"fw-bold text-primary\">Phát triển con người</h5>\n        <p>Coi con người là trung tâm, nuôi dưỡng tài năng và tạo cơ hội thăng tiến.</p>\n    </div>\n</div>','Giá trị cốt lõi'),('site_intro','<p>Được thành lập vào ngày 12/09/2025, Công ty Cổ phần Giáo dục và Công nghệ số Việt Nam (GDVCNS) ra đời với sứ mệnh trở thành cầu nối giữa giáo dục - công nghệ, đổi mới sáng tạo, góp phần thúc đẩy quá trình chuyển đổi số quốc gia và phát triển nguồn nhân lực chất lượng cao cho xã hội.</p>\n<p>Chúng tôi hoạt động mạnh mẽ trong các lĩnh vực trọng yếu:</p>\n<ul class=\"list-unstyled\">\n    <li><i class=\"fas fa-check text-primary me-2\"></i><strong>Giáo dục & Đào tạo:</strong> Kỹ năng mềm, tin học, ngoại ngữ, truyền thông và đào tạo ngắn hạn.</li>\n    <li><i class=\"fas fa-check text-primary me-2\"></i><strong>CNTT & Chuyển đổi số:</strong> Phát triển phần mềm, tư vấn giải pháp chuyển đổi số, xử lý dữ liệu lớn.</li>\n    <li><i class=\"fas fa-check text-primary me-2\"></i><strong>Nghiên cứu & Phát triển (R&D):</strong> Nghiên cứu khoa học gắn liền thực tiễn.</li>\n    <li><i class=\"fas fa-check text-primary me-2\"></i><strong>Thương mại & Dịch vụ:</strong> Cung cấp thiết bị CNTT, tổ chức sự kiện và truyền thông.</li>\n</ul>','Giới thiệu chung về công ty'),('site_mission','<p>Cam kết mang đến những giải pháp giáo dục tiên tiến và dịch vụ công nghệ chất lượng cao.</p>\n<ul class=\"list-unstyled\">\n    <li><i class=\"fas fa-bullseye text-primary me-2\"></i><strong>Đổi mới giáo dục:</strong> Chương trình đào tạo thực tiễn, chuẩn quốc tế.</li>\n    <li><i class=\"fas fa-bullseye text-primary me-2\"></i><strong>Ứng dụng công nghệ số:</strong> Tối ưu hóa học tập và quản lý bằng AI, Big Data.</li>\n    <li><i class=\"fas fa-bullseye text-primary me-2\"></i><strong>Phát triển nhân lực:</strong> Trang bị kỹ năng thiết yếu cho kỷ nguyên số.</li>\n    <li><i class=\"fas fa-bullseye text-primary me-2\"></i><strong>Đồng hành cộng đồng:</strong> Hỗ trợ khởi nghiệp và thúc đẩy sáng tạo.</li>\n</ul>','Sứ mệnh công ty'),('site_vision','<p>GDVCNS hướng tới trở thành doanh nghiệp hàng đầu trong lĩnh vực giáo dục, đào tạo và công nghệ số tại Việt Nam, đồng thời khẳng định uy tín trên thị trường quốc tế.</p>\n<p>Chúng tôi kiến tạo một hệ sinh thái giáo dục - công nghệ hiện đại, nơi mỗi cá nhân đều có cơ hội tiếp cận tri thức tiên tiến để phát triển bản thân và đóng góp vào sự thịnh vượng của đất nước.</p>','Tầm nhìn chiến lược'),('tax_code','0111215360','Mã số doanh nghiệp');
/*!40000 ALTER TABLE `system_configs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_activities`
--

DROP TABLE IF EXISTS `user_activities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_activities` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `session_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `activity_type` enum('VIEW_POST','VIEW_COURSE','CLICK_CONTACT') COLLATE utf8mb4_unicode_ci NOT NULL,
  `target_id` bigint DEFAULT NULL,
  `ip_address` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_session` (`session_id`),
  KEY `idx_target` (`target_id`,`activity_type`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_activities`
--

LOCK TABLES `user_activities` WRITE;
/*!40000 ALTER TABLE `user_activities` DISABLE KEYS */;
INSERT INTO `user_activities` VALUES (1,'uuid-1111','VIEW_POST',1,'192.168.1.1','2025-12-06 21:32:56'),(2,'uuid-1111','VIEW_COURSE',1,'192.168.1.1','2025-12-06 21:32:56'),(3,'uuid-2222','VIEW_POST',2,'192.168.1.2','2025-12-06 21:32:56'),(4,'uuid-3333','CLICK_CONTACT',NULL,'192.168.1.3','2025-12-06 21:32:56'),(5,'uuid-4444','VIEW_POST',1,'192.168.1.4','2025-12-06 21:32:56'),(6,'uuid-5555','VIEW_COURSE',5,'192.168.1.5','2025-12-06 21:32:56'),(7,'uuid-6666','VIEW_POST',4,'192.168.1.6','2025-12-06 21:32:56'),(8,'uuid-7777','VIEW_COURSE',2,'192.168.1.7','2025-12-06 21:32:56'),(9,'uuid-8888','VIEW_POST',8,'192.168.1.8','2025-12-06 21:32:56'),(10,'uuid-9999','CLICK_CONTACT',NULL,'192.168.1.9','2025-12-06 21:32:56'),(11,'uuid-aaaa','VIEW_POST',1,'192.168.1.10','2025-12-06 21:32:56'),(12,'uuid-bbbb','VIEW_POST',1,'192.168.1.11','2025-12-06 21:32:56'),(13,'uuid-cccc','VIEW_COURSE',6,'192.168.1.12','2025-12-06 21:32:56'),(14,'uuid-dddd','VIEW_POST',20,'192.168.1.13','2025-12-06 21:32:56'),(15,'uuid-eeee','VIEW_COURSE',1,'192.168.1.14','2025-12-06 21:32:56'),(16,'uuid-ffff','CLICK_CONTACT',NULL,'192.168.1.15','2025-12-06 21:32:56'),(17,'uuid-0000','VIEW_POST',11,'192.168.1.16','2025-12-06 21:32:56'),(18,'uuid-1234','VIEW_COURSE',3,'192.168.1.17','2025-12-06 21:32:56'),(19,'uuid-5678','VIEW_POST',5,'192.168.1.18','2025-12-06 21:32:56'),(20,'uuid-9012','VIEW_POST',3,'192.168.1.19','2025-12-06 21:32:56');
/*!40000 ALTER TABLE `user_activities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `full_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` enum('ADMIN','EDITOR') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'EDITOR',
  `status` enum('ACTIVE','LOCKED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','$2a$10$5dsX0rZWwviBM/23RHE9w.HFzraxMUlLOr47QIiZ9/JahuMsaVKci','Administrator','admin@gdvcns.vn','ADMIN','ACTIVE','2025-12-06 22:48:37','2025-12-06 22:48:36.834624'),(2,'editor_nam','$2a$10$eAccJf9.s7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.','Nguyễn Văn Nam','nam.nv@gdvcns.vn','EDITOR','ACTIVE','2025-12-06 21:32:56',NULL),(3,'editor_lan','$2a$10$eAccJf9.s7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.','Trần Thị Lan','lan.tt@gdvcns.vn','EDITOR','ACTIVE','2025-12-06 21:32:56',NULL),(4,'editor_hung','$2a$10$eAccJf9.s7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.','Lê Quang Hùng','hung.lq@gdvcns.vn','EDITOR','ACTIVE','2025-12-06 21:32:56',NULL),(5,'editor_mai','$2a$10$eAccJf9.s7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.','Phạm Thanh Mai','mai.pt@gdvcns.vn','EDITOR','ACTIVE','2025-12-06 21:32:56',NULL),(6,'editor_duc','$2a$10$eAccJf9.s7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.W7.','Hoàng Anh Đức','duc.ha@gdvcns.vn','EDITOR','LOCKED','2025-12-06 21:32:56',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-12  1:22:56
