package ascii_art;

import image.Image;
import image.ImageUtilities;
import image_char_matching.SubImgCharMatcher;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


import java.util.List;

/**
 * Class representing the ASCII Art Algorithm.
 */
public class AsciiArtAlgorithm {

    Parameters parameters;

    /**
     * Constructs an AsciiArtAlgorithm object with the given parameters.
     *
     * @param parameters the parameters for the ASCII art algorithm.
     */
    public AsciiArtAlgorithm(Parameters parameters){
        this.parameters = parameters;
    }

    /**
     * Runs the ASCII art algorithm.
     *
     * @return a 2D array of characters representing the ASCII art.
     */
    public char[][] run(){
        //TODO add static of brightness and of set
        Image padded = ImageUtilities.padImageWhite(this.parameters.getImage());
        List<List<Image>> subImages = ImageUtilities.splitByResolution(padded, this.parameters.getRes());
        char[][] ImgConverted = new char[padded.getHeight() / (padded.getWidth() / this.parameters.getRes())]
                [this.parameters.getRes()];
        double brightness;
        for (int i = 0; i < subImages.size(); i++) {
            for (int j = 0; j < subImages.get(i).size(); j++) {
                brightness = ImageUtilities.calculateBrightnessPct(subImages.get(i).get(j));
                ImgConverted[i][j] = this.parameters.getCharMatcher().getCharByImageBrightness(brightness);
            }
        }
        return ImgConverted;
    }
}






