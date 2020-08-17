package edu.princeton.cs.other;

import java.util.ArrayDeque;
import java.util.Deque;

import static java.lang.System.out;

/**
 * 最长公共子序列  longest common subsequence  lcs 定义：两个或多个已知数列的子序列集合中最长的就是最长公共子序列。
 * 比如数列A = “abcdef”和B = “adefcb”，那么两个数列的公共子序列集合有{”a","ab","abc","adef",等等}，
 * 其中最长的就是adef，这就是最长公共子序列。
 * 注意：最长公共子序列的公共子序列里的元素可以不相邻，但是公共子字符串必须是连接在一起的，
 * 比如A和B的公共子字符串是“def”。
 * 用动态规划法来求解最长公共子序列，因为最长公共子序列具有最有子结构性质，可以分成子问题来递归求最优解，
 * 最后组合子问题求解出问题。用c[i][j]记录X[i]与Y[j]
 *  的LCS 的长度，求解问题c[i,j]，可以分成c[i-1][j-1]、c[i-1][j]、c[i][j-1]子问题来求解，
    最长公共子串 lcs1
 * @author Mageek Chiu
 */
class LCS {

    /**
     * 动态规划
     假设Z=<z1,z2,⋯,zk>是X与Y的LCS， 我们观察到
     如果Xm=Yn，则Zk=Xm=Yn，有Zk−1是Xm−1与Yn−1的LCS；
     如果Xm≠Yn，则Zk是Xm与Yn−1的LCS，或者是Xm−1与Yn的LCS。
     因此，求解LCS的问题则变成递归求解的两个子问题。但是，上述的递归求解的办法中，重复的子问题多，效率低下。
     改进的办法——用空间换时间，用数组保存中间状态，方便后面的计算。这就是动态规划（DP)的核心思想了。
     DP求解LCS
     用二维数组c[i][j]记录串x1x2⋯xi与y1y2⋯yj的LCS长度，则可得到状态转移方程
     */
//    public static int lcs(String str1, String str2) {
    public static String lcs(String str1, String str2) {

        int len1 = str1.length();
        int len2 = str2.length();
        int c[][] = new int[len1+1][len2+1];
        int b[][] = new int[len1+1][len2+1];

        for (int i = 0; i <= len1; i++) {
            for( int j = 0; j <= len2; j++) {
                if(i == 0 || j == 0) {
                    c[i][j] = 0;
                } else if (str1.charAt(i-1) == str2.charAt(j-1)) {
                    c[i][j] = c[i-1][j-1] + 1;
                    // 记录搜索方向
                    b[i][j] = 0;// 左上
                } else {
                    c[i][j] = max(c[i - 1][j], c[i][j - 1]);
                    // 记录搜索方向
                    if (c[i - 1][j]>c[i][j - 1])
                        b[i][j] = 1;// 上
                    else
                        b[i][j] = -1;//左
                }
            }
        }
//        System.out.println("LCS: "+printLcs(str1,b,len1,len2));

//        return c[len1][len2];
        return printLcs(str1,b,len1,len2);
    }

    /**
     * 打印最长公共子序列
     * @return
     */
    public static String printLcs(String a,int[][] B,int i, int j){
        String res = "";
        if (i==0 || j==0) return "";
        if (B[i][j]==0){
            res += printLcs(a,B,i-1,j-1);
            res += a.substring(i-1,i);
        }else if(B[i][j]==1){
            res += printLcs(a,B,i-1,j);
        }else {
            res += printLcs(a,B,i,j-1);
        }
        return res;
    }

    private static int max(int a, int b){
        return (a > b) ? a : b;
    }


    /**
     * 前面提到了子串是一种特殊的子序列，因此同样可以用DP来解决。定义数组的存储含义对于后面推导转移方程显得尤为重要，
     * 糟糕的数组定义会导致异常繁杂的转移方程。考虑到子串的连续性，
     * 将二维数组c[i][j]用来记录具有这样特点的子串——结尾同时也为为串x1x2⋯xi与y1y2⋯yj的结尾——的长度。
     * @param str1
     * @param str2
     * @return
     */
//    public static int lcs1(String str1, String str2) {
    public static String lcs1(String str1, String str2) {

        int len1 = str1.length();
        int len2 = str2.length();
        int mark1=0,mark2=0;
        int result = 0;     //记录最长公共子串长度
        int c[][] = new int[len1+1][len2+1];

        for (int i = 0; i <= len1; i++) {
            for( int j = 0; j <= len2; j++) {
                if(i == 0 || j == 0) {
                    c[i][j] = 0;
                } else if (str1.charAt(i-1) == str2.charAt(j-1)) {
                    c[i][j] = c[i-1][j-1] + 1;
                    // 子序列以为可以利用前面的子序列，所以肯定是最后一个最大，子串不一定，所以要比较
                    if (c[i][j]>result){
                        result = c[i][j];
                        mark1 = i;
                        mark2=j;
                    }
                } else {// 不相等，直接置零
                    c[i][j] = 0;
                }
            }
        }

//        System.out.println("LCS1: "+printLcs1(str1,str2,mark1-1,mark2-1,result));

//        return result;
        return printLcs1(str1,str2,mark1-1,mark2-1,result);
    }

    /**
     * 打印最长公共子串
     * @return
     */
    public static String printLcs1(String a,String b,int i, int j,int len){
        String res = "";
        if (len==0) return "";
        while (i>=0&&j>=0){
            if (a.substring(i,i+1).equals(b.substring(j,j+1))){
                res = a.substring(i,i+1)+res;
                i--;j--;
            }else {// 不等直接就断了
                break;
            }
        }
        return res;

    }

    /**
     *  * 给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为1000。
     示例 1：
     输入: "babad"
     输出: "bab"
     注意: "aba"也是一个有效答案。
     示例 2：
     输入: "cbbd"
     输出: "bb"

     实际上就是最长公共子串问题,正串和反串的最大公共子串就是最大回文串
     注意上面这种想法是错误的，反例就是 "abcdasdfghjkldcba"
     我们可以看到，当 SS 的其他部分中存在非回文子串的反向副本时，最长公共子串法就会失败。
     为了纠正这一点，每当我们找到最长的公共子串的候选项时，都需要检查子串的索引是否与反向子串的原始索引相同。
     如果相同，那么我们尝试更新目前为止找到的最长回文子串；如果不是，我们就跳过这个候选项并继续寻找下一个候选。

     这给我们提供了一个复杂度为 O(n^2) 动态规划解法，它将占用 O(n^2) 的空间（可以改进为使用 O(n)O(n) 的空间）。
     请在这里阅读更多关于最长公共子串的内容。http://en.wikipedia.org/wiki/Longest_common_substring

     方法二：暴力法
     很明显，暴力法将选出所有子字符串可能的开始和结束位置，并检验它是不是回文。
     复杂度分析
     时间复杂度：O(n^3)假设 n 是输入字符串的长度，则  ​n(n−1)/2
     ​​  为此类子字符串(不包括字符本身是回文的一般解法)的总数。因为验证每个子字符串需要 O(n) 的时间，
     所以运行时间复杂度是 O(n^3)  空间复杂度：O(1)。

     方法三：动态规划
     为了改进暴力法，我们首先观察如何避免在验证回文时进行不必要的重复计算。“ababa” 这个示例。
     如果我们已经知道“bab” 是回文，那么很明显，“ababa” 一定是回文，因为它的左首字母和右尾字母是相同的。

     我们给出 P(i,j)的定义如下：

     P(i,j)=true,如果子串Si…Sj是回文子串,false,其它情况
     因此，

     P(i, j) =  P(i,j)=(P(i+1,j−1) and Si​​ ==S​j
     ​​ )

     基本示例如下：

     P(i,i)=true

     P(i,i+1)=(S​i​​ ==S​i+1​ )

     这产生了一个直观的动态规划解法，我们首先初始化一字母和二字母的回文，然后找到所有三字母回文，并依此类推…

     复杂度分析

     时间复杂度：O(n^2)这里给出我们的运行时间复杂度为 O(n^2)

     空间复杂度：O(n^2) 该方法使用 O(n^2)的空间来存储表。

     方法四：中心扩展算法
     事实上，只需使用恒定的空间，我们就可以在 O(n^2)的时间内解决这个问题。
     我们观察到回文中心的两侧互为镜像。因此，回文可以从它的中心展开，并且只有 2n - 1 个这样的中心。
     你可能会问，为什么会是 2n - 1 个，而不是 n 个中心？原因在于所含字母数为偶数的回文的中心可以处于两字母之间
     （例如 “abba” 的中心在两个 ‘b’ 之间）,可以分奇偶，也可以合并

     public String longestPalindrome(String s) {
     int start = 0, end = 0;
     for (int i = 0; i < s.length(); i++) {
     int len1 = expandAroundCenter(s, i, i);
     int len2 = expandAroundCenter(s, i, i + 1);
     int len = Math.max(len1, len2);
     if (len > end - start) {
     start = i - (len - 1) / 2;
     end = i + len / 2;
     }
     }
     return s.substring(start, end + 1);
     }

     private int expandAroundCenter(String s, int left, int right) {
     int L = left, R = right;
     while (L >= 0 && R < s.length() && s.charAt(L) == s.charAt(R)) {
     L--;
     R++;
     }
     return R - L - 1;
     }

     方法五：Manacher 算法
     还有一个复杂度为 O(n)O(n) 的 Manacher 算法，你可以在这里找到详尽的解释。然而，这是一个非同寻常的算法，
     在45分钟的编码时间内提出这个算法将会是一个不折不扣的挑战。但是，请继续阅读并理解它，我保证这将是非常有趣的。

     * @param a
     * @return
     */
    public static String longestPalindromeWithBreak(String a) {
        StringBuilder builder = new StringBuilder();
        for (int i = a.length()-1;i>=0;i--)
            builder.append(a.substring(i,i+1));
        String b = builder.toString();//反串
        return lcs1(a,b);
    }



    /**
     *
     * @param a
     * @return
     */
    public static String longestPalindrome(String a) {
        String s1 = longestPalindrome1(a);
        String s2 = longestPalindrome2(a);
        return s1.length()>s2.length()?s1:s2;

    }
    // 奇数的回文
    public static String longestPalindrome1(String a) {
        String ans = "";
        int maxLen = 0;
        for (int i = 0;i<a.length();i++){
            int s = i,e=i;
            while (s>=0 && e<a.length()){
                if (a.substring(s,s+1).equals(a.substring(e,e+1))){
                    if (e+1-s>maxLen){
                        ans = a.substring(s,e+1);
                        maxLen = e+1-s;
                    }
                }else {
                    break;
                }
                s--;
                e++;
            }
        }
        return ans;
    }
    // 偶数的回文
    public static String longestPalindrome2(String a) {
        String ans = "";
        int maxLen = 0;
        for (int i = 0;i<a.length();i++){
            int s = i,e=i+1;
            while (s>=0 && e<a.length()){
                if (a.substring(s,s+1).equals(a.substring(e,e+1))){
                    if (e+1-s>maxLen){
                        ans = a.substring(s,e+1);
                        maxLen = e+1-s;
                    }
                }else {
                    break;
                }
                s--;
                e++;
            }
        }
        return ans;
    }

    /**
     * 判断整数是否是回文，首先转化为字符串,然后可以用翻转直接比较 也可用递归
     * @param x
     * @return
     */
    public static boolean isPalindrome(int x) {
        String s = String.valueOf(x);
        return isPalindrome(s,0,s.length()-1);
    }

    // 字符串回文
    public static boolean isPalindrome(String s,int i,int j) {
        if (i >= j) return true;
        return s.charAt(i) == s.charAt(j) && isPalindrome(s,i + 1, j - 1);
    }


    // 给定一个字符串，验证它是否是回文串，只考虑字母和数字字符，可以忽略字母的大小写。
    // "A man, a plan, a canal: Panama" true
    // "race a car"  false
    public static boolean isPalindromeCharOnly(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (c>='0' && c<='9' || c>='a' && c<='z' || c>='A' && c<='Z' ){
                sb.append(c);
            }
        }
        String StringOnly = sb.toString().toLowerCase();
        return isPalindrome(StringOnly,0,StringOnly.length()-1);
    }

    /**
     给定一个非空字符串 s，最多删除一个字符。判断是否能成为回文字符串。
     示例 1:
     输入: "aba"
     输出: True
     示例 2:
     输入: "abca"
     输出: True
     解释: 你可以删除c字符。
     注意:
     字符串只包含从 a-z 的小写字母。字符串的最大长度是50000。
     */
    public static boolean validPalindrome(String s) {
        return isPalindrome1(s,0,s.length()-1,0);
    }

    // 字符串回文最多删除一个字符
    public static boolean isPalindrome1(String s,int i,int j,int deleteNumber) {
        if (i >= j) return true;
        if( s.charAt(i) == s.charAt(j)){
            return  isPalindrome1(s,i + 1, j - 1,deleteNumber);
        }else {
            deleteNumber++;
            if (deleteNumber>1) return false;
            return  isPalindrome1(s,i+1,j,deleteNumber) || isPalindrome1(s,i,j-1,deleteNumber);//尝试删除两边的
        }
    }

    /**
     * 判断数字是否回文，不用字符串 可以用栈

     * @param x
     * @return
     */
    public static boolean isPalindromeWithoutString(int x) {
        if (x<0) return false;// 因为有负号肯定不是回文

        Deque<Integer> integerStack = new ArrayDeque<>();
        int mod = x % 10;
        integerStack.push(mod);
        int left = x - mod;
        left /= 10;
        while (left>10){
            mod = left%10;
            integerStack.push(mod);
            left -= mod;
            left /= 10;
        }
        integerStack.push(left);

        Integer result = 0,tmp;
        int times = 1;
        while ((tmp = integerStack.poll()) !=null){
            result += times*tmp;
            times *= 10;
        }

        return result.equals(x);
    }

    /**
     还有更妙的 反转一半数字

     映入脑海的第一个想法是将数字转换为字符串，并检查字符串是否为回文。但是，这需要额外的非常量空间来创建问题描述中所不允许的字符串。
     第二个想法是将数字本身反转，然后将反转后的数字与原始数字进行比较，如果它们是相同的，那么这个数字就是回文。
     但是，如果反转后的数字大于 \text{int.MAX}int.MAX，我们将遇到整数溢出问题。

     按照第二个想法，为了避免数字反转可能导致的溢出问题，为什么不考虑只反转 \text{int}int 数字的一半？毕竟，如果该数字是回文，
     其后半部分反转后应该与原始数字的前半部分相同。

     例如，输入 1221，我们可以将数字“1221”的后半部分从“21”反转为“12”，并将其与前半部分“12”进行比较，因为二者相同，我们得知数字 1221 是回文。
     让我们看看如何将这个想法转化为一个算法。

     算法

     首先，我们应该处理一些临界情况。所有负数都不可能是回文，例如：-123 不是回文，因为 - 不等于 3。所以我们可以对所有负数返回 false。

     现在，让我们来考虑如何反转后半部分的数字。 对于数字 1221，如果执行 1221 % 10，我们将得到最后一位数字 1，
     要得到倒数第二位数字，我们可以先通过除以 10 把最后一位数字从 1221 中移除，1221 / 10 = 122，再求出上一步结果除以10的余数，122 % 10 = 2，
     就可以得到倒数第二位数字。如果我们把最后一位数字乘以10，再加上倒数第二位数字，1 * 10 + 2 = 12，就得到了我们想要的反转后的数字。
     如果继续这个过程，我们将得到更多位数的反转数字。

     现在的问题是，我们如何知道反转数字的位数已经达到原始数字位数的一半？
     我们将原始数字除以 10，然后给反转后的数字乘上 10，所以，当原始数字小于反转后的数字时，就意味着我们已经处理了一半位数的数字。
     * @param x
     * @return
     */
    public boolean IsPalindrome(int x) {
        // 特殊情况：
        // 如上所述，当 x < 0 时，x 不是回文数。
        // 同样地，如果数字的最后一位是 0，为了使该数字为回文，
        // 则其第一位数字也应该是 0
        // 只有 0 满足这一属性
        if(x < 0 || (x % 10 == 0 && x != 0)) {
            return false;
        }

        int revertedNumber = 0;
        while(x > revertedNumber) {
            revertedNumber = revertedNumber * 10 + x % 10;
            x /= 10;
        }

        // 当数字长度为奇数时，我们可以通过 revertedNumber/10 去除处于中位的数字。
        // 例如，当输入为 12321 时，在 while 循环的末尾我们可以得到 x = 12，revertedNumber = 123，
        // 由于处于中位的数字不影响回文（它总是与自己相等），所以我们可以简单地将其去除。
        return x == revertedNumber || x == revertedNumber/10;
    }



    // 感受：动态规划和经典LCS的应用
    public static void main(String[] args)
    {
        // 最长子序列与子串
//        String s1 = "12345"; String s2 = "34567";// 345，345
//        String s1 = "1234567890"; String s2 = "345231567890";//  34 567890，567890
//        String s1 = "1234567890"; String s2 = "34523156890";//  34 56890，345或者890
//
//        System.out.println("Length of LCS is " + lcs(s1,s2 ));
//        System.out.println("Length of LCS1 is " + lcs1(s1,s2 ));


//        String a = "babad";
//        String res = longestPalindromeWithBreak(a);
//        out.println(res);//bab或者aba   长度 3
//        String res1 = longestPalindrome(a);
//        out.println(res1);//bab或者aba   长度 3
//
//        a = "cbbd";
//        res = longestPalindromeWithBreak(a);
//        out.println(res);//bb    长度 2
//        res1 = longestPalindrome(a);
//        out.println(res1);//bb    长度 2
//
//        a = "abccbd";
//        res = longestPalindromeWithBreak(a);
//        out.println(res);//bccb    长度 4
//        res1 = longestPalindrome(a);
//        out.println(res1);//bccb    长度 4
//
//        a = "abcdasdfghjkldcba";
//        res = longestPalindromeWithBreak(a);
//        out.println(res);//abcd    长度 4
//        res1 = longestPalindrome(a);
//        out.println(res1);//a    长度 1

//        out.println(isPalindrome(121));
//        out.println(isPalindrome(1221));
//        out.println(isPalindrome(132441));
//        out.println(isPalindrome(-121));

//        out.println(isPalindromeWithoutString(121));
//        out.println(isPalindromeWithoutString(1221));
//        out.println(isPalindromeWithoutString(1345431));
//        out.println(isPalindromeWithoutString(132441));
//        out.println(isPalindromeWithoutString(-121));

//        out.println(isPalindromeCharOnly("0P"));
//        out.println(validPalindrome("0P"));
        out.println(validPalindrome("0P011"));
    }

}
