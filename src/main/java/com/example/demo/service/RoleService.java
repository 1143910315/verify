package com.example.demo.service;

import com.example.demo.bean.Role;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getRolesOfUser(String username)
    {
        return roleRepository.findRolesOfUser(username);
    }

    public List<Role> getRolesOfResource(long id)
    {
        return roleRepository.findRolesOfResource(id);
    }
}
