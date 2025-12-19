package com.litongjava.diamond.boradcast.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.litongjava.db.activerecord.Db;
import com.litongjava.db.activerecord.Row;
import com.litongjava.diamond.boradcast.dto.EpisodeDto;
import com.litongjava.diamond.boradcast.dto.HomePageDataDto;
import com.litongjava.diamond.boradcast.model.Channel;
import com.litongjava.diamond.boradcast.model.Platform;
import com.litongjava.diamond.boradcast.model.Podcaster;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BroadcastService {

  /**
   * 获取首页数据 - 使用一条SQL查询所有数据
   * 
   * @param origin   请求来源域名，用于识别平台
   * @param pageNo   页码
   * @param pageSize 每页数量
   * @return 首页数据
   */
  public HomePageDataDto getHomePageData(String origin, Integer pageNo, Integer pageSize) {
    try {
      // 1. 根据origin获取平台ID（这里简化处理，实际应该有一个域名-平台映射表）
      Long platformId = getPlatformIdByOrigin(origin);

      // 2. 获取平台信息
      Platform platformInfo = getPlatformInfo(platformId);

      // 3. 一条SQL查询剧集列表和相关信息
      List<EpisodeDto> episodes = getEpisodesWithDetails(platformId, pageNo, pageSize);

      // 4. 查询总数
      Long total = getTotalEpisodeCount(platformId);

      // 5. 计算总页数
      Integer totalPages = (int) Math.ceil((double) total / pageSize);

      return new HomePageDataDto().setPlatform(platformInfo).setEpisodes(episodes).setTotal(total).setPageNo(pageNo)
          .setPageSize(pageSize).setTotalPages(totalPages);

    } catch (Exception e) {
      log.error("获取首页数据失败", e);
      throw new RuntimeException("获取首页数据失败: " + e.getMessage());
    }
  }

  /**
   * 根据域名获取平台ID（简化实现）
   */
  private Long getPlatformIdByOrigin(String origin) {
    // 实际应该有一个映射表，这里简化为默认返回第一个平台
    Row record = Db.findFirst("SELECT id FROM db_platform WHERE deleted = 0 ORDER BY id LIMIT 1");
    return record != null ? record.getLong("id") : 1L;
  }

  /**
   * 获取平台信息
   */
  private Platform getPlatformInfo(Long platformId) {
    String sql = "SELECT id, name, description FROM db_platform WHERE id = ? AND deleted = 0";
    Row record = Db.findFirst(sql, platformId);

    if (record == null) {
      return new Platform().setId(platformId).setName("默认平台").setDescription("暂无描述");
    }

    return new Platform().setId(record.getLong("id")).setName(record.getStr("name"))
        .setDescription(record.getStr("description"));
  }

  /**
   * 核心方法：一条SQL查询剧集及相关信息
   */
  private List<EpisodeDto> getEpisodesWithDetails(Long platformId, Integer pageNo, Integer pageSize) {
    Integer offset = (pageNo - 1) * pageSize;

    String sql = """
            SELECT
                e.id as episode_id,
                e.title as episode_title,
                e.description as episode_description,
                e.duration_sec as episode_duration_sec,
                e.published_time as episode_published_time,

                c.id as channel_id,
                c.title as channel_title,
                c.description as channel_description,
                c.cover_url as channel_cover_url,

                p.id as podcaster_id,
                p.name as podcaster_name,
                p.bio as podcaster_bio,

                pl.id as platform_id,
                pl.name as platform_name,
                pl.description as platform_description

            FROM db_episode e
            JOIN db_channel c ON e.channel_id = c.id
            JOIN db_podcaster p ON c.podcaster_id = p.id
            JOIN db_platform pl ON p.platform_id = pl.id
            WHERE pl.id = ?
                AND e.deleted = 0
                AND c.deleted = 0
                AND p.deleted = 0
                AND pl.deleted = 0
            ORDER BY e.published_time DESC
            LIMIT ? OFFSET ?
        """;

    List<Row> records = Db.find(sql, platformId, pageSize, offset);
    List<EpisodeDto> episodes = new ArrayList<>();

    for (Row record : records) {
      // 构建Channel DTO
      Channel Channel = new Channel().setId(record.getLong("channel_id")).setTitle(record.getStr("channel_title"))
          .setDescription(record.getStr("channel_description")).setCoverUrl(record.getStr("channel_cover_url"));

      // 构建Podcaster DTO
      Podcaster Podcaster = new Podcaster().setId(record.getLong("podcaster_id"))
          .setName(record.getStr("podcaster_name")).setBio(record.getStr("podcaster_bio"));

      // 构建Platform DTO
      Platform platformDto = new Platform().setId(record.getLong("platform_id")).setName(record.getStr("platform_name"))
          .setDescription(record.getStr("platform_description"));

      // 构建Episode DTO
      Timestamp timestamp = record.getTimestamp("episode_published_time");
      EpisodeDto Episode = new EpisodeDto().setId(record.getLong("episode_id")).setTitle(record.getStr("episode_title"))
          .setDescription(record.getStr("episode_description")).setDurationSec(record.getInt("episode_duration_sec"))
          .setPublishedTime(timestamp).
          //
          setChannel(Channel).setPodcaster(Podcaster).setPlatform(platformDto);

      episodes.add(Episode);
    }

    return episodes;
  }

  /**
   * 获取剧集总数
   */
  private Long getTotalEpisodeCount(Long platformId) {
    String sql = """
            SELECT COUNT(*) as total
            FROM db_episode e
            JOIN db_channel c ON e.channel_id = c.id
            JOIN db_podcaster p ON c.podcaster_id = p.id
            JOIN db_platform pl ON p.platform_id = pl.id
            WHERE pl.id = ?
                AND e.deleted = 0
                AND c.deleted = 0
                AND p.deleted = 0
                AND pl.deleted = 0
        """;

    Row record = Db.findFirst(sql, platformId);
    return record != null ? record.getLong("total") : 0L;
  }
}