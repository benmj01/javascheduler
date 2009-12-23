package ru.test;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: ALeX
 * Date: 27.11.2009
 * Time: 17:44:31
 */
public class Main {
    public static void main(String[] args) {
        try {
            Process p = Runtime.getRuntime().exec("taskkill /f /IM aaCen*");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
