/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private ArrayList<String> wordsList = new ArrayList<>();
    private HashMap<String,ArrayList<String>> lettersToWord = new HashMap<>();
    private HashSet<String> wordSet = new HashSet<>();
    private HashMap<Integer,ArrayList<String >> sizeToWords = new HashMap<>();
    private Random random = new Random();
    private int wordLength = DEFAULT_WORD_LENGTH;

    AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordsList.add(word);
            addWordsToHMap(word);
        }
    }

    boolean isGoodWord(String word, String base) {
        if(!wordSet.contains(word))
            return false;
        if(word.contains(base))
            return false;

        return true;
    }

    List<String> getAnagrams(String targetWord) {
//        ArrayList<String> result = new ArrayList<String>();
//        for(String word : wordsList){
//            if(isAnagram(word,targetWord))
//                result.add(word);
//
//        }
//
//        return result;

        if(targetWord ==null || targetWord.equals(""))
            return null;
        String letters =sortLetters(targetWord);
        if(lettersToWord.containsKey(letters))
            return lettersToWord.get(letters);
        return null;
    }

    private void addWordsToHMap(String word) {
        wordSet.add(word);

        String sortLetters = sortLetters(word);
        if(lettersToWord.containsKey(sortLetters))
            Objects.requireNonNull(lettersToWord.get(sortLetters)).add(word);
            //to avoid null pointer exception
        else{
            ArrayList<String> tempList = new ArrayList<>();
            tempList.add(word);
            lettersToWord.put(sortLetters,tempList);
        }

        int wordSize = word.length();
        if(sizeToWords.containsKey(wordSize))
            Objects.requireNonNull(sizeToWords.get(wordSize)).add(word);
        else {
            ArrayList<String> temp = new ArrayList<>();
            temp.add(word);
            sizeToWords.put(wordSize,temp);
        }
    }

    static String sortLetters(String input){
        char[] res = input.toCharArray();
        Arrays.sort(res);
        return String.valueOf(res);
    }

    static boolean isAnagram(String word1,String word2){
        if(word1 == null || word2 == null || word1.equals("") || word2.equals(""))
            return false;
        if(word1.length() != word2.length())
            return false;

        return sortLetters(word1).equals(sortLetters(word2));
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(char c='a'; c<='z'; c++){
            List<String> temp = getAnagrams(word+c);

            //to remove invalid anagrams from the returned anagrams -- containing base
            //extra
            if(temp != null) {
                for (String t : temp) {
                    if (!t.contains(word))
                        result.add(t);
                }
            }

//            if(temp != null)
//                result.addAll(temp);
        }
        return result;
    }

    String pickGoodStarterWord() {
        //return "ore";

//        for(int i = 0; i< wordsList.size(); i++) {
//            int index = random.nextInt(wordsList.size());
//            String startWord = wordsList.get(index);
//            if(getAnagramsWithOneMoreLetter(startWord).size() >= MIN_NUM_ANAGRAMS)
//                return startWord;
//        }
//        return null;

        ArrayList<String> limitedWords = sizeToWords.get(wordLength);
        for(int i = 0; i< limitedWords.size(); i++) {
            int index = random.nextInt(limitedWords.size());
            String startWord = limitedWords.get(index);
            if(getAnagramsWithOneMoreLetter(startWord).size() >= MIN_NUM_ANAGRAMS) {
                if(wordLength<MAX_WORD_LENGTH)
                    wordLength++;
                return startWord;
            }
        }
        return null;

    }
}
