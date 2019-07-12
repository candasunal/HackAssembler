package tests;

import model.Assembler;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestAssembler {

    private Assembler testAssembler;

    @Before
    public void setUp() {
        testAssembler = new Assembler("C:\\Program Files\\nand2tetris\\projects\\06\\add\\Add.asm", "Add");
    }

    @Test
    public void testConvertAInstructionToBinary() {
        assertEquals("0000000000000010", testAssembler.convertAInstructionToBinary("@2"));
        assertEquals("1010101010101010", testAssembler.convertAInstructionToBinary("@43690"));
        assertEquals("0101010101010101", testAssembler.convertAInstructionToBinary("@21845"));
    }

    @Test
    public void testAInstruction() {
        assertTrue(testAssembler.AInstruction("@123"));
        assertFalse(testAssembler.AInstruction("DM=M+1"));
    }

    @Test
    public void testConvertCInstructionToBinary() {
        assertEquals("1110111111001000", testAssembler.convertCInstructionToBinary("M=1"));
        assertEquals("1110101010001000", testAssembler.convertCInstructionToBinary("M=0"));
        assertEquals("1111110000010000", testAssembler.convertCInstructionToBinary("D=M"));
        assertEquals("1110101010000111", testAssembler.convertCInstructionToBinary("0;JMP"));
        assertEquals("1111000010001000", testAssembler.convertCInstructionToBinary("M=D+M"));
        assertEquals("1111000010010100", testAssembler.convertCInstructionToBinary("D=D+M;JLT"));
    }

    @Test
    public void testConvertJumpToBinary() {
        assertEquals("000", testAssembler.convertJumpToBinary(""));
        assertEquals("001", testAssembler.convertJumpToBinary("JGT"));
        assertEquals("010", testAssembler.convertJumpToBinary("JEQ"));
        assertEquals("011", testAssembler.convertJumpToBinary("JGE"));
        assertEquals("100", testAssembler.convertJumpToBinary("JLT"));
        assertEquals("101", testAssembler.convertJumpToBinary("JNE"));
        assertEquals("110", testAssembler.convertJumpToBinary("JLE"));
        assertEquals("111", testAssembler.convertJumpToBinary("JMP"));

    }

    @Test
    public void testConvertDestToBinary() {
        assertEquals("000", testAssembler.convertDestToBinary(""));
        assertEquals("001", testAssembler.convertDestToBinary("M"));
        assertEquals("010", testAssembler.convertDestToBinary("D"));
        assertEquals("011", testAssembler.convertDestToBinary("MD"));
        assertEquals("100", testAssembler.convertDestToBinary("A"));
        assertEquals("101", testAssembler.convertDestToBinary("AM"));
        assertEquals("110", testAssembler.convertDestToBinary("AD"));
        assertEquals("111", testAssembler.convertDestToBinary("AMD"));

    }

    @Test
    public void testConvertCompToBinary() {
    }
    
}