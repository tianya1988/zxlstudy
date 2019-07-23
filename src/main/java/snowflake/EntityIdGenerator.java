package snowflake;

/**
 * com.venustech.glue.snowflake.EntityIdGenerator
 *
 * @author Maxim Khodanovich
 * @version 21.01.13 17:18
 */
public interface EntityIdGenerator {
    long generateLongId() throws InvalidSystemClockException, GetHardwareIdFailedException;
}
