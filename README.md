# IndexSidebar
索引侧边栏


# 属性
## 布局方向
- 垂直
- 水平

## 字符显示
*建议*一个字符
- 大小
- 颜色
- 全角半角

Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
Step 2. Add the dependency

	dependencies {
	        compile 'com.github.BusyCount:IndexSidebar:0.1'
	}
