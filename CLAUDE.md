# Project Notes

## MC 1.21.1 Worldgen

- Jigsaw structure JSON requires `spawn_overrides` field (even if empty `{}`). Omitting it causes a crash at startup.

## Architectury API

The Fabric implementation of `ParticleProviderRegistry` (Architectury 13.0.8) has `register(ParticleType, DeferredParticleProvider)` as a no-op. Use each platform's API directly for particle provider registration:
  - Fabric: `net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry`
  - NeoForge: `RegisterParticleProvidersEvent`
