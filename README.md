# Boss Fight

BossFight is a Spigot plugin developed to help me run custom boss fights for my siblings. 

### Features:

- Automatic respawning at a given point (set that point using `/boss spawn`)

- Scoreboard for the Player playing the boss with the health values of all the fighters

- Fancy title graphics for match updates

- Implements BossBar from Bukkit to display to everyone how close the boss is to defeat

### Settings a match up

1. Set the respawn point using `/boss spawn`. This doesn't include the pitch or yaw.

2. Get all the players who wish to play into the world. Only the players in the world the boss is will be counted as playing

3. Have the player who will be the boss to type `/boss start [health]`, with health being how much health the boss will start with. The boss doesn't take damage, but rather a certain number of hits to kill the boss. If the health is 10, it'll take 10 fist punches or 10 diamond sword hits to kill the boss. 

4. The game will end once the boss' health reaches 0, and displays a fancy graphic :-)

### TODO

- Remove all the hardcoded Strings and replace them with a `Messages.yml` system.

- Implement a way to easily set up chests of certain items

     - Clone columns
     
     - Predifiend chests
     
- Figure out a way to make the fighters glow for the boss, and the boss glow for the fighters

- Impelement a way to store custom items

- Display proper death messages when doing automatic respawning

### Depenencies:

- sk89q's command framework (WorldEdit, CommandBook)

- TitleAPI made by ConnorLinfoot (This is shaded in)
