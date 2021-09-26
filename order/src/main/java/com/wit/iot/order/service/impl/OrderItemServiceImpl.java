package com.wit.iot.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wit.iot.order.domain.Order;
import com.wit.iot.order.domain.OrderItem;
import com.wit.iot.order.mapper.BsOrderItemDao;
import com.wit.iot.order.service.OrderItemService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class OrderItemServiceImpl extends ServiceImpl<BsOrderItemDao, OrderItem> implements OrderItemService {

    //我们使用的是springboot，sqlSessionTemplate是可以自己注入的
    @Autowired
    @Qualifier("orderSqlSessionFactory")
    private SqlSessionFactory orderSqlSessionFactory;
    @Autowired
    private BsOrderItemDao orderItemDao;

    @Transactional(rollbackFor = Exception.class, transactionManager = "orderTransactionManager")
    @Override
    public boolean saveBatch(Collection<OrderItem> entityList) {
        //！！！解决mybatis plus单次批量插入1000条以上报堆栈溢出问题
        List<OrderItem> entities = new ArrayList<>(entityList);
        SqlSession sqlSession = orderSqlSessionFactory.openSession(ExecutorType.BATCH, false);

        try{
            for (int i = 0; i < entityList.size(); i++) {
                orderItemDao.insert(entities.get(i));
                if (i % 1000 == 0 || i == entities.size() - 1) {
                    //手动每400条提交一次，提交后无法回滚
                    sqlSession.commit();
                    //清理缓存，防止溢出
                    sqlSession.clearCache();
                }
            }
        }catch (Exception e){
            sqlSession.rollback();
            return false;
        }finally {
            sqlSession.close();
        }

        return true;
    }
}
