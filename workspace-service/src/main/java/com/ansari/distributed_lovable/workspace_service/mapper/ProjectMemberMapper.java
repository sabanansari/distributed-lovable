package com.ansari.distributed_lovable.workspace_service.mapper;

import com.ansari.distributed_lovable.workspace_service.dto.member.MemberResponse;
import com.ansari.distributed_lovable.workspace_service.entity.ProjectMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {
//
//    @Mapping(target="userId",source="id")
//    @Mapping(target="role",constant="OWNER")
//    MemberResponse toProjectMemberResponseFromOwner(User owner); TODO

    @Mapping(target="userId",source="id.userId")
    MemberResponse toProjectMemberResponseFromMember(ProjectMember projectMember);

}
