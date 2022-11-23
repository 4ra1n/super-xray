## 0.1-beta

初始版本

## 0.2-beta

Security:
- 修复潜在RCE漏洞 [GHSA-732j-763p-cvqg](https://github.com/4ra1n/super-xray/security/advisories/GHSA-732j-763p-cvqg)

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

## 0.4-beta

这个版本主要是提高稳定性，对已有`bug`进行修复，并且加入了两个重要：
- 皮肤选择功能，可以选择七款主题皮肤之一（需要操作系统支持）
- 保存上次xray设置的功能，不用每次打开都重新选择

Bug:
- [important] windows taskkill 命令 bug #29
- [important] 允许界面扩大缩小并重构高级配置的UI #28
- [important] 查看配置文件只改变Panel大小但Area不变 #31
- [important] windows 控制台乱码修复不完善 #27
- [important] 设置从上次配置自动加载xray #17
- [important] 反连平台支持token认证 #32

Others:
- 支持使用本地自定义PoC的功能 #6
- [new feature] 新增一个主题皮肤选择的功能 #11

## 0.5-beta

比较关键的问题是： 运行中无法访问已有的漏洞报告，因为采用的是`start/open`命令，报告被`xray`进程占用无法打开，但是可以读。
所以解决方案是复制一份实时的然后打开。其他方面是一些`UI`的小`bug`以及将日志输出到具体的文件，而不是全打在命令行。
输出的日志文件和所有产生的漏洞报告都可以在关闭软件的时候自动删除。

Bug:
- [important] 进程占导致无法打开被动扫描中的报告 #44
- [important] 换皮肤后一些按钮的背景色不应该是灰色 #42
- [important] 被动扫描可能会由于输出文件存在无法启动 #33
- 没有设置输出的情况下打开输出文件报错 #38

Others:
- [important] 添加一个跳转xray下载地址的按钮 #45
- [important] 添加start.bat和start.sh #41
- [new feature] 是否有必要提供一个下载按钮 #10 (采用 #45 的方案解决)
- 在Mac OS中如果保存了xray配置每次打开都会弹出提示 #36
- 皮肤RadioButton默认不该是空而应是当前皮肤 #37
- 应该将日志输出到临时文件并关闭后删除 #43
- 皮肤RadioButton默认不该是空而应是当前皮肤 #37
- 添加一个是否自动删除扫描报告的按钮 #39
- 增加一个清屏的按钮 #8

## 0.6-beta

支持了英文版本，修复了各种UI的问题

Bug:
- [important] 在1080P下界面无法完全显示 #46
- [important] 切换语言和皮肤时无法正确容纳UI #51
- [important] 关闭应用前应该先停止所有xray进程 #50
- 切换语言时无法正常显示PoC数量 #52

Others:
- [new feature] english version #40
- #36问题的修复不完善 #47
- 打开扫描结果的Label与Button的UI不匹配 #48
- 点击底部Label跳转Github #49
