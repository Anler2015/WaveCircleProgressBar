# Android WaveCircleProgressBar
The WaveCircleProgressBar is a circle progress bar.</br>
The reason I wanted to do it is that I saw the NumberProgressBar made by daimajia. And I also wanted to make some different progress bar.</br>
So, I learn how to make custom views by books and blogs.</br> 

---
### Demo

![](https://github.com/Anler2015/WaveCircleProgressBar/blob/master/outputs/3.gif)

[Download Demo](https://github.com/Anler2015/WaveCircleProgressBar/blob/master/outputs/sample-debug.apk) 

---
### usage

Use it in your own code:
```java
    <com.gejiahui.wavecircleprogressbar.WaveCircleProgressBar
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        />
```	
I also make some predesign style like damajia.

![](https://github.com/Anler2015/WaveCircleProgressBar/blob/master/outputs/2.gif)

Use the preset style just like below:
```java
        <com.gejiahui.wavecircleprogressbar.WaveCircleProgressBar
            style="@style/WaveCircleProgressBar_Grace_Yellow"
            />
```

In the above picture, the style is : 

`WaveCircleProgressBar_Default`
`WaveCircleProgressBar_Crazy_Black`
`WaveCircleProgressBar_Passing_Green`
`WaveCircleProgressBar_Beauty_Red`
`WaveCircleProgressBar_Warning_Red`
`WaveCircleProgressBar_Relax_Blue`
`WaveCircleProgressBar_Grace_Yellow`
`WaveCircleProgressBar_Funny_Orange`

for example, the default style:
```java
    <style name="WaveCircleProgressBar_Default">
        <item name="android:layout_height">wrap_content</item>
        <item name="android:layout_width">wrap_content</item>

        <item name="unreached_text_color">#CCCCCC</item>
        <item name="outline_circle_unreached_color">#CCCCCC</item>

        <item name="circle_color">#EEEEEE</item>

        <item name="wave_color" >#7ECDF1</item>

        <item name="reached_text_color">#0080FF</item>
        <item name="outline_circle_reached_color">#0080FF</item>

        <item name="wave_speed">15</item>
        <item name="wave_height">10</item>
        <item name="text_size">15dp</item>
        <item name="circle_radius">40dp</item>
    </style>
```

















