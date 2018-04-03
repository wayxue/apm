package com.yitaqi.common;

import java.util.regex.Pattern;

/**
 * 描述:
 * ${DESCRIPTION}
 *
 * @author xue
 * @create 2018-03-30 11:36
 */
public class WildcardMatcher {

    private final Pattern pattern;

    public boolean matches(final String s) {
        return pattern.matcher(s).matches();
    }

    public WildcardMatcher(final String expression) {

        String[] parts = expression.split("&");
        StringBuilder regex = new StringBuilder(expression.length() * 2);
        boolean next = false;
        for (String part : parts) {
            if (next) {
                regex.append('|');
            }
            regex.append('(').append(toRegex(part)).append(')');
            next = true;
        }
        System.out.println(regex);
        pattern = Pattern.compile(regex.toString());
    }

    private static CharSequence toRegex(String expression) {
        StringBuilder regex = new StringBuilder(expression.length() * 2);
        for (char c : expression.toCharArray()) {
            switch (c) {
                case '?' :
                    regex.append(".?");
                    break;
                case '*' :
                    regex.append(".*");
                    break;
                default:
                    regex.append(Pattern.quote(String.valueOf(c)));
                    break;
            }
        }
        System.out.println(regex);
        return regex;
    }

    public static void main(String[] args) {
        WildcardMatcher matcher = new WildcardMatcher("com.*.tom");
        boolean matches = matcher.matches("com.yitaqi.tom");
        System.out.println(matches);
    }
}
