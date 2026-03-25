package com.example.calculatorsimple;

import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PowerActivity extends AppCompatActivity {

    private EditText etBase, etExponent;
    private Button btnCalculate, btnClearForm, btnBack;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power);

        etBase = findViewById(R.id.etBase);
        etExponent = findViewById(R.id.etExponent);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnClearForm = findViewById(R.id.btnClearForm);
        btnBack = findViewById(R.id.btnBack);
        tvResult = findViewById(R.id.tvPowerResult);

        btnCalculate.setOnClickListener(v -> calculate());
        btnClearForm.setOnClickListener(v -> clearForm());
        btnBack.setOnClickListener(v -> finish());
    }

    private void calculate() {
        String sBase = etBase.getText().toString().trim();
        String sExp = etExponent.getText().toString().trim();

        if (sBase.isEmpty() || sExp.isEmpty()) {
            Toast.makeText(this, "⚠️ Vui lòng nhập đầy đủ cơ số và số mũ",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double base = Double.parseDouble(sBase);
            double exponent = Double.parseDouble(sExp);

            double result = Math.pow(base, exponent);

            StringBuilder sb = new StringBuilder();

            if (Double.isNaN(result)) {
                sb.append("Lỗi: Không thể tính (VD: số âm mũ thập phân)");
            } else if (Double.isInfinite(result)) {
                sb.append("Lỗi: Kết quả quá lớn (tràn số)");
            } else {
                sb.append("Kết quả:\n");
                sb.append(formatNum(base)).append("^").append(formatNum(exponent));
                sb.append(" = ").append(formatResult(result));
            }

            showResult(sb.toString());

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Lỗi: Vui lòng nhập số hợp lệ",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void showResult(String text) {
        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(400);
        fadeIn.setFillAfter(true);
        tvResult.setText(text);
        tvResult.startAnimation(fadeIn);
    }

    private void clearForm() {
        etBase.setText("");
        etExponent.setText("");
        tvResult.setText("Chưa có kết quả");
        etBase.requestFocus();
    }

    private String formatNum(double d) {
        if (d == Math.floor(d) && !Double.isInfinite(d) && Math.abs(d) < 1e15) {
            return String.format("%d", (long) d);
        }
        return String.valueOf(d);
    }

    private String formatResult(double d) {
        if (d == Math.floor(d) && !Double.isInfinite(d) && Math.abs(d) < 1e15) {
            return String.format("%d", (long) d);
        }
        String formatted = String.format("%.10f", d);
        formatted = formatted.replaceAll("0+$", "").replaceAll("\\.$", "");
        return formatted;
    }
}
