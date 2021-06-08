/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.trippledave.textstuff;

import de.trippledave.textstuff.helper.DragDropFrame;
import de.trippledave.textstuff.helper.FileListReceiver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Checks if a text file contains words that are the same in a row
 *
 * @author trippledave
 */
public class CheckDoubleWords implements FileListReceiver {

    public static void main(String[] args) {
        new DragDropFrame(new CheckDoubleWords());
    }

    @Override
    public void handleFiles(List<File> files) {
        for (File file : files) {
            if (file.isFile()) {
                InputStream inputStream = null;
                try {
                    final BufferedReader br = new BufferedReader(new InputStreamReader(
                            new FileInputStream(file), "UTF-8"));

                    String line;
                    while ((line = br.readLine()) != null) {

                        final List<String> foundWords = new LinkedList<>();
                        final String[] words = line.split(" ");
                        String start = "EMPTY-WORD";

                        for (int i = 0; i < words.length; i++) {
                            final String currentWord = words[i];

                            if (start.equals(currentWord.toLowerCase())) {
                                foundWords.add(currentWord);
                            }

                            if (!currentWord.isEmpty()) {
                                start = currentWord.toLowerCase();
                            }
                        }

                        if (!foundWords.isEmpty()) {
                            System.out.println("\n ----------- DUBS FOUND in FILE: " + file.getName());

                            System.out.println(Arrays.toString(foundWords.toArray()));

                            System.out.println("in line:");
                            System.out.println(line);
                        }
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
