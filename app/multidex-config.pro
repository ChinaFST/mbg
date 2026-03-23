
#在解决65535限制时候把这个包下的内容分到主dex文件下，该包下做了程序初始化操作，如果没在主包下程序启动失败
-keep class com.dy.colony.app.** { *; }
-keep class org.opencv.** { *; }
