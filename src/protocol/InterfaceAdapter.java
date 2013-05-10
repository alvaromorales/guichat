package protocol;

import java.lang.reflect.Type;
import com.google.gson.*;

/**
 * Interface adapter for serializing and deserializing Request and Response objects
 * Code taken from http://stackoverflow.com/questions/4795349/how-to-serialize-a-class-with-an-interface
 * @param <T> the type of object to serialize / deserialize
 */
public class InterfaceAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
    
    @Override
    public final JsonElement serialize(
        final T object, final Type interfaceType,
        final JsonSerializationContext context
    ) {
        final JsonObject member = new JsonObject();
        member.addProperty("type", object.getClass().getName());
        member.add("data", context.serialize(object));
        System.out.println("called: " + member);
        return member;
    }
    
    @Override
    public final T deserialize(
        final JsonElement elem, final Type interfaceType,
        final JsonDeserializationContext context
    ) throws JsonParseException {
        final JsonObject member = (JsonObject) elem;
        final JsonElement typeString = get(member, "type");
        final JsonElement data = get(member, "data");
        final Type actualType = typeForName(typeString);
        return context.deserialize(data, actualType);
    }
 
    private Type typeForName(final JsonElement typeElem) {
        try {
            return Class.forName(typeElem.getAsString());
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }
 
    private JsonElement get(final JsonObject wrapper, final String memberName) {
        final JsonElement elem = wrapper.get(memberName);
        if (elem == null) {
            throw new JsonParseException(
                "no '" + memberName + "' member found in json file.");
        }
        return elem;
    }
    
//    @Override
//    public JsonElement serialize(T object, Type interfaceType, JsonSerializationContext context) {
//        final JsonObject wrapper = new JsonObject();
//        wrapper.addProperty("type", object.getClass().getName());
//        wrapper.add("data", context.serialize(object));
//        return wrapper;
//    }
//
//    @Override
//    public T deserialize(JsonElement elem, Type interfaceType, JsonDeserializationContext context) throws JsonParseException {
//        final JsonObject wrapper = (JsonObject) elem;
//        final JsonElement typeName = get(wrapper, "type");
//        final JsonElement data = get(wrapper, "data");
//        final Type actualType = typeForName(typeName); 
//        return context.deserialize(data, actualType);
//    }
//
//    private Type typeForName(final JsonElement typeElem) throws JsonIOException {
//        try {
//            return Class.forName(typeElem.getAsString());
//        } catch (ClassNotFoundException e) {
//            throw new JsonParseException(e);
//        }
//    }
//
//    private JsonElement get(final JsonObject wrapper, String memberName) throws JsonParseException {
//        final JsonElement elem = wrapper.get(memberName);
//        if (elem == null) throw new JsonParseException("no '" + memberName + "' member found in what was expected to be an interface wrapper");
//        return elem;
//    }

}
