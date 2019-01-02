import java.io.*;
import java.util.Scanner;


public class Test {
    public static void main(String[] args) {
        FamilyTree fam = new FamilyTree();
        try {
            fam.buildFamilyTree("input1.txt");
            }
        catch (Exception e) {
            System.out.println("oh no, no file baby");
        }

        //fam.printAll();
        try {
            fam.evaluate("query1.txt", "output1.txt");
        } catch (Exception e) {
            System.out.println("no file :(");
        }
    }
}
