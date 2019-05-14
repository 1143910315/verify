package com.example.demo.service;

import com.example.demo.bean.User;
import com.example.demo.bean.UserDetailsImpl;
import com.example.demo.core.userdetails.UserDetails;
import com.example.demo.core.userdetails.UserDetailsService;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Transactional
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Transactional
    public List<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    //重写UserDetailsService接口里面的抽象方法
    //根据用户名 返回一个UserDetails的实现类的实例
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println("查找用户：" + s);
        User user = getByUsername(s).get(0);
        if (user == null) {
            throw new UsernameNotFoundException("没有该用户");
        }

        //查到User后将其封装为UserDetails的实现类的实例供程序调用
        //用该User和它对应的Role实体们构造UserDetails的实现类
        return new UserDetailsImpl(user, roleService.getRolesOfUser(user.getUserName()));
    }
}

