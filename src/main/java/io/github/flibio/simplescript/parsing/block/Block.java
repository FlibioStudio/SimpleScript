/*
 * This file is part of SimpleScript, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2016 - 2016 Flibio
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.flibio.simplescript.parsing.block;

import io.github.flibio.simplescript.parsing.variable.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Block {

    private List<Variable> variables = new ArrayList<>();
    private List<Block> subBlocks = new ArrayList<>();
    private int indentLevel;
    private Block superBlock;
    private boolean cancel;

    public Block(Block superBlock, int indentLevel) {
        this.superBlock = superBlock;
        this.indentLevel = indentLevel;
    }

    public Block getSuperBlock() {
        return superBlock;
    }

    public List<Block> getSubBlocks() {
        return subBlocks;
    }

    public void addSubBlock(Block subBlock) {
        subBlocks.add(subBlock);
    }

    public void addVariable(Variable variable) {
        variables.add(variable);
    }

    public void clearVariables() {
        variables.clear();
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public Optional<Variable> getVariable(String name) {
        for (Variable var : variables) {
            if (var.getName().equalsIgnoreCase(name)) {
                return Optional.of(var);
            }
        }
        Block curBlock = this;
        while (curBlock != null) {
            for (Variable var : curBlock.getVariables()) {
                if (var.getName().equalsIgnoreCase(name)) {
                    return Optional.of(var);
                }
            }
            curBlock = curBlock.getSuperBlock();
        }
        return Optional.empty();
    }

    public int getIndentLevel() {
        return indentLevel;
    }

    public void setCancelled(boolean toCancel) {
        cancel = toCancel;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public abstract void run();
}
