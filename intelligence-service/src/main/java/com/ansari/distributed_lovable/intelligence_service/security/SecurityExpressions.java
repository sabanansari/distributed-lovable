package com.ansari.distributed_lovable.intelligence_service.security;

import com.ansari.distributed_lovable.common_lib.enums.ProjectPermission;
import com.ansari.distributed_lovable.common_lib.security.AuthUtil;
import com.ansari.distributed_lovable.intelligence_service.client.WorkspaceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Component;

import static com.ansari.distributed_lovable.common_lib.enums.ProjectPermission.*;


@Component("security")
@RequiredArgsConstructor
@Slf4j
public class SecurityExpressions {

    private final AuthUtil authUtil;
    private final WorkspaceClient workspaceClient;

    private boolean hasPermission(Long projectId, ProjectPermission permission) {
        try{
            return workspaceClient.checkPermission(projectId, permission);
        }catch(FeignException.Unauthorized e){
            log.warn("Token expired or invalid during permission check for projectId: {}. Error: {}", projectId, e.getMessage());
            throw new CredentialsExpiredException("JWT token expired or invalid");
        }catch(FeignException e){
            log.error("Workspace-service failed during permission check for projectId: {}. Error: {}", projectId, e.getMessage());
            return false;
        }
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

