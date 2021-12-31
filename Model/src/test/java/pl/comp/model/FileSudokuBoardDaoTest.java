/*
 * Copyright (C) 2021 RCM.
 * All rights reserved. This software is the confidential and proprietary information of RCM.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with RCM.
 */

package pl.comp.model;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileSudokuBoardDaoTest {
    final static String PATH_TO_DIRECTORY = "./testFiles/";
    final static String PATH_TO_SUCCESSFUL_FILE = PATH_TO_DIRECTORY + "fileSuccessful";
    final static String PATH_TO_UNSUCCESSFUL_FILE = PATH_TO_DIRECTORY + "fileUnsuccessful";
    static File testDirectory;

    @BeforeAll
    static void beforeAll() throws IOException {
        testDirectory = new File(PATH_TO_DIRECTORY);
        if (testDirectory.exists()) {
            assertTrue(deleteDirectory(testDirectory));
        }
        assertTrue(testDirectory.mkdir());

        var unsuccessfulFile = new File(PATH_TO_UNSUCCESSFUL_FILE);

        if (unsuccessfulFile.isFile()) {
            boolean success = unsuccessfulFile.delete();
            if (success) {
                success = unsuccessfulFile.createNewFile();
            }
            assertTrue(success);
        } else {
            boolean success = unsuccessfulFile.createNewFile();
            assertTrue(success);
        }
        assertTrue(unsuccessfulFile.setWritable(false));
    }

    @Test
    void writeSuccessful() {
        try(var fileSudokuBoardDao = new FileSudokuBoardDao(PATH_TO_SUCCESSFUL_FILE)) {
            SudokuBoard sudokuBoard = new SudokuBoard();
            fileSudokuBoardDao.write(sudokuBoard);
        } catch (Exception e) {
            e.printStackTrace();
        }

        var f = new File(PATH_TO_SUCCESSFUL_FILE);
        assertTrue(f.exists());
        assertTrue(f.isFile());
    }

    @Test
    void writeUnsuccessful() throws FileNotFoundException {
        try(var fileSudokuBoardDao = new FileSudokuBoardDao(PATH_TO_UNSUCCESSFUL_FILE)) {
            fileSudokuBoardDao.write(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void readSuccessful() {
        SudokuBoard readSudokuBoard = null;
        try(var fileSudokuBoardDao = new FileSudokuBoardDao(PATH_TO_SUCCESSFUL_FILE)) {
            readSudokuBoard = fileSudokuBoardDao.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(readSudokuBoard);
    }

    @Test
    void readUnsuccessful() {
        SudokuBoard readSudokuBoard = null;
        try(var fileSudokuBoardDao = new FileSudokuBoardDao(PATH_TO_UNSUCCESSFUL_FILE)) {
            readSudokuBoard = fileSudokuBoardDao.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNull(readSudokuBoard);
    }

    @AfterAll
    static void afterAll() {
        assertTrue(deleteDirectory(testDirectory));
    }

    static boolean deleteDirectory(File dir)
    {
        boolean success = true;
        File[] allContents = dir.listFiles();
        assert allContents != null;
        for (File file : allContents) {
            success &= file.delete();
        }
        success &= dir.delete();
        return success;
    }
}