# TestApplication

1.1. Task
Build a native app with Android using Kotlin. Use the cities that are in the
provided JSON. Test the game and give a list of devices you have tested it on.
Explain how you would store the high score. Explain what improvements you
would suggest. You shouldn’t work longer then 8h on this project.

1.2. Game description
The player will see a map of Europe without Streets + Cities 
(Only Country X borders). Its mission is to
find the right location to the city name on this map.
After placing the needle pin, the game will show
you the right location of the city and the difference
of your needle pin and the city in kilometres. If it is
in around 50km of the city, the selection will be
defined as "correct".

1.3. Games logic
In the beginning, the player has a predetermined
score of 1500 that symbolizes kilometres. At each
round the difference between position of the city
and your needle pin are reducing your score.

1.4. Game end
The game ends when no kilometres are left. The
high score is the amount of cities you have found.

## Tested on

- Google Pixel 2 XL API27 (hardware)
- Google Nexus 5    API25 (hardware)
- Google Pixel 2 XL API26 (emulated)
- Google Pixel C    API26 (emulated)
- Google Nexus 5    API21 (emulated)

## About

- Used Kotlin language 
- Used MVP architecture pattern
- Used GoogleMaps API for map visualization
- Used Moxy library to support MVP architecture
- Used ButterKnife library for view binding
- Used Timber as logger during debugging
- HighScore data stored through SharedPreferences mechanism

## TODO

- add more cities
- add online high-score board
- add information and photos about correctly placed cities
- add hotels / air tickets booking for correctly placed cities