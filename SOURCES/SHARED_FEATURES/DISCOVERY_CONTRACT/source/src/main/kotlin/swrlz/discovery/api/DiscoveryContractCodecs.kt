package swrlz.discovery.api

import swrlz.discovery.internal.DiscoveryContractCodecImpl
import swrlz.discovery.internal.KotlinxSerializationJsonBackend

object DiscoveryContractCodecs {
    fun canonical(): DiscoveryContractCodec = DiscoveryContractCodecImpl(KotlinxSerializationJsonBackend())
}
