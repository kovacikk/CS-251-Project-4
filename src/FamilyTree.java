/*
*  Kyle Kovacik
*  Generates Family Trees from input files
*  Answers Queries Using Created Trees
*
*/

import org.w3c.dom.*;
import java.io.*;
import java.util.Scanner;

public class FamilyTree {
    private Node[] nodes;
    private int size;
    private Node startNode;
    private int distance0;
    private int distance1;
    private String[] name0Search;
    private int name0Size;
    private String[] name1Search;
    private int name1Size;
    /**
     * Declare necessary variables to describe your Tree
     * Each Node in the Tree represents a person
     * You can declare other classes if necessary
     */
    private class Node {
        Node mother;
        Node father;
        Node[] children;
        int childrenSize;
        String name;
        int distance0F;
        int distance0M;
        int distance1F;
        int disatnce1M;
        int distance0;
        int distance1;

        public Node(String name) {
            this.name = name;
            this.mother = null;
            this.father = null;
            this.children = new Node[100];
            this.childrenSize = 0;
        }

        public Node(Node mother, Node father, Node[] children, int childrenSize, String name) {
            this.children = children;
            this.mother = mother;
            this.father = father;
            this.name = name;
            this.childrenSize = childrenSize;
        }
    }

    public FamilyTree(){
        nodes = new Node[1000];
        size = 0;
    }

    /**
     * @input directory or filename of input file. This file contains the information necessary to build the child
     * parent relation. Throws exception if file is not found
     * @param familyFile
     * @throws Exception
     */

    public void buildFamilyTree(String familyFile) throws Exception {
        File file = new File(familyFile);
        Scanner scan = new Scanner(file);
        FamilyTree fam = new FamilyTree();

        while(scan.hasNext()) {
            String nextLine = scan.nextLine();
            Scanner scanLine = new Scanner(nextLine);

            Node father = new Node("unknown");
            Node mother = new Node("unknown");
            for (int i = 0; scanLine.hasNext(); i++) {
                if (i == 0) {
                    father.name = scanLine.next();

                    int index;
                    if((index = nodeExists(father.name)) != -1) {
                        father = nodes[index];
                    }
                    else {
                        nodes[size] = father;
                        size++;
                    }
                }
                else if (i == 1) {
                    mother.name = scanLine.next();
                    int index;
                    if ((index = nodeExists(mother.name)) != -1) {
                        mother = nodes[index];
                    }
                    else {
                        nodes[size] = mother;
                        size++;
                    }

                }
                else {
                    Node child = new Node(scanLine.next());
                    int index;
                    if ((index = nodeExists(child.name)) != -1) {
                        child = nodes[index];
                    }
                    else {
                        nodes[size] = child;
                        size++;
                    }

                    child.father = father;
                    child.mother = mother;
                    father.children[father.childrenSize] = child;
                    father.childrenSize++;
                    mother.children[mother.childrenSize] = child;
                    mother.childrenSize++;
                }
            }
        }
    }

    public int nodeExists(String name) {
        for (int i = 0; i < size; i++) {
            if (nodes[i].name.equals(name)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @input directory or filename of Query and Output.
     * queryFile contains the queries about the tree.
     * The output of this query should be written in file outputfile.
     * @param queryFile
     * @param outputFile
     * @throws Exception
     */

    public void evaluate(String queryFile,String outputFile) throws Exception{
        /*
         * Traverse the tree to answer the queries
         * For information on queries take a look at the handout
         */

        File query = new File(queryFile);
        File output = new File(outputFile);
        new FileOutputStream(outputFile, false);
        Scanner scanQuery = new Scanner(query);
        BufferedWriter sendOutput = new BufferedWriter(new FileWriter(outputFile));

        boolean first = true;
        String[] name0 = new String[100];
        String[] name1 = new String[100];
        int counter = 0;
        while (scanQuery.hasNext()) {
            if (first) {
                name0[counter] = scanQuery.next();
                first = false;
            }
            else {
                name1[counter] = scanQuery.next();
                first = true;
                counter++;
            }
        }

        for (int i = 0; i < counter; i++) {
            startNode = nodes[nodeExists(name0[i])];
            sendOutput.write(search2(name0[i], name1[i]));
            sendOutput.flush();
        }
    }

    private String search2 (String name0, String name1) {
        name0Search = new String[1000];
        name0Size = 0;
        name1Search = new String[1000];
        name1Size = 0;
        distance0 = 0;
        distance1 = 0;

        upSearch(name0, 0);
        upSearch(name1, 1);

        String[] common = new String[1000];
        int commonSize = 0;
        for (int i = 0; i < name0Size; i++) {
            for (int j = 0; j < name1Size; j++) {
                if (name0Search[i].equals(name1Search[j])) {
                    common[commonSize] = name0Search[i];
                    commonSize++;
                }
            }
        }

        if (commonSize == 0) {
            return "unrelated\n";
        }
        else {
            for (int i = 0; i < commonSize; i++) {
                if (name0.equals(common[i])) {
                    return name1 + " is a descendant of " + name0 + "\n";
                }
                else if (name1.equals(common[i])) {
                    return name0 + " is a descendant of " + name1 + "\n";
                }
            }
            
            String[] shavedCommon = new String[commonSize];
            int shavedCommonSize = 0;

            int minSum = 999999;
            for (int i = 0; i < commonSize; i++) {
                if (nodes[nodeExists(common[i])].distance0 + nodes[nodeExists(common[i])].distance1 < minSum) {
                    minSum = nodes[nodeExists(common[i])].distance0 + nodes[nodeExists(common[i])].distance1;
                    shavedCommonSize = 0;
                    shavedCommon[shavedCommonSize] = nodes[nodeExists(common[i])].name;
                    shavedCommonSize++;
                }
                else if (nodes[nodeExists(common[i])].distance0 + nodes[nodeExists(common[i])].distance1 == minSum){
                    shavedCommon[shavedCommonSize] = nodes[nodeExists(common[i])].name;
                    shavedCommonSize++;
                }
            }
            common = shavedCommon;
            commonSize = shavedCommonSize;
        }
        String output = "";
        lexoSort(common, commonSize);
        for (int i = 0; i < commonSize; i++) {
            if (i == 0) {
                output = common[i];
            }
            else {
                output += (" " + common[i]);
            }
        }
        return output + "\n";
    }

    private void lexoSort(String[] array, int arraySize) {
        for (int i = 0; i < arraySize; i++) {
            for (int j = 0; j < arraySize; j++) {
                if (array[i].compareTo(array[j]) < 0) {
                    String temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }
    }

    private boolean isName0(String name) {
        for (int i = 0; i < name0Size; i++) {
            if (name0Search[i].equals(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean isName1(String name) {
        for (int i = 0; i < name1Size; i++) {
            if (name1Search[i].equals(name)) {
                return true;
            }
        }
        return false;
    }
    private String[] upSearch(String name, int number) {
        Node nameNode = nodes[nodeExists(name)];

        if (number == 0) {
            int count = distance0;
            nameNode.distance0 = count;
            distance0++;

            name0Search[name0Size] = name;
            name0Size++;
            if (nameNode.father != null && !isName0(nameNode.father.name)) {
                upSearch(nameNode.father.name, 0);
            }
            if (nameNode.mother != null && !isName0(nameNode.mother.name)) {
                upSearch(nameNode.mother.name, 0);
            }
            distance0--;
            return name0Search;
        }else {
            int count = distance1;
            nameNode.distance1 = count;
            distance1++;

            name1Search[name1Size] = name;
            name1Size++;
            if (nameNode.father != null && !isName1(nameNode.father.name)) {
                upSearch(nameNode.father.name, 1);
            }
            if (nameNode.mother != null && !isName1(nameNode.mother.name)) {
                upSearch(nameNode.mother.name, 1);
            }
            distance1--;
            return name1Search;
        }
    }

    public void printAll () {
        for (int i = 0; i < size; i++) {
            Node test = nodes[i];
            System.out.printf("Name: %s\nFather: %s\nMother: %s\n", test.name, (test.father == null) ? "null" : test.father.name,(test.mother == null) ? "null" : test.mother.name);
            for (int j = 0; j < test.childrenSize; j++) {
                System.out.printf("Child %d: %s\n", j, test.children[j].name);
            }
            System.out.println("-------------------------");
        }
    }
}
