# three
Game of Three
##Rules

When a player gets started, it incepts a random (whole) number and sends it to

the second player as an apporach of starting the game.

The receiving player now can always choose between adding one of {­1, 0, 1} to get to a 

numer that's dividable by 3. Divide it by three. The resulting number then is send back to the 

original sender.

The same rules are applied, until one player reaches the number 1 (after the division).

# Requirements

Java 8

# Build

`gradlew clean build`

#Run 
##Server mode:
`java -jar build/lib/three.jar server`

##Client mode:
 `java -jar build/lib/three.jar`

