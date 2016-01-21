Welcome! Dynamic Earth is a mod that attempts to make one of Minecraft's most common blocks more interesting and, more importantly, more useful.

## Features
#### Mud
Dirt (but not grass or mycelium) slowly turns into mud when exposed to water (either through a nearby block or through rain). Mud is a stable building block until it comes into contact with running water or stays in contact with any type of water for too long (including rain) and becomes "saturated". This boggy, water-filled mud is hard to walk through and may cause...

#### Mudslides!
Saturated mud will fall like sand/gravel and will cause any connected mud blocks - even stable ones! - to fall as well! A chain reaction!

#### Reed Planting
Mud is ideal for planting reeds - it's moist, soft soil can sustain a reed plant without the need of an adjacent water block! ...Except for the one needed to keep mud from turning back into dirt, that is.

#### Mudbricks and Adobe
Mud will drop four "mud blobs" when it's harvested, which can be cooked in a furnace to make mud bricks, which can then be crafted into mud brick blocks. Mud brick is softer than regular brick, but is also much easier to come by(and easier on the eyes, in my opinion).

Moist Adobe is made by crafting together mud blobs, clay, and wheat or reeds. Moist Adobe is a soft building material that is easy to pick back up, but will dry and harden into a tough, stone-like material after being placed (it can also be cooked in a furnace for the same effect). However, once it has hardened, adobe will only produce a small amount of "adobe dust" when broken (unless Silk Touch is used).

This dust can be rehydrated into Moist Adobe through a crafting recipe or into Moist Adobe Blobs by simply right-clicking on a source block of water with the dust in hand.

#### Earthenware
Adobe blobs can be used to craft vases, which are similar to buckets (see below), and earthenware bowls. However, these must be cooked in a furnace before they can be used. Earthenware bowls can then be used to hold soup or can be combined with gunpowder and string to make small hand-held bombs (more on that below).

#### Vases
Vases are both a cheap alternative to buckets and something entirely unique. You can use them just like buckets - pick up water, milk cows, try to pick up lava, etc. - but you can also use them to hold any liquid added by other mods, as well as mushroom stew from soup bowls. Additionally, you can transfer liquids to and from the vase by using the crafting grid (for example, placing a soup bowl with the vase will fill it with 250 mB, or 1/4 a bucket, of mushroom stew, allowing the vase to hold 4 bowls of soup at a time).

#### Hand-bombs
Placing a piece of string, two earthenware bowls, and a piece of gunpowder in the crafting grid will produce an earthenware hand-bomb. When right-clicked, it will light the fuse; right-clicking again will throw it. It will blow up when either the fuse runs out or the bomb makes contact with something.

This, however, is only the most basic bomb. Bombs can also be made with up to 4 gunpowder (by default) for more explosiveness, more string for a longer fuse, a fire charge to add fire to the explosion, a fireworks charge to add fireworks to the explosion, and/or a potion to add a splash potion effect.

#### Adobe Golem
Adobe can also be used to make a cost-effective golem to protect your home or village. But it's no iron golem - it only has the attack power of a stone sword and won't take too many hits.

#### Permafrost
Not really interested in mud? Well, there's also permafrost! When dirt sits in the cold for too long, it becomes solid as a rock and gain some ice-like properties. Make sure you bring your pickaxe or diamond shovel if you want to dig through it!

#### Dirt Slabs
Dirt, Grass, and Mycelium can be crafted into fully functional dirt, grass, or mycelium slabs! These slabs act just like their full-block counterparts: they grow, spread, can be eaten by sheep, and have all the normal visual effects.

#### Peat
Peat moss, found naturally in swamplands, will (very) slowly transform dirt into a natural fuel - peat. While peat forms very slowly, it can sometimes be found in large bogs. However, peat must be dried before it can be used as fuel, either in a furnace or by drying naturally in the sun. The moss itself can be replanted by placing it in tilled soil or can be used to craft mossy cobblestone or mossy stone brick.

#### Rich Soil
Peat clumps can also be used with bonemeal and dirt to make a rich, fertile soil that is ideal for growing plants. When tilled and used as farmland, it will grow crops 2-3x as fast as regular dirt. If grass is allowed to grow over it, it will randomly produce tall grass, flowers, and/or mushrooms, depending on the biome. Likewise, if mycelium is allowed to grow over it, it will grow mushrooms and, eventually, giant mushrooms. However, rapidly growing large mushrooms is taxing on the soil and will revert it to regular dirt.

#### Sandy Soil
Is mud wrecking your builds? Try making sandy soil out of sand and dirt - it looks nearly identical to regular dirt, but due to its high sand content, it's unable to retain water and will never become mud.

## Installation
1) [Download and install Forge](http://www.minecraftforge.net/wiki/Installation/Universal)
2) Run Minecraft with Forge and make sure it works.
3) Place the DynamicEarth and KaruberuCore .jar files in the "mods" folder that was created by Forge in your Minecraft directory.

**WARNING:** Because this mod changes dirt blocks, it is highly destructive when uninstalled. Be careful which worlds you play on!
(If you accidentally load a world with this mod, there is hope: open the config file and set "restoreDirtOnChunkLoad" to "true", then visit any chunks you loaded while the mod was active. This will instantly change any mud or permafrost blocks in those chunks into dirt, which should aid in safe uninstallation.)

## Development
To get started with development, you'll need to install Git, Java, Minecraft, Forge, and Eclipse. You will also need to download a copy of the mod APIs used by Dynamic Earth's plugins.

#### Set up Forge
Currently, this mod uses the pre-gradle version of Forge. You can find a tutorial on how to set up Forge for development [here](http://www.minecraftforge.net/w/index.php?title=Installation/Source&oldid=2082).

You will want to create a new project in Eclipse specifically for Forge. This keeps the code separate so that you will not accidentally edit it and cause problems when compiling the code.

#### Set up Karuberu Core
Follow the steps outlined for setting up Karuberu Core [here](http://github.com/Karuberu/KaruberuCore).

#### Set up the APIs
You can find a bundle of the APIs used by Dynamic Earth [here](https://www.mediafire.com/?fz3c9n3q89w1q77) or download them individually from each mod's site.

Create a new Java project for the APIs, then import the code from the .zip file into the src folder of the new project.

Add the Forge project to the build path to get rid of any errors.

#### Set up Dynamic Earth
Open up a command line and navigate to where you want Dynamic Earth's source code to reside (e.g. C:/GitHub), then pull down the code with Git.

    cd GitHub
    git clone https://github.com/Karuberu/DynamicEarth.git

You should now have a folder named DynamicEarth inside of your GitHub folder (or wherever you decided to download the code to).

Create a new Java project for Dynamic Earth in Eclipse and remove any src folder that was automatically generated. Link the src folder in your GitHub directory (e.g. C:/GitHub/DynamicEarth/src) as your project's new source folder.

Add the Forge, Karuberu Core, and API projects to the build path. This should get rid of any errors that were present in the code.

You're good to code!

#### Compiling
Copy the source code for Dynamic Earth, Karuberu Core, and the APIs into Forge's mcp/src/minecraft/ folder.

Navigate back to Forge's mcp folder and run either reobfuscate.bat or reobfuscate.sh.

Navigate to mcp/reobf. You should see what looks like your source code, along with the rest of the files you copied. Delete any source code that isn't a part of Dynamic Earth. Copy the assets folder and any other non-.java files from your source code into the reobf folder.

Zip up the files in the mcp/reobf folder as DynamicEarth.zip or DynamicEarth.jar. This is your compiled mod.

Move your mod to a safe place, then delete any files you copied as well as the reobf folder.

## Screenshots
![Mud forming](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/2012-11-04_220445.png)
![Reed planting](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/2012-11-04_213515.png)
![Wet blocks](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/2012-11-04_221937.png)
![Mud brick and adobe](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/2012-11-04_221417.png)
![Mud brick and adobe slabs](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/2012-11-04_213507.png)
![Adobe Golem](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/2012-11-27_215425.png)
![Peat Moss generation](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/2013-03-10_174344_zpsc5da8e9f.png)

## Crafting Recipes
#### Mud
![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-mud_zpsbb66de88.png)![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-mud2_zpseb39003f.png)![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-mud3_zpsdeedba35.png)
![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-mudblob_zpsec5745f4.png)![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-mud-b_zpsd69c8fbc.png)

#### Mud Brick
![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-mudbrick_zps2cdc6000.png)

#### Moist Adobe
![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-moistadobe1_zps4091117a.png)![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-moistadobe2_zpsebfdd3e8.png)
![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-moistadobeblob_zpsa058937f.png)![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-moistadobe-b_zps970f0dc3.png)
![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-moistadobe-wd1_zps01ba4c77.png)![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-moistadobe-wd2_zpsb1299839.png)

#### Vase
![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-vase_zpsa3428ac9.png)

_This is the unfired vase; it must be smelted before it can be used._

#### Earthenware Bowl
![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-bowl1_zpse7db78bf.png)![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-bowl2_zps213725cc.png)

_This is the unfired bowl; it must be smelted before it can be used._

#### Earthenware Hand-bomb
![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-bomb_zps2c3e5596.png)

_This is the basic recipe. More string and gunpowder can be added, as well as fire charges, fireworks charges, and potion bottles._

#### Mossy Stone
![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-mossstone_zps143cce63.png)![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-mossbrick_zpsb161a007.png)

#### Peat
![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-peat_zps8be48365.png)![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-peatclump_zps2f4d443c.png)
![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-peatbrick_zps74715117.png)

#### Soil
![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-dirtclump_zpsa40fd834.png)![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-dirt_zps6ac0918e.png)
![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-richsoil_zps05d6d08f.png)![](http://i1183.photobucket.com/albums/x468/Karuberu/mudmod/crafting-sandysoil_zps5bd8bc92.png)

#### Other Recipes
The following recipes are simply crafted the same way as their vanilla counterparts:
* Mud Brick Stairs (using Mud Brick blocks)
* Mud Brick Slabs
* Mud Brick Walls
* Adobe Stairs (using Adobe, not Moist Adobe)
* Adobe Slabs
* Dirt Slabs
* Grass Slabs (makes 3, leaves 3 dirt slabs in your inventory)
* Mycelium Slabs (same as grass slabs)
* Cake (using Milk Vases)
* Mushroom Soup (using an Earthenware Bowl)