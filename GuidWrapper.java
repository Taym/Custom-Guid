import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;


@JsonSerialize(using = GuidWrapper.Serializer.class)
@JsonDeserialize(using = GuidWrapper.Deserializer.class)
public class GuidWrapper implements Serializable {
    private UUID wrapped;
    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();

    @JsonCreator
    public GuidWrapper(@JsonProperty("wrapped") final UUID wrapped) {
        this.wrapped = wrapped;
    }

    public String toShortGuid(){
        final ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(wrapped.getMostSignificantBits());
        buffer.putLong(wrapped.getLeastSignificantBits());
        final byte[] bytes = buffer.array();
        final String paddedShortGuid = encoder.encodeToString(bytes);
        final String shortGuid = paddedShortGuid.substring(0,paddedShortGuid.length()-2);
        final String  urlSafeGuid = shortGuid.replace("/","_").replace("+","-");
        return urlSafeGuid;
    }

    public static GuidWrapper fromString(final String stringGuid){
        Objects.requireNonNull(stringGuid);
        if (stringGuid.length() == 36){
            return new GuidWrapper(UUID.fromString(stringGuid));
        }
        else if(stringGuid.length() == 22){
            final String originalGuid = stringGuid.replace("_","/").replace("-","+");
            final String paddedGuid = originalGuid + "==";
            final byte[] bytes = decoder.decode(paddedGuid);
            final ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            final long msb = byteBuffer.getLong();
            final long lsb = byteBuffer.getLong();
            final UUID uuid = new UUID(msb,lsb);
            return new GuidWrapper(uuid);
        }
        else{
            throw new RuntimeException("The length of stringGuid is not supported");
        }
    }

    @Override
    public String toString() {
       return wrapped.toString();
    }

    public static class Serializer extends JsonSerializer<GuidWrapper> {
        @Override
        public void serialize(GuidWrapper value, JsonGenerator jgen, SerializerProvider provider)throws IOException, JsonProcessingException {
            jgen.writeString(value.toString());
        }
    }

    public static class Deserializer extends JsonDeserializer<GuidWrapper> {
        @Override
        public GuidWrapper deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            final String stringGuid = jp.readValueAs(String.class);
            return GuidWrapper.fromString(stringGuid);
        }
    }
}
