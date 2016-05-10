package io.github.flibio.simplescript.parsing.block;

public class Cancel extends Block {

    public Cancel(Block superBlock, int indentLevel) {
        super(superBlock, indentLevel);
    }

    @Override
    public void run() {
        Block curBlock = getSuperBlock();
        while (curBlock != null) {
            curBlock.setCancelled(true);
            curBlock = curBlock.getSuperBlock();
        }
    }

}
