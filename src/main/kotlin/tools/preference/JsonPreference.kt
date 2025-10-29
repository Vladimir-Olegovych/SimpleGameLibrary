package tools.preference

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

class JsonPreference<P>(
    private val preferenceName: String,
    private val defaultPreference: P
) {
    private val preferenceFile = File("$preferenceName.json")
    private val objectMapper = ObjectMapper()

    fun getPreference(): P {
        if (!preferenceFile.exists()) {
            setPreference(defaultPreference)
            return defaultPreference
        }
        return objectMapper.readValue(preferenceFile, defaultPreference!!::class.java)
    }

    fun setPreference(preference: P){
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(preferenceFile, preference)
    }
}
