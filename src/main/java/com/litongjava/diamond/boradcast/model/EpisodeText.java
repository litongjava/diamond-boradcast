package com.litongjava.diamond.boradcast.model;

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
@ATableName("db_episode_text")
public class EpisodeText extends DbBaseEntity {
  private Long id;
  private Long episodeId;
  private String chinese;
  private String english;
  private String japanese;
}