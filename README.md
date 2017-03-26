# ZhihuReader-master
非官方的知乎日报，一款基于Retrofit + Rxjava + MVP + MD的APP。

## 概述

APP截图如下，将知乎日报的主题展示方式改成了TabLayout+ViewPager的方式，热门新闻的展示方式没有什么改变，都是头部的轮播图加上listview的方式，首页的内容可以下拉刷新和上拉加载更多的往期内容，其他的主题比如“用户推荐日报”，“日常心理学”等只有下拉刷新，没有上拉加载更多。<br>
在首页时，返回键做了特殊处理，点击时会回到桌面，但是不会销毁MainActivity。就是说在首页时，点击返回键和点击home键的效果是一样的。微信，支付宝等APP都是这么做的，关键代码如下：<br>
```java
    @Override
    public void onBackPressed() {
         Intent intent = new Intent(Intent.ACTION_MAIN);
         intent.addCategory(Intent.CATEGORY_HOME);
         startActivity(intent)
    }
```
![image](https://github.com/NianguoWang/ZhihuReader-master/blob/master/screenshot/Screenshot_2017-03-26-20-17-20.png)

与之不同的是可以点击右下角的FloatActionButton，弹出日历选择的dialog后，选择任意时间的，查看对应日期所推送的内容。当然FloatActionButton在滑动列表的时候是可以隐藏和显示的。<br>
![image](https://github.com/NianguoWang/ZhihuReader-master/blob/master/screenshot/Screenshot_2017-03-26-20-17-27.png)

主题的添加方式改成了类似与掘金的主题添加方式，右边的开关控制是否在首页展示，可以长按条目上下拖动改变展示在首页的顺序。<br>
![image](https://github.com/NianguoWang/ZhihuReader-master/blob/master/screenshot/Screenshot_2017-03-26-20-17-35.png)

所有功能如图所示，其中在收藏页面的收藏条目可以通过左划取消收藏（截图没有体现），可以自行[下载APP](https://fir.im/748t)  体验。<br>
![image](https://github.com/NianguoWang/ZhihuReader-master/blob/master/screenshot/Screenshot_2017-03-26-20-17-42.png)

关于和设置页面采用了PreferenceFragment实现，不用写布局文件，直接写个xml扔进去，展示出来的就是一个符合Material Design规范的页面，不仅方便，而且美观。<br>
 ```java
   @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    
        addPreferencesFromResource(R.xml.fragment_about);
        
    }
 ```
温馨小提示：关于页面狂点作者会有彩蛋哦。<br>
![image](https://github.com/NianguoWang/ZhihuReader-master/blob/master/screenshot/Screenshot_2017-03-26-20-28-12.png)

呐，彩蛋就是这个。点一下，是不是有点小惊喜；再划一下，是不是帅哭了。<br>
![image](https://github.com/NianguoWang/ZhihuReader-master/blob/master/screenshot/Screenshot_2017-03-26-20-28-22.png)

## 接口

[Apis](https://github.com/izzyleung/ZhihuDailyPurify/wiki/%E7%9F%A5%E4%B9%8E%E6%97%A5%E6%8A%A5-API-%E5%88%86%E6%9E%90)<br>
感谢作者[izzyleung](https://github.com/izzyleung)

## 下载APP体验

[下载APP](https://fir.im/748t)<br>
托管在fir.im,发布的是debug版本。欢迎下载体验，有什么好的想法或者需求欢迎通过关于页面的反馈，或者加我微信反馈给我。
## 最后

第一个完整的个人项目，写的比较仓促，也有部分代码参考了其他大神开源的项目。<br>
[纸飞机](https://github.com/TonnyL/PaperPlane)-采用MVP架构，集合了知乎日报、果壳精选和豆瓣一刻的综合性阅读客户端<br>
[知乎](https://github.com/yiyibb/Zhihu)-高仿知乎日报 Material Design + MVP + RxJava + Retrofit for android<br>
另外，本人目前正在找工作，欢迎各大公司技术负责人骚扰。
