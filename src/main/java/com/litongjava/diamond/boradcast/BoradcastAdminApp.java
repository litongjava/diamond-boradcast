package com.litongjava.diamond.boradcast;

import com.litongjava.annotation.AComponentScan;
import com.litongjava.tio.boot.TioApplication;

@AComponentScan
public class BoradcastAdminApp {
  public static void main(String[] args) {
    long start = System.currentTimeMillis();
    TioApplication.run(BoradcastAdminApp.class, args);
    long end = System.currentTimeMillis();
    System.out.println((end - start) + "(ms)");
  }
}
