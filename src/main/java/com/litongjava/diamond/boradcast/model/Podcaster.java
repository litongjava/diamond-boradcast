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
@ATableName("db_podcaster")
public class Podcaster extends DbBaseEntity {
  private Long id;
  private Long platformId;
  private String name;
  private String bio;
}