import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */

// David Mhatre, 17 March 2023
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        // Creates all combinations without removing then generates all with removing
        ArrayList<String> allCombos = new ArrayList<>();
        char[] lettersChar = letters.toCharArray();
        findStrings(lettersChar, 0, letters.length() - 1, allCombos);
        for (int i = 0; i < allCombos.size(); i++){
            createStrings(0, allCombos.get(i));
        }
    }
    public void findStrings(char[] arr, int left, int right, ArrayList<String> combos){
        // Creates every possible combination for the word without removing
        if (left == right){
            String str = "";
            for (int i = 0; i < arr.length; i++){
                str += arr[i];
            }
            combos.add(str);
        }
        else{
            // Swaps elements to get every possible combo
            for (int i = left; i < right + 1; i++){
                swap(arr, left, i);
                findStrings(arr,left + 1, right, combos);
                swap(arr, left, i);
            }
        }

    }

    private  void swap(char[] array, int i, int j) {
        // Quick swap method to swap two index
        char temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    public void createStrings(int start, String word) {
        // Creates every possibility
        String modWord;
        if (start > word.length()) {
            words.add(word);
            return;
        }
        // Special cases to modify the word
        if (start == 0){
            modWord = word.substring(start + 1);
        }
        else if (start == word.length()){
            modWord = word.substring(0, start - 1);
        }
        else if (start == word.length() - 1){
            modWord = word.substring(0, start - 1) + word.substring(start);
        }
        else{
            modWord = word.substring(0, start) + word.substring(start + 1);
        }
        // In the end had a method call with the modify word or not
        createStrings(start + 1, word);
        createStrings(start + 1, modWord);
        return;
    }


    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        mergeSort(words,0, words.size() - 1);
    }
    public void mergeSort(ArrayList<String> arr, int start, int end){
        // Based case if the element is one then returns
        if (end - start <= 0){
            return;
        }
        // Breaks the array down in two lists
        int mid = (start + end) / 2;
        mergeSort(arr, start, mid);
        mergeSort(arr, mid + 1, end);
        // Merges the arrays back together
        merge(arr, start, mid, end);
    }

    public void merge(ArrayList<String> arr, int start, int mid, int end){
        // Initializes the variables for the index and the new arrays
        int arr1I = 0;
        int arr2I = 0;
        int bigArrI = start;
        String[] arr1 = new String[mid - start + 1];
        String[] arr2 = new String[end - mid];

        // Copies values into new arrays
        for (int i = 0; i < mid - start + 1; i++){
            arr1[i] = arr.get(i + start);
        }
        for (int i = 0; i < end - mid; i++){
            arr2[i] = arr.get(i + mid + 1);
        }

        // Merges the two arrays into an arraylist
        while (arr1I < arr1.length && arr2I < arr2.length){
            if (arr1[arr1I].compareTo(arr2[arr2I]) <= 0){
                words.set(bigArrI, arr1[arr1I]);
                arr1I++;
            }
            else{
                words.set(bigArrI, arr2[arr2I]);
                arr2I++;
            }
            bigArrI++;
        }

        // Which ever one has elements left is added to the end
        while (arr1I < arr1.length){
            words.set(bigArrI, arr1[arr1I]);
            arr1I++;
            bigArrI++;
        }

        while (arr2I < arr2.length){
            words.set(bigArrI, arr2[arr2I]);;
            arr2I++;
            bigArrI++;
        }
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
        // Loops through and compares each number to the dictionary
        for (int i = 0; i < words.size(); i++){
            if (!(binarySearch(words.get(i), 0, DICTIONARY_SIZE))){
                words.remove(i);
                i--;
            }
        }
    }

    public boolean binarySearch(String str, int low, int high){
        // based case if the word is not found return false
        if (low > high){
            return false;
        }
        int med = (low + high) / 2;
        // Checks the str against the current index in dictionary then returns based on that
        if (str.compareTo(DICTIONARY[med]) < 0){
            return binarySearch(str, low, med - 1);
        }
        else if(str.compareTo(DICTIONARY[med]) > 0){
            return binarySearch(str, med + 1, high);
        }
        else{
            return true;
        }
    }


    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
