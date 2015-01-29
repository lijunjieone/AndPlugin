##目标

可以让客户端在运行过程中下载实现代码，修改实现逻辑。客户端从服务器获取最新实现代码，动态加载，最终动态修改业务逻辑，可以应用在插件中。

##实现

首先要定义出功能接口

<code>
package com.dynamic;

public interface IDynamic {
    public String helloWorld();
}
</code>
接在在本地完成实现接口
<code>
package com.dynamic;

public class DynamicTest implements IDynamic {
    @Override
    public String helloWorld() {
        return "Hello World!";
    }
}
</code>

上述代码应该在插件工程内完成。

下面需要编写调用程序工程
在调用工程内也同样需要定义相同的接口，动态加载用来实现这个接口
<code>
package com.dynamic;

public interface IDynamic {
    public String helloWorld();

}
</code>

下面是调用方法，调用方法可以放在任何android工程中。
<code>
		private void ShowPluginContent() {
            final File optimizedDexOutputPath = new File("/sdcard/a.jar");
            Log.e(LOGTAG,"file="+optimizedDexOutputPath.getAbsolutePath());
            File dexOutputDir = getActivity().getDir("dex", 0);
            DexClassLoader cl = new DexClassLoader(optimizedDexOutputPath.getAbsolutePath(),
            		dexOutputDir.getAbsolutePath(), null, getActivity().getClassLoader());
                Class libProviderClazz = null;
                try {
                    libProviderClazz = cl.loadClass("com.dynamic.DynamicTest");
                    IDynamic lib = (IDynamic)libProviderClazz.newInstance();
                    Toast.makeText(getActivity(), lib.helloWorld(), Toast.LENGTH_SHORT).show();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
		}

</code>

接下来需要从插件工程中导出jar文件
![export.png](http://upload-images.jianshu.io/upload_images/47510-54c0779b8c98f632.png)

然后我们需要把jar通过dx工具进行处理，便于android能够识别这个格式。


> ./dx --dex --output=/tmp/my/a.jar /tmp/my/plugin.jar


然后我们把这个这个jar传到/sdcard/上

>adb push /tmp/my/a.jar /sdcard/


然后运行刚才的那个android工程，只需要触发ShowPluginContent()方法，就可以看到Toast

##遇到的问题
问题1：
产生了这个异常android4.1.2 DexClassLoader is not owned by the current user。
解决办法：
 DexClassLoader的第二个参数权限所致。使用
<code>
File dexOutputDir = context.getDir("dex", 0);
</code>
问题2：
产生了java.lang.IllegalAccessError: Class ref in pre-verified class resolved to unexpected implementation
原因：
在打插件包的时候把接口类也打进去了。出现了两个接口类。不过在Android5.0上没有这个问题
解决办法：
不要导出的时候导出接口类。

##测试程序
![测试包，可以用命令下载](http://upload-images.jianshu.io/upload_images/47510-8dd4653cf6dea4cc.png)

> wget -O a.tar.gz http://upload-images.jianshu.io/upload_images/47510-8dd4653cf6dea4cc.png
来下载源码


##进一步
我们可以不定义接口类，也可以通过反射直接来完成这些工作。

##参考
[参考1](http://www.alloyteam.com/2014/04/android-cha-jian-yuan-li-pou-xi/)




