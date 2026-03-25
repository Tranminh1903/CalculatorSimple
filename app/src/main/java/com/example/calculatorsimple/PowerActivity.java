package com.example.calculatorsimple;

import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * PowerActivity — Tính luỹ thừa & số mũ
 *
 * Chức năng:
 * - Nhập cơ số (a) và số mũ (n)
 * - Tính aⁿ = a × a × ... × a (n lần)
 * - Xử lý lỗi: 0^0, số âm mũ thập phân, overflow
 *
 * Ví dụ:
 *   2³ = 8
 *   5² = 25
 *   10⁻² = 0.01
 *   2¹⁰ = 1024
 */
public class PowerActivity extends AppCompatActivity {

    // Ô nhập cơ số và số mũ
    private EditText etBase, etExponent;
    // Nút tính, xoá, quay lại
    private Button btnCalculate, btnClearForm, btnBack;
    // Vùng hiển thị kết quả
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power);

        // Ánh xạ view
        etBase = findViewById(R.id.etBase);
        etExponent = findViewById(R.id.etExponent);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnClearForm = findViewById(R.id.btnClearForm);
        btnBack = findViewById(R.id.btnBack);
        tvResult = findViewById(R.id.tvPowerResult);

        // Sự kiện click
        btnCalculate.setOnClickListener(v -> calculate());
        btnClearForm.setOnClickListener(v -> clearForm());
        btnBack.setOnClickListener(v -> finish());
    }

    /**
     * Tính luỹ thừa aⁿ.
     * Sử dụng Math.pow(base, exponent) để tính.
     */
    private void calculate() {
        String sBase = etBase.getText().toString().trim();
        String sExp = etExponent.getText().toString().trim();

        // Kiểm tra input trống
        if (sBase.isEmpty() || sExp.isEmpty()) {
            Toast.makeText(this, "⚠️ Vui lòng nhập đầy đủ cơ số và số mũ",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double base = Double.parseDouble(sBase);
            double exponent = Double.parseDouble(sExp);

            // Tính luỹ thừa
            double result = Math.pow(base, exponent);

            // Xây dựng chuỗi kết quả
            StringBuilder sb = new StringBuilder();

            // Kiểm tra kết quả hợp lệ
            if (Double.isNaN(result)) {
                sb.append("❌ Lỗi: Không thể tính (VD: số âm mũ thập phân)");
            } else if (Double.isInfinite(result)) {
                sb.append("❌ Lỗi: Kết quả quá lớn (tràn số)");
            } else {
                sb.append("✅ Kết quả:\n");
                sb.append(formatNum(base)).append("^").append(formatNum(exponent));
                sb.append(" = ").append(formatResult(result));
            }

            showResult(sb.toString());

        } catch (NumberFormatException e) {
            Toast.makeText(this, "⚠️ Lỗi: Vui lòng nhập số hợp lệ",
                    Toast.LENGTH_SHORT).show();
        }
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
        etBase.setText("");
        etExponent.setText("");
        tvResult.setText("Chưa có kết quả");
        etBase.requestFocus();
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
