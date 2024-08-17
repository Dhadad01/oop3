package ascii_art;
import static java.lang.Math.max;

import ascii_output.ConsoleAsciiOutput;
import image.Image;
import image_char_matching.SubImgCharMatcher;
import java.io.IOException;
import java.util.HashMap;

public class Parameters {
    /**
     * The initial set of characters used for ASCII conversion.
     */
    private static final char[] INIT_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * String indicating that the resolution change did not occur due to exceeding boundaries.
     */
    private static final String EXCEEDING_BOUNDRIES =  "Did not change resolution due to exceeding boundaries.\n";

    /**
     * String indicating that the resolution has been updated.
     */
    private static final String UPDATED_RES = "Resolution set to ";

    /**
     * String indicating that the operation did not execute due to a problem with the image file.
     */
    private static final String FAILED_TO_OPEN =  "Did not execute due to problem with image file.\n";


    /**
     * The default path to the image file.
     */
    private static final String DEFAULT_PATH = "cat.jpeg";

    /**
     * The default resolution for image processing.
     */
    private static final int DEFAULT_RES = 128;
    private SubImgCharMatcher charMatcher;

    /**
     * The resolution for image processing.
     */
    private int res;

    /**
     * The image being processed.
     */
    private Image image;

    /**
     * Constructor for the Parameters class.
     *
     * @throws IOException if an I/O error occurs while loading the default image.
     */
    Parameters() throws IOException {
        try {
            this.image = new Image(DEFAULT_PATH);
        } catch (IOException e) {
            throw new IOException(e);
        }
        this.res = DEFAULT_RES;
        this.charMatcher = new SubImgCharMatcher(INIT_CHARS);
    }

    /**
     * Retrieves the resolution value.
     *
     * @return the resolution value.
     */
    int getRes(){
        return res;
    }

    /**
     * Retrieves the image object.
     *
     * @return the image object.
     */
    Image getImage(){
        return image;
    }

    /**
     * Retrieves the character matcher object.
     *
     * @return the character matcher object.
     */
    SubImgCharMatcher getCharMatcher(){
        return charMatcher;
    }

    /**
     * Sets the character matcher object.
     *
     * @param charMatcher the character matcher object to set.
     */
    void setCharMatcher(SubImgCharMatcher charMatcher){
        this.charMatcher = charMatcher;
    }


    /**
     * Increases the resolution of the image by doubling it, if the new resolution does not exceed the image width.
     * Prints a message indicating the updated resolution.
     * If the new resolution exceeds the image width, prints a message indicating the boundaries are exceeded.
     */
    void resUp(){
        if (res < image.getWidth()){
            res *= 2;
            System.out.print(UPDATED_RES + res);
            return;
        }
        System.out.print(EXCEEDING_BOUNDRIES);
    }

    /**
     * Decreases the resolution of the image by halving it, if the new resolution is greater than 1 and does not fall below the minimum resolution (1 pixel per character).
     * Prints a message indicating the updated resolution.
     * If the new resolution falls below the minimum resolution, prints a message indicating the boundaries are exceeded.
     */
    void resDown(){
        if (res > max(1, image.getWidth() / image.getHeight())){
            res /= 2;
            System.out.print(UPDATED_RES + res);
            return;
        }
        System.out.print(EXCEEDING_BOUNDRIES);
    }

    /**
     * Updates the image with the new image file located at the specified path.
     * Prints a message if there is an issue opening the image file.
     *
     * @param path the path to the new image file.
     */
    void updateImage(String path)  {
        try {
            image = new Image(path);
        } catch (IOException e) {
            System.out.print(FAILED_TO_OPEN);
        }
    }



}
