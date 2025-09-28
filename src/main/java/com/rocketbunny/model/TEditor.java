package com.rocketbunny.model;

import com.rocketbunny.exception.EditorException;

import java.util.regex.Pattern;

public class TEditor {
    private String string;
    private static final String DECIMAL_SEPARATOR = ",";
    private static final String COMPLEX_SEPARATOR = " + i*";
    private static final String ZERO_STRING = "0 + i*0";
    private static final Pattern PREFIX_PATTERN = Pattern.compile("^[+-]?\\d*(" + Pattern.quote(DECIMAL_SEPARATOR) + "\\d*)?(" + Pattern.quote(COMPLEX_SEPARATOR) + "[+-]?\\d*(" + Pattern.quote(DECIMAL_SEPARATOR) + "\\d*)?)?$");

    public TEditor() {
        this.string = ZERO_STRING;
    }

    public String getString() {
        return string;
    }

    public void setString(String s) {
        if (PREFIX_PATTERN.matcher(s).matches()) {
            this.string = s;
        } else {
            throw new EditorException("Invalid complex number format");
        }
    }

    public boolean isComplexZero() {
        try {
            double[] parts = parseComplex();
            return parts[0] == 0.0 && parts[1] == 0.0;
        } catch (EditorException e) {
            return false;
        }
    }

    public String addSign() {
        String oldString = string;
        int sepPos = string.indexOf(COMPLEX_SEPARATOR);
        if (sepPos == -1 || string.equals(ZERO_STRING)) {
            if (string.startsWith("-")) {
                string = string.substring(1);
            } else if (!string.equals("")) {
                string = "-" + string;
            } else {
                string = "-0";
            }
        } else {
            String imag = string.substring(sepPos + COMPLEX_SEPARATOR.length());
            if (imag.startsWith("-")) {
                imag = imag.substring(1);
            } else {
                imag = "-" + imag;
            }
            string = string.substring(0, sepPos + COMPLEX_SEPARATOR.length()) + imag;
        }
        if (!PREFIX_PATTERN.matcher(string).matches()) {
            string = oldString;
        }
        return string;
    }

    public String addDigit(int a) {
        if (a < 0 || a > 9) {
            throw new EditorException("Digit must be 0-9");
        }
        String oldString = string;
        if (string.equals(ZERO_STRING) || string.equals("")) {
            string = String.valueOf(a); // Начинаем заново
        } else {
            string += a;
        }
        if (!PREFIX_PATTERN.matcher(string).matches()) {
            string = oldString;
        }
        return string;
    }

    public String addZero() {
        return addDigit(0);
    }

    public String backspace() {
        if (string.length() > 0) {
            string = string.substring(0, string.length() - 1);
        }
        return string;
    }

    public String clear() {
        string = ZERO_STRING;
        return string;
    }

    public String edit(int a) {
        if (a >= 0 && a <= 9) {
            return addDigit(a);
        } else if (a == 10) {
            return addSign();
        } else if (a == 11) {
            return addDecimalSeparator();
        } else if (a == 12) {
            return addComplexSeparator();
        } else if (a == 13) {
            return backspace();
        } else if (a == 14) {
            return clear();
        } else {
            throw new EditorException("Invalid edit command");
        }
    }

    private String addDecimalSeparator() {
        String oldString = string;
        String newStr = string + DECIMAL_SEPARATOR;
        if (PREFIX_PATTERN.matcher(newStr).matches()) {
            string = newStr;
        }
        return string;
    }

    private String addComplexSeparator() {
        String oldString = string;
        String newStr = string + COMPLEX_SEPARATOR;
        if (PREFIX_PATTERN.matcher(newStr).matches()) {
            string = newStr;
        }
        return string;
    }

    private double[] parseComplex() throws EditorException {
        if (!PREFIX_PATTERN.matcher(string).matches()) {
            throw new EditorException("Invalid format for parsing");
        }
        int sepPos = string.indexOf(COMPLEX_SEPARATOR);
        String realStr = (sepPos != -1) ? string.substring(0, sepPos) : string;
        String imagStr = (sepPos != -1) ? string.substring(sepPos + COMPLEX_SEPARATOR.length()) : "0";

        realStr = realStr.replace(DECIMAL_SEPARATOR, ".");
        imagStr = imagStr.replace(DECIMAL_SEPARATOR, ".");

        try {
            double real = Double.parseDouble(realStr);
            double imag = Double.parseDouble(imagStr);
            return new double[]{real, imag};
        } catch (NumberFormatException e) {
            throw new EditorException("Parse error");
        }
    }
}
