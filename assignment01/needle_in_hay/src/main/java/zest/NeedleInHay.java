package zest;

class NeedleInHay {
    public static int find(String haystack, String needle) {
        if (haystack == null || needle == null) return -1;
        if (haystack.isEmpty() && needle.isEmpty()) return 0;
        else if (haystack.isEmpty() || needle.isEmpty()) return -1;

        int lenHay = haystack.length(), lenNed = needle.length();

        for(int i = 0; i < (lenHay-lenNed + 1); i++) {
            if(haystack.charAt(i) == needle.charAt(0)) {
                int flag = 0;
                for(int j = 1; j < lenNed; j++) {
                    if(haystack.charAt(i+j) != needle.charAt(j)) {
                        flag =1;
                        break;
                    }
                }
                if(flag == 0) {
                    return i;
                }
            }
        }
        return -1;
    }
}
