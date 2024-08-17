package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;


import static java.lang.Math.max;
import static java.lang.Math.min;

public class Shell {
    private static final String PREFIX = ">>> ";
    private static final String ADD = "add ";
    private static final String REMOVE = "remove ";
    private static final String ASCII_ART = "asciiArt";
    private static final String NO_CHARS = "Did not execute. Charset is empty.\n";
    private static final char SPACE = ' ';
    private static final String ALL = "all";

    private static final String SPACE_WORD = "space";
    private static final String OUTPUT_PREFIX = "output";
    private static final String CHARS = "chars";
    private static final String RES_UP = "res up";
    private static final String RES_DOWN = "res down";
    
    private static final String INCORRECT_FORMAT = "Did not execute due to incorrect format.";

    private static final String EXPECTION = "Did not execute due to problem with image file.";

    private static final String EXIT_COMMAND = "exit";
    private static final String ASCII_ART_COMMAND = "asciiArt";
    private static final String CHANGE_IMAGE_PREFIX = "image";

    private static final String OUTPUT_CONSOLE_COMMAND = "output console";

    private static final String OUTPUT_HTML_PATH = "out.html";
    private static final String OUTPUT_HTML_COMMAND = "output html";
    private static final String COURIERNEW="Courier New";

    private static AsciiOutput asciiOutput = new ConsoleAsciiOutput();


    public void run() throws IOException {
        Parameters parameters;
        try {
            parameters = new Parameters();
        } catch (IOException e) {
            System.out.println(EXPECTION);
            return;
        }

        while (true){
            System.out.print(PREFIX);
            String newCommand = KeyboardInput.readLine();
            if (newCommand.equals(EXIT_COMMAND)){
                return;
            }
            executeCommand(newCommand,parameters);
        }
    }

    private void executeCommand(String newCommand, Parameters parameters){
        switch (newCommand){
            case CHARS -> parameters.getCharMatcher().showChars();
            case RES_UP -> parameters.resUp();
            case RES_DOWN -> parameters.resDown();
            case OUTPUT_CONSOLE -> asciiOutput = new ConsoleAsciiOutput();
            case OUTPUT_HTML -> asciiOutput = new HtmlAsciiOutput(OUTPUT_HTML_PATH,COURIERNEW);
            case ASCII_ART -> runAlgorithm(parameters);
            default -> executeRemainsCommands(newCommand,parameters);


        }
    }

    private void runAlgorithm(Parameters parameters){
        if (parameters.getCharMatcher().hasNoChars()){
            System.out.print(NO_CHARS);
            return;
        }
        AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(parameters);
        char[][] res = asciiArtAlgorithm.run();
        asciiOutput.out(res);
    }
    private void executeRemainsCommands(String newCommand,Parameters parameters){
        if (newCommand.startsWith(ADD)){
            executeAddRemove(newCommand.substring(4),parameters,true);
        }
        else if (newCommand.startsWith(REMOVE)){
            executeAddRemove(newCommand.substring(7),parameters,false);
        }
        else if (newCommand.startsWith(CHANGE_IMAGE_PREFIX)){
            parameters.updateImage(newCommand.substring(6));
        }
        else{
            System.out.print(INCORRECT_FORMAT_COMMAND);
        }
    }
    private void executeAddRemove(String newCommand, Parameters parameters, boolean add) {
        if (newCommand.length() == 1) {
            handleSingleChar(newCommand.charAt(0), parameters, add);
        }
        else if (newCommand.equals(SPACE_WORD)) {
            handleSpaceWord(parameters, add);
        }
        else if (newCommand.length() == 3 && newCommand.charAt(1) == '-') {
            handleRange(newCommand, parameters, add);
        } 
        else if (newCommand.equals(ALL)) {
            handleAll(parameters, add);
        } 
        else {
            handleIncorrectFormat(add);
        }
    }

    private void handleSingleChar(char c, Parameters parameters, boolean add) {
        if (add) {
            parameters.getCharMatcher().addChar(c);
            return
            } 
        parameters.getCharMatcher().removeChar(c);
    }


    private void handleSpaceWord(Parameters parameters, boolean add) {
        char spaceChar = SPACE;
        if (add) {
            parameters.getCharMatcher().addChar(spaceChar);
            return;
        } 
        parameters.getCharMatcher().removeChar(spaceChar);
    
    }

    private void handleRange(String newCommand, Parameters parameters, boolean add) {
        int minimalLetter = Math.min(newCommand.charAt(0), newCommand.charAt(2));
        int maximalLetter = Math.max(newCommand.charAt(0), newCommand.charAt(2));
        for (int i = minimalLetter; i <= maximalLetter; i++) {
            if (add) {
                parameters.getCharMatcher().addChar((char) i);
            } 
            else {
            parameters.getCharMatcher().removeChar((char) i);
            }
        }
    }

    private void handleAll(Parameters parameters, boolean add) {
        if (add) {
            for (int i = 32; i < 127; i++) {
                parameters.getCharMatcher().addChar((char) i);
            }
            return;
        } 
        parameters.setCharMatcher(new SubImgCharMatcher(new char[]{}));
    }

    private void handleIncorrectFormat(boolean add) {
        if (add) {
            System.out.print(INCORRECT_FORMAT);
            return;
        } 
        System.out.print(INCORRECT_FORMAT);
    }


    public static void main(String[] args) throws IOException {
        new Shell().run();
    }

}
