# 🧮 Calculator Simple (Máy Tính Android)

Một ứng dụng máy tính đa năng trên nền tảng Android, cung cấp giao diện trực quan và tích hợp nhiều tính năng tính toán từ cơ bản đến nâng cao. Ứng dụng được xây dựng hoàn toàn từ đầu (from scratch) với thuật toán phân tích biểu thức toán học chuyên dụng.

## 🚀 Tính năng nổi bật

- **Phép tính số học cơ bản**: Cộng (`+`), Trừ (`-`), Nhân (`×`), Chia (`÷`).
- **Tính toán thời gian thực**: Hiển thị trước kết quả (preview line) ngay khi bạn đang nhập biểu thức.
- **Hỗ trợ số thập phân**: Tính toán chính xác với các số thập phân và xử lý dấu phẩy động.
- **Luỹ thừa & Số mũ**: Hỗ trợ tính toán luỹ thừa (`a^b`) cho các biểu thức nâng cao.
- **Giải Phương Trình Bậc 2**: Công cụ tiện ích chuyên dụng nhập hệ số $a, b, c$ và tìm nghiệm tự động với đầy đủ các trường hợp (vô nghiệm, nghiệm kép, hai nghiệm phân biệt).
- **Phân số**: Tính toán và rút gọn biểu thức phân số (thông qua `FractionActivity`).
- **Giao diện hiện đại**: Layout bàn phím dạng lưới khoa học, nút bấm chức năng phối màu tạo điểm nhấn (nút xoá C màu vàng, nút xoá lùi màu cam). Hỗ trợ animation mượt mà.
- **Xử lý lỗi thông minh**: Tự động thông báo và ngăn lỗi như "Chia cho 0", sai cú pháp, hoặc số quá lớn.

## 🛠 Công nghệ sử dụng

- **Nền tảng**: Android SDK (Min SDK 24, Target SDK 36)
- **Ngôn ngữ**: Java / Android XML
- **Cốt lõi thuật toán toán học**: Sử dụng **Recursive Descent Parser** (Phân tích cú pháp đệ quy xuống) trong `CalculatorEngine` để đánh giá ưu tiên và giải quyết chuỗi phép tính.

## 📂 Thành phần chính trong dự án

- **`MainActivity.java`**: Điều khiển logic giao diện máy tính cơ bản, tiếp nhận phím bấm và cập nhật hiển thị biểu thức.
- **`CalculatorEngine.java`**: Bộ máy xử lý thuật toán phân tích chuỗi biểu thức để trả về kết quả chuẩn xác.
- **`QuadraticActivity.java`**: Activity giải phương trình bậc 2 kèm các animation giao diện.
- **Layouts (`XML`)**: Sử dụng ConstraintLayout, GridLayout linh hoạt (`activity_main.xml`, `activity_quadratic.xml`, `activity_fraction.xml`...).

## 💻 Cài đặt & Chạy ứng dụng

1. Clone repository này về máy tính:
   ```bash
   git clone <repository-url>
   ```
2. Mở đồ án thư mục `CalculatorSimple` bằng **Android Studio**.
3. Chờ đồng bộ Gradle (Sync Project with Gradle Files).
4. Nhấn **Run** (mũi tên xanh lá hoặc `Shift + F10`) để cài đặt và chạy ứng dụng lên máy ảo Simulator hoặc thiết bị Android thật.

## 📝 Roadmap dự án
Quá trình xây dựng app đã trải qua nhiều giai đoạn như thiết kế UI lõi, viết Engine giải chuỗi, xử lý Số thập phân, và cài đặt các chức năng nâng cao (Luỹ thừa, PT Bậc 2). Có thể xem chi tiết tài liệu [ROADMAP.md](./ROADMAP.md) để biết thêm thông tin.

---
