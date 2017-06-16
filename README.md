# Pikolo
[![Build Status](https://travis-ci.org/Madrapps/Pikolo.svg?branch=master)](https://travis-ci.org/Madrapps/Pikolo)
[ ![Download](https://api.bintray.com/packages/madrapps/maven/com.github.madrapps%3Apikolo/images/download.svg) ](https://bintray.com/madrapps/maven/com.github.madrapps%3Apikolo/_latestVersion)

An android color picker library

<img src="/preview/arc-selectors.gif" alt="preview" title="preview" width="200" height="200" align="right" vspace="40" />
<img src="/preview/preview-full.gif" alt="preview" title="preview" width="200" height="200" align="right" vspace="40" />

Download
-----

```gradle
repositories {
  jcenter() // or mavenCentral()
}

dependencies {
  compile 'com.github.madrapps:pikolo:1.1.5'
}
```

Usage
-----
Add the `HSLColorPicker` view to your layout and use it in code as below:

```java
final HSLColorPicker colorPicker = (HSLColorPicker) findViewById(R.id.colorPicker);
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
