package image;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for image-related operations. the methods are all static
 */
public class ImageUtilities {
    /**
     * The ratio of red color in brightness calculation.
     */
    private static final float RED_RATIO = 0.2126f;

    /**
     * The ratio of green color in brightness calculation.
     */
    private static final float GREEN_RATIO = 0.7152f;

    /**
     * The ratio of blue color in brightness calculation.
     */
    private static final float BLUE_RATIO = 0.0722f;
    /**
     * Constant representing the white color value.
     */
    public static final int WHITECOLOR = 255;

    /**
     * Pads the given image with white color to make its dimensions powers of 2.
     * If the original image dimensions are already powers of 2, no padding is applied.
     *
     * @param image the original image to be padded.
     * @return the padded image with dimensions as powers of 2.
     */
    static public Image padImageWhite(Image image){
        int height = image.getHeight();
        int width = image.getWidth();
        int heightPow2 = findPow2(height);
        int widthPow2 = findPow2(width);
        int diffHeight = (heightPow2 - height) / 2;
        int diffWidth = (widthPow2 - width) / 2;
        Color[][] imagePadded = new Color[heightPow2][widthPow2];

        for (int i = 0; i < heightPow2; i++) {
            for (int j = 0; j < widthPow2; j++) {
                if (i < diffHeight || i >= height + diffHeight ||
                        j < diffWidth || j >= width + diffWidth) {
                    imagePadded[i][j] = new Color(WHITECOLOR, WHITECOLOR, WHITECOLOR);
                    continue;
                }
                imagePadded[i][j] = image.getPixel(i - diffHeight, j - diffWidth);
            }
        }
        return new Image(imagePadded, widthPow2, heightPow2);
    }

    /**
     * Finds the next power of 2 greater than or equal to the given size.
     *
     * @param size the input size.
     * @return the next power of 2 greater than or equal to the given size.
     */
    static private int findPow2(int size){
        int n = 1;
        while (n < size){
            n *= 2;
        }
        return n;
    }

    /**
     * Splits the given image into sub-images based on the specified resolution.
     * Each sub-image has a resolution of res x res pixels.
     *
     * @param image the original image to be split.
     * @param res   the resolution for each sub-image.
     * @return a list of lists containing the sub-images.
     */
    public static List<List<Image>> splitByResolution(Image image, int res){
        List<List<Image>> lstOfLst = new ArrayList<>();
        int sizeSubPct = image.getWidth() / res;
        for (int i = 0; i < image.getHeight() / sizeSubPct; i++) {
            List<Image> lstOfImages = new ArrayList<>();
            for (int j = 0; j < res ; j++) {
                Color[][] subPicture = new Color[sizeSubPct][sizeSubPct];
                for (int k = 0; k < sizeSubPct; k++) {
                    for (int l = 0; l < sizeSubPct; l++) {
                        subPicture[k][l] = image.getPixel(i * sizeSubPct + k, j * sizeSubPct + l);
                    }
                }
                lstOfImages.add(new Image(subPicture, image.getWidth() / res, image.getWidth() / res));
            }
            lstOfLst.add(lstOfImages);
        }
        return lstOfLst;
    }


    /**
     * Calculates the brightness percentage of the given image.
     * The brightness percentage represents the average brightness of all pixels in the image.
     *
     * @param image the image for which to calculate the brightness percentage.
     * @return the brightness percentage of the image.
     */
    public static double calculateBrightnessPct(Image image){
        double brightness = 0.0;
        Color color;
        double greyPixel;
        double normalizedGreyPixel;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                color = image.getPixel(i, j);
                greyPixel = color.getRed() * RED_RATIO + color.getGreen() * GREEN_RATIO
                        + color.getBlue() * BLUE_RATIO;
                normalizedGreyPixel = greyPixel / (image.getHeight() * image.getWidth() * 255);
                brightness += normalizedGreyPixel;
            }
        }
        return brightness;
    }


}
