# 🧮 Lộ Trình Xây Dựng Ứng Dụng Máy Tính (Android)

## Tổng Quan

Xây dựng ứng dụng máy tính trên Android với các chức năng cơ bản và nâng cao, bao gồm các phép toán số học, luỹ thừa, và giải phương trình bậc 2.

---

## Giai Đoạn 1: Thiết Kế Giao Diện (UI/Layout)

### 1.1. Màn hình hiển thị (Display)
- [ ] `TextView` hiển thị biểu thức đang nhập
- [ ] `TextView` hiển thị kết quả tính toán

### 1.2. Bàn phím số
- [ ] Các nút số từ `0` đến `9`
- [ ] Bố cục dạng lưới (GridLayout)

### 1.3. Nút xoá
- [ ] **Nút xoá 1 số phía trước** (⌫) — màu **cam** 🟠
- [ ] **Nút xoá tất cả** (C) — màu **vàng** 🟡

### 1.4. Nút phép toán cơ bản
- [ ] Cộng (`+`)
- [ ] Trừ (`-`)
- [ ] Nhân (`×`)
- [ ] Chia (`÷`)
- [ ] Nút bằng (`=`)

### 1.5. Nút chức năng nâng cao
- [ ] Nút **thập phân** (`.`)
- [ ] Nút **luỹ thừa** (`^`)
- [ ] Nút **số mũ** (`xⁿ`)
- [ ] Nút **giải phương trình bậc 2** (`ax²+bx+c=0`)

---

## Giai Đoạn 2: Xử Lý Logic Phép Toán Cơ Bản

### 2.1. Nhập liệu
- [ ] Xử lý nhập số liên tiếp (ghép chữ số)
- [ ] Xử lý nhập phép toán (+, -, ×, ÷)
- [ ] Ngăn chặn nhập sai định dạng (ví dụ: `++`, `÷÷`)

### 2.2. Xoá
- [ ] **Xoá 1 ký tự** (Backspace) — xoá ký tự cuối cùng trong biểu thức
- [ ] **Xoá tất cả** (Clear) — đưa màn hình về trạng thái ban đầu (`0`)

### 2.3. Tính kết quả
- [ ] Phân tích biểu thức (parsing)
- [ ] Thực hiện phép toán theo thứ tự ưu tiên (nhân/chia trước, cộng/trừ sau)
- [ ] Hiển thị kết quả

---

## Giai Đoạn 3: Hỗ Trợ Số Thập Phân

- [ ] Cho phép nhập dấu `.` để tạo số thập phân
- [ ] Ngăn chặn nhập nhiều dấu `.` trong cùng 1 số (ví dụ: `3..5`)
- [ ] Tính toán chính xác với số thập phân
- [ ] Hiển thị kết quả thập phân gọn gàng (bỏ `.0` nếu là số nguyên)

---

## Giai Đoạn 4: Luỹ Thừa & Số Mũ

### 4.1. Luỹ thừa
- [ ] Hỗ trợ phép tính `a ^ b` (a mũ b)
- [ ] Ví dụ: `2 ^ 3 = 8`

### 4.2. Số mũ
- [ ] Hỗ trợ nhập số mũ (ví dụ: `xⁿ`)
- [ ] Tích hợp vào engine tính toán

---

## Giai Đoạn 5: Giải Phương Trình Bậc 2

### 5.1. Giao diện nhập
- [ ] Mở dialog/màn hình phụ khi nhấn nút "PT Bậc 2"
- [ ] 3 ô nhập: hệ số `a`, `b`, `c`
- [ ] Nút "Giải"

### 5.2. Logic giải phương trình
- [ ] Tính delta: `Δ = b² - 4ac`
- [ ] **Δ > 0**: 2 nghiệm phân biệt `x₁`, `x₂`
- [ ] **Δ = 0**: 1 nghiệm kép `x = -b / 2a`
- [ ] **Δ < 0**: Vô nghiệm (thông báo cho người dùng)
- [ ] Xử lý trường hợp `a = 0` (không phải phương trình bậc 2)

### 5.3. Hiển thị kết quả
- [ ] Hiển thị delta và các nghiệm
- [ ] Định dạng kết quả dễ đọc

---

## Giai Đoạn 6: Xử Lý Lỗi & Hoàn Thiện

- [ ] Chia cho 0 → hiển thị thông báo "Error"
- [ ] Biểu thức không hợp lệ → thông báo lỗi
- [ ] Tràn số (overflow) → xử lý graceful
- [ ] Kiểm tra và test toàn bộ chức năng
- [ ] Tối ưu giao diện cho nhiều kích thước màn hình

---

## Tóm Tắt Các File Cần Tạo

| File | Mô tả |
|------|--------|
| `activity_main.xml` | Layout giao diện chính |
| `activity_quadratic.xml` | Layout màn hình giải PT bậc 2 |
| `MainActivity.java` | Xử lý sự kiện giao diện chính |
| `CalculatorEngine.java` | Engine tính toán biểu thức |
| `QuadraticSolver.java` | Logic giải phương trình bậc 2 |

---

## Tiến Độ

| Giai đoạn | Trạng thái |
|-----------|------------|
| 1. Thiết kế giao diện | ⬜ Chưa bắt đầu |
| 2. Logic phép toán cơ bản | ⬜ Chưa bắt đầu |
| 3. Số thập phân | ⬜ Chưa bắt đầu |
| 4. Luỹ thừa & Số mũ | ⬜ Chưa bắt đầu |
| 5. Phương trình bậc 2 | ⬜ Chưa bắt đầu |
| 6. Xử lý lỗi & Hoàn thiện | ⬜ Chưa bắt đầu |
