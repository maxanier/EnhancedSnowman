# Enhanced Snowman
Finally, your snowman army is not completely useless anymore.  
With this mod they deal a small amount of damage with their snowballs.  
Thereby they can finally protect themselves from these evil monsters.  

If you want to go crazy, you can even allow snowmen to convert killed mobs into snowmen to join your army (config option).

### Details
By default snowballs thrown by snowmen deal a small amount of damage to any hostile creature.  
It takes about 40 snowballs to kill a zombie, but if you have enough snowmen this should be quite quickly.
Especially since the snowballs also freeze (slow down) the hit mob for two seconds.
Snowballs thrown by players do not deal damage. 

Most of this is configurable.

### Server
If you want to use this on a server, it is sufficient to install it on the server itself. Clients do not need this.

### Configuration
Can either be done in the ingame mod list GUI (MC1.11 or higher) or by editing the config/enhanced_snowman.cfg

```
    # Convert creatures killed by snowmen to a snowman
    B:convert=false

    # Only deal damage to hostile creatures
    B:onlyHostile=true

    # Allow players to deal damage with snowballs
    B:playersDealDamage=false

    # Add a short freeze/slowness to a creature damaged by a snowball
    B:slowness=true

    # Damage dealt by one snowman snowball
    # Min: 0.0
    # Max: 100.0
    D:snowballDamage=0.5
```
