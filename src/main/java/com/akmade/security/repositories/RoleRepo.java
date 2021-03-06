package com.akmade.security.repositories;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.akmade.security.data.Role;
import com.akmade.security.data.RoleType;
import com.akmade.security.data.User;
import com.akmade.security.data.UserRole;
import com.akmade.security.data.UserRoleId;
import com.akmade.security.repositories.SessionRepo.Qry;
import com.akmade.security.repositories.SessionRepo.Txn;
import com.akmade.messaging.security.dto.SecurityDTO;

public class RoleRepo {
	protected static Function<SecurityDTO.Role, Qry<Role>> getDBRole =
			dto ->
				session ->  QueryManager.getRole(dto.getRole(), session);
				
	protected static BiFunction<User, SecurityDTO.Role, Qry<UserRole>> makeUserRole = (user,
			dto) -> session -> {
				Role role = getDBRole.apply(dto).apply(session);
				return role != null ? new UserRole(new UserRoleId(user.getUserId(), role.getRoleId()), role, user)
						: null;
			};

	protected static BiFunction<User, SecurityDTO.Role, Qry<UserRole>> getUserRole = (user,
			dto) -> session -> {
				UserRole uRole = QueryManager.getUserRole(user.getUserId(), dto.getRole(), session);
				return uRole == null ? makeUserRole.apply(user, dto).apply(session) : null;
			};

	protected static BiFunction<User, Collection<SecurityDTO.Role>, Qry<Collection<UserRole>>> makeUserRoles = (
			user, roles) -> session -> roles.stream().map(role -> getUserRole.apply(user, role).apply(session))
					.collect(Collectors.toSet());
			
	protected static Function<Collection<RoleType>, Collection<SecurityDTO.Type>> makeNewRoleTypesDTOs =
			at -> at
					.stream()
					.map(t -> SecurityDTO.Type.newBuilder()
								.setType(t.getType())
								.setDescription(t.getDescription())
								.build())
					.collect(Collectors.toList());
			
	protected static Qry<Collection<SecurityDTO.Type>> getRoleTypeDTOS = 
			session -> makeNewRoleTypesDTOs.apply(QueryManager.getRoleTypes(session));
				
	
	protected static Function<Role, SecurityDTO.Role> makeNewRoleDTO = 
			role -> SecurityDTO
						.Role.newBuilder()
							.setRole(role.getRole())
							.setRoleType(SecurityDTO.Role.RoleType.valueOf(role.getRoleType().getType()))
							.setDescription(role.getDescription())
							.build();
				
	protected static Function<Collection<UserRole>, Collection<SecurityDTO.Role>> makeNewRoleDTOs =
				userRoles -> userRoles
								.parallelStream()
								.map(ur -> makeNewRoleDTO.apply(ur.getRole()))
								.collect(Collectors.toList());

	protected static Function<RoleType, Function<SecurityDTO.Type, RoleType>> mutateRoleType =
			oldRoleType ->
					dto -> {
								oldRoleType.setType(dto.getType());
								oldRoleType.setDescription(dto.getDescription());
								return oldRoleType;
					};
						
	protected static Function<SecurityDTO.Type, RoleType> makeRoleType =
			dto -> new RoleType(dto.getType(), dto.getDescription(), null);
			
	protected static Function<String, Qry<RoleType>> getRoleTypeByType =
			type ->
				session ->
					QueryManager.getRoleTypeByType(type,session);
					
	protected static BiPredicate<UserRole, UserRole> isSameUserRole = (uc,
			uc2) -> uc.getId().getRoleId() == uc2.getId().getRoleId()
					&& uc.getId().getUserId() == uc2.getId().getUserId();

	protected static Function<Collection<UserRole>, Predicate<UserRole>> containsUserRole = userRoles -> ur -> userRoles
			.stream().anyMatch(our -> isSameUserRole.test(our, ur));

	protected static BiFunction<Collection<UserRole>, Collection<UserRole>, Txn> deleteUserRoles = (
			oldUserRoles, newUserRoles) -> session -> {
				oldUserRoles.stream().filter(ur -> containsUserRole.apply(newUserRoles).test(ur) != true)
						.map(ur -> CommandManager.deleteUserRole.apply(ur)).collect(Collectors.toList());
			};
			
	protected static BiFunction<User, Collection<SecurityDTO.Role>, Txn> persistUserRoles = 
		(user, roles) -> 
			session -> {
				Collection<UserRole> newUserRoles = RoleRepo.makeUserRoles.apply(user, roles).apply(session);
				deleteUserRoles.apply(user.getUserRoles(), newUserRoles)
						.andThen(CommandManager.saveUserRoles.apply(newUserRoles)).accept(session);;
			};
			
	protected static Function<SecurityDTO.Type, Txn> persistRoleType =
			roleTypeDTO ->
				session -> {
					RoleType roleType =	getRoleTypeByType.apply(roleTypeDTO.getType()).apply(session);
					if (roleType!=null)
						CommandManager.saveRoleType.apply(mutateRoleType.apply(roleType).apply(roleTypeDTO)).accept(session);
					else
						CommandManager.saveRoleType.apply(makeRoleType.apply(roleTypeDTO));
				};

	protected static Function<SecurityDTO.Type, Txn> deleteRoleType =
			roleTypeDTO ->
				session -> CommandManager.deleteRoleType.apply(getRoleTypeByType.apply(roleTypeDTO.getType()).apply(session)).accept(session);


}
