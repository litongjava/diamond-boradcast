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
@ATableName("db_channel")
public class Channel extends DbBaseEntity {
  private Long id;
  private Long podcasterId;
  private String title;
  private String description;
  private String coverUrl;
}