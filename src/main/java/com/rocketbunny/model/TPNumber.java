package com.rocketbunny.model;

import com.rocketbunny.exception.PNumberException;

public class TPNumber {
    private double n;
    private int b;
    private int c;

    public TPNumber(double a, int b, int c) {
        validateBase(b);
        validatePrecision(c);
        this.n = a;
        this.b = b;
        this.c = c;
    }

    public TPNumber(String a, String bs, String cs) {
        int b = Integer.parseInt(bs);
        int c = Integer.parseInt(cs);
        validateBase(b);
        validatePrecision(c);
        this.n = parsePNumber(a, b);
        this.b = b;
        this.c = c;
    }

    public TPNumber copy() {
        return new TPNumber(this.n, this.b, this.c);
    }

    public TPNumber add(TPNumber d) {
        validateSameBaseAndPrecision(d);
        return new TPNumber(this.n + d.n, this.b, this.c);
    }

    public TPNumber multiply(TPNumber d) {
        validateSameBaseAndPrecision(d);
        return new TPNumber(this.n * d.n, this.b, this.c);
    }

    public TPNumber subtract(TPNumber d) {
        validateSameBaseAndPrecision(d);
        return new TPNumber(this.n - d.n, this.b, this.c);
    }

    public TPNumber divide(TPNumber d) {
        validateSameBaseAndPrecision(d);
        if (d.n == 0) {
            throw new PNumberException("Division by zero");
        }
        return new TPNumber(this.n / d.n, this.b, this.c);
    }

    public TPNumber inverse() {
        if (this.n == 0) {
            throw new PNumberException("Inverse of zero");
        }
        return new TPNumber(1 / this.n, this.b, this.c);
    }

    public TPNumber square() {
        return new TPNumber(this.n * this.n, this.b, this.c);
    }

    public double getNumber() {
        return this.n;
    }

    public String getNumberAsString() {
        return toPString(this.n, this.b, this.c);
    }

    public int getBase() {
        return this.b;
    }

    public String getBaseAsString() {
        return Integer.toString(this.b);
    }

    public int getPrecision() {
        return this.c;
    }

    public String getPrecisionAsString() {
        return Integer.toString(this.c);
    }

    public void setBase(int newb) {
        validateBase(newb);
        this.b = newb;
    }

    public void setBase(String bs) {
        int newb = Integer.parseInt(bs);
        setBase(newb);
    }

    public void setPrecision(int newc) {
        validatePrecision(newc);
        this.c = newc;
    }

    public void setPrecision(String cs) {
        int newc = Integer.parseInt(cs);
        setPrecision(newc);
    }

    private static void validateBase(int b) {
        if (b < 2 || b > 16) {
            throw new PNumberException("Base must be between 2 and 16");
        }
    }

    private static void validatePrecision(int c) {
        if (c < 0) {
            throw new PNumberException("Precision must be >= 0");
        }
    }

    private void validateSameBaseAndPrecision(TPNumber d) {
        if (this.b != d.b || this.c != d.c) {
            throw new PNumberException("Numbers must have the same base and precision");
        }
    }

    private static double parsePNumber(String s, int base) {
        if (s == null || s.isEmpty()) {
            throw new PNumberException("Invalid number string");
        }
        boolean negative = false;
        if (s.startsWith("-")) {
            negative = true;
            s = s.substring(1);
        }
        String[] parts = s.split("\\.");
        if (parts.length > 2) {
            throw new PNumberException("Invalid number format");
        }
        String intPart = parts[0];
        String fracPart = parts.length > 1 ? parts[1] : "";

        double value = 0.0;

        for (int i = 0; i < intPart.length(); i++) {
            char ch = intPart.charAt(i);
            int digit = digitValue(ch);
            if (digit < 0 || digit >= base) {
                throw new PNumberException("Invalid digit for base: " + ch);
            }
            value += digit * Math.pow(base, intPart.length() - 1 - i);
        }

        for (int i = 0; i < fracPart.length(); i++) {
            char ch = fracPart.charAt(i);
            int digit = digitValue(ch);
            if (digit < 0 || digit >= base) {
                throw new PNumberException("Invalid digit for base: " + ch);
            }
            value += digit * Math.pow(base, -(i + 1));
        }

        return negative ? -value : value;
    }

    private static int digitValue(char ch) {
        if (ch >= '0' && ch <= '9') return ch - '0';
        if (ch >= 'A' && ch <= 'F') return 10 + ch - 'A';
        if (ch >= 'a' && ch <= 'f') return 10 + ch - 'a';
        return -1;
    }

    private static String toPString(double num, int base, int prec) {
        if (num == 0) {
            String frac = prec > 0 ? "." + "0".repeat(prec) : "";
            return "0" + frac;
        }

        boolean negative = num < 0;
        num = Math.abs(num);

        StringBuilder sb = new StringBuilder();

        // Целая часть
        long intVal = (long) num;
        StringBuilder intSb = new StringBuilder();
        if (intVal == 0) {
            intSb.append('0');
        } else {
            while (intVal > 0) {
                int rem = (int) (intVal % base);
                intSb.append(charDigit(rem));
                intVal /= base;
            }
        }
        sb.append(intSb.reverse());

        // Дробная часть
        double frac = num - (long) num;
        if (prec > 0) {
            sb.append('.');
            StringBuilder fracSb = new StringBuilder();
            for (int i = 0; i < prec; i++) {
                frac *= base;
                int digit = (int) frac;
                fracSb.append(charDigit(digit));
                frac -= digit;
            }
            // Округление: если остаток >= 0.5, rounding up
            if (frac >= 0.5) {
                roundUp(fracSb, base);
            }
            sb.append(fracSb);
        }

        if (negative) {
            sb.insert(0, '-');
        }
        return sb.toString();
    }

    private static char charDigit(int d) {
        if (d < 10) return (char) ('0' + d);
        return (char) ('A' + d - 10);
    }

    private static void roundUp(StringBuilder sb, int base) {
        int i = sb.length() - 1;
        while (i >= 0) {
            int digit = digitValue(sb.charAt(i));
            if (digit < base - 1) {
                sb.setCharAt(i, charDigit(digit + 1));
                return;
            } else {
                sb.setCharAt(i, '0');
                i--;
            }
        }
    }

}
