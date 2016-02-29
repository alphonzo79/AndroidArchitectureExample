Android Architecture Example
===
This project is meant to support a Code Camp talk on one effective method for implementing MVP on Android. There are plenty of good ways to build an Android Application, and there are plenty of good ways to implement MVP. This is just one way to build an Android app that provides a lot of benefits, including testability.

Branches
---

The project is broken up into a couple of branches for the purpose of demonstration. 

####Master

The master branch is the starting point, and doesn't actually have much of use other than a bare project and this README. Maybe at some point one of the other branches will be merged into master, but for now it will be kept separate.

####Standard

The standard branch holds the starting point for the discussion, what I consider to be the "standard" way of building an Android app. This version of the app is build according to most tutorials and documented examples that are available. This includes the Android version of and MVC architecture with very prominent Activities and Fragments that handle much of the view and business logic for the app. It also includes using Loaders for data loading and maybe even an AsyncTask or two. Some great tools can be included in this type of archictecture, including Dagger and RxAndroid to name just a couple.

This style of application works well. There are countless wonderful apps built in this way, including most of the apps that I have built or worked on. But it presents a few challenges. Primarily, Android is notoriously difficult to test because so much is wrapped up in components that require instrumentation. If you're like me you use this as a really great excuse: 
*"I would love to unit test my app, but you just can't unit test Android."*
Developer absolved!

####MVPI

The mvpi branch holds a second version of the same app, but one where the app is reorganized to leverage Model View Presenter. In this implementation we also use Interactors and Gateways to help divide responsibilities. The primary goal of this architecture is to build fairly solid divisions between view display, view logic, business logic and data operations.

This style of application does have its own challenges and targets of criticism: Often your packages will balloon,  sometimes with interfaes that are implemented preciesly once in the app, sometimes with classes that appear to be mostly pass-through and superfluous. 

But the result is that much of the infrastructure can now be either agnostic of Android or, at least, more agnostic of Android. You now have an architecture where certain pieces can now be easily wrapped into libraries and shared across Java applications, Android or not. You now have an architecture where you can easily swap out small parts of the pipeline to provide slightly different functionality while still maintaining the interface contract of inputs and outputs. And best of all, you now have an architecture where vast pieces of the application are unit testable with only small, distinct parts that must be left to instrumented, integration or manual testing.

Dependencies
---

For version numbers and build variant usage see app/build.gradle

| Name                 | Provider    | Summary                                                                     |
|:--------------------:|:-----------:|:---------------------------------------------------------------------------:|
|                      |             |                                                                             |
|                      |             |                                                                             |

To Do ;)

AndroidStudio and Gradle
---

This project was set up on Android Studio 2.0 and using Gradle 2.8, but should work with most current versions of either

Presentation Slides
---

To Do ;)