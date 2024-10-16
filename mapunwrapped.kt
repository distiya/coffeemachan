object MapUnwrappedSerializer : KSerializer<Map<String, String>> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("MapUnwrapped") {
        // We don't need individual elements here, as we are dynamically unwrapping.
    }

    override fun serialize(encoder: Encoder, value: Map<String, String>) {
        val compositeEncoder = encoder.beginStructure(descriptor)
        
        // For each entry in the map, encode the key as the field name and the value as the field's value.
        value.forEach { (key, value) ->
            compositeEncoder.encodeStringElement(descriptor, 0, key)
            compositeEncoder.encodeStringElement(descriptor, 1, value)
        }
        compositeEncoder.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): Map<String, String> {
        val map = mutableMapOf<String, String>()
        val compositeDecoder = decoder.beginStructure(descriptor)

        while (true) {
            when (val index = compositeDecoder.decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break
                else -> {
                    // Decode each key-value pair
                    val key = compositeDecoder.decodeStringElement(descriptor, 0)
                    val value = compositeDecoder.decodeStringElement(descriptor, 1)
                    map[key] = value
                }
            }
        }
        compositeDecoder.endStructure(descriptor)
        return map
    }
}
