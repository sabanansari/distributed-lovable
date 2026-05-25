package com.ansari.distributed_lovable.workspace_service.service;

import com.ansari.distributed_lovable.workspace_service.dto.member.InviteMemberRequest;
import com.ansari.distributed_lovable.workspace_service.dto.member.MemberResponse;
import com.ansari.distributed_lovable.workspace_service.dto.member.UpdateMemberRoleRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectMemberService {
     List<MemberResponse> getProjectMembers(Long projectId);

     MemberResponse inviteMember(Long projectId, InviteMemberRequest request);

     MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request);

    MemberResponse removeProjectMember(Long projectId, Long memberId);
}
