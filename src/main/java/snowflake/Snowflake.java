package snowflake;

/**
 * Created by h on 16-11-29.
 */
public final class Snowflake {

    private Snowflake() {

    }

    public static long getId() {
        return BasicEntityIdGenerator.getInstance().generateLongId();
    }

}
