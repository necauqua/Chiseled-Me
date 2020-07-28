# Chiseled-Me

This is (obviously) a coremod.
It adds some items, recipes to which you can see with JEI/NEI.
These items are pretty self-explanatory, but to get the recalibrator that makes you normal size again,
you need to deplete any other recalibrator(so the cheaper one you make - the more you click).

Known issues:
 * Liquids are buggy
 * Ladders are buggy
 * Throwables are buggy
 * No riding (but will be in the future)

About coremods:
* They can corrupt worlds just like any other mod can (except that maybe they have just a bit more tools for that).
My mod does no specific interactions with your saves (just regular NBT to store your size as a number - all mods do that kind of stuff).
* They DO NOT EVER corrupt you jar-files, the whole point of them is not to modify
the jar but still modify the program at the point of loading classes from jar-files into RAM.
* They MAY not work just for you for unknown reasons, but most likely they will.

If you have any issues, feel free to report them at issues page, ALWAYS including your *`minecraft_profile_folder/logs/debug.log`* file.

## Installation

If you want to modify/compile, you can just run the following commands (assuming you are on Linux and have git installed):

```
git clone https://github.com/necauqua/chiseled-me.git
cd chiseled-me
```

If you want to change anything, do this:
```
./gradlew setupDecompWorkspace
```
Then apply the changes.


And if you want and compile it - you can with:
```
./gradlew build
```
The jars will be in build/libs folder.

## Contribution
I'll accept pull requests if they fix small and obvious bugs, add localization or something like that.

If you want to suggest a big idea - just create an issue about it or contact me in any other way.
