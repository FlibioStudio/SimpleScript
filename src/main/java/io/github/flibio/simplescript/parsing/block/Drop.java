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
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnCause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class Drop extends Block {

    private String targetLoc, item, amount;

    public Drop(Block superBlock, int indentLevel, String targetLoc, String item, String amount) {
        super(superBlock, indentLevel);
        this.targetLoc = targetLoc;
        this.item = item;
        this.amount = amount;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        Optional<Variable> iOpt = getVariable(item);
        Optional<Variable> lOpt = getVariable(targetLoc);
        Optional<Variable> aOpt = getVariable(amount);
        if (iOpt.isPresent() && lOpt.isPresent() && aOpt.isPresent()) {
            Variable loc = lOpt.get();
            Variable item = iOpt.get();
            Variable amount = aOpt.get();
            if (item.getValue() instanceof ItemType && loc.getValue() instanceof Location<?> && amount.getValue() instanceof Double) {
                ItemType itemType = (ItemType) item.getValue();
                Location<World> location = (Location<World>) loc.getValue();
                int quantity = (int) Math.round((Double) amount.getValue());
                ItemStack stack = Sponge.getRegistry().createBuilder(ItemStack.Builder.class).itemType(itemType).quantity(quantity).build();
                Optional<Entity> eOpt = location.getExtent().createEntity(EntityTypes.ITEM, location.getPosition());
                if (eOpt.isPresent()) {
                    Entity entity = eOpt.get();
                    entity.offer(Keys.REPRESENTED_ITEM, stack.createSnapshot());
                    location.getExtent().spawnEntity(entity, Cause.source(SpawnCause.builder().type(SpawnTypes.PLUGIN).build()).build());
                }
            }
        }
    }
}
