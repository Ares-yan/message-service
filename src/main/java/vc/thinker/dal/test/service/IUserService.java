package vc.thinker.dal.test.service;

import vc.thinker.dal.test.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YZM
 * @since 2019-07-05
 */
public interface IUserService extends IService<User> {

	public User findOne(Long id);
}
