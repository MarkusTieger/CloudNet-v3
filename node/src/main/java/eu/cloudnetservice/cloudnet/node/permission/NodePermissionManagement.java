/*
 * Copyright 2019-2022 CloudNetService team & contributors
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

package eu.cloudnetservice.cloudnet.node.permission;

import eu.cloudnetservice.cloudnet.driver.permission.PermissionGroup;
import eu.cloudnetservice.cloudnet.driver.permission.PermissionManagement;
import eu.cloudnetservice.cloudnet.node.permission.handler.PermissionManagementHandler;
import java.util.Collection;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

public interface NodePermissionManagement extends PermissionManagement {

  void addGroupSilently(@NonNull PermissionGroup permissionGroup);

  void updateGroupSilently(@NonNull PermissionGroup permissionGroup);

  void deleteGroupSilently(@NonNull PermissionGroup permissionGroup);

  void setGroupsSilently(@Nullable Collection<? extends PermissionGroup> groups);

  @NonNull PermissionManagementHandler permissionManagementHandler();

  void permissionManagementHandler(@NonNull PermissionManagementHandler permissionManagementHandler);
}
