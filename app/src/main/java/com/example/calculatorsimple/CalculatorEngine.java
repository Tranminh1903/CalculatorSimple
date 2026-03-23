package com.example.calculatorsimple;

import java.util.ArrayList;
import java.util.List;

/**
 * CalculatorEngine — Bộ xử lý biểu thức toán học
 *
 * Sử dụng thuật toán Recursive Descent Parser để phân tích
 * và tính giá trị biểu thức toán học.
 *
 * Hỗ trợ:
 * - Cộng (+), Trừ (-), Nhân (×/*), Chia (÷//)
 * - Luỹ thừa (^)
 * - Số thập phân
 * - Số âm (unary minus)
 * - Xử lý lỗi: chia cho 0, biểu thức không hợp lệ, tràn số
 *
 * Thứ tự ưu tiên phép toán (từ thấp đến cao):
 * 1. Cộng, Trừ
 * 2. Nhân, Chia
 * 3. Luỹ thừa
 * 4. Số và dấu ngoặc
 */
public class CalculatorEngine {

    // Hằng số thông báo lỗi
    private static final String ERROR_DIVISION_BY_ZERO = "Lỗi: Chia cho 0";
    private static final String ERROR_INVALID_EXPRESSION = "Lỗi: Biểu thức không hợp lệ";
    private static final String ERROR_OVERFLOW = "Lỗi: Số quá lớn";

    /**
     * Đánh giá (tính) một biểu thức toán học dạng chuỗi.
     *
     * @param expression Biểu thức cần tính, ví dụ: "2+3×4"
     * @return Kết quả dạng chuỗi, hoặc thông báo lỗi
     *
     * Ví dụ:
     *   evaluate("2+3")    → "5"
     *   evaluate("10÷3")   → "3.3333333333"
     *   evaluate("2^10")   → "1024"
     *   evaluate("5÷0")    → "Lỗi: Chia cho 0"
     */
    public String evaluate(String expression) {
        try {
            // Bước 1: Thay thế ký hiệu hiển thị thành ký hiệu chuẩn
            // "×" → "*" và "÷" → "/" để dễ xử lý
            String sanitized = expression.replace("×", "*").replace("÷", "/");

            // Bước 2: Loại bỏ khoảng trắng thừa
            sanitized = sanitized.trim();

            // Bước 3: Kiểm tra biểu thức rỗng
            if (sanitized.isEmpty()) {
                return "0";
            }

            // Bước 4: Tính giá trị bằng recursive descent parser
            double result = eval(sanitized);

            // Bước 5: Kiểm tra kết quả có bị tràn số (Infinity, NaN) không
            if (Double.isInfinite(result)) {
                return ERROR_OVERFLOW;
            }
            if (Double.isNaN(result)) {
                return ERROR_INVALID_EXPRESSION;
            }

            // Bước 6: Định dạng kết quả
            return formatResult(result);

        } catch (ArithmeticException e) {
            // Bắt lỗi chia cho 0
            return ERROR_DIVISION_BY_ZERO;
        } catch (Exception e) {
            // Bắt mọi lỗi khác (biểu thức sai cú pháp, v.v.)
            return ERROR_INVALID_EXPRESSION;
        }
    }

    /**
     * Định dạng kết quả cho đẹp:
     * - Nếu kết quả là số nguyên (VD: 5.0) → hiển thị "5"
     * - Nếu kết quả thập phân → giữ tối đa 10 chữ số sau dấu chấm,
     *   bỏ các số 0 thừa ở cuối
     *
     * @param result Giá trị cần định dạng
     * @return Chuỗi kết quả đã format
     */
    private String formatResult(double result) {
        // Nếu là số nguyên (VD: 8.0 → 8)
        if (result == Math.floor(result) && !Double.isInfinite(result)
                && Math.abs(result) < 1e15) {
            return String.format("%d", (long) result);
        }

        // Định dạng tối đa 10 chữ số thập phân
        String formatted = String.format("%.10f", result);

        // Loại bỏ các số 0 thừa ở cuối: "3.14000000" → "3.14"
        formatted = formatted.replaceAll("0+$", "");

        // Loại bỏ dấu chấm nếu không còn số thập phân: "5." → "5"
        formatted = formatted.replaceAll("\\.$", "");

        return formatted;
    }

    // ================================================================
    //  RECURSIVE DESCENT PARSER
    //  Thuật toán phân tích biểu thức theo đệ quy
    //
    //  Cấu trúc ngữ pháp (grammar):
    //    expression = term (('+' | '-') term)*
    //    term       = factor (('*' | '/') factor)*
    //    factor     = ('+' | '-')? atom ('^' factor)?
    //    atom       = NUMBER | '(' expression ')'
    //
    //  Cách hoạt động:
    //  - Mỗi mức ưu tiên là 1 hàm riêng
    //  - Hàm mức thấp gọi hàm mức cao hơn
    //  - Đảm bảo đúng thứ tự ưu tiên phép toán
    // ================================================================

    /**
     * Hàm chính để tính biểu thức.
     * Tạo 1 anonymous Object chứa parser state (vị trí hiện tại, ký tự hiện tại)
     * và gọi parse() để bắt đầu phân tích.
     *
     * @param str Biểu thức đã được chuẩn hoá (VD: "2+3*4")
     * @return Giá trị số thực kết quả
     */
    private double eval(final String str) {
        return new Object() {
            // pos: vị trí hiện tại trong chuỗi (bắt đầu từ -1)
            // ch: ký tự hiện tại đang đọc
            int pos = -1, ch;

            /**
             * Đọc ký tự tiếp theo trong biểu thức.
             * Nếu hết chuỗi thì ch = -1 (EOF).
             */
            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            /**
             * Kiểm tra và "ăn" (consume) ký tự mong đợi.
             * Bỏ qua khoảng trắng trước khi kiểm tra.
             *
             * @param charToEat Ký tự cần kiểm tra
             * @return true nếu ký tự hiện tại khớp (và đã di chuyển con trỏ)
             */
            boolean eat(int charToEat) {
                // Bỏ qua khoảng trắng
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            /**
             * Điểm bắt đầu của parser.
             * Đọc ký tự đầu tiên → phân tích biểu thức → kiểm tra đã hết chuỗi chưa.
             */
            double parse() {
                nextChar(); // Đọc ký tự đầu tiên
                double x = parseExpression(); // Phân tích biểu thức

                // Nếu chưa hết chuỗi → có ký tự lạ → lỗi cú pháp
                if (pos < str.length()) {
                    throw new RuntimeException("Ký tự không hợp lệ: " + (char) ch);
                }
                return x;
            }

            /**
             * Phân tích biểu thức: Cộng và Trừ (ưu tiên THẤP nhất)
             * expression = term (('+' | '-') term)*
             *
             * Ví dụ: "2 + 3 - 1" → parseTerm(2) + parseTerm(3) - parseTerm(1)
             */
            double parseExpression() {
                double x = parseTerm(); // Đọc số hạng đầu tiên
                for (; ; ) {
                    if (eat('+')) {
                        // Cộng: lấy kết quả + số hạng tiếp theo
                        x += parseTerm();
                    } else if (eat('-')) {
                        // Trừ: lấy kết quả - số hạng tiếp theo
                        x -= parseTerm();
                    } else {
                        return x; // Không còn + hoặc - → trả về kết quả
                    }
                }
            }

            /**
             * Phân tích số hạng: Nhân và Chia (ưu tiên TRUNG BÌNH)
             * term = factor (('*' | '/') factor)*
             *
             * Ví dụ: "2 * 3 / 4" → parseFactor(2) * parseFactor(3) / parseFactor(4)
             */
            double parseTerm() {
                double x = parseFactor(); // Đọc thừa số đầu tiên
                for (; ; ) {
                    if (eat('*')) {
                        // Nhân
                        x *= parseFactor();
                    } else if (eat('/')) {
                        // Chia — kiểm tra chia cho 0
                        double divisor = parseFactor();
                        if (divisor == 0) {
                            throw new ArithmeticException("Chia cho 0");
                        }
                        x /= divisor;
                    } else {
                        return x; // Không còn * hoặc / → trả về
                    }
                }
            }

            /**
             * Phân tích thừa số: Luỹ thừa, số, dấu ngoặc (ưu tiên CAO nhất)
             * factor = ('+' | '-')? atom ('^' factor)?
             *
             * Xử lý:
             * - Dấu + / - đầu số (unary): +5, -3
             * - Dấu ngoặc: (2+3)
             * - Số: 42, 3.14
             * - Luỹ thừa: 2^3 (right-associative: 2^3^2 = 2^(3^2) = 2^9 = 512)
             */
            double parseFactor() {
                // Xử lý dấu + / - đầu số (unary operators)
                if (eat('+')) return parseFactor();  // +5 → 5
                if (eat('-')) return -parseFactor();  // -5 → -(5)

                double x;
                int startPos = this.pos;

                if (eat('(')) {
                    // Dấu ngoặc: tính biểu thức bên trong
                    x = parseExpression();
                    if (!eat(')')) {
                        throw new RuntimeException("Thiếu dấu đóng ngoặc )");
                    }
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    // Đọc số (có thể có dấu chấm thập phân)
                    // Ví dụ: "123", "3.14", ".5"
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Ký tự không hợp lệ: " + (char) ch);
                }

                // Kiểm tra phép luỹ thừa (^)
                // Right-associative: 2^3^2 = 2^(3^2) vì parseFactor gọi đệ quy chính nó
                if (eat('^')) {
                    x = Math.pow(x, parseFactor());
                }

                return x;
            }
        }.parse();
    }
}