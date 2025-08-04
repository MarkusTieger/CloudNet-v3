/*
 * Copyright 2019-2024 CloudNetService team & contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.cloudnetservice.modules.signs.impl.platform.minestom;

import eu.cloudnetservice.ext.component.ComponentFormat;
import lombok.NonNull;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.text.Component;
import net.minestom.server.adventure.serializer.nbt.NbtComponentSerializer;

public class MinestomNBTComponentFormat implements ComponentFormat<BinaryTag> {

  @Override
  public @NonNull BinaryTag fromAdventure(@NonNull Component adventure) {
    return NbtComponentSerializer.nbt().serialize(adventure);
  }

  @Override
  public @NonNull Component toAdventure(@NonNull BinaryTag component) {
    return NbtComponentSerializer.nbt().deserialize(component);
  }
}
