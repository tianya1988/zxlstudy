package utils;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.io.UnsupportedEncodingException;

public class StringZkSerializer implements ZkSerializer {
    @Override
    public byte[] serialize(final Object data) throws ZkMarshallingError {
        try {
            if (data instanceof String) {
                return ((String) data).getBytes("UTF-8");
            } else {
                return data.toString().getBytes("UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object deserialize(final byte[] bytes) throws ZkMarshallingError {
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
