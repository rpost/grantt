[![Valid Gradle Wrapper](https://github.com/rpost/grantt/workflows/Validate%20Gradle%20Wrapper/badge.svg)](https://github.com/rpost/grantt/actions/workflows/gradle-wrapper-validation.yml)
[![Gradle Status](https://gradleupdate.appspot.com/rpost/grantt/status.svg)](https://gradleupdate.appspot.com/rpost/grantt/status)

## Why?

Picture is worth a thousand words they say. They also wonder why their gradle multimodule build takes so long.

## How?

Follow instructions from [gradle plugin portal entry](https://plugins.gradle.org/plugin/rpost.grantt) to add plugin, ie either:
```groovy
plugins {
  id "rpost.grantt" version "0.3"
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
    classpath "gradle.plugin.rpost:grantt:0.3"
  }
}

apply plugin: "rpost.grantt"
```

and run your build.

## What?

Open `build/gantt.html`. Expect something similiar to [this](http://htmlpreview.github.io/?https://github.com/rpost/grantt/blob/master/sample/gantt.html)




