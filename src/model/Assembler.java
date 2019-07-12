package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assembler {

    private List<String> codeLines;
    private String fileNameToBeCreated;
    private Map<String,String> RAMMap;
    private int RAMCursor = 16;

    public Assembler(String targetFile, String fileNameToBeCreated) {
        codeLines = readFile(targetFile);
        this.fileNameToBeCreated = fileNameToBeCreated;
    }

    private void createPredefinedSymbols() {
        RAMMap = new HashMap<>();
        RAMMap.put("SP",   "0");
        RAMMap.put("LCL",  "1");
        RAMMap.put("ARG",  "2");
        RAMMap.put("THIS", "3");
        RAMMap.put("THAT", "4");

        RAMMap.put("R0",  "0");
        RAMMap.put("R1",  "1");
        RAMMap.put("R2",  "2");
        RAMMap.put("R3",  "3");
        RAMMap.put("R4",  "4");
        RAMMap.put("R5",  "5");
        RAMMap.put("R6",  "6");
        RAMMap.put("R7",  "7");
        RAMMap.put("R8",  "8");
        RAMMap.put("R9",  "9");
        RAMMap.put("R10", "10");
        RAMMap.put("R11", "11");
        RAMMap.put("R12", "12");
        RAMMap.put("R13", "13");
        RAMMap.put("R14", "14");
        RAMMap.put("R15", "15");
    }



    public void createFile(){
        // create pre-defined symbols
        createPredefinedSymbols();

        // delete whitespace from the lines
        List<String> linesWithoutWhiteSpace = deleteWhiteSpaceFromFile(codeLines);

        // mark labels
        List<String> linesWithoutLabels = convertLabels(linesWithoutWhiteSpace);

        // convert symbols to numbers
        List<String> linesWithoutSymbols = convertSymbols(linesWithoutLabels);

        // convert lines to binary code
        List<String> binaryCode = convertLinesToBinaryCode(linesWithoutSymbols);

        // create file .hack
        Path fileHack = Paths.get(fileNameToBeCreated + ".hack");
        try {
            Files.write(fileHack, binaryCode, Charset.forName("UTF-8"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> convertLinesToBinaryCode(List<String> linesWithoutSymbols) {

        List<String> binaryCode = new ArrayList<>();

        for (String line : linesWithoutSymbols){
            if(AInstruction(line)){
                binaryCode.add(convertAInstructionToBinary(line));
            }
            else{
                binaryCode.add(convertCInstructionToBinary(line));
            }
        }

        return binaryCode;
    }

    public String convertCInstructionToBinary(String line) {
        String prefix = "111";

        String comp="";

        String dest="";
        int endOfDest = 0;

        String jump="";

        for(int i = 0; i < line.length(); i++){
            if(line.charAt(i) == '='){
                dest = line.substring(0, i);
                endOfDest = i + 1;
                comp = line.substring(i + 1);
            }
            else if (line.charAt(i) == ';'){
                comp = line.substring(endOfDest , i);
                jump = line.substring(i + 1);
            }
        }

        comp = convertCompToBinary(comp);
        dest = convertDestToBinary(dest);
        jump = convertJumpToBinary(jump);

        return prefix + comp + dest + jump;
    }

    public String convertJumpToBinary(String jump) {
        Map<String, String>  jumpMap = new HashMap<>();
        jumpMap.put("", "000");
        jumpMap.put("JGT","001");
        jumpMap.put("JEQ","010");
        jumpMap.put("JGE","011");
        jumpMap.put("JLT","100");
        jumpMap.put("JNE","101");
        jumpMap.put("JLE","110");
        jumpMap.put("JMP","111");

        return jumpMap.get(jump);
    }

    public String convertDestToBinary(String dest) {
        Map<String, String>  destMap = new HashMap<>();
        destMap.put("", "000");
        destMap.put("M","001");
        destMap.put("D","010");
        destMap.put("MD","011");
        destMap.put("A","100");
        destMap.put("AM","101");
        destMap.put("AD","110");
        destMap.put("AMD","111");

        return destMap.get(dest);
    }

    public String convertCompToBinary(String comp) {

        Map<String, String>  compMap = new HashMap<>();
        compMap.put("0",  "0101010");
        compMap.put("1",  "0111111");
        compMap.put("-1", "0111010");
        compMap.put("D",  "0001100");
        compMap.put("A",  "0110000");
        compMap.put("!D", "0001101");
        compMap.put("!A", "0110001");
        compMap.put("-D", "0001111");
        compMap.put("-A", "0110011");
        compMap.put("D+1","0011111");
        compMap.put("A+1","0110111");
        compMap.put("D-1","0001110");
        compMap.put("A-1","0110010");
        compMap.put("D+A","0000010");
        compMap.put("D-A","0010011");
        compMap.put("A-D","0000111");
        compMap.put("D&A","0000000");
        compMap.put("D|A","0010101");

        compMap.put("M",  "1110000");
        compMap.put("!M", "1110001");
        compMap.put("-M", "1110011");
        compMap.put("M+1","1110111");
        compMap.put("M-1","1110010");
        compMap.put("D+M","1000010");
        compMap.put("D-M","1010011");
        compMap.put("M-D","1000111");
        compMap.put("D&M","1000000");
        compMap.put("D|M","1010101");

        return compMap.get(comp);
    }

    public String convertAInstructionToBinary(String line) {
        String address = line.substring(1);
        String binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(address))).replace(' ', '0');
        return binary;

    }

    public boolean AInstruction(String line) {
        return (line.charAt(0) == '@');
    }

    private List<String> convertSymbols(List<String> linesWithoutLabels) {

        List<String> linesWithoutSymbols = new ArrayList<>();

        for (String line : linesWithoutLabels){
            // if the line is A-instruction
            if(AInstruction(line)){
                String lineWithoutAt = line.substring(1);
                // if the A instruction is a symbol and it's allocated in the RAM
                if(RAMMap.containsKey(lineWithoutAt)){
                    linesWithoutSymbols.add("@" + RAMMap.get(lineWithoutAt));
                }
                // if the A-instruction is a number
                else if(isNumeric(lineWithoutAt)){
                    linesWithoutSymbols.add(line);
                }
                // if the A-instruction is a symbol but it's not allocated in the RAM
                else{
                    RAMMap.put(lineWithoutAt, String.valueOf(RAMCursor));
                    linesWithoutSymbols.add("@" +RAMMap.get(lineWithoutAt));
                    RAMCursor++;
                }
            }
            else {
                linesWithoutSymbols.add(line);
            }
        }

        return linesWithoutSymbols;
    }

    // checks if the string is numeric
    public static boolean isNumeric(String strNum) {
        try {
            double d = Integer.parseInt(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    private List<String> convertLabels(List<String> linesWithoutWhiteSpace) {

        List<String> linesWithoutLabels = new ArrayList<>();
        int counter = 0;


        for (String line : linesWithoutWhiteSpace){
            if(line.charAt(0) == '('){
                String label = line.substring(1, line.length() - 1);
                if (!RAMMap.containsKey(label)){
                    RAMMap.put(label, String.valueOf(counter));
                }
                else {
                    System.out.println("There are more than one " + label + " as label.");
                    break;
                }
            }
            else {
                linesWithoutLabels.add(line);
                counter++;
            }
        }

        return linesWithoutLabels;
    }

    private List<String> deleteWhiteSpaceFromFile(List<String> linesFromFile) {

        // deleting comments
        for (String s : linesFromFile){
            int firstIndexNumberForSlash = 0;
            boolean commentFound = false;

            for (int i = 0; i < s.length(); i++){
                char c = s.charAt(i);
                if (c == '/'){
                    firstIndexNumberForSlash = i;
                    commentFound = true;
                    break;
                }
            }
            if(commentFound) {
                String newString = s.substring(0, firstIndexNumberForSlash);
                linesFromFile.set(linesFromFile.indexOf(s), newString);
            }
        }

        // deleting the spaces
        List<String> linesWithoutSpaces = new ArrayList<>();
        for (String s : linesFromFile) {
            linesWithoutSpaces.add(s.replaceAll("\\p{Z}",""));
        }

        // deleting empty lines
        List<String> lastVersion = new ArrayList<>();
        for(String s : linesWithoutSpaces){
            if (!s.equals("")){
                lastVersion.add(s);
            }
        }
        return lastVersion;
    }

    // reading from file and copying lines
    private List<String> readFile(String filename) {
        List<String> records = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                records.add(line);
            }
            reader.close();
            return records;
        } catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
            return null;
        }
    }
}
