package com.litongjava.diamond.boradcast.model;

import java.sql.Timestamp;

import com.litongjava.db.activerecord.model.DbBaseEntity;
import com.litongjava.db.annotation.ATableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ATableName("db_episode")
public class Episode extends DbBaseEntity {
  private Long id;
  private Long channelId;
  private String title;
  private String description;
  private String audioUrl;
  private Integer durationSec;
  private Timestamp publishedTime;
}
