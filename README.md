# Better HUD Fabric Rewrite

I am experimenting with [Fabric](https://fabricmc.net/) and think it would be a
good fit for [Better HUD](https://github.com/mccreery/better-hud/), the original
being written in Forge. This also gives me a chance to improve some of the
shoddy design decisions I made years ago, although I will probably keep a
substantial portion of the code.

Long-term, I'd like to provide a public API so that other mods can add Better
HUD support for their own bars, indicators, meters etc. This would provide steps
to fixing incompatibility bug reports due to other mods not being aware of
Better HUD and placing their bars in the "expected" (unmanaged) positions on the
HUD.

I haven't ironed it out yet, nor do I have Fabric experience, but
[MealAPI's method](https://github.com/FoundationGames/MealAPI) seems promising.
It seems to be based on "don't call us, we'll call you" which gets around
runtime checks for which mods are installed and so on. Better HUD will be an
*optional* dependency in most cases.
