# Super Xray
[![](https://img.shields.io/github/v/release/4ra1n/super-xray)](https://github.com/4ra1n/super-xray/releases/latest)
![](https://img.shields.io/github/downloads/4ra1n/super-xray/total)
![](https://img.shields.io/badge/build-JDK8-orange)
![](https://img.shields.io/badge/Java%20Code%20Lines-4805-orange)

## Introduce

[xray](https://github.com/chaitin/xray) is an excellent web vulnerability scanning tool, But only the command line version, Start via `config.yaml` file. In many cases, it is difficult to get started, and a GUI tool is needed to help newcomers use it faster. This tool is just a simple command line wrapper, not a direct method call. In the planning of xray, there will be a truly perfect GUI version of XrayPro tool in the future. Please look forward to it.

Please Note:
- The screenshot in this doc is Chinese, but there is a button to select English UI
- Must be running above JDK8
- Please use a resolution of 1080P or above, and it may not be fully displayed at a resolution of 720P or below

Other Note:

- There is JRE 8+ environment locally (Recommended that JDK version should not be higher than 17)
- **Must**use `java -jar SuperXray.jar` start（Double click startup will cause permission problems in `Windows`）
- Please use the latest version of xray (this tool is not compatible with the old version of xray)

**After 0.7 version we support `exe` version**

After 0.6-beta version we support English Version:

![](../img/16.png)

![](../img/17.png)

It is easy to search poc and run:

![](../img/21.png)

## Download

Latest Download：[Latest Release](https://github.com/4ra1n/super-xray/releases/latest)

## With rad

After version 0.8, it can be linked with `rad`:

Note: First enter the port to enable passive scanning, and then open the `rad` coordination

![](../img/20.png)

## Reverse

1. Click Configure Server
2. Enter any database file name
3. Enter the token password arbitrarily
4. Do not change the IP address and enter a listening port
5. Click Export Configuration File to get a reverse/config.yaml
6. Copy xray and this file to the server
7. Server `./xray reverse` Start the reverse platform
8. Enter the corresponding token and http url on the reverse connection platform (note that the IP format is http://1.1.1.1:8080 ）
9. Enable active scanning or passive scanning