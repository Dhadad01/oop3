package image_char_matching;
import java.util.*;

/**
 * SubImgCharMatcher class is responsible for matching an ASCII character
 * to give an image with a given brightness.
 * This class is used by the algorithm to match brightness with characters.
 */
public class SubImgCharMatcher {

    /**
     * Number of Boolean values that single char get converted to
     */
    public static final int NUM_OF_BOOLEAN_VALUES = 256;
    /**
     * number of rows of char conversion
     */
    public static final int CONVERTED_ROWS = 16;
    /**
     * number of cols of char conversion
     */
    public static final int CONVERTED_COLS = 16;
    /**
     * space to put between chars in print
     */
    public static final char SPACE = ' ';

    /**
     * static treemap to save all the letters we translated so far in order to
     * avoid re-calculations
     */
    public static TreeMap<Character,Double> allBeforeNormal = new TreeMap<>();
    /**
     * static hash - translate between List of chars to their SubImgCharMatcher
     * if we ever created SubImgCharMatcher with those specific chars before
     */
    static final HashMap<List<Character>,SubImgCharMatcher> subImgCharMatchers= new HashMap<>();

    /**
     * List of characters that make up the set of characters for the algorithm to use.
     */
    List<Character> chars = new ArrayList<>();

    /**
     * max brightness of this specific Charmatcher
     */
    private double maxBrightness;
    private char brightestChar;
    /**
     * min brightness of this specific Charmatcher
     */
    private double minBrightness;
    private char darkestChar;
    /**
     * tree with brightness and translation
     */
    private TreeMap<Double,Character> normalizedTree;

    /**
     * Constructor that receives as a parameter an array of characters that will make up the set of characters for the algorithm to use.
     *
     * @param charset an array of characters representing the set of characters for the algorithm to use.
     */
    public SubImgCharMatcher(char[] charset) {
        Arrays.sort(charset);
        for (char c:charset){
            chars.add(c);
        }
        if (subImgCharMatchers.get(chars) != null){
            normalizedTree = subImgCharMatchers.get(chars).normalizedTree;
            return;
        }
        normalizedTree = new TreeMap<>();
        boolean first = true;
        for (char c : charset) {
            double count = retrieveOrFindCharVal(c);
            double normalized = count / NUM_OF_BOOLEAN_VALUES;
            if (first){
                first = false;
                minBrightness = normalized;
                maxBrightness = normalized;
                brightestChar = c;
                darkestChar = c;
            }
            if (normalized>maxBrightness){
                maxBrightness=normalized;
                brightestChar =c;
            }
            if (minBrightness>normalized){
                minBrightness=normalized;
                darkestChar = c;
            }
        }
        for (char c:charset){
            double diff = maxBrightness-minBrightness;
            normalizedTree.put((allBeforeNormal.get(c)/NUM_OF_BOOLEAN_VALUES-minBrightness)/diff,c);
        }




//            if (normalizedTree.get(normalized)!=null){
//                if (normalizedTree.get(normalized)!=c){
//                    normalizedTree.put(normalized, c);
//                }
//                continue;
//            }
//            normalizedTree.put(normalized, c);

        subImgCharMatchers.put(chars,this);
    }
    /**
     * Method to show characters.
     */
    public void showChars(){
        for (char c : chars) {
            System.out.print(c);
            System.out.print(SPACE);

        }
    }
    /**
     * Method to check if there are no characters.
     *
     * @return true if there are no characters in the list; false otherwise.
     */
    public boolean hasNoChars(){
        return chars.isEmpty();
    }


    /**
     * Computes the brightness value of a character by counting the number of 'true' values
     * in the boolean array representation of the character.
     *
     * @param c the character whose brightness value needs to be computed.
     * @return the brightness value of the character.
     */
    private double getCharVal(char c){
        boolean[][] converted = CharConverter.convertToBoolArray(c);
        double count = 0;
        //Check how much true, then normalize
        for (int j = 0; j < CONVERTED_ROWS; j++) {
            for (int k = 0; k < CONVERTED_COLS; k++) {
                if (converted[j][k]) {
                    count++;
                }
            }
        }
        return count;
    }
    /**
     * Given a brightness value of a sub-image, the method will return the character from the set
     * that has the brightness closest in absolute value to the given brightness.
     * If there are several characters from the set with the same brightness, the character with the lowest ASCII value will be returned among them.
     *
     * @param brightness the brightness value of the sub-image.
     * @return the character from the set that matches the given brightness.
     */
    public char getCharByImageBrightness(double brightness){
        if (normalizedTree.ceilingKey(brightness)==null){
            return normalizedTree.get(normalizedTree.floorKey(brightness));
        } else if (normalizedTree.floorKey(brightness)==null) {
            return normalizedTree.get(normalizedTree.ceilingKey(brightness));
        }
        double ceil = normalizedTree.ceilingKey(brightness);
        double floor = normalizedTree.floorKey(brightness);
        if (ceil-brightness>brightness-floor){
            return normalizedTree.get(floor);
        }
        return normalizedTree.get(ceil);
    }
    /**
     * Retrieves the brightness value of a character if it exists in the 'allBeforeNormal' map,
     * otherwise calculates the brightness value and stores it in the map.
     *
     * @param c the character whose brightness value needs to be retrieved or calculated.
     * @return the brightness value of the character.
     */
    private double retrieveOrFindCharVal(char c){
        double count;
        if (allBeforeNormal.get(c)!= null){
            count = allBeforeNormal.get(c);
        }
        else {
            count = getCharVal(c);
            allBeforeNormal.put(c,count);
        }
        return count;
    }
    /**
     * Method that adds the character c to the character set.
     *
     * @param c the character to be added to the character set.
     */
    public void addChar(char c){
        //insertion to array

        int insertionPoint = findInsertionPoint(chars, c);
        if (chars.size()== insertionPoint||chars.get(insertionPoint)!=c){
            chars.add(insertionPoint,c);
        }
        double count = retrieveOrFindCharVal(c);
        allBeforeNormal.put(c,count);
        double normalized = count/ NUM_OF_BOOLEAN_VALUES;
//        double normalized = (count/ NUM_OF_BOOLEAN_VALUES -minBrightness)/(maxBrightness-minBrightness);
        if ((normalized <=maxBrightness)&&(normalized >=minBrightness)){
            normalizedTree.put((normalized-minBrightness)/(maxBrightness-minBrightness),c);
            return;
        }

        if (subImgCharMatchers.get(chars) != null){
            normalizedTree = subImgCharMatchers.get(chars).normalizedTree;
            return;
        }
        normalizedTree.put(normalized,c);
        newMaxOrMin(count/ NUM_OF_BOOLEAN_VALUES, count/ NUM_OF_BOOLEAN_VALUES > maxBrightness);
        subImgCharMatchers.put(chars,this);
    }


    /**
     * binary search in array to find insertion point
     *
     * @param list the list of chars
     * @param target the char we want to insert
     */
    private static int findInsertionPoint(List<Character> list, char target) {
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            char midChar = list.get(mid);

            if (midChar == target) {
                return mid; // Insert at the current position if the character already exists
            } else if (midChar < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return low; // Insert after the last smaller element if not found
    }
    /**
     * Adjusts the brightness values in the normalizedTree after adding or removing a character
     * to maintain the proper order.
     *
     * @param num the brightness value of the newly added or removed character.
     * @param max flag indicating whether the new brightness value is maximum or not.
     */
    private void newMaxOrMin(double num,boolean max){
        TreeMap<Double,Character> newNormalized = new TreeMap<>();
        if (max){
            for (char val: normalizedTree.values()){
                Double normalizedNewMax = (allBeforeNormal.get(val)/NUM_OF_BOOLEAN_VALUES-minBrightness)/(num-minBrightness);
                newNormalized.put(normalizedNewMax,val);
            }
            normalizedTree = newNormalized;
            maxBrightness = num;
            return;
        }
        for (char val: normalizedTree.values()){
            Double normalizedNewMax = (allBeforeNormal.get(val)/NUM_OF_BOOLEAN_VALUES-num)/(maxBrightness-num);
            newNormalized.put(normalizedNewMax,val);
            minBrightness = num;
        }
        normalizedTree = newNormalized;

    }


    /**
     * Method that removes the character c from the character set.
     *
     * @param c the character to be removed from the character set.
     */
    public void removeChar(char c){
        if (findInsertionPoint(chars,c)>=chars.size()||chars.get(findInsertionPoint(chars,c))!=c){
            return;
        }
        chars.remove(findInsertionPoint(chars,c));
        double key = (allBeforeNormal.get(c)/ NUM_OF_BOOLEAN_VALUES -minBrightness)/(maxBrightness-minBrightness);
        normalizedTree.remove(key);
        if (key!=maxBrightness&&key!=minBrightness){
            return;
        }
        if (subImgCharMatchers.get(chars)!=null){
            normalizedTree = subImgCharMatchers.get(chars).normalizedTree;
        }
        if (key==maxBrightness){
            double newMax = normalizedTree.lastKey();
            newMaxOrMin(newMax,true);
            return;
        }
        double newMin = normalizedTree.firstKey();
        newMaxOrMin(newMin,false);
        subImgCharMatchers.put(chars,this);
        //else
    }

}
