* 安装Genymotion模拟器(AMD CPU)  

  前提:BIOS打开CPU虚拟化

  1. 安装VirtualBox 5.1.14.exe 

  2. 安装 genymotion-2.8.1.exe  
  3. 安装完成后,将 player.exe 、genyshell.exe 、genymotion.exe 替换掉根目录的文件  
  4. 启动genymotion.exe添加一个设备:
     VirtualBox -> 配置Oracle VM VirtualBox安装路径(D:/program/install/64/VirtualBox/)
     ADB -> 配置Android SDK路径  
  5. 下载安装Genymotion插件
     Settings -> Plugins -> Browse repositories... -> Genymotion(下载插件)
     Settings -> Other Settings -> Genymotion(配置安装路径)  
  6. 单击Android Studio中的Genymotion图标,在Genymotion Device Manager窗口中,单击New...按钮  
  7. Genymotion账号:
     用户名:changan25801@163.com
     密码:19830217  
  8. 选择模拟器Google Nexus5-4.4.4-API19  
  9. 选中新建的模拟器,单击Start按钮启动模拟器
     **NOTE:**Intel CPU使用Android Studio内置的模拟器需要安装HAXM插件
     File -> Settings -> Appearance & Behavior -> System Settings -> Android SDK -> SDK Tools -> 勾选Intel x86 Emulator Accelerator(HAXM installer)  

* 设置SDK  

  Settings -> ADB -> Use custom Android SDK tools

* Device settings  

  Your virtual devices -> 点击扳手图标  

  Processor(s):1  

  Base memory(MB):1024  

  Predefined screen size: 720*1280-320dpi  

* 拖拉APK到模拟器上出现下述问题  

  Installation error:INSTALL_FAILED_CPU_ABI_INCOMPATIBLE

  Please check logcat output for more details.

  Launch canceled!

  拖拽**<u>Genymotion-ARM-Translation.zip</u>**到模拟器窗口上,弹出对话框点击确定,接着重启下模拟器即可  

* 查看模拟器的sdk目录  

  `/mnt/shell/emulated/0/ `