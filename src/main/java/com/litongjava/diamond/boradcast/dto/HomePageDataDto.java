package com.litongjava.diamond.boradcast.dto;

import java.util.List;

import com.litongjava.diamond.boradcast.model.Platform;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class HomePageDataDto {
  private Platform platform;
  private List<EpisodeDto> episodes;
  private Long total;
  private Integer pageNo;
  private Integer pageSize;
  private Integer totalPages;
}
