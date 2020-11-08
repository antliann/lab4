package com.company;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            File myObj = new File("program.c");
            Scanner reader = new Scanner(myObj);
            Lexer lexer = new Lexer();
            while (reader.hasNextLine())
            {
                String line = reader.nextLine();
                lexer.process(line);
            }
            lexer.outResults();

        } catch (FileNotFoundException e) {
            System.out.println("File is not found.");
            e.printStackTrace();
        }
    }
}
