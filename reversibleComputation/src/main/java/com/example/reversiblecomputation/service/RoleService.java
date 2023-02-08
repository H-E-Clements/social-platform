package com.example.reversiblecomputation.service;


import com.example.reversiblecomputation.models.Role;
import com.example.reversiblecomputation.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Saves a role to the database.
     * @param role The role to save
     */
    public void saveRole(Role role) {
        roleRepository.setRole(
                role.getId(),
                role.getName()
        );
    }

    /**
     * Gets all users that match the given parameters.
     * @return a {@link List} of {@link Role}s
     */
    @Transactional
    public List<Role> getRoles(String id, String name) {
        return roleRepository.getRoles(id, name);
    }

    /**
     * Gets a single role using the given parameters. If multiple
     * roles are present, the first one is returned.
     * @return a {@link Role} or `null` if none exists
     */
    @Transactional
    public Role getRole(String id, String name) {
        List<Role> roles = this.getRoles(id, name);
        if (roles.size() > 0) {
            return roles.get(0);
        } else {
            return null;
        }
    }

    /**
     * Deletes all roles that match the given parameters
     */
    public void delRoles(String id, String name) {
        roleRepository.delRoles(id, name);
    }

    /**
     * Gets a list of all roles.
     * @return a {@link List} of all {@link Role}s
     */
    @Transactional
    public List<Role> getAllRoles() {
        return this.getRoles(null, null);
    }

    /**
     * Gets a role by its name.
     * @param name the name to use
     * @return a {@link Role} or `null` if none exists
     */
    @Transactional
    public Role getByName(String name) {
        return this.getRole(null, name);
    }
}

