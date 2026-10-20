package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单
     */
    @Scheduled(cron = "0 * * * * ?") // 每分钟触发一次
    public void processTimeoutOrder() {
        log.info("定时处理超时订单：{}", LocalDateTime.now());

        // 当前时间减去15分钟
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);

        // 查询状态为“待付款”且下单时间在15分钟前的订单
        List<Orders> orderList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);

        // 判断并处理
        if (orderList != null && orderList.size() > 0) {
            for (Orders orders : orderList) {
                // 1. 修改订单状态为已取消
                orders.setStatus(Orders.CANCELLED);
                // 2. 设置取消原因
                orders.setCancelReason("订单超时，自动取消");
                // 3. 设置取消时间
                orders.setCancelTime(LocalDateTime.now());

                // 4. 更新数据库
                orderMapper.update(orders);
            }
        }
    }

    /**
     * 处理一周处于派送中的订单
     */
    @Scheduled(cron = "0 0 1 * * ?") // 每天凌晨1点触发一次
    public void processUnfinishedOrders() {
        log.info("定时处理一周处于派送中的订单：{}", LocalDateTime.now());

        // 当前时间减去60分钟
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);

        // 查询状态为“派送中”且订单完成时间在一周前的订单
        List<Orders> orderList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);

        // 判断并处理
        if (orderList != null && orderList.size() > 0) {
            for (Orders orders : orderList) {
                //  修改订单状态为已完成
                orders.setStatus(Orders.COMPLETED);
                //  更新数据库
                orderMapper.update(orders);
            }
        }
    }
}