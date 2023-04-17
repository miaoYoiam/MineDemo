package com.example.demo.lib_leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Day：2021/11/29 12:04 下午
 *
 * @author zhanglei
 */
public class TestLeekCode {

    public static void main(String[] args) {

        ListNode listNode1 = new ListNode(1);
        listNode1.next = new ListNode(2);
        listNode1.next.next = new ListNode(4);
        listNode1.next.next.next = new ListNode(8);
        listNode1.next.next.next.next = new ListNode(10);

        ListNode listNode2 = new ListNode(1);
        listNode2.next = new ListNode(3);
        listNode2.next.next = new ListNode(4);
        listNode2.next.next.next = new ListNode(5);
        listNode2.next.next.next.next = new ListNode(6);
        listNode2.next.next.next.next.next = new ListNode(7);
        listNode2.next.next.next.next.next.next = new ListNode(9);

        mergeTwoLists(listNode1, listNode2);
    }

    /**
     * 输入：l1 = [1,2,4,8,10], l2 = [1,3,4,5,6,7,9]
     * 输出：[1,1,2,3,4,4,5,6,7]
     * <p>
     * ListNode listNode1 = new ListNode(1);
     * listNode1.next = new ListNode(2);
     * listNode1.next.next = new ListNode(4);
     * listNode1.next.next.next = new ListNode(8);
     * listNode1.next.next.next.next = new ListNode(10);
     * <p>
     * ListNode listNode2 = new ListNode(1);
     * listNode1.next = new ListNode(3);
     * listNode1.next.next = new ListNode(4);
     * listNode1.next.next.next = new ListNode(5);
     * listNode1.next.next.next.next = new ListNode(6);
     * listNode1.next.next.next.next.next = new ListNode(7);
     * listNode1.next.next.next.next.next.next = new ListNode(9);
     *
     * <p>
     * 将两个升序链表合并为一个新的 升序 链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
     */
    public static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        if (list1 == null) {
            return list2;
        }

        if (list2 == null) {
            return list1;
        }

        ListNode dummmy = new ListNode(0);
        ListNode cur = dummmy;

        while (list1 != null && list2 != null) {
            boolean diff = list1.val < list2.val;
            cur.next = diff ? list1 : list2;
            cur = cur.next;
            if (diff) {
                list1 = list1.next;
            } else {
                list2 = list2.next;
            }
        }
        cur.next = list1 == null ? list2 : list1;
        System.out.println(dummmy.next);
        return dummmy.next;
    }

    /**
     * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。
     * <p>
     * 有效字符串需满足：
     * <p>
     * 左括号必须用相同类型的右括号闭合。
     * 左括号必须以正确的顺序闭合。
     * <p>
     * "{(){}()[]()[]}"
     * "{[]}"
     * "([)]"
     *
     * @param s
     * @return
     */
    public static boolean isValid(String s) {
        boolean r = true;

        if (s.length() <= 1) {
            return false;
        }

        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char v = s.charAt(i);
            if (v == '(') {
                stack.push(')');
            } else if (v == '{') {
                stack.push('}');
            } else if (v == '[') {
                stack.push(']');
            } else {
                if (stack.isEmpty()) {
                    r = false;
                    break;
                }

                if (stack.pop() != v) {
                    r = false;
                    break;
                }
            }
        }

        if (!stack.isEmpty()) {
            r = false;
        }

        System.out.println(r);

        return r;
    }

    /**
     * 给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。
     * <p>
     * 输入：head = [1,2,3,4,5], n = 2
     * 输出：[1,2,3,5]
     * <p>
     * 输入：head = [1], n = 1
     * 输出：[]
     * <p>
     * 输入：head = [1,2], n = 1
     * 输出：[1]
     */
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        if (head.next == null && n >= 1) {
            return null;
        }

        Stack<Integer> s0 = new Stack<>();
        while (head != null) {
            s0.push(head.val);
            head = head.next;
        }

        Stack<Integer> s1 = new Stack<>();
        int k = 1;
        while (!s0.empty()) {
            if (k == n) {
                k++;
                s0.pop();
                continue;
            }
            s1.push(s0.pop());
            k++;
        }
        ListNode result = null;

        ListNode r = null;
        while (!s1.empty()) {
            if (r == null) {
                r = new ListNode(s1.pop());
                result = r;
            } else {
                r.next = new ListNode(s1.pop());
                r = r.next;
            }
        }
        System.out.println(result);
        return result;
    }

    /**
     * 给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。答案可以按 任意顺序 返回。
     * <p>
     * 给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。
     * <p>
     * <p>
     * 2:adc
     * 3:def
     * 4:ghi
     * 5:jkl
     * 6:mno
     * 7:pqrs
     * 8:tuv
     * 9:wxyz
     *
     * @param digits
     * @return 输入：digits = "23"
     * 输出：["ad","ae","af","bd","be","bf","cd","ce","cf"]
     * <p>
     * 输入：digits = ""
     * 输出：[]
     */
    public static List<String> letterCombinations(String digits) {
        List<String> r = new ArrayList<>();
        if (digits == null || digits.length() == 0) {
            return r;
        }

        int length = digits.length();
        List<List<String>> data = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            char v = digits.charAt(i);
            switch (v) {
                case '2': {
                    data.add(Arrays.asList("a", "b", "c"));
                    break;
                }
                case '3': {
                    data.add(Arrays.asList("d", "e", "f"));
                    break;
                }
                case '4': {
                    data.add(Arrays.asList("g", "h", "i"));
                    break;
                }
                case '5': {
                    data.add(Arrays.asList("j", "k", "l"));
                    break;
                }
                case '6': {
                    data.add(Arrays.asList("m", "n", "o"));
                    break;
                }
                case '7': {
                    data.add(Arrays.asList("p", "q", "r", "s"));
                    break;
                }
                case '8': {
                    data.add(Arrays.asList("t", "u", "v"));
                    break;
                }
                case '9': {
                    data.add(Arrays.asList("w", "x", "y", "z"));
                    break;
                }
                default: {

                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.setLength(0);
        if (data.size() != 0) {
            appendChild(sb, r, data, 0);
        }
        System.out.println(r);
        return r;

    }

    public static void appendChild(StringBuilder sb, List<String> result, List<List<String>> data, int index) {
        int totalSize = data.size();
        List<String> c = data.get(index);
        for (int k = 0; k < c.size(); k++) {
            String v = c.get(k);
            if (index == totalSize - 1) {
                sb.append(v);
                result.add(sb.toString());
                System.out.println(String.format("last index:%d  v:%s   sb:%s", index, v, sb.toString()));
                sb.setLength(sb.length() - 1);
            } else {
                sb.setLength(index);
                sb.append(v);
                System.out.println(String.format("index:%d  v:%s sb:%s", index, v, sb.toString()));
                int i = index + 1;
                appendChild(sb, result, data, i);
            }
        }
    }


    /**
     * 给你一个包含 n 个整数的数组 nums，
     * 判断 nums 中是否存在三个元素 a，b，c ，
     * 使得 a + b + c = 0 ？请你找出所有和为 0 且不重复的三元组。
     * <p>
     * 注意：答案中不可以包含重复的三元组。
     * <p>
     * 输入：nums = [-1,0,1,2,-1,-4]
     * 输出：[[-1,-1,2],[-1,0,1]]
     */
    public static List<List<Integer>> threeSumV2(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length < 3) {
            return result;
        }
        Arrays.sort(nums);

        int length = nums.length;
        int aKey = 0;
        while (aKey < length - 2) {
            int a = nums[aKey];
            if (a > 0) {
                break;
            }
            if (aKey > 0 && a == nums[aKey - 1]) {
                aKey++;
                continue;
            }
            int bKey = aKey + 1;
            int b = nums[bKey];
            if (a + b > 0) {
                break;
            }
            int cKey = length - 1;
            while (cKey > bKey) {
                b = nums[bKey];
                int c = nums[cKey];
                if (c < Math.abs(a + b)) {
                    break;
                }
                if (a + b + c == 0) {
                    result.add(Arrays.asList(a, b, c));
                    while (bKey < cKey && nums[bKey + 1] == b) {
                        bKey++;
                    }
                    while (bKey < cKey && nums[cKey - 1] == c) {
                        cKey--;
                    }
                    cKey--;
                    bKey++;
                } else if (a + b + c > 0) {
                    cKey--;
                } else {
                    bKey++;
                }
            }
            aKey++;
        }
        System.out.println(result);
        return result;
    }

    /**
     * 给定一个长度为 n 的整数数组 height 。有 n 条垂线，第 i 条线的两个端点是 (i, 0) 和 (i, height[i]) 。
     * <p>
     * 找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
     * <p>
     * 返回容器可以储存的最大水量。
     * <p>
     * 说明：你不能倾斜容器。
     * <p>
     * 输入：[1,8,6,2,5,4,8,3,7]
     * 输出：49
     * 解释：图中垂直线代表输入数组 [1,8,6,2,5,4,8,3,7]。在此情况下，容器能够容纳水（表示为蓝色部分）的最大值为 49。
     */
    public static int maxAreaV2(int[] height) {
        int length = height.length;
        int max = 0;

        int left = 0;
        int right = length - 1;

        while (left <= right) {
            int x = right - left;
            int y = Math.min(height[right], height[left]);
            int area = x * y;
            if (area > max) {
                max = area;
            }

            if (y == height[right]) {
                right--;
            }
            if (y == height[left]) {
                left++;
            }
        }

        System.out.println(max);

        return max;
    }

    public static int maxArea(int[] height) {
        int length = height.length;
        int max = 0;
        for (int i = 0; i < length; i++) {
            int startY = height[i];
            for (int j = i + 1; j < length; j++) {
                int endY = height[j];
                int minY = endY;
                if (endY > startY) {
                    minY = startY;
                }
                int area = (j - i) * minY;
                if (area > max) {
                    max = area;
                }
            }
        }
        return max;
    }

    /**
     * 给你一个字符串 s，找到 s 中最长的回文子串。
     * <p>
     * 输入：s = "babad"
     * 输出："bab"
     * 解释："aba" 同样是符合题意的答案。
     * <p>
     * 示例 2：
     * <p>
     * 输入：s = "cbbd"
     * 输出："bb"
     * <p>
     * a
     * aa
     * aba
     * abba
     * abbba
     * abbbba
     */
    public static String longestPalindrome(String s) {
        if (s == null || s.length() == 0) {
            return null;
        }

        if (s.length() == 1) {
            return s;
        }

        String r = "";
        int maxLength = 0;
        StringBuilder pBuilder = new StringBuilder();
        StringBuilder nBuilder = new StringBuilder();

        String reverse = pBuilder.append(s).reverse().toString();
        if (s.equals(reverse)) {
            return s;
        }

        for (int i = 0; i < s.length(); i++) {
            pBuilder.setLength(0);
            pBuilder.append(s.charAt(i));

            nBuilder.setLength(0);
            nBuilder.insert(0, s.charAt(i));

            for (int j = i + 1; j < s.length(); j++) {
                pBuilder.append(s.charAt(j));
                String v = pBuilder.toString();

                nBuilder.insert(0, s.charAt(j));
                String n = nBuilder.toString();

                if (v.equals(n) && v.length() > maxLength) {
                    r = v;
                    maxLength = v.length();
                }
            }
        }

        if (r.length() == 0 && s.length() != 0) {
            r = String.valueOf(s.charAt(0));
        }

        System.out.println(r);

        return r;

    }

    public static int test(int nums[]) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        Arrays.sort(nums);

        int maxSum = 0;

        int sum = 0;
        int previous = nums[0];
        for (int i = 0; i < nums.length; i++) {
            if (previous + 1 != nums[i]) {
                if (sum > maxSum) {
                    maxSum = sum;
                }
                sum = nums[i];
            } else {
                sum += nums[i];
            }
            previous = nums[i];
        }
        if (sum > maxSum) {
            maxSum = sum;
        }
        return maxSum;
    }


    /**
     * 给你一个数组 nums，请你从中抽取一个子序列，满足该子序列的元素之和 严格 大于未包含在该子序列中的各元素之和。
     * <p>
     * 如果存在多个解决方案，只需返回 长度最小 的子序列。如果仍然有多个解决方案，则返回 元素之和最大 的子序列。
     * <p>
     * 与子数组不同的地方在于，「数组的子序列」不强调元素在原数组中的连续性，也就是说，它可以通过从数组中分离一些（也可能不分离）元素得到。
     * <p>
     * 注意，题目数据保证满足所有约束条件的解决方案是 唯一 的。同时，返回的答案应当按 非递增顺序 排列。
     * <p>
     * <p>
     * 解题思路：根据题意可以得出是顺序的，所以先排序。 然后长度最小，和最大的子序列，肯定是顺序依次往下index递增
     *
     * @param
     * @return
     */
    public static List<Integer> minSubsequenceV2(int[] nums) {
        List<Integer> list = new ArrayList<Integer>();
        int total = 0;
        for (int s : nums) {
            total += s;
        }

        Arrays.sort(nums);

        int sub = 0;
        for (int i = nums.length - 1; i >= 0; i--) {

            System.out.println("list");
            System.out.println(list);

            sub += nums[i];
            list.add(nums[i]);
            if (sub > total - sub) {
                break;
            }

        }
        return list;
    }

    /**
     * 给你一个长度为n的整数数组nums，其中n > 1，返回输出数组output，
     * 其中 output[i]等于nums中除nums[i]之外其余各元素的乘积。
     * <p>
     *
     * <p>
     * 示例:
     * <p>
     * 输入: [1,2,3,4]
     * 输出: [24,12,8,6]
     *
     * <p>
     * n0 n1 n2 n3
     * o0 =    n1*n2*n3
     * o1 = n0*   n2*n3
     * o2 = n0*n1   *n3
     * o3 = n0*n1*n2
     * <p>
     * 提示：题目数据保证数组之中任意元素的全部前缀元素和后缀（甚至是整个数组）的乘积都在 32 位整数范围内。
     * <p>
     * 说明: 请不要使用除法，且在O(n) 时间复杂度内完成此题。
     * <p>
     * 进阶：
     * 你可以在常数空间复杂度内完成这个题目吗？（ 出于对空间复杂度分析的目的，输出数组不被视为额外空间。）
     * <p>
     */
    public static int[] productExceptSelf(int[] nums) {
        int[] output = new int[nums.length];

        int[] left = new int[nums.length];
        int[] right = new int[nums.length];

        int totalLeft = 1;
        for (int i = 0; i < nums.length; i++) {
            if (left[i] == 0) {
                left[i] = 1;
            }
            left[i] *= totalLeft;
            totalLeft *= nums[i];
        }

        int totalRight = 1;
        for (int i = nums.length - 1; i >= 0; i--) {
            if (right[i] == 0) {
                right[i] = 1;
            }
            right[i] *= totalRight;
            totalRight *= nums[i];
        }

        for (int i = 0; i < nums.length; i++) {
            output[i] = left[i] * right[i];
        }

        return output;
    }

    /**
     * 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
     * <p>
     * 请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
     * <p>
     * 示例 1:
     * 输入: [3,2,1,5,6,4] 和 k = 2
     * 输出: 5
     * <p>
     * 示例2:
     * 输入: [3,2,3,1,2,4,5,5,6] 和 k = 4
     * 输出: 4
     * <p>
     * 提示：
     * 1 <= k <= nums.length <= 104
     * -104<= nums[i] <= 104
     */
    public int findKthLargest(int[] nums, int k) {
        Arrays.sort(nums);

        return nums[nums.length - k - 1];
    }


    /**
     * 从上到下打印出二叉树的每个节点，同一层的节点按照从左到右的顺序打印。
     * 例如:
     * 给定二叉树:[3,9,20,null,null,15,7],
     * <p>
     * 3
     * /       \
     * 9        20
     * /  \      /  \
     * 15  7     8    9
     * 返回：
     * <p>
     * [3,9,20,15,7]
     * <p>
     * <p>
     * 提示：
     * 节点总数 <= 1000
     */

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public static int[] levelOrder() {
        ArrayList<Integer> result = new ArrayList<>();

        TreeNode node = new TreeNode(3);
        node.left = new TreeNode(9);
        node.right = new TreeNode(20);
        node.left.left = new TreeNode(15);
        node.left.right = new TreeNode(7);
        node.right.left = new TreeNode(8);
        node.right.right = new TreeNode(9);
        getNode(result, node);

        int[] array = new int[result.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = result.get(i);
        }
        return array;
    }

    public static void getNode(ArrayList<Integer> result, TreeNode root) {
        result.add(root.val);

        List<TreeNode> childList = new ArrayList<>();
        if (root.left != null) {
            childList.add(root.left);
            result.add(root.left.val);
        }
        if (root.right != null) {
            childList.add(root.right);
            result.add(root.right.val);
        }
        getNodeChild(result, childList);
    }

    public static void getNodeChild(ArrayList<Integer> result, List<TreeNode> root) {

        List<TreeNode> childList = new ArrayList<>();

        System.out.println(root.size());
        for (int i = 0; i < root.size(); i++) {
            TreeNode item = root.get(i);

            if (item.left != null) {
                result.add(item.left.val);
                childList.add(item.left);
            }
            if (item.right != null) {
                result.add(item.right.val);
                childList.add(item.right);
            }
        }

        root.clear();
        if (!childList.isEmpty()) {
            getNodeChild(result, childList);
        }
    }

    /**
     * 给你一个整数数组nums ，除某个元素仅出现 一次 外，其余每个元素都恰出现 三次 。请你找出并返回那个只出现了一次的元素。
     * <p>
     * <p>
     * 示例 1：
     * <p>
     * 输入：nums = [2,2,3,2]
     * 输出：3
     * 示例 2：
     * <p>
     * 输入：nums = [0,1,0,1,0,1,99]
     * 输出：99
     */


    public static int singleNumber(int nums[]) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        Arrays.sort(nums);

        boolean single = true;
        int result = -1;
        for (int i = 0; i < nums.length; i++) {
            int item = nums[i];
            if (i == nums.length - 1) {
                if (single) {
                    result = item;
                }
                break;
            }
            if (item == nums[i + 1]) {
                single = false;
            } else {

                if (!single) {
                    single = true;
                    continue;
                }

                result = item;
                break;
            }
        }

        System.out.println(result);
        return result;
    }


    public int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];

        for (int i = 0; i < nums.length; i++) {
            int curV = nums[i];
            if (curV > target) {
                continue;
            }
            for (int j = i + 1; j < nums.length; j++) {
                int nextV = nums[j];
                if (curV + nextV == target) {
                    result[0] = i;
                    result[1] = j;
                    break;
                }
            }
        }
        return result;
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

        @Override
        public String toString() {
            return "val:" + val + " " + (next == null ? "" : next.toString());
        }
    }

    /**
     * 给你两个 非空 的链表，表示两个非负的整数。它们每位数字都是按照 逆序 的方式存储的，并且每个节点只能存储 一位 数字。
     * <p>
     * 请你将两个数相加，并以相同形式返回一个表示和的链表。
     * <p>
     * 你可以假设除了数字 0 之外，这两个数都不会以 0 开头。
     * <p>
     * <p>
     * 输入：l1 = [2,4,3], l2 = [5,6,4]
     * 输出：[7,0,8]
     * 解释：342 + 465 = 807
     */
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        l1 = new ListNode(2);
        l1.next = new ListNode(4);
        l1.next.next = new ListNode(3);

        l2 = new ListNode(5);
        l2.next = new ListNode(6);
        l2.next.next = new ListNode(4);

        //l1 l2 非空
        StringBuilder sb1 = new StringBuilder();
        while (l1 != null) {
            sb1.append(l1.val);
            l1 = l1.next;
        }
        int v1 = Integer.parseInt(sb1.reverse().toString());

        StringBuilder sb2 = new StringBuilder();
        while (l2 != null) {
            sb2.append(l2.val);
            l2 = l2.next;
        }
        int v2 = Integer.parseInt(sb2.reverse().toString());
        String v3 = String.valueOf((v1 + v2));

        ListNode node = new ListNode(0);
        ListNode result = node;
        for (int i = v3.length() - 1; i >= 0; i--) {
            int v = Integer.parseInt(String.valueOf(v3.charAt(i)));
            if (i == v3.length() - 1) {
                node.val = v;
            } else {
                node.next = new ListNode(v);
                node = node.next;
            }
        }
        return result;
    }

    public static ListNode addTwoNumbersV2(ListNode l1, ListNode l2) {
        ListNode node = new ListNode(0);
        ListNode r = node;
        while (l1 != null || l2 != null) {
            int v1 = l1 != null ? l1.val : 0;
            int v2 = l2 != null ? l2.val : 0;
            int sum = v1 + v2 + node.val;
            if (sum >= 10) {
                node.val = sum % 10;
                node.next = new ListNode(1);
            } else {
                node.val = sum;
            }
            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }

            if (l1 == null && l2 == null) {
                break;
            }

            if (node.next == null) {
                node.next = new ListNode(0);
            }
            node = node.next;
        }
        return r;
    }

    /**
     * 给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
     * <p>
     * 输入: s = "abcabcbb"
     * 输出: 3
     * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
     * <p>
     * 输入: s = "bbbbb"
     * 输出: 1
     * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
     * <p>
     * 输入: s = "pwwkew"
     * 输出: 3
     * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
     *      请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
     */

    public static int lengthOfLongestSubstring(String s) {
        int max = 0;

        if (s == null || s.length() == 0) {
            return 0;
        }

        if (s.length() == 1) {
            return 1;
        }

        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            for (int j = i; j < s.length(); j++) {
                int v = s.charAt(j);
                if (temp.contains(v)) {
                    int cur = temp.size();
                    if (cur > max) {
                        max = cur;
                    }
                    temp.clear();
                    break;
                } else {
                    temp.add(v);
                }
            }
        }
        return max;
    }

}
