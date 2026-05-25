package com.ansari.distributed_lovable.workspace_service.service.impl;

import com.ansari.distributed_lovable.common_lib.dto.PlanDto;
import com.ansari.distributed_lovable.common_lib.dto.UserDto;
import com.ansari.distributed_lovable.common_lib.enums.ProjectRole;
import com.ansari.distributed_lovable.common_lib.error.BadRequestException;
import com.ansari.distributed_lovable.common_lib.error.ResourceNotFoundException;
import com.ansari.distributed_lovable.common_lib.security.AuthUtil;
import com.ansari.distributed_lovable.workspace_service.client.AccountClient;
import com.ansari.distributed_lovable.workspace_service.dto.project.ProjectRequest;
import com.ansari.distributed_lovable.workspace_service.dto.project.ProjectResponse;
import com.ansari.distributed_lovable.workspace_service.dto.project.ProjectSummaryResponse;
import com.ansari.distributed_lovable.workspace_service.entity.Project;
import com.ansari.distributed_lovable.workspace_service.entity.ProjectMember;
import com.ansari.distributed_lovable.workspace_service.entity.ProjectMemberId;
import com.ansari.distributed_lovable.workspace_service.mapper.ProjectMapper;
import com.ansari.distributed_lovable.workspace_service.repository.ProjectMemberRepository;
import com.ansari.distributed_lovable.workspace_service.repository.ProjectRepository;
import com.ansari.distributed_lovable.workspace_service.service.ProjectService;
import com.ansari.distributed_lovable.workspace_service.service.ProjectTemplateService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class ProjectServiceImpl implements ProjectService {

    ProjectRepository projectRepository;
    ProjectMemberRepository projectMemberRepository;
//    UserRepository userRepository;
    ProjectMapper projectMapper;
    AuthUtil authUtil;
//    SubscriptionService subscriptionService;
    ProjectTemplateService projectTemplateService;
    AccountClient accountClient;

    @Override
    public ProjectResponse createProject(ProjectRequest request) {

        if(!canCreateNewProject()){
            throw new BadRequestException("Cannot create newd project with current plan, upgrade plan now.");
        }

        Long owner = authUtil.getCurrentUserId();
//        User owner = userRepository.findById(userId).orElseThrow(
//                () -> new ResourceNotFoundException("User", userId.toString())
//        );

     //   User owner = userRepository.getReferenceById(userId); //It will provide Hibernate proxy, won't hit database

        Project project = Project.builder()
                .name(request.name())
                .isPublic(false)
                .build();

        project = projectRepository.save(project);

        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), owner);

        ProjectMember member = ProjectMember.builder()
                .id(projectMemberId)
                .projectRole(ProjectRole.OWNER)
                .acceptedAt(Instant.now())
                .invitedAt(Instant.now())
                .project(project)
                .build();

        projectMemberRepository.save(member);

        projectTemplateService.initializeProjectFromTemplate(project.getId());

        return projectMapper.toProjectResponse(project);
    }

    @Override

    public List<ProjectSummaryResponse> getUserProjects() {
        Long userId = authUtil.getCurrentUserId();
        var projectWithRoles = projectRepository.findAllAccessibleByUser(userId);
        return projectWithRoles.stream()
                .map(p -> projectMapper.toProjectSummaryResponse(p.getProject(), p.getRole()))
                .toList();
    }

    @Override
    @PreAuthorize("@security.canViewProject(#id)")
    public ProjectSummaryResponse getUserProjectById(Long id) {
        Long userId = authUtil.getCurrentUserId();

        var projectWithRole = projectRepository.findAccessibleProjectByIdWithRole(id, userId).orElseThrow(
                () -> new BadRequestException("Project not found with id " + id.toString())
        );
        return projectMapper.toProjectSummaryResponse(projectWithRole.getProject(), projectWithRole.getRole());
    }



    @Override
    @PreAuthorize("@security.canEditProject(#id)")
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Long userId = authUtil.getCurrentUserId();
        Project project = getAccessibleProjectById(id, userId);
        project.setName(request.name());
        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    @PreAuthorize("@security.canDeleteProject(#id)")
    public void softDelete(Long id) {
        Long userId = authUtil.getCurrentUserId();
        Project project = getAccessibleProjectById(id, userId);

        project.setDeletedAt(Instant.now());
        projectRepository.save(project);
    }

    ///  INTERNAl FUNCTIONS
    private Project getAccessibleProjectById(Long id,Long userId){
        return projectRepository.findAccessibleProjectById(id, userId)
                .orElseThrow(()-> new ResourceNotFoundException("Project", id.toString()));
    }

    private boolean canCreateNewProject(){
        Long userId = authUtil.getCurrentUserId();

        if(userId == null){
            return false;
        }

        PlanDto plan = accountClient.getCurrentSubscriptionPlanByUser();

        int maxAllowed = plan.maxProjects();
        int ownedCount = projectMemberRepository.countProjectOwnedByUser(userId);

        return ownedCount < maxAllowed; // For example, limit to 5 projects per user
    }

}
