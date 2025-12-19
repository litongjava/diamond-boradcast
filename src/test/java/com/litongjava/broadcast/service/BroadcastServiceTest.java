package com.litongjava.broadcast.service;

import java.sql.Timestamp;

import org.junit.Before;
import org.junit.Test;

import com.litongjava.db.activerecord.Db;
import com.litongjava.db.activerecord.Row;
import com.litongjava.diamond.boradcast.config.AdminAppConfig;
import com.litongjava.diamond.boradcast.dto.HomePageDataDto;
import com.litongjava.diamond.boradcast.model.Channel;
import com.litongjava.diamond.boradcast.model.Episode;
import com.litongjava.diamond.boradcast.model.Platform;
import com.litongjava.diamond.boradcast.model.Podcaster;
import com.litongjava.diamond.boradcast.service.BroadcastService;
import com.litongjava.tio.boot.testing.TioBootTest;
import com.litongjava.tio.utils.json.JsonUtils;
import com.litongjava.tio.utils.snowflake.SnowflakeIdUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BroadcastServiceTest {

  private BroadcastService broadcastService;

  @Before
  public void setUp() {
    // 启动应用上下文 - 替换为您的配置类
    TioBootTest.runWith(AdminAppConfig.class);
    broadcastService = new BroadcastService();
  }

  /**
   * 测试数据初始化
   */
  @Test
  public void testInitData() {
    try {
      // 1. 创建平台数据
      Long platformId = SnowflakeIdUtils.id();
      Platform platform = new Platform().setId(platformId).setName("硅谷101").setDescription("探索科技前沿，分享创业故事");
      Db.save(Row.fromBean(platform));
      log.info("创建平台: {}", platform);

      // 2. 创建博主数据
      Long podcasterId = SnowflakeIdUtils.id();
      Podcaster podcaster = new Podcaster().setId(podcasterId).setPlatformId(platformId).setName("张三").setBio("资深创业导师");
      Db.save(Row.fromBean(podcaster));
      log.info("创建博主: {}", podcaster);

      // 3. 创建栏目数据
      Long channelId = SnowflakeIdUtils.id();
      Channel channel = new Channel().setId(channelId).setPodcasterId(podcasterId).setTitle("创业指南")
          .setDescription("为创业者提供实用建议").setCoverUrl("https://example.com/covers/startup-guide.jpg");
      Db.save(Row.fromBean(channel));
      log.info("创建栏目: {}", channel);

      // 4. 创建剧集数据
      for (int i = 1; i <= 25; i++) {
        Long episodeId = SnowflakeIdUtils.id();
        Episode episode = new Episode().setId(episodeId).setChannelId(channelId).setTitle("第" + i + "期：创业者的第一桶金")
            .setDescription("在这期节目中，我们邀请了几位成功的创业者分享他们的创业经历，特别是如何找到适合自己的商业模式...")
            .setAudioUrl("https://example.com/audio/episode-" + i + ".mp3").setDurationSec(3600 + i * 60) // 1小时+递增分钟
            .setPublishedTime(new Timestamp(System.currentTimeMillis() - i * 24 * 60 * 60 * 1000L)); // 递减日期

        Db.save(Row.fromBean(episode));
        log.info("创建剧集 {}: {}", i, episode.getTitle());
      }

      log.info("测试数据初始化完成！");

    } catch (Exception e) {
      log.error("初始化测试数据失败", e);
    }
  }

  /**
   * 测试首页数据获取
   */
  @Test
  public void testGetHomePageData() {
    try {
      // 测试获取首页数据
      HomePageDataDto homePageData = broadcastService.getHomePageData("localhost:3000", 1, 20);

      // 输出结果
      String json = JsonUtils.toJson(homePageData);
      log.info("首页数据: {}", json);

      // 验证数据
      assert homePageData != null;
      assert homePageData.getPlatform() != null;
      assert homePageData.getEpisodes() != null;
      assert homePageData.getTotal() > 0;

      log.info("测试通过！");

    } catch (Exception e) {
      log.error("获取首页数据测试失败", e);
    }
  }

  /**
   * 测试分页功能
   */
  @Test
  public void testPagination() {
    try {
      // 测试第1页
      HomePageDataDto page1 = broadcastService.getHomePageData("localhost:3000", 1, 10);
      log.info("第1页数据量: {}", page1.getEpisodes().size());
      log.info("总数: {}, 总页数: {}", page1.getTotal(), page1.getTotalPages());

      // 测试第2页
      HomePageDataDto page2 = broadcastService.getHomePageData("localhost:3000", 2, 10);
      log.info("第2页数据量: {}", page2.getEpisodes().size());

      // 验证分页数据不同
      if (!page1.getEpisodes().isEmpty() && !page2.getEpisodes().isEmpty()) {
        assert !page1.getEpisodes().get(0).getId().equals(page2.getEpisodes().get(0).getId());
        log.info("分页测试通过！");
      }

    } catch (Exception e) {
      log.error("分页功能测试失败", e);
    }
  }

  /**
   * 清理测试数据
   */
  @Test
  public void testCleanData() {
    try {
      // 按依赖顺序删除数据
      Db.update("DELETE FROM db_episode WHERE deleted = 0");
      Db.update("DELETE FROM db_channel WHERE deleted = 0");
      Db.update("DELETE FROM db_podcaster WHERE deleted = 0");
      Db.update("DELETE FROM db_platform WHERE deleted = 0");

      log.info("测试数据清理完成！");

    } catch (Exception e) {
      log.error("清理测试数据失败", e);
    }
  }
}