package com.example.calculatorsimple;

import java.util.ArrayList;
import java.util.List;

public class CalculatorEngine {

    private static final String ERROR_DIVISION_BY_ZERO = "Lỗi: Chia cho 0";
    private static final String ERROR_INVALID_EXPRESSION = "Lỗi: Biểu thức không hợp lệ";
    private static final String ERROR_OVERFLOW = "Lỗi: Số quá lớn";

    public String evaluate(String expression) {
        try {
            String sanitized = expression.replace("×", "*").replace("÷", "/");

            sanitized = sanitized.trim();

            if (sanitized.isEmpty()) {
                return "0";
            }

            double result = eval(sanitized);

            if (Double.isInfinite(result)) {
                return ERROR_OVERFLOW;
            }
            if (Double.isNaN(result)) {
                return ERROR_INVALID_EXPRESSION;
            }

            return formatResult(result);

        } catch (ArithmeticException e) {
            return ERROR_DIVISION_BY_ZERO;
        } catch (Exception e) {
            return ERROR_INVALID_EXPRESSION;
        }
    }

    private String formatResult(double result) {
        if (result == Math.floor(result) && !Double.isInfinite(result)
                && Math.abs(result) < 1e15) {
            return String.format("%d", (long) result);
        }

        String formatted = String.format("%.10f", result);

        formatted = formatted.replaceAll("0+$", "");

        formatted = formatted.replaceAll("\\.$", "");

        return formatted;
    }

    private double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar(); 
                double x = parseExpression(); 

                if (pos < str.length()) {
                    throw new RuntimeException("Ký tự không hợp lệ: " + (char) ch);
                }
                return x;
            }

            double parseExpression() {
                double x = parseTerm(); 
                for (; ; ) {
                    if (eat('+')) {
                        x += parseTerm();
                    } else if (eat('-')) {
                        x -= parseTerm();
                    } else {
                        return x; 
                    }
                }
            }

            double parseTerm() {
                double x = parseFactor(); 
                for (; ; ) {
                    if (eat('*')) {
                        x *= parseFactor();
                    } else if (eat('/')) {
                        double divisor = parseFactor();
                        if (divisor == 0) {
                            throw new ArithmeticException("Chia cho 0");
                        }
                        x /= divisor;
                    } else {
                        return x; 
                    }
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();  
                if (eat('-')) return -parseFactor();  

                double x;
                int startPos = this.pos;

                if (eat('(')) {
                    x = parseExpression();
                    if (!eat(')')) {
                        throw new RuntimeException("Thiếu dấu đóng ngoặc )");
                    }
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Ký tự không hợp lệ: " + (char) ch);
                }

                if (eat('^')) {
                    x = Math.pow(x, parseFactor());
                }

                return x;
            }
        }.parse();
    }
}
