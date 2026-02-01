# Farmer's Miracle

## Overview

- Add 3 types of small temple structures.
- Visit each temple and collect orbs, which are custom items.
- All types of temples will be placed at least once within a 2000 block range from the initial spawn point.
- Maintain a certain distance between temples to prevent them from being too close, especially between different types of temples (300 blocks or more).
- Achieve advancements when obtaining each orb and when collecting all of them.
- When each advancement is achieved, a permanent buff is granted that increases the growth speed of specific crops around that user or expands the range.

## Structure (Temple) Types

- EN: Temple of Golden Wheat / JP: 黄金麦の神殿
	- ID: farmersmiracle:temple_of_golden_wheat
	- Items in chest:
		- Wheat Orb
		- ID: farmersmiracle:wheat_orb
- EN: Temple of Pumpkins / JP: カボチャの神殿
	- ID: farmersmiracle:temple_of_pumpkin
	- Items in chest:
		- Pumpkin Orb
		- ID: farmersmiracle:pumpkin_orb
- EN: Temple of Melons / JP: スイカの神殿
	- ID: farmersmiracle:temple_of_melons
	- Items in chest:
		- Melon Orb
		- ID: farmersmiracle:melon_orb

## Types of Progress

- EN: The First Harvest / JP: 実りの始まり
	- ID: farmersmiracle:the_first_harvest
	- Achievement condition: Obtain wheat orb
- EN: Fruit of the Earth / JP: 大地の膨らみ
	- ID: farmersmiracle:fruit_of_the_earth
	- Achievement condition: Obtain Pumpkin Orb
- EN: A Juicy Reward / JP: 瑞々しき報酬
	- ID: farmersmiracle:a_juicy_reward
	- Achievement condition: Obtain watermelon orb
- EN: Pilgrimage of the Three Grain Temples / JP: 穀物三神殿の巡礼
	- ID: farmersmiracle:pilgrimage_of_grains
	- When the above three progressions are achieved (when three orbs are obtained)

## Buff Types

1. Increased growth rate for grain crops
	- Name:
		- EN: Blessing of Grains: Growth I - IV
		- JP: 穀物の加護：成長 I - IV
	- ID:
		- farmersmiracle:grain_growth_speed
	- Duration: Infinite (permanent buff per player)
	- Effect:
		- When crops undergo growth determination by randomTick, this buff provides a certain probability of additional growth
			- Additional growth opportunity = Number of progress achievements * Base growth rate
		- Example: When base growth rate = 3%
		    - "Blessing of Grains: Growth I": +3%
		    - "Blessing of Grains: Growth II": +6%
		    - "Blessing of Grains: Growth III": +9%
		    - "Blessing of Grains: Growth IV": +12%
	- Applicable range:
		- Player-centered radius of 16 blocks
	- Target crops:
		- Wheat, pumpkin, watermelon, potato, carrot, beetroot
			- Manage these with tags (farmersmiracle:grains) and process them against the tags
2. Expansion of grain crops growth speed increase range
	- Name:
		- EN: Blessing of Grains: Spread I
		- JP: 穀物の加護：波及 I
	- ID:
		- farmersmiracle:grain_growth_range
	- Duration: Infinite (permanent buff per player)
	- Effect:
		- Extends the application range of the "Increased growth rate for grain crops" buff to a radius of 24 blocks

## Sample User Introduction Text

> Collect three sacred orbs from small shrines to slightly boost crop growth around you.

## Concept Supplement

- Looking ahead to future series expansion, the crops that can receive buffs will be limited rather than including all crops.
- This limitation should be documented in the description to address player questions.
	- > The Blessing of Grains accelerates the growth of staple farmland crops (Wheat, Pumpkin, Melon, Potato, Carrot, and Beetroot).

## Architecture

- Minecraft mod using Architectury
- Compatible with both Fabric and NeoForge
- Initially implemented for Minecraft version 1.21.1
- The project will be configured with Gradle as a multi-project setup

## Directory

- common-shared
    - Common code without loader dependencies or version dependencies. Not a Gradle subproject, but incorporated as one of the srcDirs from each version-specific subproject
- common-1.21.1
    - Common code for Minecraft 1.21.1 without loader dependencies. Gradle subproject.
- fabric-base
    - Code for Fabric without Minecraft version dependencies. Gradle subproject.
- fabric-1.21.1
    - Code for Fabric and Minecraft 1.21.1. Gradle subproject. Depends on fabric-base.
- neoforge-base
    - Code for NeoForge without Minecraft version dependencies. Gradle subproject.
- neoforge-1.21.1
    - Code for NeoForge and Minecraft 1.21.1. Gradle subproject. Depends on neoforge-base.

## Misc

- License is LGPL-3.0-only
