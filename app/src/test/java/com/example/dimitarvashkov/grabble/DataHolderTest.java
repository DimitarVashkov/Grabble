package com.example.dimitarvashkov.grabble;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

import junit.framework.TestCase;

/**
 * Test DataHolder - Singleton. Stores all the shared information. Something like a god class.
 */
public class DataHolderTest  extends TestCase{

    @Test
    public void test_addLetter_isCorrect(){
        DataHolder.getInstance().addLetter("M");
       int i = DataHolder.getInstance().getLetters().size();
        DataHolder.getInstance().addLetter("M");
        int m = DataHolder.getInstance().getLetters().size();
        assertThat(i+1 == m,is(true));
    }

    @Test
    public void test_removeLetter_isCorrect(){
        DataHolder.getInstance().addLetter("M");
        int i = DataHolder.getInstance().getLetters().size();
        DataHolder.getInstance().removeLetters("M");
        int m = DataHolder.getInstance().getLetters().size();
        assertThat(i-1 == m,is(true));
    }

    @Test
    public void test_addPoints_isCorrect(){
        int i = DataHolder.getInstance().getScore();
        assertEquals(0,i);

        DataHolder.getInstance().addToScore(100);
        i = DataHolder.getInstance().getScore();
        assertEquals(100,i);
    }

    @Test
    public void test_powerUser_isCorrect(){
        assertEquals(DataHolder.getInstance().getPowerUser(),false);
        DataHolder.getInstance().addToScore(1050);
        if(DataHolder.getInstance().getScore()>1000){
            DataHolder.getInstance().setPowerUser();
        }
        assertEquals(DataHolder.getInstance().getPowerUser(),true);

    }

    @Test
    public void test_removeALetter_isCorrect(){
        //Insert only 10 letters; letter remove should not work if <11 letters
        for(int i=0;i<10;i++){
            DataHolder.getInstance().addLetter("M");
        }
        int i = DataHolder.getInstance().getLetters().size();
        DataHolder.getInstance().removeALetter();
        int m = DataHolder.getInstance().getLetters().size();

        assertEquals(i,m);

        //Insert 11th letter; which should be removable
        DataHolder.getInstance().addLetter("M");
        i = DataHolder.getInstance().getLetters().size();

        DataHolder.getInstance().removeALetter();

        m = DataHolder.getInstance().getLetters().size();
        assertNotEquals(i,m);

    }

}