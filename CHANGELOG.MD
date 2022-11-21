## 0.1-beta
初始版本

## 0.2-beta
Security:
- [GHSA-732j-763p-cvqg - RCE Vulnerability in URL input](https://github.com/4ra1n/super-xray/security/advisories/GHSA-732j-763p-cvqg)

Bug:
- [important] 复制和删除文件应该选择Java代码实现 #3
- [important] 最终执行命令不应该使用bash/cmd方式 #4
- [important] 使用PoC模式后再使用Plugins不会生效的问题 #5
- [important] Windows中cmd窗口乱码 #16

## 0.3-beta

该版本为**重大更新**

之前强制停止的逻辑并不能真正地停止，使用`kill/taskkill`命令才可以做到，该版本仅在`Mac OS`测试通过，在`Windows`中不确定。开一个新线程循环监听是否强制停止，如果收到消息立即结束。

Security:
- 修复潜在提权漏洞 [GHSA-2g28-xrw6-fq5f](https://github.com/4ra1n/super-xray/security/advisories/GHSA-2g28-xrw6-fq5f)

Bug:
- [important] 被动扫描后无法主动扫描的问题 #23
- [important] PoC参数为空的问题 #21
- [important] 被动扫描是否应该增加一个停止监听的功能 #20
- [important] windows11中高级配置的xss设置无法显示 #19
- [important] windows保存配置编码问题 #18
- [important] 完成真正的停止逻辑 #26（仅在MacOS测试通过）

Others:
- 打印命令应该用一个新的Form更优雅地展示 #9