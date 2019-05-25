# Pikolo
[![Build Status](https://travis-ci.org/Madrapps/Pikolo.svg?branch=master)](https://travis-ci.org/Madrapps/Pikolo)
[ ![Download](https://api.bintray.com/packages/madrapps/maven/com.github.madrapps%3Apikolo/images/download.svg) ](https://bintray.com/madrapps/maven/com.github.madrapps%3Apikolo/_latestVersion)

An android color picker library

<img src="/preview/arc-selectors.gif" alt="preview" title="preview" width="200" height="200"/><img src="/preview/preview-full.gif" alt="preview" title="preview" width="200" height="200"/><img src="/preview/rgb-picker.gif" alt="preview" title="preview" width="200" height="200"/>

<br/>
<br/>

Download
-----

```gradle
repositories {
  jcenter() // or mavenCentral()
}

dependencies {
  implementation 'com.github.madrapps:pikolo:2.0.0'
}
```

Features
-----
- Includes `HSLColorPicker` and `RGBColorPicker`
- Full customization of the various parts of the color picker (like arc length, arc position, indicator size, color, etc...) using XML attributes

Usage
-----
Add the `HSLColorPicker` or `RGBColorPicker` view to your layout and use it in code as below:

```java
final ColorPicker colorPicker = findViewById(R.id.colorPicker);
colorPicker.setColorSelectionListener(new SimpleColorSelectionListener() {
  @Override
  public void onColorSelected(int color) {
    // Do whatever you want with the color
    imageView.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
  }
});
```

License
-----

Pikolo by [Madrapps](http://madrapps.github.io/) is licensed under a [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
