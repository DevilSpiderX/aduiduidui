package com.dali3a215.aduiduidui.service.impl;

import com.dali3a215.aduiduidui.entity.Statistics;
import com.dali3a215.aduiduidui.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.teasoft.bee.osql.Op;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.honey.osql.core.BeeFactoryHelper;
import org.teasoft.honey.osql.core.ConditionImpl;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service("statisticsService")
public class StatisticsServiceImpl implements StatisticsService {
    private final Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);
    private final SuidRich dao = BeeFactoryHelper.getSuidRich();
    private final TaskThread taskThread = new TaskThread();

    @Override
    public void init() {
        Statistics allUserStatistics = new Statistics();
        allUserStatistics.setKey("AllUser");
        if (!dao.exist(allUserStatistics)) {
            allUserStatistics.setId(1);
            allUserStatistics.setUploadSize(0L);
            allUserStatistics.setDownloadSize(0L);
            allUserStatistics.setFileSize(0L);
            dao.insert(allUserStatistics);
        }
        taskThread.start();
    }

    @Override
    public void uploadSizeAdd(String key, long size) {
        taskThread.addTask(() -> {
            List<Statistics> list = dao.select(new Statistics(), new ConditionImpl().op("key", Op.eq, key));
            if (list.isEmpty()) {
                Statistics keyStatistics = new Statistics();
                keyStatistics.setKey(key);
                keyStatistics.setUploadSize(size);
                keyStatistics.setDownloadSize(0L);
                keyStatistics.setFileSize(0L);
                dao.insert(keyStatistics);
            } else {
                Statistics keyStatistics = list.get(0);
                long oldSize = keyStatistics.getUploadSize();
                keyStatistics.setUploadSize(oldSize + size);
                dao.updateById(keyStatistics, null);
            }
            logger.info("{}的upload_size增加了{}字节", key, size);

            Statistics allUserStatistics = dao.select(new Statistics(),
                    new ConditionImpl().op("key", Op.eq, "AllUser")).get(0);
            long oldSize = allUserStatistics.getUploadSize();
            allUserStatistics.setUploadSize(oldSize + size);
            dao.updateById(allUserStatistics, null);
        });
    }

    @Override
    public void downloadSizeAdd(String key, long size) {
        taskThread.addTask(() -> {
            List<Statistics> list = dao.select(new Statistics(), new ConditionImpl().op("key", Op.eq, key));
            if (list.isEmpty()) {
                Statistics keyStatistics = new Statistics();
                keyStatistics.setKey(key);
                keyStatistics.setUploadSize(0L);
                keyStatistics.setDownloadSize(size);
                keyStatistics.setFileSize(0L);
                dao.insert(keyStatistics);
            } else {
                Statistics keyStatistics = list.get(0);
                long oldSize = keyStatistics.getDownloadSize();
                keyStatistics.setDownloadSize(oldSize + size);
                dao.updateById(keyStatistics, null);
            }
            logger.info("{}的download_size增加了{}字节", key, size);

            Statistics allUserStatistics = dao.select(new Statistics(),
                    new ConditionImpl().op("key", Op.eq, "AllUser")).get(0);
            long oldSize = allUserStatistics.getDownloadSize();
            allUserStatistics.setDownloadSize(oldSize + size);
            dao.updateById(allUserStatistics, null);
        });
    }

    @Override
    public void fileSizeAdd(String key, long size) {
        taskThread.addTask(() -> {
            List<Statistics> list = dao.select(new Statistics(), new ConditionImpl().op("key", Op.eq, key));
            if (list.isEmpty()) {
                Statistics keyStatistics = new Statistics();
                keyStatistics.setKey(key);
                keyStatistics.setUploadSize(0L);
                keyStatistics.setDownloadSize(0L);
                keyStatistics.setFileSize(size);
                dao.insert(keyStatistics);
            } else {
                Statistics keyStatistics = list.get(0);
                long oldSize = keyStatistics.getFileSize();
                keyStatistics.setFileSize(oldSize + size);
                dao.updateById(keyStatistics, null);
            }
            logger.info("{}的file_size增加了{}字节", key, size);

            Statistics allUserStatistics = dao.select(new Statistics(),
                    new ConditionImpl().op("key", Op.eq, "AllUser")).get(0);
            long oldSize = allUserStatistics.getFileSize();
            allUserStatistics.setFileSize(oldSize + size);
            dao.updateById(allUserStatistics, null);
        });
    }

    @Override
    public void fileSizeSubtract(String key, long size) {
        taskThread.addTask(() -> {
            Statistics keyStatistics = dao.select(new Statistics(),
                    new ConditionImpl().op("key", Op.eq, key)).get(0);
            long oldSize = keyStatistics.getFileSize();
            keyStatistics.setFileSize(oldSize - size);
            dao.updateById(keyStatistics, null);
            logger.info("{}的file_size减少了{}字节", key, size);

            Statistics allUserStatistics = dao.select(new Statistics(),
                    new ConditionImpl().op("key", Op.eq, "AllUser")).get(0);
            oldSize = allUserStatistics.getFileSize();
            allUserStatistics.setFileSize(oldSize - size);
            dao.updateById(allUserStatistics, null);
        });
    }

    private static class TaskThread extends Thread {
        private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

        public void addTask(Runnable task) {
            try {
                queue.put(task);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        @Override
        public void run() {
            while (!this.isInterrupted()) {
                try {
                    Runnable task = queue.take();
                    task.run();
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}
