package model;

public class HackAssembler {

    private Assembler add;
    private Assembler max;

    public static void main(String[] args) {
        new HackAssembler();
    }

    public HackAssembler(){
        add = new Assembler("C:\\Program Files\\nand2tetris\\projects\\06\\add\\Add.asm", "Add");
        add.createFile();

        max = new Assembler("C:\\Program Files\\nand2tetris\\projects\\06\\max\\Max.asm", "Max");
        max.createFile();
    }
}
