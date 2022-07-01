package rw.automation.birthdayWishing.v1.services;

import rw.automation.birthdayWishing.v1.enums.ERole;
import rw.automation.birthdayWishing.v1.models.Role;

import java.util.Optional;
import java.util.Set;

public interface IRoleService {

    Role findByName(ERole role);

    Set<Role> getRoleInaHashSet(ERole role);
}