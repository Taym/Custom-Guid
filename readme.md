The code in this repository was created in 2021.

# Description
GuidWrapper represents a Java data type that can be used as an alternative to UUID. It allows users to handle a UUID either as a standard UUID or as a 22-character Base64 string, making it easy to convert between UUID and its Base64 representation.

This library is useful when short UUIDs are needed instead of the standard 36-character UUIDs `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx`.

Base64 encoding includes two special characters, "+" and "/", which are replaced with "_" and "-" to ensure the Base64 string is URL and filename-safe.

Additionally, the library provides custom JSON serializer and deserializer, allowing this data type to be used in HTTP request/response bodies or as a path variable in URLs.

### Example
The UUID `e2c20987-140c-4221-b797-ebfb8e3bd159` will be encoded as the Base64 string `4sIJhxQMQiG3l-v7jjvRWQ`. 