# Pikolo
[![Build Status](https://travis-ci.org/Madrapps/Pikolo.svg?branch=master)](https://travis-ci.org/Madrapps/Pikolo)

An android color picker library

<img src="/preview/preview-full.gif" alt="preview" title="preview" width="303" height="300" align="right" vspace="20" />

Download
-----

```gradle
repositories {
  jcenter() // or mavenCentral()
}

dependencies {
  compile 'com.github.madrapps:pikolo:1.0.0'
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
