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

重大更新内容：

之前强制停止的逻辑并不能真正地停止，必须使用`kill/taskkill`命令才可以实现

Bug:
- [important] 被动扫描后无法主动扫描的问题 #23
- [important] PoC参数为空的问题 #21
- [important] 被动扫描是否应该增加一个停止监听的功能 #20
- [important] windows11中高级配置的xss设置无法显示 #19
- [important] windows保存配置编码问题 #18
- [important] 完成真正的停止逻辑 #26（仅在MacOS测试通过）

Others:
- 支持使用本地自定义PoC的功能 #6
- 监听端口工具应该增加一个停止按钮 #7
- 增加一个清屏的按钮 #8
- 打印命令应该用一个新的Form更优雅地展示 #9