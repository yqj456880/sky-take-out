package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    //cacheNames：缓存名称，key：使用方法参数categoryId作为缓存key  dishUserCache::18
    @Cacheable(cacheNames = "dishUserCache",key = "#categoryId")
    public Result<List<DishVO>> list(Long categoryId) {
        //未使用注解
        /*// 1. 拼接Redis的key：dish_分类id，
        String key = "dish_" + categoryId;
        // 2. 去Redis中读取这个key对应的菜品列表数据
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        // 3. 判断：Redis里有数据（缓存命中）
        if (list != null && list.size() > 0) {
            // 直接返回菜品列表，不去访问MySQL数据库
            return Result.success(list);
        }
        // 4. 缓存没命中：构造查询条件，查询【起售状态】的菜品
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//查启用/起售的菜品
        // 5. 调用service查询MySQL，拿到菜品+口味数据
        list = dishService.listWithFlavor(dish);
        // 6. 把从数据库查到的结果存入Redis，下次请求直接读缓存
        redisTemplate.opsForValue().set(key, list);
        // 7. 返回结果
        return Result.success(list);*/

        //使用注解后
        // 缓存没命中时，才会执行下面这段数据库查询代码
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//查询起售中的菜品

        List<DishVO> list = dishService.listWithFlavor(dish);

        // 不需要手动set到redis！@Cacheable自动把返回值存入Redis
        return Result.success(list);
    }
}
