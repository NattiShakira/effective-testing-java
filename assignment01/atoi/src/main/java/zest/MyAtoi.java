package zest;

class MyAtoi {
    public static int myAtoi(String s) {
        if (s == null) {
            return 0;
        }

        // ADDED:Initial pass to skip only whitespaces
        int i = 0;
        while (i < s.length() && s.charAt(i) == ' ') {
            i++;
        }

        // 'i' is at the first non-space character or past the end of the string
        if (i == s.length()) {
            return 0;
        }

        long num = 0;
        int sign = 1;

        // Check for sign, leveraging the current position of 'i'
        if (s.charAt(i) == '+' || s.charAt(i) == '-') {
            sign = (s.charAt(i) == '-') ? -1 : 1;
            i++; // Move past the sign for digit processing
        }

        // Process digits
        while (i < s.length() && Character.isDigit(s.charAt(i))) {
            int digit = s.charAt(i) - '0';

            // Check for overflow
            if (num > (Integer.MAX_VALUE - digit) / 10) {
                return (sign == 1) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            }

            num = num * 10 + digit;
            i++;
        }

        return (int) (sign * num);
    }
}
