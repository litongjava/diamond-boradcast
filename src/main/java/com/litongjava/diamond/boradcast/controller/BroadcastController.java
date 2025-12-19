package com.litongjava.diamond.boradcast.controller;

import com.litongjava.annotation.RequestPath;
import com.litongjava.diamond.boradcast.dto.HomePageDataDto;
import com.litongjava.diamond.boradcast.service.BroadcastService;
import com.litongjava.jfinal.aop.Aop;
import com.litongjava.model.body.RespBodyVo;
import com.litongjava.tio.http.common.HttpRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestPath("/boradcast")
public class BroadcastController {

  private final BroadcastService broadcastService = Aop.get(BroadcastService.class);

  /**
   * 首页接口
   * 
   * @param request HTTP请求对象
   * @return 首页数据
   */
  @RequestPath("/index")
  public RespBodyVo index(HttpRequest request) {
    try {
      // 1. 获取请求参数
      Integer pageNo = request.getInt("pageNo", 1);
      Integer pageSize = request.getInt("pageSize", 20);
      String lang = request.getParam("lang", "zh");

      // 2. 获取Origin头，用于识别平台
      String origin = request.getHeader("origin");

      if (origin == null || origin.trim().isEmpty()) {
        origin = "default"; // 默认平台
      }

      // 3. 参数校验
      if (pageNo < 1) {
        pageNo = 1;
      }
      if (pageSize < 1 || pageSize > 100) {
        pageSize = 20;
      }

      log.info("首页请求参数: origin={}, pageNo={}, pageSize={}, lang={}", origin, pageNo, pageSize, lang);

      // 4. 调用服务获取数据
      HomePageDataDto homePageData = broadcastService.getHomePageData(origin, pageNo, pageSize);

      // 5. 返回成功响应
      return RespBodyVo.ok(homePageData);

    } catch (Exception e) {
      log.error("首页接口异常", e);
      return RespBodyVo.fail("服务器内部错误: " + e.getMessage());
    }
  }

  /**
   * 健康检查接口
   */
  @RequestPath("/health")
  public RespBodyVo health() {
    return RespBodyVo.ok("Service is healthy");
  }
}