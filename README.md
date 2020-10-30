![123](https://github.com/millerM907/forecast_Inflows/blob/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher2.png)

# About Tides in Magadan

A seaside holiday with the family, fishing with friends, a walk? You don't have to worry about being late anymore. The application "Tides in Magadan" allows you to find out: 
- the current state of the tide and weather conditions in Nagaev Bay (Magadan); 
- chart of times and heights of full and small waters for today and the next 12 days.

The app consists of three pages:
1) Now:

the first page of the app shows information about the state of the tide,
which is supplemented by an image of a wave in the background, the nearest time of full / low water,
the time remaining to low/full water, air temperature,
humidity, wind speed and direction.

2) Today:

on the second page of the app, you can find out the time of sunrise/sunset,
as well as the times and heights of full and small waters for today.

3) Pick of the day:

the third page of the app allows you to view the tidal chart for one of the next 12 days.


# Solve the problem
### 1) Optimization of graphics
Description:

When scrolling through fragments, the GUI slowed down due to the large number of layers. Layers:
  1) background layer;
  2) wave image that visualizes the water level near the shore (10 images);
  3) the icons;
  4) text.
  
Solution:

We combined the background layer with all the wave images.

### 2) Slow operation of parsers
Description:

During each launch, the app parsed the weather site, which increased the launch time, especially when the Internet speed was low.

Solution:

Developed a web application [TidesServer](https://github.com/millerM907/TidesServer) using the Spring Framework that provides a REST API.
The parsers have been moved to the backend.

### 3) Support for new libraries with Android versions lower than 8.0
Description:

Android versions below 8.0 do not natively support the java.time library.

Solution:

The "desugar_jdk_libs" library is used to provide the inverse dependency.



# Technologies in the project
Java, XML, SQLite


# Authors
Programming and architecture: [Ageenko Mikhail](https://github.com/millerM907)

e-mail: ageenkomihael@mail.ru

UI/UX Design: [Timofeeva Anna](https://github.com/timofann)

e-mail: anytat@inbox.ru

