package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A package-private class of the package image.
 * @author Dan Nirel
 */
public class Image {
    private static final int ONEPRIME = 5;
    private static final int ANOTHERPRIME = 11;

    private final Color[][] pixelArray;
    private final int width;
    private final int height;

    public Image(String filename) throws IOException {
        BufferedImage im = ImageIO.read(new File(filename));
        width = im.getWidth();
        height = im.getHeight();


        pixelArray = new Color[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixelArray[i][j]=new Color(im.getRGB(j, i));
            }
        }
    }

    public Image(Color[][] pixelArray, int width, int height) {
        this.pixelArray = pixelArray;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getPixel(int x, int y) {
        return pixelArray[x][y];
    }
    /**
     * Overrides the equals method to compare this Image object with another object.
     *
     * @param other the object to compare with this Image.
     * @return true if the other object is an Image with the same width, height, and pixel values as this Image; false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Image)) {
            return false;
        }
        Image otherImage = (Image) other;
        if (width != otherImage.getWidth() || height != otherImage.getHeight()) {
            return false;
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (!pixelArray[i][j].equals(otherImage.getPixel(i, j))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Overrides the hashCode method to generate a hash code for this Image object.
     *
     * @return the hash code value for this Image object.
     */
    @Override
    public int hashCode() {
        int result = ONEPRIME;
        result = ANOTHERPRIME * result + width;
        result = ANOTHERPRIME * result + height;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                result = ANOTHERPRIME * result + pixelArray[i][j].hashCode();
            }
        }

        return result;
    }



    public void saveImage(String fileName){
        // Initialize BufferedImage, assuming Color[][] is already properly populated.
        BufferedImage bufferedImage = new BufferedImage(pixelArray[0].length, pixelArray.length,
                BufferedImage.TYPE_INT_RGB);
        // Set each pixel of the BufferedImage to the color from the Color[][].
        for (int x = 0; x < pixelArray.length; x++) {
            for (int y = 0; y < pixelArray[x].length; y++) {
                bufferedImage.setRGB(y, x, pixelArray[x][y].getRGB());
            }
        }
        File outputfile = new File(fileName+".jpeg");
        try {
            ImageIO.write(bufferedImage, "jpeg", outputfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
