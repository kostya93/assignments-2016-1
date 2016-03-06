package ru.spbau.mit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by kostya on 3/6/16.
 */
public class Cp {
    private Cp(){}
    public static final int MAX_BUFF = 1024;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("wrong args");
            return;
        }

        File file1 = new File(args[0]);
        FileInputStream fin;

        try {
            fin = new FileInputStream(file1);
        } catch (IOException e) {
            System.out.println("Problem with file \"" + args[0]);
            return;
        }

        File file2 = new File(args[1]);
        FileOutputStream fout;

        try {
            fout = new FileOutputStream(file2);
        }  catch (IOException e) {
            System.out.println("Problem with file \"" + args[1]);
            return;
        }

        byte[] contents = new byte[MAX_BUFF];
        int bytesRead;
        try {
            while ((bytesRead = fin.read(contents)) != -1) {
                fout.write(contents, 0, bytesRead);
            }
        } catch (IOException e) {
            System.out.println("Problem with read or write");
        }

    }
}
