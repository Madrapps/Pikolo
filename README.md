# Pikolo

An android color picker library

<img src="/preview/arc-selectors.gif" alt="preview" title="preview" width="200" height="200"/><img src="/preview/preview-full.gif" alt="preview" title="preview" width="200" height="200"/><img src="/preview/rgb-picker.gif" alt="preview" title="preview" width="200" height="200"/>

Download
-----

```gradle
repositories {
  mavenCentral()
}

dependencies {
  implementation 'com.github.madrapps:pikolo:2.0.2'
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

You can take a look at the [sample](https://github.com/Madrapps/Pikolo/tree/master/sample) app to see how
the color picker can be customised. There are 3 components in both pickers. You can change their properties
together or individually. For instance, `arc_length` changes the length of the arc for all 3 components, while
`hue_arc_length` affects only the Hue component. Various other XML attributes are as follows:<br>

`arc_width` - width (thickness) of the components<br>
`arc_length` - length of the components<br>
`stroke_width` - width of the stroke of the components<br>
`stroke_color` - stroke color of the components<br>
`indicator_radius` - radius of the control indicator used to change color<br>
`indicator_stroke_width` - stroke width of indicator<br>
`indicator_stroke_color` - stroke color of indicator<br>
`radius_offset` - the offset of the components from the center of the picker<br>


License
-----

Pikolo by [Madrapps](http://madrapps.github.io/) is licensed under a [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
