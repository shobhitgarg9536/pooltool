package com.shobhit.pooltool.utils;

import java.util.Random;

/**
 * Created by Shobhit-pc on 1/3/2017.
 */

public class GenerateUniqueString {

    public String getUniqueString() {

        char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }
}
