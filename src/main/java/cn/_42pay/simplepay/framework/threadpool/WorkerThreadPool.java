package cn._42pay.simplepay.framework.threadpool;

import cn._42pay.simplepay.framework.log.Log;
import cn._42pay.simplepay.framework.log.constant.LogScene;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by kevin on 2018/6/25.
 */
public class WorkerThreadPool {
    private ExecutorService executor;

    public WorkerThreadPool() {
    }

    /**
     *
     * @param minThreadPoolSize
     * @param maxThreadPoolSize
     * @param queueSize
     * @param idleTime
     */
    public void init(int minThreadPoolSize,int maxThreadPoolSize, int queueSize,long idleTime) {
        Log.i(LogScene.THREAD_POOL,"初始化线程池:[MinThreadPoolSize:"+minThreadPoolSize+",maxThreadPoolSize:"+maxThreadPoolSize+",QueueSize:"+queueSize+",idleTime:"+idleTime+"]");
        executor  = new ThreadPoolExecutor(minThreadPoolSize, maxThreadPoolSize, idleTime, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueSize));
    }

    public void execute(Runnable task) {
        if (executor == null) {
            Log.i(LogScene.THREAD_POOL,"executor is null");
            return;
        }

        int threadCount=((ThreadPoolExecutor)executor).getActiveCount();
        long taskCount=((ThreadPoolExecutor)executor).getTaskCount();
        int queueCount=((ThreadPoolExecutor)executor).getQueue().size();
        Log.i(LogScene.THREAD_POOL,"当前存活的线程数为:"+threadCount+",总共任务数："+taskCount+",队列上的任务："+queueCount);
        executor.execute(task);
        Log.i(LogScene.THREAD_POOL,"调用线程池执行了任务!");
    }
}
