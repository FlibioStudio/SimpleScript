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
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Optional;

public class Broadcast extends Block {

    private String msg;

    public Broadcast(Block superBlock, int indentLevel, String msg) {
        super(superBlock, indentLevel);
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }

    @Override
    public void run() {
        Optional<Variable> message = getVariable(msg);
        if (message.isPresent() && message.get().getValue() instanceof String) {
            Sponge.getServer().getBroadcastChannel().send(TextSerializers.FORMATTING_CODE.deserialize((String) message.get().getValue()));
        }
    }

}
