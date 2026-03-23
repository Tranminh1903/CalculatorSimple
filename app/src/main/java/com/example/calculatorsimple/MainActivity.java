package com.example.calculatorsimple;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity — Màn hình chính tích hợp tất cả tính năng
 *
 * Sử dụng ViewFlipper để chuyển đổi giữa các chế độ:
 * - Chế độ 0 (MODE_CALC): Máy tính cơ bản (+, -, ×, ÷, ^, thập phân)
 * - Chế độ 1 (MODE_FRACTION): Chuyển phân số → thập phân
 * - Chế độ 2 (MODE_POWER): Luỹ thừa & Số mũ
 * - Chế độ 3 (MODE_QUADRATIC): Giải phương trình bậc 2
 *
 * Bàn phím số luôn hiển thị ở dưới cùng và hoạt động cho TẤT CẢ chế độ:
 * - Ở chế độ máy tính: nhập vào biểu thức
 * - Ở chế độ nâng cao: nhập vào EditText đang được focus
 */
public class MainActivity extends AppCompatActivity {

    // ============================================
    // HẰNG SỐ CHẾ ĐỘ
    // ============================================
    private static final int MODE_CALC = 0;
    private static final int MODE_FRACTION = 1;
    private static final int MODE_POWER = 2;
    private static final int MODE_QUADRATIC = 3;
    private static final int MODE_LINEAR = 4;

    // ============================================
    // BIẾN CHUNG
    // ============================================
    private ViewFlipper viewFlipper;
    private int currentMode = MODE_CALC;

    // — Chế độ Máy tính cơ bản —
    private TextView tvExpression, tvResult;
    private StringBuilder expression = new StringBuilder();
    private CalculatorEngine engine = new CalculatorEngine();
    private boolean justCalculated = false;

    // — Chế độ Phân số —
    private EditText etNumerator, etDenominator;
    private TextView tvFractionResult;

    // — Chế độ Luỹ thừa —
    private EditText etBase, etExponent;
    private TextView tvPowerResult;

    // — Chế độ Phương trình bậc 1 —
    private EditText etLinearA, etLinearB;
    private TextView tvLinearResult, tvLinearPreview;

    // — Chế độ Phương trình bậc 2 —
    private EditText etA, etB, etC;
    private TextView tvQuadraticResult, tvEquationPreview;

    /** EditText đang được focus hiện tại (dùng cho chế độ nâng cao) */
    private EditText currentFocusedEditText = null;

    // ============================================
    // KHỞI TẠO
    // ============================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewFlipper = findViewById(R.id.viewFlipper);

        // Ánh xạ & thiết lập cho từng chế độ
        initCalculatorViews();
        initFractionViews();
        initPowerViews();
        initQuadraticViews();
        initLinearViews();

        // Bàn phím
        setNumericClickListeners();
        setOperatorClickListeners();
        setSpecialClickListeners();
    }

    // ============================================
    // KHỞI TẠO VIEW + TẮT BÀN PHÍM ẢO
    // ============================================

    private void initCalculatorViews() {
        tvExpression = findViewById(R.id.tvExpression);
        tvResult = findViewById(R.id.tvResult);
    }

    private void initFractionViews() {
        etNumerator = findViewById(R.id.etNumerator);
        etDenominator = findViewById(R.id.etDenominator);
        tvFractionResult = findViewById(R.id.tvFractionResult);

        // Tắt bàn phím ảo, dùng bàn phím của ứng dụng
        disableSoftKeyboard(etNumerator);
        disableSoftKeyboard(etDenominator);

        // Theo dõi focus
        setupFocusTracking(etNumerator);
        setupFocusTracking(etDenominator);

        findViewById(R.id.btnConvert).setOnClickListener(v -> convertFraction());
        findViewById(R.id.btnClearFraction).setOnClickListener(v -> {
            etNumerator.setText("");
            etDenominator.setText("");
            tvFractionResult.setText("Chưa có kết quả");
            etNumerator.requestFocus();
        });

    }

    private void initPowerViews() {
        etBase = findViewById(R.id.etBase);
        etExponent = findViewById(R.id.etExponent);
        tvPowerResult = findViewById(R.id.tvPowerResult);

        disableSoftKeyboard(etBase);
        disableSoftKeyboard(etExponent);
        setupFocusTracking(etBase);
        setupFocusTracking(etExponent);

        findViewById(R.id.btnCalcPower).setOnClickListener(v -> calculatePower());
        findViewById(R.id.btnClearPower).setOnClickListener(v -> {
            etBase.setText("");
            etExponent.setText("");
            tvPowerResult.setText("Chưa có kết quả");
            etBase.requestFocus();
        });

    }

    private void initQuadraticViews() {
        etA = findViewById(R.id.etA);
        etB = findViewById(R.id.etB);
        etC = findViewById(R.id.etC);
        tvQuadraticResult = findViewById(R.id.tvQuadraticResult);
        tvEquationPreview = findViewById(R.id.tvEquationPreview);
        
        TextWatcher equationWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                updateEquationPreview();
            }
        };
        etA.addTextChangedListener(equationWatcher);
        etB.addTextChangedListener(equationWatcher);
        etC.addTextChangedListener(equationWatcher);

        disableSoftKeyboard(etA);
        disableSoftKeyboard(etB);
        disableSoftKeyboard(etC);
        setupFocusTracking(etA);
        setupFocusTracking(etB);
        setupFocusTracking(etC);

        findViewById(R.id.btnSolve).setOnClickListener(v -> solveQuadratic());
        findViewById(R.id.btnClearQuadratic).setOnClickListener(v -> {
            etA.setText("");
            etB.setText("");
            etC.setText("");
            tvQuadraticResult.setText("Chưa có kết quả");
            etA.requestFocus();
        });

    }

    private void updateEquationPreview() {
        String strA = etA.getText().toString();
        String strB = etB.getText().toString();
        String strC = etC.getText().toString();

        StringBuilder eq = new StringBuilder();

        // Phần A
        if (strA.isEmpty()) {
            eq.append("ax²");
        } else if (strA.equals("-")) {
            eq.append("-ax²");
        } else if (strA.equals("1")) {
            eq.append("x²");
        } else if (strA.equals("-1")) {
            eq.append("-x²");
        } else {
            eq.append(strA).append("x²");
        }

        // Phần B
        if (strB.isEmpty()) {
            eq.append(" + bx");
        } else if (strB.equals("-")) {
            eq.append(" - bx");
        } else {
            if (strB.startsWith("-")) {
                String val = strB.substring(1);
                if (val.equals("1")) eq.append(" - x");
                else eq.append(" - ").append(val).append("x");
            } else {
                if (strB.equals("1")) eq.append(" + x");
                else eq.append(" + ").append(strB).append("x");
            }
        }

        // Phần C
        if (strC.isEmpty()) {
            eq.append(" + c");
        } else if (strC.equals("-")) {
            eq.append(" - c");
        } else {
            if (strC.startsWith("-")) {
                eq.append(" - ").append(strC.substring(1));
            } else {
                eq.append(" + ").append(strC);
            }
        }

        eq.append(" = 0");
        if (tvEquationPreview != null) {
            tvEquationPreview.setText(eq.toString());
        }
    }

    private void initLinearViews() {
        etLinearA = findViewById(R.id.etLinearA);
        etLinearB = findViewById(R.id.etLinearB);
        tvLinearResult = findViewById(R.id.tvLinearResult);
        tvLinearPreview = findViewById(R.id.tvLinearPreview);

        TextWatcher linearWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                updateLinearPreview();
            }
        };
        etLinearA.addTextChangedListener(linearWatcher);
        etLinearB.addTextChangedListener(linearWatcher);

        disableSoftKeyboard(etLinearA);
        disableSoftKeyboard(etLinearB);
        setupFocusTracking(etLinearA);
        setupFocusTracking(etLinearB);

        findViewById(R.id.btnSolveLinear).setOnClickListener(v -> solveLinear());
        findViewById(R.id.btnClearLinear).setOnClickListener(v -> {
            etLinearA.setText("");
            etLinearB.setText("");
            tvLinearResult.setText("Chưa có kết quả");
            etLinearA.requestFocus();
        });
    }

    private void updateLinearPreview() {
        String strA = etLinearA.getText().toString();
        String strB = etLinearB.getText().toString();

        StringBuilder eq = new StringBuilder();

        // Phần A
        if (strA.isEmpty()) {
            eq.append("ax");
        } else if (strA.equals("-")) {
            eq.append("-ax");
        } else if (strA.equals("1")) {
            eq.append("x");
        } else if (strA.equals("-1")) {
            eq.append("-x");
        } else {
            eq.append(strA).append("x");
        }

        // Phần B
        if (strB.isEmpty()) {
            eq.append(" + b");
        } else if (strB.equals("-")) {
            eq.append(" - b");
        } else {
            if (strB.startsWith("-")) {
                eq.append(" - ").append(strB.substring(1));
            } else {
                eq.append(" + ").append(strB);
            }
        }

        eq.append(" = 0");
        if (tvLinearPreview != null) {
            tvLinearPreview.setText(eq.toString());
        }
    }

    /**
     * Tắt bàn phím ảo (soft keyboard) cho EditText.
     * Người dùng sẽ dùng bàn phím trên giao diện ứng dụng để nhập liệu.
     */
    private void disableSoftKeyboard(EditText editText) {
        editText.setShowSoftInputOnFocus(false);
        editText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
    }

    /**
     * Theo dõi focus của EditText: khi EditText được nhấn vào,
     * cập nhật biến currentFocusedEditText để bàn phím biết nhập vào đâu.
     */
    private void setupFocusTracking(EditText editText) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                currentFocusedEditText = editText;
                // Ẩn bàn phím ảo nếu đang hiện
                hideSoftKeyboard();
            }
        });
        editText.setOnClickListener(v -> {
            currentFocusedEditText = editText;
            hideSoftKeyboard();
        });
    }

    /** Ẩn bàn phím ảo */
    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View focused = getCurrentFocus();
        if (imm != null && focused != null) {
            imm.hideSoftInputFromWindow(focused.getWindowToken(), 0);
        }
    }

    // ============================================
    // CHUYỂN ĐỔI CHẾ ĐỘ
    // ============================================

    private void switchMode(int mode) {
        currentMode = mode;
        viewFlipper.setDisplayedChild(mode);

        // Đặt focus mặc định khi chuyển chế độ
        switch (mode) {
            case MODE_FRACTION:
                etNumerator.requestFocus();
                currentFocusedEditText = etNumerator;
                break;
            case MODE_POWER:
                etBase.requestFocus();
                currentFocusedEditText = etBase;
                break;
            case MODE_QUADRATIC:
                etA.requestFocus();
                currentFocusedEditText = etA;
                break;
            case MODE_LINEAR:
                etLinearA.requestFocus();
                currentFocusedEditText = etLinearA;
                break;
            default:
                currentFocusedEditText = null;
                break;
        }
    }

    // ============================================
    // BÀN PHÍM: NÚT SỐ (0-9) VÀ DẤU CHẤM (.)
    // ============================================

    private void setNumericClickListeners() {
        View.OnClickListener listener = view -> {
            Button b = (Button) view;
            String text = b.getText().toString();

            if (currentMode == MODE_CALC) {
                // Chế độ máy tính: nhập vào biểu thức
                handleCalcNumericInput(text);
            } else {
                // Chế độ nâng cao: nhập vào EditText đang focus
                insertTextToFocusedEditText(text);
            }
        };

        int[] ids = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btnDot
        };
        for (int id : ids) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    /**
     * Nhập ký tự vào EditText đang được focus.
     * Chèn tại vị trí con trỏ hiện tại.
     * Xử lý: không cho nhập 2 dấu chấm, cho phép dấu trừ đầu.
     */
    private void insertTextToFocusedEditText(String text) {
        if (currentFocusedEditText == null) return;

        String current = currentFocusedEditText.getText().toString();
        int cursorPos = currentFocusedEditText.getSelectionStart();
        if (cursorPos < 0) cursorPos = current.length();

        // Không cho nhập 2 dấu chấm
        if (text.equals(".") && current.contains(".")) return;

        // Chèn text tại vị trí con trỏ
        String newText = current.substring(0, cursorPos) + text + current.substring(cursorPos);
        currentFocusedEditText.setText(newText);
        currentFocusedEditText.setSelection(cursorPos + text.length());
    }

    private void handleCalcNumericInput(String text) {
        if (justCalculated) {
            expression.setLength(0);
            justCalculated = false;
        }
        if (text.equals(".")) {
            if (!canAddDot()) return;
            if (expression.length() == 0 || isOperator(expression.charAt(expression.length() - 1))) {
                expression.append("0");
            }
        }
        if (text.equals("0") && expression.length() > 0) {
            String lastNumber = getLastNumber();
            if (lastNumber.equals("0")) return;
        }
        expression.append(text);
        updateDisplay();
        updatePreview();
    }

    private boolean canAddDot() {
        if (expression.length() == 0) return true;
        return !getLastNumber().contains(".");
    }

    private String getLastNumber() {
        String[] parts = expression.toString().split("[+\\-×÷]");
        return parts.length == 0 ? "" : parts[parts.length - 1];
    }

    // ============================================
    // BÀN PHÍM: PHÉP TOÁN
    // ============================================

    private void setOperatorClickListeners() {
        View.OnClickListener listener = view -> {
            Button b = (Button) view;
            String op = b.getText().toString();

            if (currentMode == MODE_CALC) {
                handleCalcOperatorInput(op);
            } else {
                // Ở chế độ nâng cao: chỉ cho phép nhập dấu trừ (số âm)
                if (op.equals("-") && currentFocusedEditText != null) {
                    String current = currentFocusedEditText.getText().toString();
                    if (current.isEmpty()) {
                        currentFocusedEditText.setText("-");
                        currentFocusedEditText.setSelection(1);
                    } else if (current.equals("-")) {
                        currentFocusedEditText.setText("");
                    }
                }
            }
        };

        int[] ids = {R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide};
        for (int id : ids) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void handleCalcOperatorInput(String op) {
        if (justCalculated) {
            String result = tvResult.getText().toString();
            if (!result.startsWith("Lỗi")) {
                expression.setLength(0);
                expression.append(result);
            }
            justCalculated = false;
        }
        if (expression.length() == 0) {
            if (op.equals("-")) expression.append(op);
            updateDisplay();
            return;
        }
        char last = expression.charAt(expression.length() - 1);
        if (isOperator(last)) {
            expression.setCharAt(expression.length() - 1, op.charAt(0));
        } else if (last != '.') {
            expression.append(op);
        }
        updateDisplay();
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '×' || c == '÷';
    }

    // ============================================
    // BÀN PHÍM: NÚT ĐẶC BIỆT (C, ⌫, =, Menu)
    // ============================================

    private void setSpecialClickListeners() {

        // === NÚT C (XOÁ TẤT CẢ) ===
        findViewById(R.id.btnClear).setOnClickListener(v -> {
            if (currentMode == MODE_CALC) {
                expression.setLength(0);
                tvExpression.setText("0");
                tvResult.setText("0");
                justCalculated = false;
            } else if (currentMode == MODE_FRACTION) {
                etNumerator.setText("");
                etDenominator.setText("");
                tvFractionResult.setText("Chưa có kết quả");
                etNumerator.requestFocus();
            } else if (currentMode == MODE_POWER) {
                etBase.setText("");
                etExponent.setText("");
                tvPowerResult.setText("Chưa có kết quả");
                etBase.requestFocus();
            } else if (currentMode == MODE_QUADRATIC) {
                etA.setText("");
                etB.setText("");
                etC.setText("");
                tvQuadraticResult.setText("Chưa có kết quả");
                etA.requestFocus();
            } else if (currentMode == MODE_LINEAR) {
                etLinearA.setText("");
                etLinearB.setText("");
                tvLinearResult.setText("Chưa có kết quả");
                etLinearA.requestFocus();
            }
        });

        // === NÚT ⌫ (XOÁ 1 KÝ TỰ) ===
        findViewById(R.id.btnBackspace).setOnClickListener(v -> {
            if (currentMode == MODE_CALC) {
                if (justCalculated) {
                    expression.setLength(0);
                    tvResult.setText("0");
                    justCalculated = false;
                } else if (expression.length() > 0) {
                    expression.deleteCharAt(expression.length() - 1);
                    updatePreview();
                }
                updateDisplay();
            } else if (currentFocusedEditText != null) {
                // Ở chế độ nâng cao: xoá 1 ký tự tại con trỏ
                String current = currentFocusedEditText.getText().toString();
                int cursorPos = currentFocusedEditText.getSelectionStart();
                if (cursorPos > 0 && current.length() > 0) {
                    String newText = current.substring(0, cursorPos - 1) + current.substring(cursorPos);
                    currentFocusedEditText.setText(newText);
                    currentFocusedEditText.setSelection(cursorPos - 1);
                }
            }
        });

        // === NÚT = ===
        findViewById(R.id.btnEqual).setOnClickListener(v -> {
            switch (currentMode) {
                case MODE_CALC:
                    if (expression.length() > 0) {
                        String expr = sanitizeExpression(expression.toString());
                        String result = engine.evaluate(expr);
                        tvResult.setText(result);
                        justCalculated = true;
                        if (!result.startsWith("Lỗi")) {
                            tvExpression.setText(expression.toString() + " =");
                        }
                    }
                    break;
                case MODE_FRACTION:
                    convertFraction();
                    break;
                case MODE_POWER:
                    calculatePower();
                    break;
                case MODE_QUADRATIC:
                    solveQuadratic();
                    break;
                case MODE_LINEAR:
                    solveLinear();
                    break;
            }
        });

        // === NÚT MENU ===
        findViewById(R.id.btnMenu).setOnClickListener(v -> showAdvancedMenu());
    }

    // ============================================
    // MENU TÍNH NĂNG NÂNG CAO
    // ============================================

    private void showAdvancedMenu() {
        String[] menuItems = {
                "Giải phương trình bậc 1",
                "Giải phương trình bậc 2",
                "Luỹ thừa & số mũ",
                "Phân số sang thập phân",
                "Trở về (Máy tính cơ bản)"
        };

        new AlertDialog.Builder(this)
                .setTitle("Tính năng nâng cao")
                .setItems(menuItems, (dialog, which) -> {
                    switch (which) {
                        case 0: switchMode(MODE_LINEAR); break;
                        case 1: switchMode(MODE_QUADRATIC); break;
                        case 2: switchMode(MODE_POWER); break;
                        case 3: switchMode(MODE_FRACTION); break;
                        case 4: switchMode(MODE_CALC); break;
                    }
                })
                .setNegativeButton("Đóng", null)
                .show();
    }

    // ============================================
    // CẬP NHẬT HIỂN THỊ (Máy tính cơ bản)
    // ============================================

    private void updateDisplay() {
        String text = expression.toString();
        tvExpression.setText(text.isEmpty() ? "0" : text);
    }

    private void updatePreview() {
        if (expression.length() == 0) { tvResult.setText("0"); return; }
        String expr = expression.toString();
        boolean hasOp = false;
        for (int i = 1; i < expr.length(); i++) {
            if (isOperator(expr.charAt(i))) { hasOp = true; break; }
        }
        if (!hasOp) return;
        String result = engine.evaluate(sanitizeExpression(expr));
        if (!result.startsWith("Lỗi")) tvResult.setText(result);
    }

    private String sanitizeExpression(String expr) {
        while (expr.length() > 0) {
            char last = expr.charAt(expr.length() - 1);
            if (isOperator(last) || last == '.') expr = expr.substring(0, expr.length() - 1);
            else break;
        }
        return expr;
    }

    // ============================================
    // LOGIC: PHÂN SỐ → THẬP PHÂN
    // ============================================

    private void convertFraction() {
        String sNum = etNumerator.getText().toString().trim();
        String sDen = etDenominator.getText().toString().trim();

        if (sNum.isEmpty() || sDen.isEmpty()) {
            Toast.makeText(this, "⚠️ Nhập đầy đủ tử số và mẫu số", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            double num = Double.parseDouble(sNum);
            double den = Double.parseDouble(sDen);

            if (den == 0) {
                showAnimatedResult(tvFractionResult, "❌ Lỗi: Mẫu số không được bằng 0");
                return;
            }
            double result = num / den;
            StringBuilder sb = new StringBuilder();
            sb.append("Hiển thị: ").append(formatNum(num)).append(" / ").append(formatNum(den));
            sb.append("\n\nKết quả: ").append(formatResult(result));

            if (num == Math.floor(num) && den == Math.floor(den)) {
                long n = (long) num, d = (long) den;
                long gcd = gcd(Math.abs(n), Math.abs(d));
                if (gcd > 1) {
                    sb.append("\n📌 Rút gọn: ").append(n / gcd).append("/").append(d / gcd);
                }
            }
            showAnimatedResult(tvFractionResult, sb.toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "⚠️ Số không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    private long gcd(long a, long b) {
        while (b != 0) { long t = b; b = a % b; a = t; }
        return a;
    }

    // ============================================
    // LOGIC: LUỸ THỪA & SỐ MŨ
    // ============================================

    private void calculatePower() {
        String sBase = etBase.getText().toString().trim();
        String sExp = etExponent.getText().toString().trim();

        if (sBase.isEmpty() || sExp.isEmpty()) {
            Toast.makeText(this, "⚠️ Nhập đầy đủ cơ số và số mũ", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            double base = Double.parseDouble(sBase);
            double exponent = Double.parseDouble(sExp);
            double result = Math.pow(base, exponent);

            StringBuilder sb = new StringBuilder();
            sb.append("Hiển thị: ").append(formatNum(base)).append(" ^ ").append(formatNum(exponent));
            sb.append("\n\n");

            if (Double.isNaN(result)) {
                sb.append("❌ Không thể tính");
            } else if (Double.isInfinite(result)) {
                sb.append("❌ Số quá lớn (tràn số)");
            } else {
                sb.append("Kết quả: ").append(formatResult(result));
                if (exponent == Math.floor(exponent) && exponent > 0 && exponent <= 5) {
                    int n = (int) exponent;
                    sb.append("\n📌 = ");
                    for (int i = 0; i < n; i++) {
                        if (i > 0) sb.append(" × ");
                        sb.append(formatNum(base));
                    }
                }
            }
            showAnimatedResult(tvPowerResult, sb.toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "⚠️ Số không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    // ============================================
    // LOGIC: GIẢI PHƯƠNG TRÌNH BẬC 2
    // ============================================

    private void solveQuadratic() {
        String sA = etA.getText().toString().trim();
        String sB = etB.getText().toString().trim();
        String sC = etC.getText().toString().trim();

        if (sA.isEmpty() || sB.isEmpty() || sC.isEmpty()) {
            Toast.makeText(this, "⚠️ Nhập đầy đủ hệ số a, b, c", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            double a = Double.parseDouble(sA);
            double b = Double.parseDouble(sB);
            double c = Double.parseDouble(sC);

            if (a == 0) {
                String result;
                if (b == 0) {
                    result = c == 0 ? "⚠️ a=0, b=0, c=0\nVô số nghiệm" : "⚠️ a=0, b=0\nVô nghiệm";
                } else {
                    double x = -c / b;
                    result = "⚠️ a=0 → Bậc 1\nx = " + formatResult(x);
                }
                showAnimatedResult(tvQuadraticResult, result);
                return;
            }

            double delta = b * b - 4 * a * c;
            StringBuilder sb = new StringBuilder();
            sb.append("Δ = ").append(formatResult(delta)).append("\n\n");

            if (delta > 0) {
                double x1 = (-b + Math.sqrt(delta)) / (2 * a);
                double x2 = (-b - Math.sqrt(delta)) / (2 * a);
                sb.append("Kết quả: 2 nghiệm phân biệt\n");
                sb.append("x₁ = ").append(formatResult(x1)).append("\n");
                sb.append("x₂ = ").append(formatResult(x2));
            } else if (delta == 0) {
                double x = -b / (2 * a);
                sb.append("Kết quả: Nghiệm kép\nx = ").append(formatResult(x));
            } else {
                sb.append("❌ Vô nghiệm thực (Δ < 0)");
            }
            showAnimatedResult(tvQuadraticResult, sb.toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "⚠️ Số không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    // ============================================
    // LOGIC: GIẢI PHƯƠNG TRÌNH BẬC 1
    // ============================================

    private void solveLinear() {
        String sA = etLinearA.getText().toString().trim();
        String sB = etLinearB.getText().toString().trim();

        if (sA.isEmpty() || sB.isEmpty()) {
            Toast.makeText(this, "⚠️ Nhập đầy đủ hệ số a, b", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double a = Double.parseDouble(sA);
            double b = Double.parseDouble(sB);

            if (a == 0) {
                if (b == 0) {
                    showAnimatedResult(tvLinearResult, "Kết quả: Vô số nghiệm");
                } else {
                    showAnimatedResult(tvLinearResult, "Kết quả: Vô nghiệm");
                }
            } else {
                double x = -b / a;
                showAnimatedResult(tvLinearResult, "Kết quả: x = " + formatResult(x));
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "⚠️ Số không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    // ============================================
    // TIỆN ÍCH
    // ============================================

    private void showAnimatedResult(TextView tv, String text) {
        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(300);
        fadeIn.setFillAfter(true);
        tv.setText(text);
        tv.startAnimation(fadeIn);
    }

    private String formatNum(double d) {
        if (d == Math.floor(d) && !Double.isInfinite(d) && Math.abs(d) < 1e15)
            return String.format("%d", (long) d);
        return String.valueOf(d);
    }

    private String formatResult(double d) {
        if (d == Math.floor(d) && !Double.isInfinite(d) && Math.abs(d) < 1e15)
            return String.format("%d", (long) d);
        String f = String.format("%.10f", d);
        return f.replaceAll("0+$", "").replaceAll("\\.$", "");
    }
}
