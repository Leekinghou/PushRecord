package com.example.pushdocument.service.impl;

import com.example.pushdocument.dao.PushProcessMapper;
import com.example.pushdocument.entity.PushProcess;
import com.example.pushdocument.service.PushProcessService;
import com.example.pushdocument.util.PushUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class PushProcessServiceImpl implements PushProcessService {
    @Autowired
    private PushUtil pushUtil;
    @Autowired
    private PushProcessMapper pushProcessMapper;

    private final static Logger logger = LoggerFactory.getLogger(PushProcessServiceImpl.class);

    // 每个线程每次查询的条数
    private static final Integer LIMIT = 5000;
    // 核心线程数：设置为操作系统CPU数量乘以2
    private static final Integer CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    // 最大线程数：设置为核心线程数
    private static final Integer MAXIMUM_POOL_SIZE = CORE_POOL_SIZE;
    // 创建线程池
    ThreadPoolExecutor pool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE * 2, 0,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>(100));

    @Override
    public void pushData() throws ExecutionException, InterruptedException {
        // 计数器，需要保证线程安全
        int count = 0;
        // 未推送数据总数
        Integer total = pushProcessMapper.countPushRecordsByState(0);
        logger.info("未推送数据条数: {}", total);
        // 计算需要多少轮
        int num = total / (LIMIT * CORE_POOL_SIZE) + 1;
        logger.info("要经过的轮数: {}", num);
        // 统计总共推送成功的数据条数
        int totalSuccessCount = 0;
        // 总轮数
        for (int i = 0; i < num; i++) {
            // 接收线程返回结果
            List<Future<Integer>> futureList= new ArrayList<>(32);
            // 起CORE_POOL_SIZE个线程并行查询更新库，加锁
            for (int j = 0; j < CORE_POOL_SIZE; j++) {
//                synchronized (PushProcessServiceImpl.class) {
                int start = count * LIMIT;
                count++;
                // 提交线程，用数据起始位置标识线程
                Future<Integer> future = pool.submit(new PushDataTask(start, LIMIT, start));
                // 先不取值，防止阻塞,放进集合
                futureList.add(future);
//                }
            }
            // 统计本轮推送成功的数据
            for(Future future: futureList) {
                totalSuccessCount = totalSuccessCount + (int) future.get();
            }
        }
        // 关闭线程池
        // pool.shutdown();
        // 更新推送标志
        pushProcessMapper.updateAllState(1);
        logger.info("推送数据完成，需推送数据:{},推送成功：{}", total, totalSuccessCount);
    }

    /**
     * 推送数据线程类
     */
    class PushDataTask implements Callable<Integer> {
        // 利用 数据库分页的方式，每个线程取 [start,limit] 区间的数据推送，我们需要保证start的一致性
        int start;
        int limit;

        PushDataTask(int start, int limit, int threadNo) {
            this.limit = limit;
            this.start = start;
        }


        @Override
        public Integer call() throws Exception {
            // 设置线程名字
            Thread.currentThread().setName("Thread" + start);

            int count = 0;
            // 推送的数据
            List<PushProcess> pushProcessList = pushProcessMapper.findPushRecordsByStateLimit(0, start, limit);
            if(CollectionUtils.isEmpty(pushProcessList)) {
                return count;
            }
            logger.info("推送区间: [{},{}]", start, start + limit);
            for(PushProcess process: pushProcessList) {
//                logger.info("数据参考: {}", process.getAuditPerson());
                boolean isSuccess = pushUtil.sendRecord(process);
                if(isSuccess) {
                    // 推送成功
                    pushProcessMapper.updateFlagById(process.getId(), 1);
                    count++;
                } else {
                    pushProcessMapper.updateFlagById(process.getId(), 2);
                }
            }
            logger.info("推送区间[{},{}]数据，推送成功{}条", start, start + limit, count);
            return count;
        }
    }

}
