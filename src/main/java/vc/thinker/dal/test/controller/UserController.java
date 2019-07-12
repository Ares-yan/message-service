package vc.thinker.dal.test.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import vc.thinker.dal.test.service.IUserService;
import vc.thinker.dal.test.entity.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YZM
 * @since 2019-07-05
 */
@Api(tags = {"用户"})
@RestController
@RequestMapping("test/user")
public class UserController {
    @Autowired
    private IUserService targetService;
    
//    @Value("${service.port}")
//    String port;

    /**
     * 查询分页数据
     */
    @ApiOperation(value = "查询分页数据")
    @PostMapping("page")
    public Page page(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,@RequestParam(name = "pageSize", defaultValue = "20") int pageSize){
        return null;
    }


    /**
     * 根据id查询
     */
    @ApiOperation(value = "根据id查询数据")
    @GetMapping("test/{id}")
    public String test(@PathVariable("id") Long id){
    	User user = targetService.getById(id);
        return user.toString() + "8761";
    }
    /**
     * 根据id查询
     */
    @ApiOperation(value = "根据id查询数据")
    @GetMapping("detail/{id}")
    public User detail(@PathVariable("id") Long id){
    	return targetService.getById(id);
    }

    /**
     * 新增
     */
    @ApiOperation(value = "新增数据")
    @PatchMapping("save")
    public User save(@RequestBody User user){
        return null;
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除数据")
    @DeleteMapping("delete")
    public String delete(@RequestParam("ids") List<String> ids){
        return null;
    }
}
