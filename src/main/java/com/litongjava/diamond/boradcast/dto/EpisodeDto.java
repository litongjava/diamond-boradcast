package com.litongjava.diamond.boradcast.dto;


import java.sql.Timestamp;

import com.litongjava.diamond.boradcast.model.Channel;
import com.litongjava.diamond.boradcast.model.Platform;
import com.litongjava.diamond.boradcast.model.Podcaster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

// 剧集DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EpisodeDto {
  private Long id;
  private String title;
  private String description;
  private Integer durationSec;
  private Timestamp publishedTime;
  private Channel channel;
  private Podcaster podcaster;
  private Platform platform;
}