package com.ansari.distributed_lovable.intelligence_service.security;

import com.ansari.distributed_lovable.common_lib.enums.ProjectPermission;
import com.ansari.distributed_lovable.common_lib.security.AuthUtil;
import com.ansari.distributed_lovable.intelligence_service.client.WorkspaceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.ansari.distributed_lovable.common_lib.enums.ProjectPermission.*;


@Component("security")
@RequiredArgsConstructor
public class SecurityExpressions {

    private final AuthUtil authUtil;
    private final WorkspaceClient workspaceClient;

    private boolean hasPermission(Long projectId, ProjectPermission permission) {
        return workspaceClient.checkPermission(projectId, permission);
    }

    public boolean canViewProject(Long projectId) {
        return hasPermission(projectId, VIEW);

    }

    public boolean canEditProject(Long projectId) {
        return hasPermission(projectId, EDIT);
    }

    public boolean canDeleteProject(Long projectId) {
        return hasPermission(projectId, DELETE);
    }

    public boolean canViewMembers(Long projectId) {
        return hasPermission(projectId, VIEW_MEMBERS);
    }

    public boolean canManageMembers(Long projectId) {
        return hasPermission(projectId, MANAGE_MEMBERS);
    }
}

