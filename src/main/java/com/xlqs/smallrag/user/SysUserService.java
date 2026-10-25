package com.xlqs.smallrag.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xlqs.smallrag.entity.SysUser;
import com.xlqs.smallrag.mapper.SysUserDao;
import com.xlqs.smallrag.utils.PasswordUtil;
import com.xlqs.smallrag.vo.RegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/23 18:47
 * @description SysUserService 类说明：TODO
 * @since 2026/10/23
 */
@Service
@RequiredArgsConstructor
public class SysUserService {

    private final SysUserDao sysUserDao;

    /**
     * 用户注册。
     */
    @Transactional
    public void register(RegisterDto dto) {
        SysUser existing = sysUserDao.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, dto.getUsername())
                        .last("LIMIT 1")
        );

        if (existing != null) {
            throw new IllegalArgumentException("用户名已存在");
        }

        String salt = PasswordUtil.generateSalt();

        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setNickname(dto.getNickname());
        user.setSalt(salt);
        user.setPassword(PasswordUtil.encode(dto.getPassword(), salt));
        user.setStatus(1);

        try {
            sysUserDao.insert(user);
        } catch (DuplicateKeyException e) {
            // 防止并发注册相同用户名
            throw new IllegalArgumentException("用户名已存在");
        }
    }

    /**
     * 根据用户名查询用户。
     */
    public SysUser findByUsername(String username) {
        return sysUserDao.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, username)
                        .last("LIMIT 1")
        );
    }

    /**
     * 校验账号状态和密码。
     */
    public SysUser authenticate(String username, String rawPassword) {
        SysUser user = findByUsername(username);

        if (user == null
                || !Integer.valueOf(1).equals(user.getStatus())
                || !PasswordUtil.matches(
                rawPassword,
                user.getSalt(),
                user.getPassword())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }

        return user;
    }
}
