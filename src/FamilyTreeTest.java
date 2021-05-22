import org.junit.Test;

import java.io.File;
import java.util.Scanner;

import static org.junit.Assert.*;

public class FamilyTreeTest {

    @Test
    //@ScoringWeight(0.5)
    public void testOne() throws Exception
    {
        int fileNo=1;
        File f = new File("files/out/out" + fileNo + ".txt");
        runCase(fileNo);
        assertTrue("Output file "+fileNo+" is not created yet", f.exists());
        assertEquals(true, matchFiles("files/correctout/out"+fileNo+".txt","files/out/out"+fileNo+".txt"));
    }


    @Test
    //@ScoringWeight(0.5)
    public void testFive() throws Exception
    {
        int fileNo=5;
        File f = new File("files/out/out" + fileNo + ".txt");
        runCase(fileNo);
        assertTrue("Output file "+fileNo+" is not created yet", f.exists());
        assertEquals(true, matchFiles("files/correctout/out"+fileNo+".txt","files/out/out"+fileNo+".txt"));

    }

   

    public void runCase(int fileNo){
        try {
            FamilyTree familyTree = new FamilyTree();
            //File f = new File("files/in/input" + fileNo + ".txt");
            //assertTrue(f.exists());
            familyTree.buildFamilyTree("files/in/input" + fileNo + ".txt");
            familyTree.evaluate("files/in/query"+fileNo+".txt", "files/out/out"+fileNo+".txt");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean matchFiles(String real, String out){
        try {
            Scanner inf1 = new Scanner(new File(real));
            Scanner inf2 = new Scanner(new File(out));

            while(inf1.hasNextLine()){
                String actual=inf1.nextLine();
                actual=actual.trim();
                if(actual.length()==0)break;
                if(!inf2.hasNextLine())return false;

                String got=inf2.nextLine();
                got=got.trim();
                assertEquals(actual, got);
                /*
                if(actual.compareTo(got)!=0){
                    return false;
                }
                */
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return true;
    }
}