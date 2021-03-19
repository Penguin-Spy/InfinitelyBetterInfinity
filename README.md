# Infinitely Better Infinity
Makes the Minecraft infinity snapshot (`20w14infinity`) a bit better:
* `/warp` requires an OP level of 2 or higher, the same as `/tp`.
* When a portal is created by an entity traveling through it, it will link back to the dimension the entity came from. This means that random portals are 2-way, unless manually broken/overwritten or if they link to a pre-existing portal.
* If the `allow-nether` server.properties value is set to `false`, random portals will still work & all dimensions will tick (including the nether). By default, all random dimensions are treated like the nether & traveling/ticking them is disabled. With this enabled, only travelling to/from the nether dimension is disabled.

This is intended for server-side only use, and vanilla clients can connect to a server running this mod.  
If installed on the client, it only affects singleplayer (applying the above mentioned mechanics).

Uses the [Fabric launcher](https://fabricmc.net/use/), does not use the Fabric API.
