# 🧮 Lộ Trình Xây Dựng Ứng Dụng Máy Tính (Android)

## Tổng Quan

Xây dựng ứng dụng máy tính trên Android với các chức năng cơ bản và nâng cao, bao gồm các phép toán số học, luỹ thừa, và giải phương trình bậc 2.

---

## Giai Đoạn 1: Thiết Kế Giao Diện (UI/Layout)

### 1.1. Màn hình hiển thị (Display)
- [x] `TextView` hiển thị biểu thức đang nhập
- [x] `TextView` hiển thị kết quả tính toán

### 1.2. Bàn phím số
- [x] Các nút số từ `0` đến `9`
- [x] Bố cục dạng lưới (GridLayout)

### 1.3. Nút xoá
- [x] **Nút xoá 1 số phía trước** (⌫) — màu **cam** 🟠
- [x] **Nút xoá tất cả** (C) — màu **vàng** 🟡

### 1.4. Nút phép toán cơ bản
- [x] Cộng (`+`)
- [x] Trừ (`-`)
- [x] Nhân (`×`)
- [x] Chia (`÷`)
- [x] Nút bằng (`=`)

### 1.5. Nút chức năng nâng cao
- [x] Nút **thập phân** (`.`)
- [x] Nút **luỹ thừa** (`^`)
- [x] Nút **số mũ** (`xⁿ`) — tích hợp qua nút `^`
- [x] Nút **giải phương trình bậc 2** (`PT Bậc 2`)

---

## Giai Đoạn 2: Xử Lý Logic Phép Toán Cơ Bản

### 2.1. Nhập liệu
- [x] Xử lý nhập số liên tiếp (ghép chữ số)
- [x] Xử lý nhập phép toán (+, -, ×, ÷)
- [x] Ngăn chặn nhập sai định dạng (ví dụ: `++`, `÷÷`)

### 2.2. Xoá
- [x] **Xoá 1 ký tự** (Backspace) — xoá ký tự cuối cùng trong biểu thức
- [x] **Xoá tất cả** (Clear) — đưa màn hình về trạng thái ban đầu (`0`)

### 2.3. Tính kết quả
- [x] Phân tích biểu thức (parsing) — Recursive Descent Parser
- [x] Thực hiện phép toán theo thứ tự ưu tiên (nhân/chia trước, cộng/trừ sau)
- [x] Hiển thị kết quả
- [x] Preview realtime khi nhập

---

## Giai Đoạn 3: Hỗ Trợ Số Thập Phân

- [x] Cho phép nhập dấu `.` để tạo số thập phân
- [x] Ngăn chặn nhập nhiều dấu `.` trong cùng 1 số (ví dụ: `3..5`)
- [x] Tính toán chính xác với số thập phân
- [x] Hiển thị kết quả thập phân gọn gàng (bỏ `.0` nếu là số nguyên)

---

## Giai Đoạn 4: Luỹ Thừa & Số Mũ

### 4.1. Luỹ thừa
- [x] Hỗ trợ phép tính `a ^ b` (a mũ b)
- [x] Ví dụ: `2 ^ 3 = 8`

### 4.2. Số mũ
- [x] Hỗ trợ nhập số mũ (ví dụ: `xⁿ`)
- [x] Tích hợp vào engine tính toán

---

## Giai Đoạn 5: Giải Phương Trình Bậc 2

### 5.1. Giao diện nhập
- [x] Mở Activity riêng khi nhấn nút "PT Bậc 2"
- [x] 3 ô nhập: hệ số `a`, `b`, `c`
- [x] Nút "Giải" và nút "Xoá"

### 5.2. Logic giải phương trình
- [x] Tính delta: `Δ = b² - 4ac`
- [x] **Δ > 0**: 2 nghiệm phân biệt `x₁`, `x₂`
- [x] **Δ = 0**: 1 nghiệm kép `x = -b / 2a`
- [x] **Δ < 0**: Vô nghiệm (thông báo cho người dùng)
- [x] Xử lý trường hợp `a = 0` (không phải phương trình bậc 2)

### 5.3. Hiển thị kết quả
- [x] Hiển thị delta và các nghiệm
- [x] Định dạng kết quả dễ đọc + animation fade-in

---

## Giai Đoạn 6: Xử Lý Lỗi & Hoàn Thiện

- [x] Chia cho 0 → hiển thị thông báo "Lỗi: Chia cho 0"
- [x] Biểu thức không hợp lệ → thông báo lỗi
- [x] Tràn số (overflow) → xử lý "Lỗi: Số quá lớn"
- [ ] Kiểm tra và test toàn bộ chức năng (cần build trên Android Studio)
- [ ] Tối ưu giao diện cho nhiều kích thước màn hình

---

## Tóm Tắt Các File

| File | Mô tả |
|------|--------|
| `activity_main.xml` | Layout giao diện chính (GridLayout 5x4) |
| `activity_quadratic.xml` | Layout màn hình giải PT bậc 2 (gradient header) |
| `MainActivity.java` | Xử lý sự kiện giao diện + preview realtime |
| `CalculatorEngine.java` | Recursive Descent Parser + xử lý lỗi |
| `QuadraticActivity.java` | Logic giải PT bậc 2 + animation |
| `bg_edit_text.xml` | Drawable bo tròn cho EditText |
| `bg_header_gradient.xml` | Drawable gradient cho header |
| `bg_result_card.xml` | Drawable card cho vùng kết quả |

---

## Tiến Độ

| Giai đoạn | Trạng thái |
|-----------|------------|
| 1. Thiết kế giao diện | ✅ Hoàn thành |
| 2. Logic phép toán cơ bản | ✅ Hoàn thành |
| 3. Số thập phân | ✅ Hoàn thành |
| 4. Luỹ thừa & Số mũ | ✅ Hoàn thành |
| 5. Phương trình bậc 2 | ✅ Hoàn thành |
| 6. Xử lý lỗi & Hoàn thiện | 🔶 Cần test trên thiết bị |
