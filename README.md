#### Why?

Picture is worth a thousand words they say. They also wonder why their gradle multimodule build takes so long.

#### How?

Follow instructions from [gradle plugin portal entry](https://plugins.gradle.org/plugin/rpost.grantt) to add plugin, ie either:
```groovy
plugins {
  id "rpost.grantt" version "0.2"
}
```
or:
```groovy
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.rpost:grantt:0.2"
  }
}

apply plugin: "rpost.grantt"
```

and run your build.

#### What?

Open `build/gantt.html`. Expect something similiar to [this](sample/gantt.html)




