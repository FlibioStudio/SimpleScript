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
import io.github.flibio.simplescript.parsing.variable.VariableProperty;

import java.util.Optional;

public class Conditional extends Block {

    private String varName;
    private Object expectedValue;

    private VariableProperty<?> prop;

    public Conditional(Block superBlock, int indentLevel, String variableName, VariableProperty<?> prop, Object expectedValue) {
        super(superBlock, indentLevel);
        this.varName = variableName;
        this.prop = prop;
        this.expectedValue = expectedValue;
    }

    @Override
    public void run() {
        Optional<Variable> vOpt = getVariable(varName);
        if (vOpt.isPresent()) {
            Variable variable = vOpt.get();
            System.out.println("step 1");
            if (variable.getType().getProperties().contains(prop)) {
                System.out.println("comparing " + variable.getValue() + " to " + expectedValue);
                if (!prop.test(variable.getValue(), expectedValue)) {
                    getSuperBlock().setCancelled(true);
                }
            }
        }
    }
}
