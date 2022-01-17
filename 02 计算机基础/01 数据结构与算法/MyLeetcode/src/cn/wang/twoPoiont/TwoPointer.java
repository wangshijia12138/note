package cn.wang.twoPoiont;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 双指针
 */
public class TwoPointer {
    public static void main(String[] args) {
//        System.out.println("hello");
//        int[] ints = {2, 7, 11, 15};
//        int[] ints1 = twoSum(ints, 9);
//        System.out.println(Arrays.toString(ints1));
//        boolean b = judgeSquareSum(2147483600);
//        System.out.println(b);
//        reverseVowels("hello");
//        validPalindrome("aguokepatgbnvfqmgmlcupuufxoohdfpgjdmysgvhmvffcnqxjjxqncffvmhvgsymdjgpfdhooxfuupuculmgmqfvnbgtapekouga");
        merge(new int[]{0}, 0, new int[]{1}, 1);
    }

    // 在有序数组中找出两个数，使它们的和为 target
    private static int[] twoSum(int[] numbers, int target) {
        if (numbers.length < 2) {
            return null;
        }
        int font = 0;
        int rear = numbers.length - 1;
        while (font < rear) {
            int sum = numbers[font] + numbers[rear];
            if (sum > target) {
                rear--;
            } else if (sum < target) {
                font++;
            } else {
                return new int[]{font + 1, rear + 1};
            }
        }
        return null;
    }

    // 判断一个非负整数是否为两个整数的平方和
    private static boolean judgeSquareSum(int c) {
        if (c < 0) {
            return false;
        }
        long a = 0;
        long b = (int) Math.sqrt(c);
        while (a <= b) {
            long powSum = a * a + b * b;
            if (powSum > c) {
                b--;
            } else if (powSum < c) {
                a++;
            } else {
                return true;
            }
        }
        return false;
    }

    //Reverse Vowels of a String
    private static String reverseVowels(String s) {
        if (Objects.isNull(s)) {
            return null;
        }
        List<Character> vowels = Arrays.asList('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U');
        char[] res = new char[s.length()];

        int font = 0;
        int rear = s.length() - 1;
        while (font <= rear) {
            char f = s.charAt(font);
            char r = s.charAt(rear);
            if (!vowels.contains(f)) {
                res[font++] = f;
                continue;
            }
            if (!vowels.contains(r)) {
                res[rear--] = r;
                continue;
            }
            res[font++] = r;
            res[rear--] = f;
        }
        return new String(res);
    }

    //Valid Palindrome
    private static boolean validPalindrome(String s) {
        if (Objects.isNull(s)) {
            return false;
        }
        for (int font = 0, rear = s.length() - 1; font < rear; font += 1, rear -= 1) {
            if (s.charAt(font) != s.charAt(rear)) {
                return isPalindrome(s, font + 1, rear) || isPalindrome(s, font, rear - 1);
            }
        }
        return true;
    }

    private static boolean isPalindrome(String s, int i, int j) {
        while (i < j) {
            if (s.charAt(i++) != s.charAt(j--)) {
                return false;
            }
        }
        return true;
    }

    //Merge Sorted Array
    private static void merge(int[] nums1, int m, int[] nums2, int n) {
        if (nums1.length != m + n || nums2.length != n) {
            return;
        }
        int index1 = m - 1;
        int index2 = n - 1;
        int indexMerge = m + n - 1;

        while (index2 >= 0) {
            if (index1 < 0) {
                nums1[indexMerge--] = nums2[index2--];
            } else if (nums1[index1] >= nums2[index2]) {
                nums1[indexMerge--] = nums1[index1--];
            } else {
                nums1[indexMerge--] = nums2[index2--];
            }
        }
    }

    //Longest Word in Dictionary through Deleting
    private static String findLongestWord(String s, List<String> dictionary) {
        String res = "";
        for (String value : dictionary) {
            if (!isSubstr(s, value)) {
                continue;
            }
            if (value.length() < res.length()) {
                continue;
            }
            if (value.length() == res.length() && res.compareTo(value) < 0) {
                continue;
            }
            res = value;
        }
        return res;
    }

    private static boolean isSubstr(String str, String subStr) {
        int index1 = 0;
        int index2 = 0;
        while (index1 <= str.length() - 1 && index2 <= subStr.length() - 1) {
            if (str.charAt(index1) == subStr.charAt(index2)) {
                index1++;
                index2++;
            } else {
                index1++;
            }
        }
        return index2 == subStr.length();
    }
}

