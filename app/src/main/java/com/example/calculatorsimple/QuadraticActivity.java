package com.example.calculatorsimple;

import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * QuadraticActivity — Giải phương trình bậc 2
 *
 * Giải phương trình dạng: ax² + bx + c = 0
 *
 * Thuật toán:
 * 1. Tính delta (Δ) = b² - 4ac
 * 2. Nếu Δ > 0: 2 nghiệm phân biệt x₁ = (-b + √Δ) / 2a, x₂ = (-b - √Δ) / 2a
 * 3. Nếu Δ = 0: nghiệm kép x = -b / 2a
 * 4. Nếu Δ < 0: phương trình vô nghiệm thực
 *
 * Xử lý đặc biệt:
 * - a = 0, b ≠ 0: phương trình bậc 1, x = -c/b
 * - a = 0, b = 0, c = 0: vô số nghiệm
 * - a = 0, b = 0, c ≠ 0: vô nghiệm
 */
public class QuadraticActivity extends AppCompatActivity {

    // ============================================
    // KHAI BÁO BIẾN
    // ============================================

    /** Ô nhập hệ số a (hệ số bậc 2) */
    private EditText etA;
    /** Ô nhập hệ số b (hệ số bậc 1) */
    private EditText etB;
    /** Ô nhập hệ số c (hằng số) */
    private EditText etC;

    /** Nút giải phương trình */
    private Button btnSolve;
    /** Nút xoá form (reset tất cả ô nhập) */
    private Button btnClearForm;
    /** Nút quay lại màn hình chính */
    private Button btnBack;

    /** TextView hiển thị kết quả giải phương trình */
    private TextView tvResult;

    // ============================================
    // KHỞI TẠO
    // ============================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quadratic);

        // Ánh xạ các view từ layout
        etA = findViewById(R.id.etA);
        etB = findViewById(R.id.etB);
        etC = findViewById(R.id.etC);
        btnSolve = findViewById(R.id.btnSolve);
        btnClearForm = findViewById(R.id.btnClearForm);
        btnBack = findViewById(R.id.btnBack);
        tvResult = findViewById(R.id.tvQuadraticResult);

        // === SỰ KIỆN NÚT GIẢI ===
        btnSolve.setOnClickListener(v -> solve());

        // === SỰ KIỆN NÚT XOÁ FORM ===
        // Reset tất cả ô nhập và kết quả về trạng thái ban đầu
        btnClearForm.setOnClickListener(v -> clearForm());

        // === SỰ KIỆN NÚT QUAY LẠI ===
        // Đóng Activity hiện tại, quay về MainActivity
        btnBack.setOnClickListener(v -> finish());
    }

    // ============================================
    // GIẢI PHƯƠNG TRÌNH BẬC 2
    // ============================================

    /**
     * Giải phương trình ax² + bx + c = 0.
     *
     * Quy trình:
     * 1. Đọc và kiểm tra input (3 hệ số a, b, c)
     * 2. Xử lý trường hợp đặc biệt (a = 0)
     * 3. Tính delta và tìm nghiệm
     * 4. Hiển thị kết quả với animation
     */
    private void solve() {
        // Bước 1: Đọc dữ liệu từ các ô nhập
        String sA = etA.getText().toString().trim();
        String sB = etB.getText().toString().trim();
        String sC = etC.getText().toString().trim();

        // Bước 2: Kiểm tra ô nhập có trống không
        if (sA.isEmpty() || sB.isEmpty() || sC.isEmpty()) {
            Toast.makeText(this, "⚠️ Vui lòng nhập đầy đủ hệ số a, b, c", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Bước 3: Chuyển đổi chuỗi thành số thực
            double a = Double.parseDouble(sA);
            double b = Double.parseDouble(sB);
            double c = Double.parseDouble(sC);

            // Bước 4: Xử lý trường hợp a = 0 (KHÔNG phải phương trình bậc 2)
            if (a == 0) {
                handleLinearCase(b, c);
                return;
            }

            // Bước 5: Tính delta (Δ = b² - 4ac)
            double delta = b * b - 4 * a * c;

            // Bước 6: Xây dựng chuỗi kết quả
            StringBuilder resultText = new StringBuilder();

            // Hiển thị phương trình
            resultText.append("📐 Phương trình: ");
            resultText.append(formatEquation(a, b, c));
            resultText.append("\n\n");

            // Hiển thị delta
            resultText.append("Δ = b² - 4ac = ").append(format(delta)).append("\n\n");

            if (delta > 0) {
                // ===== TRƯỜNG HỢP 1: Δ > 0 — 2 nghiệm phân biệt =====
                double x1 = (-b + Math.sqrt(delta)) / (2 * a);
                double x2 = (-b - Math.sqrt(delta)) / (2 * a);
                resultText.append("✅ Δ > 0: Có 2 nghiệm phân biệt\n\n");
                resultText.append("x₁ = (-b + √Δ) / 2a = ").append(format(x1)).append("\n");
                resultText.append("x₂ = (-b - √Δ) / 2a = ").append(format(x2));

            } else if (delta == 0) {
                // ===== TRƯỜNG HỢP 2: Δ = 0 — Nghiệm kép =====
                double x = -b / (2 * a);
                resultText.append("✅ Δ = 0: Có nghiệm kép\n\n");
                resultText.append("x = -b / 2a = ").append(format(x));

            } else {
                // ===== TRƯỜNG HỢP 3: Δ < 0 — Vô nghiệm thực =====
                resultText.append("❌ Δ < 0: Phương trình vô nghiệm thực");
            }

            // Bước 7: Hiển thị kết quả với animation fade-in
            showResultWithAnimation(resultText.toString());

        } catch (NumberFormatException e) {
            Toast.makeText(this, "⚠️ Lỗi: Hệ số phải là số hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    // ============================================
    // XỬ LÝ TRƯỜNG HỢP ĐẶC BIỆT
    // ============================================

    /**
     * Xử lý trường hợp a = 0 (phương trình bậc 1 hoặc vô nghĩa).
     *
     * Khi a = 0, phương trình trở thành: bx + c = 0
     * - Nếu b ≠ 0: x = -c/b (phương trình bậc 1)
     * - Nếu b = 0 và c = 0: 0 = 0 → vô số nghiệm
     * - Nếu b = 0 và c ≠ 0: c = 0 → vô nghiệm
     *
     * @param b Hệ số bậc 1
     * @param c Hằng số
     */
    private void handleLinearCase(double b, double c) {
        String result;
        if (b == 0) {
            if (c == 0) {
                result = "⚠️ a = 0, b = 0, c = 0\n\nPhương trình: 0 = 0\n→ Phương trình có vô số nghiệm";
            } else {
                result = "⚠️ a = 0, b = 0, c = " + format(c) + "\n\nPhương trình: " + format(c) + " = 0\n→ Phương trình vô nghiệm";
            }
        } else {
            double x = -c / b;
            result = "⚠️ a = 0 → Đây là phương trình bậc 1\n\n"
                    + "Phương trình: " + format(b) + "x + " + format(c) + " = 0\n\n"
                    + "→ x = " + format(x);
        }
        showResultWithAnimation(result);
    }

    // ============================================
    // HIỂN THỊ & ANIMATION
    // ============================================

    /**
     * Hiển thị kết quả với hiệu ứng fade-in.
     * Kết quả sẽ từ từ hiện ra để tạo cảm giác mượt mà.
     *
     * @param text Nội dung kết quả cần hiển thị
     */
    private void showResultWithAnimation(String text) {
        // Tạo animation fade-in (từ trong suốt → hiện rõ)
        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(400); // 400ms
        fadeIn.setFillAfter(true); // Giữ trạng thái sau animation

        tvResult.setText(text);
        tvResult.startAnimation(fadeIn);
    }

    // ============================================
    // XOÁ FORM
    // ============================================

    /**
     * Xoá toàn bộ dữ liệu trong form:
     * - 3 ô nhập hệ số a, b, c
     * - Vùng hiển thị kết quả
     * - Đặt focus về ô nhập a
     */
    private void clearForm() {
        etA.setText("");
        etB.setText("");
        etC.setText("");
        tvResult.setText("Chưa có kết quả");
        etA.requestFocus(); // Đặt con trỏ về ô nhập a
    }

    // ============================================
    // ĐỊNH DẠNG SỐ & PHƯƠNG TRÌNH
    // ============================================

    /**
     * Định dạng số cho đẹp:
     * - Số nguyên: 5.0 → "5"
     * - Số thập phân: giữ tối đa 4 chữ số (3.14159 → "3.1416")
     *
     * @param d Giá trị số cần format
     * @return Chuỗi đã format
     */
    private String format(double d) {
        if (d == Math.floor(d) && !Double.isInfinite(d) && Math.abs(d) < 1e15) {
            return String.format("%d", (long) d);
        }
        // Format 4 chữ số thập phân, bỏ số 0 thừa
        String formatted = String.format("%.4f", d);
        formatted = formatted.replaceAll("0+$", "").replaceAll("\\.$", "");
        return formatted;
    }

    /**
     * Tạo chuỗi hiển thị phương trình dạng đẹp.
     * Ví dụ: a=1, b=-3, c=2 → "x² - 3x + 2 = 0"
     *
     * @param a Hệ số bậc 2
     * @param b Hệ số bậc 1
     * @param c Hằng số
     * @return Chuỗi dạng "ax² + bx + c = 0"
     */
    private String formatEquation(double a, double b, double c) {
        StringBuilder eq = new StringBuilder();

        // Phần ax²
        if (a == 1) eq.append("x²");
        else if (a == -1) eq.append("-x²");
        else eq.append(format(a)).append("x²");

        // Phần bx
        if (b > 0) eq.append(" + ").append(b == 1 ? "" : format(b)).append("x");
        else if (b < 0) eq.append(" - ").append(b == -1 ? "" : format(Math.abs(b))).append("x");

        // Phần c
        if (c > 0) eq.append(" + ").append(format(c));
        else if (c < 0) eq.append(" - ").append(format(Math.abs(c)));

        eq.append(" = 0");
        return eq.toString();
    }
}