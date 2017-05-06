# NightShift
This is a game built for Software Development class at Macalester College.
The game play is simple: you are navigating through maps, there are ghost roaming around. Ghosts will chase you if you get close enough. Run from the ghost using arrow keys or WASD, get to the destination!
## User Documentation
### System Requirements:
+ [Java Davelopment Kit 7+](http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html)
+ [Intellij IDEA](https://www.jetbrains.com/idea/) or [Ecllipse]
+ [libgdx](https://github.com/libgdx/libgdx/wiki/Project-Setup-Gradle)

To get this game running on your computer, first clone the project. Then, open a Java SDE and run ```DesktopLauncher``` in the project.

## Developer Documentation
There are a lot of things you can do to improve our project, such as
+ Add more levels,
+ Add checkpoints

The structure of the program is fairly self-explanitory. It is divided up into three sections: Data, Screens, and Sprites, as well as the base class, called NightShift. Screens holds all the code for the behaviour of the levels, as well as the start, end and success screens. The Sprites hold the behaviour for the player character, enemies, and life bar. The Data holds inportant constants, as well as maps and spawn points. To add onto and modify the levels, this would be the place to work.
In order to add actual map files, create them in Tiled Map Editor, then add them into the MapData. Info on Tiled can be found here:
http://www.gamefromscratch.com/post/2014/04/15/A-quick-look-at-Tiled-An-open-source-2D-level-editor.aspx

## Credits
Skins credit to [dermetfan](https://bitbucket.org/dermetfan/libgdx-utils/wiki/Home)
