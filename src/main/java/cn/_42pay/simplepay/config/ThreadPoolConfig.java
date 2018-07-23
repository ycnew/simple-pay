package cn._42pay.simplepay.config;

import cn._42pay.simplepay.framework.log.Log;
import cn._42pay.simplepay.framework.log.constant.LogScene;
import cn._42pay.simplepay.framework.threadpool.WorkerThreadPool;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by kevin on 2017/8/19.
 */
@Configuration
@ConfigurationProperties(prefix = "thread")
@Getter
@Setter
public class ThreadPoolConfig {
    /**
     * 最大线程池
     */
    private int minThreadPoolSize;

    /**
     * 最小线程池
     */
    private int maxThreadPoolSize;

    /**
     * 阻塞的线程个数
     */
    private int queueSize;

    /**
     * 线程空闲时间 时间(秒)
     */
    private int idleTime;

    @Bean(name = "workerThreadPool")
    public WorkerThreadPool createWorkerThreadPool() {
        Log.i(LogScene.THREAD_POOL,"开始初始化线程池!");
        WorkerThreadPool workerThreadPool = new WorkerThreadPool();
        workerThreadPool.init(minThreadPoolSize, maxThreadPoolSize, queueSize, idleTime);
        Log.i(LogScene.THREAD_POOL,"初始化线程池完毕!");
        return workerThreadPool;
    }
}
