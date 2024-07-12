package conprgV1com.example.conprg.Repository;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import conprgV1com.example.conprg.Entity.ProductImage;

import java.io.IOException;

public class ProductImageSerializer extends JsonSerializer<ProductImage> {

    @Override
    public void serialize(ProductImage value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value.getImageId());
    }
}
