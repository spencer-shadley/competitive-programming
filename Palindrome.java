import java.util.Scanner;

/**
 * Created by Spencer on 9/4/2015.
 *
 * Solving CodeForces: http://codeforces.com/problemset/problem/505/A
 *
 */
public class Palindrome {

    public static void main(String[] args) {

        // get input
        Scanner scan = new Scanner(System.in);
        String input = scan.next();

        // go through each character in input
        for(int index = 0; index <= input.length(); index++) {

            // each letter of the alphabet
            for (int i = 0; i < 26; i++) {
                char currChar = (char) (i + 97);

                // construct potential palindrome
                String first = input.substring(0, index);
                String end = input.substring(index, input.length());
                String putTogether = first + currChar + end;

                if (isPalindrome(putTogether)) {
                    System.out.println(putTogether);
                    scan.close();
                    System.exit(0);
                }
            }
        }
        System.out.println("NA");
        scan.close();
    }

    private static boolean isPalindrome(String word) {
        int i = 0;
        int j = word.length() - 1;
        while(i < j)
            if (word.charAt(i++) != word.charAt(j--))
                return false;
        return true;
    }

}