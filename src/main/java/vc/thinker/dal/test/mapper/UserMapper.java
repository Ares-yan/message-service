package vc.thinker.dal.test.mapper;

import vc.thinker.dal.test.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author YZM
 * @since 2019-07-05
 */
public interface UserMapper extends BaseMapper<User> {
	
	User findOne(Long id);
}
