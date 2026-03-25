package com.example.calculatorsimple;

import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class QuadraticActivity extends AppCompatActivity {



    private EditText etA;

    private EditText etB;

    private EditText etC;


    private Button btnSolve;

    private Button btnClearForm;

    private Button btnBack;

    private TextView tvResult;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quadratic);


        etA = findViewById(R.id.etA);
        etB = findViewById(R.id.etB);
        etC = findViewById(R.id.etC);
        btnSolve = findViewById(R.id.btnSolve);
        btnClearForm = findViewById(R.id.btnClearForm);
        btnBack = findViewById(R.id.btnBack);
        tvResult = findViewById(R.id.tvQuadraticResult);


        btnSolve.setOnClickListener(v -> solve());


        btnClearForm.setOnClickListener(v -> clearForm());


        btnBack.setOnClickListener(v -> finish());
    }


    private void solve() {

        String sA = etA.getText().toString().trim();
        String sB = etB.getText().toString().trim();
        String sC = etC.getText().toString().trim();


        if (sA.isEmpty() || sB.isEmpty() || sC.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ hệ số a, b, c", Toast.LENGTH_SHORT).show();
            return;
        }

        try {

            double a = Double.parseDouble(sA);
            double b = Double.parseDouble(sB);
            double c = Double.parseDouble(sC);


            if (a == 0) {
                handleLinearCase(b, c);
                return;
            }


            double delta = b * b - 4 * a * c;


            StringBuilder resultText = new StringBuilder();


            resultText.append("📐 Phương trình: ");
            resultText.append(formatEquation(a, b, c));
            resultText.append("\n\n");


            resultText.append("Δ = b² - 4ac = ").append(format(delta)).append("\n\n");

            if (delta > 0) {

                double x1 = (-b + Math.sqrt(delta)) / (2 * a);
                double x2 = (-b - Math.sqrt(delta)) / (2 * a);
                resultText.append(" Δ > 0: Có 2 nghiệm phân biệt\n\n");
                resultText.append("x₁ = (-b + √Δ) / 2a = ").append(format(x1)).append("\n");
                resultText.append("x₂ = (-b - √Δ) / 2a = ").append(format(x2));

            } else if (delta == 0) {

                double x = -b / (2 * a);
                resultText.append(" Δ = 0: Có nghiệm kép\n\n");
                resultText.append("x = -b / 2a = ").append(format(x));

            } else {

                resultText.append(" Δ < 0: Phương trình vô nghiệm thực");
            }


            showResultWithAnimation(resultText.toString());

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Lỗi: Hệ số phải là số hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    // ============================================
    // XỬ LÝ TRƯỜNG HỢP ĐẶC BIỆT
    // ============================================


    private void handleLinearCase(double b, double c) {
        String result;
        if (b == 0) {
            if (c == 0) {
                result = " a = 0, b = 0, c = 0\n\nPhương trình: 0 = 0\n→ Phương trình có vô số nghiệm";
            } else {
                result = " a = 0, b = 0, c = " + format(c) + "\n\nPhương trình: " + format(c) + " = 0\n→ Phương trình vô nghiệm";
            }
        } else {
            double x = -c / b;
            result = "a = 0 → Đây là phương trình bậc 1\n\n"
                    + "Phương trình: " + format(b) + "x + " + format(c) + " = 0\n\n"
                    + "→ x = " + format(x);
        }
        showResultWithAnimation(result);
    }




    private void showResultWithAnimation(String text) {

        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(400); // 400ms
        fadeIn.setFillAfter(true);

        tvResult.setText(text);
        tvResult.startAnimation(fadeIn);
    }




    private void clearForm() {
        etA.setText("");
        etB.setText("");
        etC.setText("");
        tvResult.setText("Chưa có kết quả");
        etA.requestFocus();
    }




    private String format(double d) {
        if (d == Math.floor(d) && !Double.isInfinite(d) && Math.abs(d) < 1e15) {
            return String.format("%d", (long) d);
        }

        String formatted = String.format("%.4f", d);
        formatted = formatted.replaceAll("0+$", "").replaceAll("\\.$", "");
        return formatted;
    }


    private String formatEquation(double a, double b, double c) {
        StringBuilder eq = new StringBuilder();


        if (a == 1) eq.append("x²");
        else if (a == -1) eq.append("-x²");
        else eq.append(format(a)).append("x²");


        if (b > 0) eq.append(" + ").append(b == 1 ? "" : format(b)).append("x");
        else if (b < 0) eq.append(" - ").append(b == -1 ? "" : format(Math.abs(b))).append("x");


        if (c > 0) eq.append(" + ").append(format(c));
        else if (c < 0) eq.append(" - ").append(format(Math.abs(c)));

        eq.append(" = 0");
        return eq.toString();
    }
}
