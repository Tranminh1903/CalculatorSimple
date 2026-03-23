package com.example.calculatorsimple;

import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * FractionActivity — Chuyển phân số sang số thập phân
 *
 * Chức năng:
 * - Nhập tử số và mẫu số
 * - Tính kết quả thập phân: tử số ÷ mẫu số
 * - Xử lý lỗi: mẫu số = 0, input trống
 *
 * Ví dụ:
 *   1/2 = 0.5
 *   1/3 = 0.3333333333
 *   22/7 ≈ 3.1428571429 (xấp xỉ π)
 */
public class FractionActivity extends AppCompatActivity {

    // Ô nhập tử số và mẫu số
    private EditText etNumerator, etDenominator;
    // Nút chuyển đổi, xoá form, quay lại
    private Button btnConvert, btnClearForm, btnBack;
    // Vùng hiển thị kết quả
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fraction);

        // Ánh xạ view
        etNumerator = findViewById(R.id.etNumerator);
        etDenominator = findViewById(R.id.etDenominator);
        btnConvert = findViewById(R.id.btnConvert);
        btnClearForm = findViewById(R.id.btnClearForm);
        btnBack = findViewById(R.id.btnBack);
        tvResult = findViewById(R.id.tvFractionResult);

        // Sự kiện click
        btnConvert.setOnClickListener(v -> convert());
        btnClearForm.setOnClickListener(v -> clearForm());
        btnBack.setOnClickListener(v -> finish());
    }

    /**
     * Chuyển đổi phân số sang thập phân.
     * Tử số ÷ Mẫu số = Kết quả thập phân
     */
    private void convert() {
        String sNum = etNumerator.getText().toString().trim();
        String sDen = etDenominator.getText().toString().trim();

        // Kiểm tra input trống
        if (sNum.isEmpty() || sDen.isEmpty()) {
            Toast.makeText(this, "⚠️ Vui lòng nhập đầy đủ tử số và mẫu số",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double numerator = Double.parseDouble(sNum);
            double denominator = Double.parseDouble(sDen);

            // Kiểm tra mẫu số = 0
            if (denominator == 0) {
                showResult("❌ Lỗi: Mẫu số không được bằng 0");
                return;
            }

            double result = numerator / denominator;

            // Xây dựng chuỗi kết quả
            StringBuilder sb = new StringBuilder();
            sb.append("📐 Phân số: ").append(formatNum(numerator))
                    .append(" / ").append(formatNum(denominator)).append("\n\n");
            sb.append("✅ Kết quả thập phân:\n");
            sb.append(formatResult(result));

            // Nếu là phân số tối giản, hiển thị thêm
            if (numerator == Math.floor(numerator) && denominator == Math.floor(denominator)) {
                long num = (long) numerator;
                long den = (long) denominator;
                long gcd = gcd(Math.abs(num), Math.abs(den));
                if (gcd > 1) {
                    sb.append("\n\n📌 Rút gọn: ")
                            .append(num / gcd).append("/").append(den / gcd)
                            .append(" = ").append(formatResult(result));
                }
            }

            showResult(sb.toString());

        } catch (NumberFormatException e) {
            Toast.makeText(this, "⚠️ Lỗi: Vui lòng nhập số hợp lệ",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Tính ước chung lớn nhất (GCD) bằng thuật toán Euclid.
     */
    private long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    /** Hiển thị kết quả với animation fade-in */
    private void showResult(String text) {
        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(400);
        fadeIn.setFillAfter(true);
        tvResult.setText(text);
        tvResult.startAnimation(fadeIn);
    }

    /** Xoá form về trạng thái ban đầu */
    private void clearForm() {
        etNumerator.setText("");
        etDenominator.setText("");
        tvResult.setText("Chưa có kết quả");
        etNumerator.requestFocus();
    }

    /** Format số: bỏ .0 nếu là số nguyên */
    private String formatNum(double d) {
        if (d == Math.floor(d) && !Double.isInfinite(d) && Math.abs(d) < 1e15) {
            return String.format("%d", (long) d);
        }
        return String.valueOf(d);
    }

    /** Format kết quả: tối đa 10 chữ số thập phân, bỏ 0 thừa */
    private String formatResult(double d) {
        if (d == Math.floor(d) && !Double.isInfinite(d) && Math.abs(d) < 1e15) {
            return String.format("%d", (long) d);
        }
        String formatted = String.format("%.10f", d);
        formatted = formatted.replaceAll("0+$", "").replaceAll("\\.$", "");
        return formatted;
    }
}
