package vc.thinker.dal.test.service.impl;

import vc.thinker.dal.test.entity.User;
import vc.thinker.dal.test.mapper.UserMapper;
import vc.thinker.dal.test.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YZM
 * @since 2019-07-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

	@Autowired
	UserMapper userMapper;
	
	@Override
	public User findOne(Long id) {
		return userMapper.findOne(id);
	}

}
